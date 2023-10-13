package haidnor.util.core.functional;

@FunctionalInterface
public interface Param3Function<T, E, S, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @param e the function argument
     * @param s the function argument
     * @return the function result
     */
    R apply(T t, E e, S s);
}