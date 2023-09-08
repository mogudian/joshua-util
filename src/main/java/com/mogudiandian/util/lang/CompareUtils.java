package com.mogudiandian.util.lang;

import com.mogudiandian.util.function.TriPredicate;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

/**
 * 比较工具
 *
 * @author sunbo
 * @since 1.0.9
 */
public final class CompareUtils {

    private CompareUtils() {}

    /**
     * Returns true if the two given objects are equivalent.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if the two objects are equivalent, false otherwise
     */
    public static <T extends Comparable<T>> boolean isEquivalent(T o1, T o2) {
        if (o1 == null) {
            return o2 == null;
        }
        if (o2 == null) {
            return false;
        }
        return o1.compareTo(o2) == 0;
    }

    /**
     * Returns true if the first object is less than the second object,
     * indicating that a sequence of objects is increasing.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if o1 is less than o2, false otherwise
     */
    public static <T extends Comparable<T>> boolean isIncreasing(T o1, T o2) {
        if (o1 == null) {
            return o2 != null;
        }
        if (o2 == null) {
            return false;
        }
        return o1.compareTo(o2) < 0;
    }

    /**
     * Returns true if the first object is less than or equal to the second object,
     * indicating that a sequence of objects is increasing or equivalent.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if o1 is less than or equal to o2, false otherwise
     */
    public static <T extends Comparable<T>> boolean isIncreasingOrEquivalent(T o1, T o2) {
        if (o1 == null) {
            return true;
        }
        if (o2 == null) {
            return false;
        }
        return o1.compareTo(o2) <= 0;
    }

    /**
     * Returns true if the first object is greater than the second object,
     * indicating that a sequence of objects is decreasing.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if o1 is greater than o2, false otherwise
     */
    public static <T extends Comparable<T>> boolean isDecreasing(T o1, T o2) {
        if (o2 == null) {
            return o1 != null;
        }
        if (o1 == null) {
            return false;
        }
        return o1.compareTo(o2) > 0;
    }

    /**
     * Returns true if the first object is greater than or equal to the second object,
     * indicating that a sequence of objects is decreasing or equivalent.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if o1 is greater than or equal to o2, false otherwise
     */
    public static <T extends Comparable<T>> boolean isDecreasingOrEquivalent(T o1, T o2) {
        if (o2 == null) {
            return true;
        }
        if (o1 == null) {
            return false;
        }
        return o1.compareTo(o2) > 0;
    }

    /**
     * Compares elements in a stream based on a given predicate.
     *
     * @param stream the stream of objects to compare
     * @param predicate the predicate defining the comparison logic for the objects
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all elements in the stream satisfy the predicate, false otherwise
     */
    private static <T extends Comparable<T>> boolean compare(Stream<T> stream, BiPredicate<T, T> predicate) {
        Iterator<T> iterator = stream.iterator();
        for (T current = iterator.next(), next; iterator.hasNext(); current = next) {
            next = iterator.next();
            if (!predicate.test(current, next)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if multiple objects are equivalent based on a given comparison logic.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os the remaining objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are equivalent based on the comparison logic, false otherwise
     */
    public static <T extends Comparable<T>> boolean isEquivalent(T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(stream, CompareUtils::isEquivalent);
    }

    /**
     * Checks if multiple objects are in increasing order based on their natural ordering.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os the remaining objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are in increasing order, false otherwise
     */
    public static <T extends Comparable<T>> boolean isIncreasing(T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(stream, CompareUtils::isIncreasing);
    }

    /**
     * Checks if multiple objects are in increasing order or equivalent based on their natural ordering.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os the remaining objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are in increasing order or equivalent, false otherwise
     */
    public static <T extends Comparable<T>> boolean isIncreasingOrEquivalent(T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(stream, CompareUtils::isIncreasingOrEquivalent);
    }

    /**
     * Checks if multiple objects are in decreasing order based on their natural ordering.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os the remaining objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are in decreasing order, false otherwise
     */
    public static <T extends Comparable<T>> boolean isDecreasing(T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(stream, CompareUtils::isDecreasing);
    }

    /**
     * Checks if multiple objects are in decreasing order or equivalent based on their natural ordering.
     *
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os the remaining objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are in decreasing order or equivalent, false otherwise
     */
    public static <T extends Comparable<T>> boolean isDecreasingOrEquivalent(T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(stream, CompareUtils::isDecreasingOrEquivalent);
    }

    /**
     * Checks if multiple objects are equivalent based on their natural ordering.
     *
     * @param os the objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are equivalent, false otherwise
     */
    public static <T extends Comparable<T>> boolean isEquivalent(T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(Arrays.stream(os), CompareUtils::isEquivalent);
    }

    /**
     * Checks if multiple objects are in increasing order based on their natural ordering.
     *
     * @param os the objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are in increasing order, false otherwise
     */
    public static <T extends Comparable<T>> boolean isIncreasing(T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(Arrays.stream(os), CompareUtils::isIncreasing);
    }

    /**
     * Checks if multiple objects are in increasing or equivalent order based on their natural ordering.
     *
     * @param os the objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are in increasing or equivalent order, false otherwise
     */
    public static <T extends Comparable<T>> boolean isIncreasingOrEquivalent(T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(Arrays.stream(os), CompareUtils::isIncreasingOrEquivalent);
    }

    /**
     * Checks if multiple objects are in decreasing order based on their natural ordering.
     *
     * @param os the objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are in decreasing order, false otherwise
     */
    public static <T extends Comparable<T>> boolean isDecreasing(T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(Arrays.stream(os), CompareUtils::isDecreasing);
    }

    /**
     * Checks if multiple objects are in decreasing order or equivalent based on their natural ordering.
     *
     * @param os the objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects are in decreasing order or equivalent, false otherwise
     */
    public static <T extends Comparable<T>> boolean isDecreasingOrEquivalent(T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(Arrays.stream(os), CompareUtils::isDecreasingOrEquivalent);
    }

    /**
     * Checks if all elements in a collection are equivalent based on their natural ordering.
     *
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects in the collection are equivalent, false otherwise
     */
    public static <T extends Comparable<T>> boolean isEquivalent(Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(collection.stream(), CompareUtils::isEquivalent);
    }

    /**
     * Checks if all elements in a collection are in increasing order based on their natural ordering.
     *
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects in the collection are in increasing order, false otherwise
     */
    public static <T extends Comparable<T>> boolean isIncreasing(Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(collection.stream(), CompareUtils::isIncreasing);
    }

    /**
     * Checks if all elements in a collection are in increasing order or equivalent based on their natural ordering.
     *
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects in the collection are in increasing order or equivalent, false otherwise
     */
    public static <T extends Comparable<T>> boolean isIncreasingOrEquivalent(Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(collection.stream(), CompareUtils::isIncreasingOrEquivalent);
    }

    /**
     * Checks if all elements in a collection are in decreasing order based on their natural ordering.
     *
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects in the collection are in decreasing order, false otherwise
     */
    public static <T extends Comparable<T>> boolean isDecreasing(Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(collection.stream(), CompareUtils::isDecreasing);
    }

    /**
     * Checks if all elements in a collection are in decreasing or equivalent order based on their natural ordering.
     *
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared, must implement the Comparable interface
     * @return true if all objects in the collection are in decreasing or equivalent order, false otherwise
     */
    public static <T extends Comparable<T>> boolean isDecreasingOrEquivalent(Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(collection.stream(), CompareUtils::isDecreasingOrEquivalent);
    }

    /**
     * Compares elements in a stream using the specified comparator and predicate.
     *
     * @param comparator the comparator used for comparing elements
     * @param stream the stream of elements to compare
     * @param predicate the predicate defining the comparison logic for the objects
     * @param <T> the type of the objects being compared
     * @return true if all elements in the stream satisfy the given predicate, false otherwise
     */
    private static <T> boolean compare(Comparator<T> comparator, Stream<T> stream, TriPredicate<Comparator<T>, T, T> predicate) {
        Iterator<T> iterator = stream.iterator();
        for (T current = iterator.next(), next; iterator.hasNext(); current = next) {
            next = iterator.next();
            if (!predicate.test(comparator, current, next)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines if two objects are equivalent using the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared
     * @return true if the objects are equivalent according to the comparator, false otherwise
     */
    public static <T> boolean isEquivalent(Comparator<T> comparator, T o1, T o2) {
        if (o1 == null) {
            return o2 == null;
        }
        if (o2 == null) {
            return false;
        }
        return comparator.compare(o1, o2) == 0;
    }

    /**
     * Determines if the second object is greater than the first object based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared
     * @return true if the second object is greater than the first object according to the comparator, false otherwise
     */
    public static <T> boolean isIncreasing(Comparator<T> comparator, T o1, T o2) {
        if (o1 == null) {
            return o2 != null;
        }
        if (o2 == null) {
            return false;
        }
        return comparator.compare(o1, o2) < 0;
    }

    /**
     * Determines if the second object is greater than or equal to the first object based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared
     * @return true if the second object is greater than or equal to the first object according to the comparator, false otherwise
     */
    public static <T> boolean isIncreasingOrEquivalent(Comparator<T> comparator, T o1, T o2) {
        if (o1 == null) {
            return true;
        }
        if (o2 == null) {
            return false;
        }
        return comparator.compare(o1, o2) <= 0;
    }

    /**
     * Determines if the second object is less than the first object based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared
     * @return true if the second object is less than the first object according to the comparator, false otherwise
     */
    public static <T> boolean isDecreasing(Comparator<T> comparator, T o1, T o2) {
        if (o2 == null) {
            return o1 != null;
        }
        if (o1 == null) {
            return false;
        }
        return comparator.compare(o1, o2) > 0;
    }

    /**
     * Determines if the second object is less than or equal to the first object based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param <T> the type of the objects being compared
     * @return true if the second object is less than or equal to the first object according to the comparator, false otherwise
     */
    public static <T> boolean isDecreasingOrEquivalent(Comparator<T> comparator, T o1, T o2) {
        if (o2 == null) {
            return true;
        }
        if (o1 == null) {
            return false;
        }
        return comparator.compare(o1, o2) >= 0;
    }

    /**
     * Determines if all the objects in the given arguments are equivalent based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os additional objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects are equivalent according to the comparator, false otherwise
     */
    public static <T> boolean isEquivalent(Comparator<T> comparator, T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(comparator, stream, CompareUtils::isEquivalent);
    }

    /**
     * Determines if all the objects in the given arguments are in increasing order based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os additional objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects are in increasing order according to the comparator, false otherwise
     */
    public static <T> boolean isIncreasing(Comparator<T> comparator, T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(comparator, stream, CompareUtils::isIncreasing);
    }

    /**
     * Determines if all the objects in the given arguments are in increasing order or equivalent based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os additional objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects are in increasing order or equivalent according to the comparator, false otherwise
     */
    public static <T> boolean isIncreasingOrEquivalent(Comparator<T> comparator, T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(comparator, stream, CompareUtils::isIncreasingOrEquivalent);
    }

    /**
     * Determines if all the objects in the given arguments are in decreasing order based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os additional objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects are in decreasing order according to the comparator, false otherwise
     */
    public static <T> boolean isDecreasing(Comparator<T> comparator, T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(comparator, stream, CompareUtils::isDecreasing);
    }

    /**
     * Determines if all the objects in the given arguments are in decreasing order or are equivalent based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param o1 the first object to compare
     * @param o2 the second object to compare
     * @param o3 the third object to compare
     * @param os additional objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects are in decreasing order or are equivalent according to the comparator, false otherwise
     */
    public static <T> boolean isDecreasingOrEquivalent(Comparator<T> comparator, T o1, T o2, T o3, T... os) {
        Stream<T> stream = Stream.concat(Stream.of(o1, o2, o3), Optional.ofNullable(os).map(Arrays::stream).orElseGet(Stream::empty));
        return compare(comparator, stream, CompareUtils::isDecreasingOrEquivalent);
    }

    /**
     * Determines if all the objects in the given array are equivalent based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param os the array of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the array are equivalent according to the comparator, false otherwise
     */
    public static <T> boolean isEquivalent(Comparator<T> comparator,T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(comparator, Arrays.stream(os), CompareUtils::isEquivalent);
    }

    /**
     * Determines if all the objects in the given array are in increasing order based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param os the array of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the array are in increasing order according to the comparator, false otherwise
     */
    public static <T> boolean isIncreasing(Comparator<T> comparator,T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(comparator, Arrays.stream(os), CompareUtils::isIncreasing);
    }

    /**
     * Determines if all the objects in the given array are in increasing order or equivalent based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param os the array of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the array are in increasing order or equivalent according to the comparator, false otherwise
     */
    public static <T> boolean isIncreasingOrEquivalent(Comparator<T> comparator,T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(comparator, Arrays.stream(os), CompareUtils::isIncreasingOrEquivalent);
    }

    /**
     * Determines if all the objects in the given array are in decreasing order based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param os the array of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the array are in decreasing order according to the comparator, false otherwise
     */
    public static <T> boolean isDecreasing(Comparator<T> comparator,T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(comparator, Arrays.stream(os), CompareUtils::isDecreasing);
    }

    /**
     * Determines if all the objects in the given array are in decreasing or equivalent order based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param os the array of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the array are in decreasing or equivalent order according to the comparator, false otherwise
     */
    public static <T> boolean isDecreasingOrEquivalent(Comparator<T> comparator,T[] os) {
        if (os.length < 2) {
            return false;
        }
        return compare(comparator, Arrays.stream(os), CompareUtils::isDecreasingOrEquivalent);
    }

    /**
     * Determines if all the objects in the given collection are equivalent based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the collection are equivalent according to the comparator, false otherwise
     */
    public static <T> boolean isEquivalent(Comparator<T> comparator,Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(comparator, collection.stream(), CompareUtils::isEquivalent);
    }

    /**
     * Determines if all the objects in the given collection are in increasing order based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the collection are in increasing order according to the comparator, false otherwise
     */
    public static <T> boolean isIncreasing(Comparator<T> comparator,Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(comparator, collection.stream(), CompareUtils::isIncreasing);
    }

    /**
     * Determines if all the objects in the given collection are in increasing or equivalent order based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the collection are in increasing or equivalent order according to the comparator, false otherwise
     */
    public static <T> boolean isIncreasingOrEquivalent(Comparator<T> comparator,Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(comparator, collection.stream(), CompareUtils::isIncreasingOrEquivalent);
    }

    /**
     * Determines if all the objects in the given collection are in decreasing order based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the collection are in decreasing order according to the comparator, false otherwise
     */
    public static <T> boolean isDecreasing(Comparator<T> comparator,Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(comparator, collection.stream(), CompareUtils::isDecreasing);
    }

    /**
     * Determines if all the objects in the given collection are in decreasing or equivalent order based on the specified comparator.
     *
     * @param comparator the comparator used for comparing the objects
     * @param collection the collection of objects to compare
     * @param <T> the type of the objects being compared
     * @return true if all the objects in the collection are in decreasing or equivalent order according to the comparator, false otherwise
     */
    public static <T> boolean isDecreasingOrEquivalent(Comparator<T> comparator,Collection<T> collection) {
        if (collection.size() < 2) {
            return false;
        }
        return compare(comparator, collection.stream(), CompareUtils::isDecreasingOrEquivalent);
    }

}
