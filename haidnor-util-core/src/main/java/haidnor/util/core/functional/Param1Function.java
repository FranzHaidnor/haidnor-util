package haidnor.util.core.functional;

@FunctionalInterface
public interface Param1Function<T, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param t the function argument
     * @return the function result
     */
    R apply(T t);
}