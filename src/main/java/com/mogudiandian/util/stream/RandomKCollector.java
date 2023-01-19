package com.mogudiandian.util.stream;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 实现RandomK的collector
 * 使用方式：
 * <pre>
 *     int[] arr = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
 *     List<Integer> list = Arrays.stream(arr).boxed().collect(RandomKCollector.newInstance(6));
 *     System.out.println(list);
 * </pre>
 *
 * @param <T> 元素
 * @author sunbo
 */
@NotThreadSafe
public final class RandomKCollector<T> implements Collector<T, PriorityQueue<Map.Entry<Double, T>>, List<T>> {

    private int k;

    private RandomKCollector(int k) {
        if (k <= 0) {
            throw new IllegalArgumentException("K must be positive");
        }
        this.k = k;
    }

    /**
     * 创建RandomK收集器实例
     * @param k 需要多少个元素
     * @param <T> 元素类型
     * @return RandomK收集器
     */
    public static <T> RandomKCollector<T> newInstance(int k) {
        return new RandomKCollector<>(k);
    }

    @Override
    public Supplier<PriorityQueue<Map.Entry<Double, T>>> supplier() {
        return () -> new PriorityQueue<>(k, Map.Entry.<Double, T>comparingByKey());
    }

    @Override
    public BiConsumer<PriorityQueue<Map.Entry<Double, T>>, T> accumulator() {
        return (queue, t) -> {
            queue.offer(new AbstractMap.SimpleImmutableEntry<>(Math.random(), t));
            if (queue.size() > k) {
                queue.poll();
            }
        };
    }

    @Override
    public BinaryOperator<PriorityQueue<Map.Entry<Double, T>>> combiner() {
        return (q1, q2) -> {
            q1.addAll(q2);
            return q1;
        };
    }

    @Override
    public Function<PriorityQueue<Map.Entry<Double, T>>, List<T>> finisher() {
        return queue -> IntStream.iterate(0, x -> x + 1)
                                 .limit(Math.min(k, queue.size()))
                                 .mapToObj(x -> queue.poll())
                                 .filter(Objects::nonNull)
                                 .map(Map.Entry::getValue)
                                 .collect(Collectors.toList());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.unmodifiableSet(EnumSet.of(Characteristics.UNORDERED));
    }
}