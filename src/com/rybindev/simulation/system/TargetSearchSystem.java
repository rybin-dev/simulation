package com.rybindev.simulation.system;

import com.rybindev.ecs.Coordinator;
import com.rybindev.ecs.Systemable;
import com.rybindev.simulation.component.PositionComponent;
import lombok.Setter;

import java.util.Comparator;

@Setter
public abstract class TargetSearchSystem extends Systemable {
    protected Coordinator coordinator;
    @Override
    public void update() {
    }

    public PositionComponent getTarget(PositionComponent source){
       return getEntities().stream()
                .map(entity -> coordinator.getComponent(entity, PositionComponent.class))
                .min(Comparator.comparing(p -> Math.abs(p.getX() - source.getX()) + Math.abs(p.getY()) - source.getY()))
               .orElse(null);

    }

}
