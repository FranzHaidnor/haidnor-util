package haidnor.util.core.functional;

@FunctionalInterface
public interface Param4Function<T, E, S, V, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @param e the function argument
     * @param s the function argument
     * @param v the function argument
     * @return the function result
     */
    R apply(T t, E e, S s, V v);
}