package com.rybindev.simulation.system;

import com.rybindev.simulation.Config;



public class CrassSpawnSystem extends SpawnSystem {


    @Override
    public void update() {
        if (getEntities().size() < Config.MIN_CRASS) {
            while (getEntities().size() < Config.MAX_CRASS) {
                createCrass(random.nextInt(Config.WIDTH - 1), random.nextInt(Config.HEIGHT - 1), Config.CRASS_SPRITE);

            }
        }
    }


}
