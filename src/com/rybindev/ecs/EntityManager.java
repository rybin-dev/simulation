package com.rybindev.ecs;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class EntityManager {
    private final Deque<Entity> availableEntities;
    private final List<Signature> signatures;
    private int livingEntityCount;
    private final int maxEntities;

    public EntityManager(int maxEntities) {
        this.maxEntities = maxEntities;
        this.availableEntities = new ArrayDeque<>(maxEntities);
        this.signatures = new ArrayList<>(maxEntities);

        for (int i = 0; i < maxEntities; ++i) {
            this.availableEntities.push(new Entity(i));
        }

        for (int i = 0; i < maxEntities; ++i) {
            this.signatures.add(new Signature());
        }
    }

    public Entity createEntity() {
        assert livingEntityCount < maxEntities : "Too many entities in existence.";

        Entity id = availableEntities.pop();
        livingEntityCount++;
        return id;
    }

    public void destroyEntity(Entity entity) {
        assert (entity.value() < maxEntities);

        signatures.get(entity.value()).clear();
        availableEntities.push(entity);
        livingEntityCount--;
    }

    public void setSignature(Entity entity, Signature signature) {
        assert entity.value() < maxEntities : "Entity out of range.";

        signatures.set(entity.value(), signature);
    }

    public Signature getSignature(Entity entity) {
        assert entity.value() < maxEntities : "Entity out of range.";

        return signatures.get(entity.value());
    }

}
