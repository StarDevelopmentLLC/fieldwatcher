package com.stardevllc.fieldwatcher;

import java.util.HashSet;
import java.util.Set;

public class WatcherManager {
    private final Set<FieldWatcher> fieldWatchers = new HashSet<>();

    public WatcherManager() {}

    public FieldWatcher watchField(Object object, String fieldName) {
        FieldWatcher watcher = new FieldWatcher(object, fieldName);
        if (!this.fieldWatchers.contains(watcher)) {
            this.fieldWatchers.add(watcher);
            return watcher;
        } else {
            for (FieldWatcher fieldWatcher : this.fieldWatchers) {
                if (fieldWatcher.equals(watcher)) {
                    return fieldWatcher;
                }
            }
        }

        return null;
    }
}
