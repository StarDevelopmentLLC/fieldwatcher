package com.stardevllc.fieldwatcher;

import com.stardevllc.helper.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FieldWatcher {
    
    private Object object; //The object that holds the field
    private Field field; //The field itself
    private Object value; //The value of the field to detect changes
    
    private List<FieldChangeListener> changeListeners = new ArrayList<>();
    
    private final Thread thread;
    
    public FieldWatcher(Object object, String fieldName) {
        this.object = object;
        this.field = ReflectionHelper.getClassField(object.getClass(), fieldName);
        if (this.field == null) {
            throw new IllegalArgumentException("Invalid Field name " + fieldName + " for class " + object.getClass().getName());
        }
        
        field.setAccessible(true);
        
        updateValue();
        
        this.thread = Thread.ofVirtual().start(() -> {
            //noinspection InfiniteLoopStatement
            while (true) {
                update();
            }
        });
    }
    
    public void update() {
        Object oldValue = this.value;
        updateValue();
        Object newValue = this.value;
        
        if (!Objects.equals(oldValue, newValue)) {
            callChangeListeners(oldValue, newValue);
        }
    }
    
    private void callChangeListeners(Object oldValue, Object newValue) {
        this.changeListeners.forEach(cl -> cl.changed(this.object, this.field, oldValue, newValue));
    }
    
    private void updateValue() {
        try {
            this.value = field.get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    public void addChangeListener(FieldChangeListener listener) {
        this.changeListeners.add(listener);
    }
    
    public void removeChangeListener(FieldChangeListener listener) {
        this.changeListeners.remove(listener);
    }

    public Object getObject() {
        return object;
    }

    public Field getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object1) {
        if (object1 == null || getClass() != object1.getClass()) return false;

        FieldWatcher that = (FieldWatcher) object1;
        return Objects.equals(object, that.object) && Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(object);
        result = 31 * result + Objects.hashCode(field);
        return result;
    }
}
