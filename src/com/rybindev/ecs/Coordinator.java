package com.rybindev.ecs;

public class Coordinator {
    private ComponentManager componentManager;
    private EntityManager entityManager;
    private SystemManager systemManager;
    private int maxEntities;

    public Coordinator(int maxEntities) {
        this.maxEntities = maxEntities;
        componentManager = new ComponentManager(maxEntities);
        entityManager = new EntityManager(maxEntities);
        systemManager = new SystemManager();
    }

    public Entity createEntity() {
        return entityManager.createEntity();
    }

    public void destroyEntity(Entity entity) {
        entityManager.destroyEntity(entity);
        componentManager.entityDestroyed(entity);
        systemManager.entityDestroyed(entity);
    }


    public <T> void registerComponent(Class<T> componentClass) {
        componentManager.registerComponent(componentClass);
    }

    public <T> void addComponent(Entity entity, T component) {
        componentManager.addComponent(entity, component);
        Signature signature = entityManager.getSignature(entity);
        signature.set(componentManager.getComponentType(component.getClass()).value(), true);
        entityManager.setSignature(entity, signature);
        systemManager.entitySignatureChanged(entity, signature);
    }

    public <T> void removeComponent(Entity entity, Class<T> componentClass) {
        componentManager.removeComponent(entity, componentClass);
        Signature signature = entityManager.getSignature(entity);
        signature.set(componentManager.getComponentType(componentClass).value(), false);
        entityManager.setSignature(entity, signature);
        systemManager.entitySignatureChanged(entity, signature);
    }

    public <T> T getComponent(Entity entity, Class<T> componentClass) {
        return componentManager.getComponent(entity, componentClass);
    }

    public <T> ComponentType getComponentType(Class<T> componentClass) {
        return componentManager.getComponentType(componentClass);
    }

    public <T extends Systemable> T registerSystem(Class<T> systemClass) {
        return systemManager.registerSystem(systemClass);
    }

    public <T extends Systemable> void setSystemSignature(Class<T> systemClass, Signature signature) {
        systemManager.setSignature(systemClass, signature);
    }
}
