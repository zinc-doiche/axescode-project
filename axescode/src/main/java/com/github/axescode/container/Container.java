package com.github.axescode.container;

import java.util.*;

public class Container<D extends Data> {
    protected final Map<String, D> container = new HashMap<>();

    public List<D> getAll() {
        return container.values().stream().toList();
    }
    public void addData(String key, D data) {
        container.put(key, data);
    }
    public D getData(String key) {
        return container.get(key);
    }
    public Optional<D> getData(Long key) {
        return container.values().stream().filter(d -> Objects.equals(d.key(), key)).findFirst();
    }
    public boolean hasData(String key) {
        return container.containsKey(key);
    }
    public void removeData(String key) {
        container.remove(key);
    }
    public boolean isEmpty() {
        return container.isEmpty();
    }
}
