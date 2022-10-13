package de.veraimt.litelocker.utils;

public interface MixinAccessible<T> {

    T get();

    T set(T newVal);
}
