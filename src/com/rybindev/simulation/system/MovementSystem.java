package com.rybindev.simulation.system;

import com.rybindev.ecs.Coordinator;
import com.rybindev.ecs.Systemable;
import lombok.Setter;

@Setter
public abstract class MovementSystem extends Systemable {
    protected Coordinator coordinator;
    protected TargetSearchSystem targetSearchSystem;
    protected FindPathSystem findPathSystem;

}
