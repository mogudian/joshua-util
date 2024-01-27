package com.mogudiandian.util.function;

import java.util.function.BinaryOperator;

/**
 * 封装了常用的BinaryOperator
 *
 * @author Joshua Sun
 * @since 1.0.15
 */
public interface BinaryOperators {

    /**
     * Returns a {@link BinaryOperator} which returns the first argument it receives.
     *
     * @param <T> the type of the input arguments of the operator
     * @return a {@link BinaryOperator} which returns the first argument it receives
     */
    static <T> BinaryOperator<T> useFirst() {
        return (t1, t2) -> t1;
    }

    /**
     * Returns a {@link BinaryOperator} which returns the last argument it receives.
     *
     * @param <T> the type of the input arguments of the operator
     * @return a {@link BinaryOperator} which returns the last argument it receives
     */
    static <T> BinaryOperator<T> useLast() {
        return (t1, t2) -> t2;
    }

    /**
     * Returns a {@link BinaryOperator} which returns the first non-null argument it receives.
     *
     * @param <T> the type of the input arguments of the operator
     * @return a {@link BinaryOperator} which returns the first non-null argument it receives
     */
    static <T> BinaryOperator<T> useFirstNonNull() {
        return (t1, t2) -> t1 != null ? t1 : t2;
    }

    /**
     * Returns a {@link BinaryOperator} which returns the last non-null argument it receives.
     *
     * @param <T> the type of the input arguments of the operator
     * @return a {@link BinaryOperator} which returns the last non-null argument it receives
     */
    static <T> BinaryOperator<T> useLastNonNull() {
        return (t1, t2) -> t2 != null ? t2 : t1;
    }

}
