package com.rybindev.simulation.system;

import com.rybindev.ecs.Coordinator;
import com.rybindev.ecs.Systemable;
import com.rybindev.simulation.component.DamageRequestComponent;
import com.rybindev.simulation.component.HealthComponent;
import lombok.Setter;

@Setter
public class DamageSystem extends Systemable {
    protected Coordinator coordinator;

    @Override
    public void update() {
        getEntities().stream()
                .toList()
                .forEach(entity -> {
                    HealthComponent health = coordinator.getComponent(entity, HealthComponent.class);
                    DamageRequestComponent damageRequest = coordinator.getComponent(entity, DamageRequestComponent.class);

                    health.setHealth(health.getHealth() - damageRequest.getDamage());

                    if (health.getHealth() < 1) {
                        coordinator.destroyEntity(entity);
                    } else {
                        coordinator.removeComponent(entity, DamageRequestComponent.class);
                    }

                });
    }
}
