package com.exozet.android.core.interfaces;


import androidx.annotation.NonNull;

/**
 * Created by Jan Rabe on 25/09/15.
 */
public interface ChainableCommand<T> {

    T execute(@NonNull final T t);
}
