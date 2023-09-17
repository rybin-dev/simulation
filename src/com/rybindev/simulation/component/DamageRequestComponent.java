package com.rybindev.simulation.component;

import com.rybindev.ecs.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DamageRequestComponent {
    private Entity source;
    private int damage;
}
