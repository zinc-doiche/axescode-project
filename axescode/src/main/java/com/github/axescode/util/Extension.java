package com.github.axescode.util;

import java.util.function.Consumer;
import java.util.function.Function;

public interface Extension<T> {
    T apply(Function<T, T> function);
    void apply(Consumer<T> consumer);
}
