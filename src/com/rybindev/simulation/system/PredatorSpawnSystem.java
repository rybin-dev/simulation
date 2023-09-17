package com.rybindev.simulation.system;

import com.rybindev.simulation.Config;

public class PredatorSpawnSystem extends SpawnSystem {

    @Override
    public void update() {
        if (getEntities().size() < Config.MIN_PREDATOR) {
            while (getEntities().size() < Config.MAX_PREDATOR) {
                createPredator(
                        random.nextInt(Config.WIDTH - 1),
                        random.nextInt(Config.HEIGHT - 1),
                        Config.PREDATOR_HEALTH,
                        Config.PREDATOR_VELOCITY,
                        Config.PREDATOR_ATTACK,
                        Config.PREDATOR_SPRITE);

            }
        }
    }


}
