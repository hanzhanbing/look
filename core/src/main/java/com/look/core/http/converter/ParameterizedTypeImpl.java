package com.look.core.http.converter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by huyg on 2020-02-12.
 */
public class ParameterizedTypeImpl  implements ParameterizedType {
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;


    ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
        // Require an owner type if the raw type needs it.
        if (rawType instanceof Class<?>
                && (ownerType == null) != (((Class<?>) rawType).getEnclosingClass() == null)) {
            throw new IllegalArgumentException();
        }
        this.ownerType = ownerType;
        this.rawType = rawType;
        this.typeArguments = typeArguments.clone();
    }

    @NonNull
    @Override
    public Type[] getActualTypeArguments() {
        return typeArguments.clone();
    }

    @NonNull
    @Override
    public Type getRawType() {
        return rawType;
    }

    @Nullable
    @Override
    public Type getOwnerType() {
        return ownerType;
    }
}
