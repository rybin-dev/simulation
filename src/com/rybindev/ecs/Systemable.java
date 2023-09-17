package com.rybindev.ecs;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public abstract class Systemable {
    @Getter
    protected Set<Entity> entities = new HashSet<>();

    public abstract void update();
}
