package com.rybindev.ecs;

import java.util.HashMap;
import java.util.Map;

public class ComponentArray<T> implements ComponentArrayable<T> {
    private Object[] componentArray;
    private Map<Entity, Integer> entityToIndexMap;
    private Map<Integer, Entity> indexToEntityMap;
    private int mSize;

    public ComponentArray(int maxEntities) {
        componentArray = new Object[maxEntities];
        entityToIndexMap = new HashMap<>();
        indexToEntityMap = new HashMap<>();
        mSize = 0;
    }

    public void insertData(Entity entity, T component) {
        assert !entityToIndexMap.containsKey(entity) : "Component added to same entity more than once.";

        int newIndex = mSize;
        entityToIndexMap.put(entity, newIndex);
        indexToEntityMap.put(newIndex, entity);
        componentArray[newIndex] = component;
        mSize++;
    }

    public void removeData(Entity entity) {
        assert entityToIndexMap.containsKey(entity) : "Removing non-existent component.";

        int indexOfRemovedEntity = entityToIndexMap.get(entity);
        int indexOfLastElement = mSize - 1;

        componentArray[indexOfRemovedEntity] = componentArray[indexOfLastElement];
        Entity entityOfLastElement = indexToEntityMap.get(indexOfLastElement);

        entityToIndexMap.put(entityOfLastElement, indexOfRemovedEntity);
        indexToEntityMap.put(indexOfRemovedEntity, entityOfLastElement);

        entityToIndexMap.remove(entity);
        indexToEntityMap.remove(indexOfLastElement);

        mSize--;
    }

    public T getData(Entity entity) {
        assert entityToIndexMap.containsKey(entity) : "Retrieving non-existent component.";

        int index = entityToIndexMap.get(entity);
        return (T) componentArray[index];
    }

    @Override
    public void entityDestroyed(Entity entity) {
        if (entityToIndexMap.containsKey(entity)) {
            removeData(entity);
        }
    }
}