package com.rybindev.simulation.component;

import com.rybindev.ecs.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PositionComponent {
    private Entity entity;
    private int x;
    private int y;
}
