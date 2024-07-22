package com.mogudiandian.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对象关系匹配器 可匹配一对一或一对多 <br/>
 * 非线程安全 <br/>
 * 用于在一个已有的集合（源集合）中，每个元素中的几个属性要靠另一个接口/服务去查，查完拿回来匹配并组装到当前集合内对应的元素中 <br/>
 * 例如要查询评论信息返回评论对象集合，一个评论类中包含了评论ID、评论内容、用户ID、用户名、用户头像、文章ID、文章标题、文章内容 <br/>
 * 通常分为下面几步 <br/>
 * 1.根据检索条件分页查询评论信息，包含了评论ID、评论内容、用户ID、文章ID <br/>
 * 2.抽取出所有的用户ID，根据这些用户ID调用用户查询服务获取用户信息集合（新集合），然后用评论集合和用户集合进行join(or mapping)，再设置对应的用户名和用户头像 <br/>
 * 3.抽取出所有的文章ID，根据这些文章ID调用文章查询接口获取文章信息集合（新集合），然后用评论集合和文章集合进行一一匹配，再设置对应的文章标题和文章内容 <br/>
 * 对这类问题的解决方案大致可抽象为下面几步 <br/>
 * 1.在源集合中找出要根据哪些标识（ID or condition）来查询 <br/>
 *   1.1.可能只有某些元素有这种标识，需要过滤出包含标识的元素 <br/>
 * 2.根据这些标识查询需要的新集合 <br/>
 *   2.1.根据这些标识查询缓存 <br/>
 *   2.2.将未命中缓存的标识调用一个批量查询集合的方法或用多线程调用单个查询的方法 <br/>
 *     2.2.1.某些接口会有限制，比如最多只能查100条，需要分批查询 <br/>
 *     2.2.2.失败的话可能需要重试 <br/>
 *   2.3.将未在缓存中的数据放入缓存 <br/>
 *   2.4.将上面两步查出来的数据合并 <br/>
 * 3.将查询出来的新集合按照指定的方式分组变为Map <br/>
 *   3.1.某些接口只能进行简单查询（比如只能查全部无法根据状态过滤），需要查出后自己过滤（只要生效中的） <br/>
 * 4.遍历源集合并按照指定的规则来从上面Map中找到关联的对象 <br/>
 * 5.找出Map的数据后如果非空则调用setter来设置属性值 <br/>
 *
 * 调用方式（例如根据评论中的文章ID查询文章并设置文章的标题和内容）： <br/>
 * ObjectRelationMatcher<CommentDTO, Long, List<Long>, ArticleDTO, Long> matcher = new ObjectRelationMatcher<>(); <br/>
 * matcher.setElements(comments) <br/>
 *        .setElementIdentifierExtractor(CommentDTO::getArticleId) <br/>
 *        .setIdentifierCollectorType(List.class) <br/>
 *        .setBatchQueryMethod(articleService::queryByIds) <br/>
 *        .setDataKeyGenerator(ArticleDTO::getId) <br/>
 *        .setElementToKeyMapping(CommentDTO::getArticleId) <br/>
 *        .match() <br/>
 *        .processOneToOne((comment, article) -> { <br/>
 *            comment.setArticleTitle(article.getTitle()); <br/>
 *            comment.setArticleContent(article.getContent()); <br/>
 *        }, comment -> { <br/>
 *            comment.setArticleTitle("未知标题"); <br/>
 *            comment.setArticleContent("未知内容"); <br/>
 *        }); <br/>
 *
 * @param <E> 源集合的元素类型
 * @param <I> 源集合元素生成标识的类型
 * @param <C> 标识的集合类型
 * @param <D> 新集合的元素类型
 * @param <K> 新集合元素生成key的类型
 * @author Joshua Sun
 * @since 1.0.0
 */
@NotThreadSafe
public final class ObjectRelationMatcher<E, I, C extends Collection<I>, D, K> {

    /**
     * 默认缓存的集合数量为16
     */
    private static final int DEFAULT_CACHE_COLLECTION_COUNT = 16;

    /**
     * 默认每个集合缓存的元素数量为128
     */
    private static final int DEFAULT_CACHE_IDENTIFIER_COUNT_PER_COLLECTION = 128;

    /**
     * 线程池产生的现成命名规则为orm-parallel-query-pool-现成的tid
     */
    private static final String EXECUTOR_THREAD_NAME_FORMAT = "orm-parallel-query-pool-%d";

    /**
     * 线程池并发的数量默认为CPU逻辑处理器数量（同FJP） 这样不太适合Intel-HT的CPU
     */
    private static final int EXECUTOR_PARALLEL_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * 线程池队列长度默认为1024
     */
    private static final int EXECUTOR_QUEUE_SIZE = 1024;

    /**
     * 通用缓存(name, identifier) -> data
     */
    private static Table<String, Object, Object> cache;

    /**
     * 并发查询的线程池
     */
    private static ListeningExecutorService parallelQueryPool;

    /**
     * 源集合
     */
    private Collection<E> elements;

    /**
     * 需要过滤元素的方法，有的场景会根据一个类型来筛选，比如集合中的元素本身就不属于一种类型
     */
    private Predicate<E> elementFilter;

    /**
     * 提取元素标识符的方法
     */
    private Function<E, I> elementIdentifierExtractor;

    /**
     * 提取元素标识符的方法，用于多对多
     */
    private Function<E, Collection<I>> elementIdentifiersExtractor;

    /**
     * 将标识符聚合成什么集合 要看查询新集合的参数是什么 可能是List或Set
     */
    private Collector<I, ?, C> identifierCollector;

    /**
     * 批量查询新集合的方法 如果有批量的查询方法 优先使用批量查询
     */
    private Function<C, ? extends Collection<D>> batchQueryMethod;

    /**
     * 批量查询最多可查的数据条数 某些接口会对数量做限制 可以使用这个参数实现分批查询
     */
    private int maxBatchQuerySize;

    /**
     * 单个查询的方法 如果没有批量查询方法（对方没有这种接口）可以使用这个参数
     * 如果和batchQueryMethod同时存在 如果查询标识的数量为1会优先使用这个方法
     */
    private Function<I, D> singleQueryMethod;

    /**
     * 是否需要并发调用查询方法 默认为true 如果某些接口有QPS的限流可以将这个值设置为false进行串行查询
     */
    private boolean parallelExecuteQuery = true;

    /**
     * 如果查询异常 需要重试的次数 默认为0（不重试）
     */
    private int queryExceptionRetryTimes;

    /**
     * 如果查询异常 重试的等待间隔 默认为0（立即重试）
     */
    private long queryExceptionRetryInterval;

    /**
     * 查询出来的新集合需要过滤元素的方法 某些场景下 查询接口无法做到最细粒度的查询（比如无法排除"已失效"的） 则用这个参数自己进行过滤
     */
    private Predicate<D> dataFilter;

    /**
     * 新集合的元素生成唯一键（key）的方法
     */
    private Function<D, K> dataKeyGenerator;

    /**
     * 源集合的元素映射到新集合唯一键的方法
     */
    private Function<E, K> elementToKeyMapping;

    /**
     * 源集合的元素映射到新集合唯一键的方法
     */
    private Function<E, Collection<K>> elementToKeysMapping;

    /**
     * 源集合和新集合的关系是否为一对多 默认为false（一对一）
     */
    private boolean oneToMany;

    /**
     * 源集合和新集合的关系是否为多对多 默认为false（一对一）
     */
    private boolean manyToMany;

    /**
     * 如果关系为一对多 新集合为空集合（[]）是否算未匹配 默认为false（不算 也就是算匹配上了0条）
     */
    private boolean emptyAsUnmatched;

    /**
     * 查询新集合是否使用缓存 默认为false（不使用）
     */
    private boolean useCache;

    /**
     * 缓存键的名称
     */
    private String cacheKeyName;

    /**
     * 匹配后是否需要立即清理缓存 默认为false（不清理 需要自己手动清）
     */
    private boolean clearCacheAfterMatch;

    /**
     * 匹配器的事件监听器
     */
    private ObjectRelationMatcherEvent<E, I, C, D, K> eventListener;

    /**
     * 是否已完成匹配
     */
    private boolean matched;

    /**
     * 匹配后的一对一关系
     */
    private Map<E, D> oneToOneMap;

    /**
     * 匹配后的一对多关系
     */
    private Map<E, List<D>> oneToManyMap;

    /**
     * 匹配后的一对多关系
     */
    private Map<E, List<D>> manyToManyMap;

    // 初始化缓存和线程池
    static {
        cache = HashBasedTable.create(DEFAULT_CACHE_COLLECTION_COUNT, DEFAULT_CACHE_IDENTIFIER_COUNT_PER_COLLECTION);

        ExecutorService originalPool = new ThreadPoolExecutor(
                EXECUTOR_PARALLEL_SIZE,
                EXECUTOR_PARALLEL_SIZE,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(EXECUTOR_QUEUE_SIZE),
                new ThreadFactoryBuilder().setNameFormat(EXECUTOR_THREAD_NAME_FORMAT).build(),
                new ThreadPoolExecutor.CallerRunsPolicy());

        parallelQueryPool = MoreExecutors.listeningDecorator(originalPool);
    }

    public ObjectRelationMatcher() {
        super();
    }

    /**
     * 校验参数设置是否有问题
     */
    private void validate() {
        Objects.requireNonNull(elements, "源集合不能为空");
        if (manyToMany) {
            Objects.requireNonNull(elementIdentifiersExtractor, "标识符提取器不能为空");
        } else {
            Objects.requireNonNull(elementIdentifierExtractor, "标识符提取器不能为空");
        }

        // 只有设置了批量查询方法时才需要指定collector（要保持和批量查询方法的参数一致）
        if (batchQueryMethod != null) {
            Objects.requireNonNull(identifierCollector, "批量查询模式下标识聚合器不能为空");
        } else {
            // 没有批量查询的方法按照单个查询方法来
            setIdentifierCollectorType(List.class);

            Objects.requireNonNull(singleQueryMethod, "批量查询方法和单个查询方法至少要指定一个");
        }

        // 重试配置修正
        if (queryExceptionRetryTimes < 0) {
            queryExceptionRetryTimes = 0;
        }
        if (queryExceptionRetryInterval < 0) {
            queryExceptionRetryInterval = 0;
        }

        Objects.requireNonNull(dataKeyGenerator, "新集合唯一键生成器不能为空");

        if (manyToMany) {
            Objects.requireNonNull(elementToKeysMapping, "源集合到唯一键的映射关系不能为空");
        } else {
            Objects.requireNonNull(elementToKeyMapping, "源集合到唯一键的映射关系不能为空");
        }

        if (useCache) {
            if (manyToMany) {
                throw new RuntimeException("多对多的情况下不能使用缓存");
            }
            Objects.requireNonNull(cacheKeyName, "使用缓存的情况下缓存名称不能为空");
        }
    }

    @SuppressWarnings("unchecked")
    public ObjectRelationMatcher<E, I, C, D, K> setIdentifierCollectorType(Class<? extends Collection> type) {
        Objects.requireNonNull(type, "标识收集器类型不能设置为null");
        if (List.class.isAssignableFrom(type)) {
            this.setIdentifierCollector((Collector<I, ?, C>) Collectors.toList());
        } else if (Set.class.isAssignableFrom(type)) {
            this.setIdentifierCollector((Collector<I, ?, C>) Collectors.toSet());
        } else {
            throw new RuntimeException("无法处理的标识收集器类型: " + type);
        }
        return this;
    }

    /**
     * 关系匹配
     * @return 当前对象本身
     */
    public ObjectRelationMatcher<E, I, C, D, K> match() {
        validate();

        // 源集合的元素和新集合的元素对应关系（一对一）
        Map<E, D> elementDataMap;

        // 源集合的元素和新集合的元素对应关系（一对多）
        Map<E, List<D>> elementDataListMap;

        if (manyToMany) {
            // 多对多
            elementDataMap = null;
            elementDataListMap = new HashMap<>(elements.size(), 1);
        } else if (oneToMany) {
            // 一对多
            elementDataMap = null;
            elementDataListMap = new HashMap<>(elements.size(), 1);
        } else {
            // 一对一
            elementDataMap = new HashMap<>(elements.size(), 1);
            elementDataListMap = null;
        }

        // 过滤后的源集合
        Stream<E> elementStream = filteredElementStream();

        // 待查询的标识集合
        Stream<I> identifierStream;

        // 如果使用缓存 先从缓存里找 这个方法体为了填充已命中的关系映射和待查询列表
        if (useCache) {
            // 没命中缓存的标识
            List<I> identifierList = new ArrayList<>(elements.size());
            if (manyToMany) {
                // 多对多
                elementStream.forEach(element -> {
                    Collection<I> identifiers = elementIdentifiersExtractor.apply(element);
                    if (CollectionUtils.isNotEmpty(identifiers)) {
                        // 多对多没有缓存
                        identifierList.addAll(identifiers);
                    }
                });
            } else if (oneToMany) {
                // 一对多
                elementStream.forEach(element -> {
                    I identifier = elementIdentifierExtractor.apply(element);
                    List<D> list = getDataListFormCache(identifier);
                    // 如果命中缓存了不为null 并且 不是空集合 或空集合算匹配成功
                    if (list != null && (!list.isEmpty() || !emptyAsUnmatched)) {
                        elementDataListMap.put(element, list);
                    } else {
                        // 如果没找到 或者是找到了但是配置了emptyAsUnmatched=true 都认为是没找到
                        identifierList.add(identifier);
                    }
                });
            } else {
                // 一对一
                elementStream.forEach(element -> {
                    I identifier = elementIdentifierExtractor.apply(element);
                    D data = getDataFromCache(identifier);
                    // 非空表示命中缓存 命中了则加入关系映射中 未命中则加入待查询列表中
                    if (data != null) {
                        elementDataMap.put(element, data);
                    } else {
                        identifierList.add(identifier);
                    }
                });
            }
            identifierStream = identifierList.stream();
        } else {
            // 不使用缓存的话 直接提取所有标识符
            if (manyToMany) {
                // 多对多
                identifierStream = elementStream.map(elementIdentifiersExtractor)
                                                .flatMap(Collection::stream);
            } else {
                identifierStream = elementStream.map(elementIdentifierExtractor);
            }
        }

        // 待查询的标识符集合
        C identifierCollection = identifierStream.collect(identifierCollector);

        // 如果有待查询的标识符 说明没走缓存或缓存中有未命中的数据
        if (!identifierCollection.isEmpty()) {
            Collection<D> dataCollection = null;

            int retryCount = 0;
            Exception lastException = null;

            do {
                try {
                    // 如果需要查询的数据只有一个 并且配置了单个查询的方法 优先查单个的方法
                    if (identifierCollection.size() == 1 && singleQueryMethod != null) {
                        D data = singleQueryMethod.apply(identifierCollection.iterator().next());
                        dataCollection = Optional.ofNullable(data)
                                                 .map(Collections::singletonList)
                                                 .orElse(Collections.emptyList());
                    } else {
                        // 如果需要查询多个 那么看是否配置了批量查询方法 如果配置了则优先使用批量查询方法
                        if (batchQueryMethod != null) {
                            // 如果没有配置批量最大可查条数 直接调用批量查询方法
                            if (maxBatchQuerySize <= 0) {
                                dataCollection = batchQueryMethod.apply(identifierCollection);
                            } else {
                                // 如果接口有限制最大查询数量 则分批查
                                // 按照分页来分批 之所以不用guava的是因为guava的只能拆为List<List<?>> 但是我们的collector是自定义的
                                int pages = (identifierCollection.size() + maxBatchQuerySize - 1) / maxBatchQuerySize;
                                List<C> identifierListBatches = Stream.iterate(0, x -> x + 1)
                                                                      .limit(pages)
                                                                      .map(x -> identifierCollection.stream()
                                                                                                    .skip((long) x * maxBatchQuerySize)
                                                                                                    .limit(maxBatchQuerySize)
                                                                                                    .collect(identifierCollector))
                                                                      .collect(Collectors.toList());
                                // 如果配置了并发查询 则用线程池来调用 否则使用串行查询
                                if (parallelExecuteQuery) {
                                    List<ListenableFuture<? extends Collection<D>>> futures = identifierListBatches.stream()
                                                                                                                   .map(x -> parallelQueryPool.submit(() -> batchQueryMethod.apply(x)))
                                                                                                                   .collect(Collectors.toList());
                                    dataCollection = Futures.allAsList(futures)
                                                            .get()
                                                            .stream()
                                                            .flatMap(Collection::stream)
                                                            .collect(Collectors.toList());
                                } else {
                                    // 只有显式设置了parallelExecuteQuery=false 才会串行查询
                                    dataCollection = identifierListBatches.stream()
                                                                          .map(batchQueryMethod)
                                                                          .flatMap(Collection::stream)
                                                                          .collect(Collectors.toList());
                                }
                            }
                        } else if (singleQueryMethod != null) {
                            // 如果配置了并发查询 则用线程池来调用 否则使用串行查询
                            if (parallelExecuteQuery) {
                                List<ListenableFuture<D>> futures = identifierCollection.stream()
                                                                                        .map(x -> parallelQueryPool.submit(() -> singleQueryMethod.apply(x)))
                                                                                        .collect(Collectors.toList());
                                dataCollection = Futures.allAsList(futures).get();
                            } else {
                                dataCollection = identifierCollection.stream()
                                                                     .map(singleQueryMethod)
                                                                     .collect(Collectors.toList());
                            }
                        }
                    }
                    if (eventListener != null) {
                        eventListener.onQuerySuccess(identifierCollection, retryCount, dataCollection);
                    }
                    // 上面没有异常的话 说明已经成功了 则结束掉循环
                    break;
                } catch (Exception e) {
                    lastException = e;
                    if (eventListener != null) {
                        eventListener.onQueryException(identifierCollection, retryCount, e);
                    }
                    // 休眠再重试
                    try {
                        Thread.sleep(queryExceptionRetryInterval);
                    } catch (InterruptedException ignored) {
                    }
                }
            } while (++retryCount <= queryExceptionRetryTimes);

            // 超过重试次数 说明每次执行都异常没有走到break
            if (retryCount > queryExceptionRetryTimes) {
                throw new RuntimeException("query for" + identifierCollection + "failure", lastException);
            }

            // 返回的数据不是空才处理 因为空的话没有处理的必要 这里认为方法（主要是批量方法）返回的empty是没查到
            if (CollectionUtils.isNotEmpty(dataCollection)) {
                Stream<D> dataStream = dataCollection.stream();
                // 过滤新集合
                if (dataFilter != null) {
                    dataStream = dataStream.filter(dataFilter);
                }

                // 过滤源集合 因为只根据过滤后的内容进行了查询
                elementStream = filteredElementStream();

                if (manyToMany) {
                    // 多对多
                    Map<K, List<D>> keyDataListMap = dataStream.filter(Objects::nonNull)
                                                               .collect(Collectors.groupingBy(dataKeyGenerator));
                    elementStream.filter(element -> !keyDataListMap.containsKey(element))
                                 .forEach(element -> {
                                     // keys 1 2 3
                                     Collection<K> collection = elementToKeysMapping.apply(element);
                                     for (K key : collection) {
                                         List<D> list = keyDataListMap.get(key);
                                         if (list != null) {
                                             elementDataListMap.computeIfAbsent(element, k -> new ArrayList<>()).addAll(list);
                                         }
                                     }
                                 });
                } else if (oneToMany) {
                    // 一对多
                    Map<K, List<D>> keyDataListMap = dataStream.filter(Objects::nonNull)
                                                               .collect(Collectors.groupingBy(dataKeyGenerator));
                    elementStream.filter(element -> !keyDataListMap.containsKey(element))
                                 .forEach(element -> {
                                     K key = elementToKeyMapping.apply(element);
                                     List<D> list = keyDataListMap.get(key);
                                     elementDataListMap.put(element, list);
                                     if (useCache && list != null && (!list.isEmpty() || !emptyAsUnmatched)) {
                                         putListToCache(elementIdentifierExtractor.apply(element), list);
                                     }
                                 });
                } else {
                    // 一对一
                    Map<K, D> keyDataMap = dataStream.filter(Objects::nonNull)
                                                     .collect(HashMap::new, (m, e) -> m.put(dataKeyGenerator.apply(e), e), Map::putAll);
                    elementStream.filter(element -> !elementDataMap.containsKey(element))
                                 .forEach(element -> {
                                     K key = elementToKeyMapping.apply(element);
                                     D data = keyDataMap.get(key);
                                     elementDataMap.put(element, data);
                                     if (useCache && data != null) {
                                         putToCache(elementIdentifierExtractor.apply(element), data);
                                     }
                                 });
                }
            }
        }

        // 补充未匹配的
        if (manyToMany) {
            // 多对多
            elements.stream()
                    .filter(element -> !elementDataListMap.containsKey(element))
                    .forEach(element -> elementDataListMap.put(element, null));
        } else if (oneToMany) {
            // 一对多
            elements.stream()
                    .filter(element -> !elementDataListMap.containsKey(element))
                    .forEach(element -> elementDataListMap.put(element, null));
        } else {
            // 一对一
            elements.stream()
                    .filter(element -> !elementDataMap.containsKey(element))
                    .forEach(element -> elementDataMap.put(element, null));
        }

        this.oneToOneMap = elementDataMap;
        this.oneToManyMap = elementDataListMap;
        this.manyToManyMap = elementDataListMap;

        // 如果需要match后清理缓存 则立即清理
        if (useCache && clearCacheAfterMatch) {
            clearCache();
        }

        matched = true;

        return this;
    }

    /**
     * 根据配置过滤源集合并返回源集合流
     * @return 源集合流
     */
    private Stream<E> filteredElementStream() {
        Stream<E> stream = elements.stream();
        return Optional.ofNullable(elementFilter).map(stream::filter).orElse(stream);
    }

    /**
     * 根据标识从缓存获取数据
     * @param identifier 标识
     * @return 缓存中的数据
     */
    @SuppressWarnings("unchecked")
    private D getDataFromCache(I identifier) {
        return (D) cache.get(cacheKeyName, identifier);
    }

    /**
     * 根据标识从缓存获取集合
     * @param identifier 标识
     * @return 缓存中的集合
     */
    @SuppressWarnings("unchecked")
    private List<D> getDataListFormCache(I identifier) {
        return (List<D>) cache.get(cacheKeyName, identifier);
    }

    /**
     * 将数据添加到缓存
     * @param identifier 标识
     * @param data 数据
     */
    private void putToCache(I identifier, D data) {
        cache.put(cacheKeyName, identifier, data);
    }

    /**
     * 将集合添加到缓存
     * @param identifier 标识
     * @param list 集合
     */
    private void putListToCache(I identifier, List<D> list) {
        cache.put(cacheKeyName, identifier, list);
    }

    /**
     * 清除当前缓存
     */
    private void clearCache() {
        clearCache(cacheKeyName);
    }

    /**
     * 清除某个缓存
     * @param cacheKeyName 缓存名称
     */
    public static void clearCache(String cacheKeyName) {
        Map<String, Object> map = cache.columnMap().get(cacheKeyName);
        synchronized (map) {
            map.clear();
        }
    }

    /**
     * 清除全部缓存
     */
    public static void clearAllCaches() {
        cache.clear();
    }

    /**
     * 获取一对一的关系
     * @param containsUnmatched 是否包含未匹配的 false表示不包含（inner join） true表示包含（left join）
     * @return 源集合对象和新集合对象的对应关系
     */
    public Map<E, D> getOneToOneRelations(boolean containsUnmatched) {
        if (!matched) {
            throw new RuntimeException("需要先调用match方法");
        }
        if (oneToMany) {
            throw new RuntimeException("一对多关系应该调用getOneToManyRelations方法");
        }
        Map<E, D> map = new HashMap<>(oneToOneMap);
        // 如果不需要未匹配的 则将全map中value为null的元素移除
        if (!containsUnmatched) {
            map.entrySet()
               .stream()
               .filter(x -> x.getValue() == null)
               .map(Map.Entry::getKey)
               .collect(Collectors.toList())
               .forEach(map::remove);
        }
        return map;
    }

    /**
     * 获取一对一的关系 只返回已匹配的
     * @return 源集合对象和新集合对象的对应关系
     */
    public Map<E, D> getOneToOneRelations() {
        return getOneToOneRelations(false);
    }

    /**
     * 获取一对多的关系
     * @param containsUnmatched 是否包含未匹配的 false表示不包含（inner join） true表示包含（left join）
     * @return 源集合对象和新集合对象的对应关系
     */
    public Map<E, List<D>> getOneToManyRelations(boolean containsUnmatched) {
        if (!matched) {
            throw new RuntimeException("需要先调用match方法");
        }
        if (!oneToMany) {
            throw new RuntimeException("一对一关系应该调用getOneToOneRelations方法");
        }
        Map<E, List<D>> map = new HashMap<>(oneToManyMap);
        // 如果不需要未匹配的 则将全map中value为null的元素移除
        if (!containsUnmatched) {
            map.entrySet()
               .stream()
               .filter(x -> x.getValue() == null || (x.getValue().isEmpty() && emptyAsUnmatched))
               .map(Map.Entry::getKey)
               .collect(Collectors.toList())
               .forEach(map::remove);
        }
        return map;
    }

    /**
     * 获取一对多的关系 只返回已匹配的
     * @return 源集合对象和新集合对象的对应关系
     */
    public Map<E, List<D>> getOneToManyRelations() {
        return getOneToManyRelations(false);
    }

    /**
     * 处理所有一对一的关系
     * @param matchedProcessor 已匹配到关系的处理器
     * @param unmatchedProcessor 未匹配到关系的处理器
     */
    public void processOneToOne(BiConsumer<E, D> matchedProcessor, Consumer<E> unmatchedProcessor) {
        if (!matched) {
            throw new RuntimeException("需要先调用match方法");
        }
        if (oneToMany) {
            throw new RuntimeException("一对多关系应该调用getOneToManyRelations方法");
        }
        oneToOneMap.forEach((k, v) -> {
            if (v != null) {
                matchedProcessor.accept(k, v);
            } else if (unmatchedProcessor != null) {
                unmatchedProcessor.accept(k);
            }
        });
    }

    /**
     * 处理已匹配的一对一的关系
     * @param matchedProcessor 已匹配到关系的处理器
     */
    public void processOneToOne(BiConsumer<E, D> matchedProcessor) {
        processOneToOne(matchedProcessor, null);
    }

    /**
     * 处理所有一对多的关系
     * @param matchedProcessor 已匹配到关系的处理器
     * @param unmatchedProcessor 未匹配到关系的处理器
     */
    public void processOneToMany(BiConsumer<E, List<D>> matchedProcessor, Consumer<E> unmatchedProcessor) {
        if (!matched) {
            throw new RuntimeException("需要先调用match方法");
        }
        if (!oneToMany) {
            throw new RuntimeException("一对一关系应该调用getOneToOneRelations方法");
        }
        oneToManyMap.forEach((k, v) -> {
            if (v != null && (!v.isEmpty() || !emptyAsUnmatched)) {
                matchedProcessor.accept(k, v);
            } else if (unmatchedProcessor != null) {
                unmatchedProcessor.accept(k);
            }
        });
    }

    /**
     * 处理已匹配的一对多的关系
     * @param matchedProcessor 已匹配到关系的处理器
     */
    public void processOneToMany(BiConsumer<E, List<D>> matchedProcessor) {
        processOneToMany(matchedProcessor, null);
    }

    /**
     * 处理所有多对多的关系
     * @param matchedProcessor 已匹配到关系的处理器
     * @param unmatchedProcessor 未匹配到关系的处理器
     */
    public void processManyToMany(BiConsumer<E, List<D>> matchedProcessor, Consumer<E> unmatchedProcessor) {
        if (!matched) {
            throw new RuntimeException("需要先调用match方法");
        }
        if (!manyToMany) {
            throw new RuntimeException("一对一关系应该调用getOneToOneRelations方法");
        }
        manyToManyMap.forEach((k, v) -> {
            if (v != null && (!v.isEmpty() || !emptyAsUnmatched)) {
                matchedProcessor.accept(k, v);
            } else if (unmatchedProcessor != null) {
                unmatchedProcessor.accept(k);
            }
        });
    }

    /**
     * 处理所有多对多的关系
     * @param matchedProcessor 已匹配到关系的处理器
     */
    public void processManyToMany(BiConsumer<E, List<D>> matchedProcessor) {
        processManyToMany(matchedProcessor, null);
    }

    private ObjectRelationMatcher<E, I, C, D, K> markUnmatched() {
        matched = false;
        return this;
    }

    public ObjectRelationMatcher<E, I, C, D, K> setElements(Collection<E> elements) {
        this.elements = elements;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setElementFilter(Predicate<E> elementFilter) {
        this.elementFilter = elementFilter;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setElementIdentifierExtractor(Function<E, I> elementIdentifierExtractor) {
        this.elementIdentifierExtractor = elementIdentifierExtractor;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setIdentifierCollector(Collector<I, ?, C> identifierCollector) {
        this.identifierCollector = identifierCollector;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setBatchQueryMethod(Function<C, ? extends Collection<D>> batchQueryMethod) {
        this.batchQueryMethod = batchQueryMethod;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setMaxBatchQuerySize(int maxBatchQuerySize) {
        this.maxBatchQuerySize = maxBatchQuerySize;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setSingleQueryMethod(Function<I, D> singleQueryMethod) {
        this.singleQueryMethod = singleQueryMethod;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setParallelExecuteQuery(boolean parallelExecuteQuery) {
        this.parallelExecuteQuery = parallelExecuteQuery;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setQueryExceptionRetryTimes(int queryExceptionRetryTimes) {
        this.queryExceptionRetryTimes = queryExceptionRetryTimes;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setQueryExceptionRetryInterval(long queryExceptionRetryInterval) {
        this.queryExceptionRetryInterval = queryExceptionRetryInterval;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setDataFilter(Predicate<D> dataFilter) {
        this.dataFilter = dataFilter;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setDataKeyGenerator(Function<D, K> dataKeyGenerator) {
        this.dataKeyGenerator = dataKeyGenerator;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setElementToKeyMapping(Function<E, K> elementToKeyMapping) {
        this.elementToKeyMapping = elementToKeyMapping;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setOneToMany(boolean oneToMany) {
        this.oneToMany = oneToMany;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setEmptyAsUnmatched(boolean emptyAsUnmatched) {
        this.emptyAsUnmatched = emptyAsUnmatched;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setUseCache(boolean useCache) {
        this.useCache = useCache;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setCacheKeyName(String cacheKeyName) {
        this.cacheKeyName = cacheKeyName;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setClearCacheAfterMatch(boolean clearCacheAfterMatch) {
        this.clearCacheAfterMatch = clearCacheAfterMatch;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setEventListener(ObjectRelationMatcherEvent<E, I, C, D, K> eventListener) {
        this.eventListener = eventListener;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setElementIdentifiersExtractor(Function<E, Collection<I>> elementIdentifiersExtractor) {
        this.elementIdentifiersExtractor = elementIdentifiersExtractor;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setElementToKeysMapping(Function<E, Collection<K>> elementToKeysMapping) {
        this.elementToKeysMapping = elementToKeysMapping;
        return markUnmatched();
    }

    public ObjectRelationMatcher<E, I, C, D, K> setManyToMany(boolean manyToMany) {
        this.manyToMany = manyToMany;
        return markUnmatched();
    }

    /**
     * 停机 释放资源
     */
    public static void shutdown() {
        try {
            // 清理缓存
            clearAllCaches();
        } catch (Exception ignored) {
        }
        try {
            // 关闭线程池
            parallelQueryPool.shutdown();
        } catch (Exception ignored) {
        }
    }

    @Slf4j
    public static class ObjectRelationMatcherEvent<E, I, C extends Collection<I>, D, K> {

        /**
         * 查询新集合成功
         * @param identifierCollection 标识集合
         * @param count 重试次数 0表示首次查询未重试
         * @param dataCollection 查询到的数据
         */
        public void onQuerySuccess(C identifierCollection, int count, Collection<D> dataCollection) {
            if (count == 0) {
                log.debug("orm query for {} results are: {}", identifierCollection, dataCollection);
            } else {
                log.debug("orm query(retry={}) for {} results are: {}", count, identifierCollection, dataCollection);
            }
        }

        /**
         * 查询新集合发生异常
         * @param identifierCollection 标识集合
         * @param count 重试次数 0表示首次查询未重试
         * @param e 异常信息
         */
        public void onQueryException(C identifierCollection, int count, Exception e) {
            if (count == 0) {
                log.warn("orm query for {} throws exception: ", identifierCollection, e);
            } else {
                log.warn("orm query(retry={}) for {} throws exception: ", count, identifierCollection, e);
            }
        }

    }
}
