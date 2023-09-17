package com.rybindev.simulation.system;

import com.rybindev.ecs.Entity;
import com.rybindev.simulation.Config;
import com.rybindev.simulation.Point;
import com.rybindev.simulation.component.AttackComponent;
import com.rybindev.simulation.component.DamageRequestComponent;
import com.rybindev.simulation.component.HealthComponent;
import com.rybindev.simulation.component.PositionComponent;

import java.util.List;

public class PredatorMovementSystem extends MovementSystem {

    @Override
    public void update() {
        for (Entity entity : entities) {
            PositionComponent source = coordinator.getComponent(entity, PositionComponent.class);
            PositionComponent target = targetSearchSystem.getTarget(source);

            if (target == null) {
                continue;
            }

            List<Point> path = findPathSystem.findPath(source, target);

            if (path.isEmpty()) continue;

            if (path.size() <= 2) {
                HealthComponent healthTarget = coordinator.getComponent(target.getEntity(), HealthComponent.class);
                AttackComponent attack = coordinator.getComponent(entity, AttackComponent.class);

                healthTarget.setHealth(healthTarget.getHealth() - attack.getAttack());

                if (healthTarget.getHealth() < 1){
                    coordinator.destroyEntity(target.getEntity());

                    HealthComponent health = coordinator.getComponent(entity, HealthComponent.class);
                    health.setHealth(health.getHealth() + Config.PREDATOR_PLUS);
                }else {
                    AttackComponent attackTarget = coordinator.getComponent(target.getEntity(), AttackComponent.class);
                    coordinator.addComponent(entity,new DamageRequestComponent(target.getEntity(),attackTarget.getAttack()));
                }
            } else {
                Point newPosition = path.get(Math.min(path.size()-2, Config.PREDATOR_VELOCITY));
                source.setX(newPosition.x());
                source.setY(newPosition.y());
            }

        }


    }
}
