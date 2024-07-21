package de.veraimt.litelocker.utils;

import org.jetbrains.annotations.Nullable;

public interface MixinDirectAccessible<T> extends MixinAccessible<T> {
    @Nullable
    T getDirect();

    @Override
    default T get() {
        return getDirect();
    }
}
