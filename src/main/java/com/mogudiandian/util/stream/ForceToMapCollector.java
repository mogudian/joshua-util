package com.mogudiandian.util.stream;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * 强制toMap收集器
 * 直接使用默认的Collectors.toMap会出现两个问题
 * 1、key重复时抛异常
 * 2、value为空时抛异常
 * 因此封装了这个工具解决这两个问题
 *
 * 使用方式：
 * <pre>
 *     int[] arr = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 *     Map<Integer, Boolean> map = Arrays.stream(arr).boxed().collect(ForceToMapCollector.collect(Functional.identity(), x -> x % 2 == 0));
 *     System.out.println(map);
 * </pre>
 *
 * @param <T> stream中元素的类型
 * @param <K> key的类型
 * @param <V> value的类型
 * @author Joshua Sun
 */
public final class ForceToMapCollector<T, K, V> implements Collector<T, Map<K, V>, Map<K, V>> {

	private Function<? super T, ? extends K> keyMapper;

	private Function<? super T, ? extends V> valueMapper;

	public ForceToMapCollector(Function<? super T, ? extends K> keyMapper,
			Function<? super T, ? extends V> valueMapper) {
		super();
		this.keyMapper = keyMapper;
		this.valueMapper = valueMapper;
	}

	/**
	 * 创建ForceToMap收集器实例
	 * @param keyMapper key的映射器
	 * @param valueMapper value的映射器
	 * @param <T> stream中元素的类型
	 * @param <K> key的类型
	 * @param <V> value的类型
	 * @return ForceToMapCollector收集器
	 */
	public static <T, K, V> ForceToMapCollector<T, K, V> collect(Function<T, K> keyMapper, Function<T, V> valueMapper) {
		return new ForceToMapCollector<>(keyMapper, valueMapper);
	}

	/**
	 * 创建ForceToMap收集器实例 收集后map的value类型为流的类型
	 * @param keyMapper key的映射器
	 * @param <T> stream中元素的类型
	 * @param <K> key的类型
	 * @return ForceToMapCollector收集器
	 */
	public static <T, K> ForceToMapCollector<T, K, T> collect(Function<T, K> keyMapper) {
		return new ForceToMapCollector<>(keyMapper, Function.identity());
	}

	@Override
	public BiConsumer<Map<K, V>, T> accumulator() {
		return (map, element) -> map.put(keyMapper.apply(element), valueMapper.apply(element));
	}

	@Override
	public Supplier<Map<K, V>> supplier() {
		return HashMap::new;
	}

	@Override
	public BinaryOperator<Map<K, V>> combiner() {
		return (x, y) -> {
			x.putAll(y);
			return x;
		};
	}

	@Override
	public Function<Map<K, V>, Map<K, V>> finisher() {
		return Function.identity();
	}

	@Override
	public Set<Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
	}

}