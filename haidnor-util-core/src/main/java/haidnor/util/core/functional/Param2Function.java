package haidnor.util.core.functional;

@FunctionalInterface
public interface Param2Function<T, E, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @param e the function argument
     * @return the function result
     */
    R apply(T t, E e);
}