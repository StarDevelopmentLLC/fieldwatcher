package com.stardevllc.fieldwatcher;

import java.lang.reflect.Field;

@FunctionalInterface
public interface FieldChangeListener {
    void changed(Object object, Field field, Object oldValue, Object newValue);
}
