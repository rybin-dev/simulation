package com.rybindev.simulation.system;

import com.rybindev.ecs.Coordinator;
import com.rybindev.ecs.Entity;
import com.rybindev.ecs.Systemable;
import com.rybindev.simulation.component.AttackComponent;
import com.rybindev.simulation.component.CrassMarker;
import com.rybindev.simulation.component.CreatureMarker;
import com.rybindev.simulation.component.HealthComponent;
import com.rybindev.simulation.component.HerbivoreMarker;
import com.rybindev.simulation.component.MovementComponent;
import com.rybindev.simulation.component.PositionComponent;
import com.rybindev.simulation.component.PredatorMarker;
import com.rybindev.simulation.component.SpriteComponent;
import lombok.Setter;

import java.util.Random;
@Setter
public abstract class SpawnSystem extends Systemable {
    protected Coordinator coordinator;
    protected Random random;



    protected Entity createCrass(int x, int y, String sprite) {
        Entity crass = coordinator.createEntity();

        coordinator.addComponent(crass, new PositionComponent(crass, x, y));
        coordinator.addComponent(crass, new SpriteComponent(sprite));
        coordinator.addComponent(crass, new CrassMarker());

        return crass;
    }

    protected Entity createHerbivore(int x, int y, int hp, int velocity, int attack, String sprite) {
        Entity herbivore = coordinator.createEntity();

        coordinator.addComponent(herbivore, new PositionComponent(herbivore, x, y));
        coordinator.addComponent(herbivore, new HealthComponent(hp));
        coordinator.addComponent(herbivore, new MovementComponent(velocity));
        coordinator.addComponent(herbivore, new AttackComponent(attack));
        coordinator.addComponent(herbivore, new SpriteComponent(sprite));
        coordinator.addComponent(herbivore, new HerbivoreMarker());
        coordinator.addComponent(herbivore, new CreatureMarker());

        return herbivore;
    }

    protected Entity createPredator(int x, int y, int hp, int velocity, int attack, String sprite) {
        Entity predator = coordinator.createEntity();

        coordinator.addComponent(predator, new PositionComponent(predator, x, y));
        coordinator.addComponent(predator, new HealthComponent(hp));
        coordinator.addComponent(predator, new MovementComponent(velocity));
        coordinator.addComponent(predator, new AttackComponent(attack));
        coordinator.addComponent(predator, new SpriteComponent(sprite));
        coordinator.addComponent(predator, new PredatorMarker());
        coordinator.addComponent(predator, new CreatureMarker());

        return predator;
    }
}
