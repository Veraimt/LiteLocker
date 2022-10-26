package de.veraimt.litelocker.utils;

import javax.annotation.Nullable;

public interface MixinDirectAccessible<T> extends MixinAccessible<T> {
    @Nullable
    T getDirect();

    @Override
    default T get() {
        return getDirect();
    }
}
