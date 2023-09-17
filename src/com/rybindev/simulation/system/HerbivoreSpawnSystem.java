package com.rybindev.simulation.system;

import com.rybindev.simulation.Config;

public class HerbivoreSpawnSystem extends SpawnSystem {


    @Override
    public void update() {
        if (getEntities().size() < Config.MIN_HERBIVORE) {
            while (getEntities().size() < Config.MAX_HERBIVORE) {
                createHerbivore(
                        random.nextInt(Config.WIDTH - 1),
                        random.nextInt(Config.HEIGHT - 1),
                        Config.HERBIVORE_HEALTH,
                        Config.HERBIVORE_VELOCITY,
                        Config.HERBIVORE_ATTACK,
                        Config.HERBIVORE_SPRITE);

            }
        }
    }


}
