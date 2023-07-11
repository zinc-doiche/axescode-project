package com.github.axescode.container;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public boolean hasData(String key) {
        return container.containsKey(key);
    }
    public void removeData(String key) {
        container.remove(key);
    }
}
