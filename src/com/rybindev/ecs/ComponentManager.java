package com.rybindev.ecs;

import java.util.HashMap;
import java.util.Map;

public class ComponentManager {
    private Map<Class<?>, ComponentType> componentTypes;
    private Map<Class<?>, ComponentArrayable<?>> componentArrays;
    private ComponentType nextComponentType = new ComponentType(0);
    private int maxEntities;

    public ComponentManager(int maxEntities) {
        this.maxEntities = maxEntities;
        this.componentTypes = new HashMap<>();
        this.componentArrays = new HashMap<>();
    }

    public <T> void registerComponent(Class<T> typeName) {
        assert !componentTypes.containsKey(typeName) : "Registering component type more than once.";

        componentTypes.put(typeName, nextComponentType);
        componentArrays.put(typeName, new ComponentArray<T>(maxEntities));
        nextComponentType = new ComponentType(nextComponentType.value() + 1);
    }

    public <T> ComponentType getComponentType(Class<T> typeName) {
        assert componentTypes.containsKey(typeName) : "Component not registered before use.";

        return componentTypes.get(typeName);
    }

    public <T> void addComponent(Entity entity, T component) {
        Class<?> typeName = component.getClass();

        assert componentTypes.containsKey(typeName) : "Component not registered before use.";

        ComponentArray<T> componentArray = (ComponentArray<T>) componentArrays.get(typeName);
        componentArray.insertData(entity, component);
    }

    public <T> void removeComponent(Entity entity, Class<T> typeName) {
        assert componentTypes.containsKey(typeName) : "Component not registered before use.";

        ComponentArray<T> componentArray = (ComponentArray<T>) componentArrays.get(typeName);
        componentArray.removeData(entity);
    }

    public <T> T getComponent(Entity entity, Class<T> typeName) {
        assert componentTypes.containsKey(typeName) : "Component not registered before use.";

        ComponentArray<T> componentArray = (ComponentArray<T>) componentArrays.get(typeName);
        return componentArray.getData(entity);
    }

    public void entityDestroyed(Entity entity) {
        for (Map.Entry<Class<?>, ComponentArrayable<?>> entry : componentArrays.entrySet()) {
            ComponentArrayable<?> componentArray = entry.getValue();
            componentArray.entityDestroyed(entity);
        }
    }
}
