package com.rybindev.ecs;

import java.util.HashMap;
import java.util.Map;

public class SystemManager {
    private Map<Class<?>, Signature> signatures = new HashMap<>();
    private Map<Class<?>, Systemable> systems = new HashMap<>();

    public <T extends Systemable> T registerSystem(Class<T> typeName) {
        assert !systems.containsKey(typeName) : "Registering system more than once.";

        try {
            T system = typeName.getDeclaredConstructor().newInstance();
            systems.put(typeName, system);
            return system;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create an instance of the system.");
        }

    }

    public <T extends Systemable> void setSignature(Class<T> typeName, Signature signature) {
        assert systems.containsKey(typeName) : "System used before registered.";

        signatures.put(typeName, signature);
    }

    public void entityDestroyed(Entity entity) {
        for (Systemable systemable : systems.values()) {
            systemable.getEntities().remove(entity);
        }
    }

    public void entitySignatureChanged(Entity entity, Signature entitySignature) {
        for (Map.Entry<Class<?>, Systemable> entry : systems.entrySet()) {
            Class<?> typeName = entry.getKey();
            Systemable systemable = entry.getValue();
            Signature systemSignature = signatures.get(typeName);
            Signature clone = (Signature) systemSignature.clone();
            clone.and(entitySignature);
            if (clone.equals(systemSignature)) {
                systemable.getEntities().add(entity);
            } else {
                systemable.getEntities().remove(entity);
            }
        }
    }
}
