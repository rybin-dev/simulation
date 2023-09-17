package com.rybindev.simulation;

import com.rybindev.ecs.Coordinator;
import com.rybindev.ecs.Signature;
import com.rybindev.ecs.Systemable;
import com.rybindev.simulation.component.CrassMarker;
import com.rybindev.simulation.component.DamageRequestComponent;
import com.rybindev.simulation.component.HealthComponent;
import com.rybindev.simulation.component.HerbivoreMarker;
import com.rybindev.simulation.component.PositionComponent;
import com.rybindev.simulation.component.PredatorMarker;
import com.rybindev.simulation.component.SpriteComponent;
import com.rybindev.simulation.system.CrassSpawnSystem;
import com.rybindev.simulation.system.DamageSystem;
import com.rybindev.simulation.system.FindPathSystem;
import com.rybindev.simulation.system.GraphicsSystem;
import com.rybindev.simulation.system.HerbivoreMovementSystem;
import com.rybindev.simulation.system.HerbivoreSpawnSystem;
import com.rybindev.simulation.system.HerbivoreTargetSearchSystem;
import com.rybindev.simulation.system.PredatorMovementSystem;
import com.rybindev.simulation.system.PredatorSpawnSystem;
import com.rybindev.simulation.system.PredatorTargetSearchSystem;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

public class SimulationRunner {

    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        Random random = new Random();

        Graphics graphics = new Graphics(Config.WIDTH, Config.HEIGHT);

        Coordinator coordinator = new Coordinator(Config.MAX_ENTITIES);


        for (Class<?> clazz : getClasses("com.rybindev.simulation.component")) {
            coordinator.registerComponent(clazz);
        }

        GraphicsSystem graphicsSystem = registerSystem(coordinator, GraphicsSystem.class, SpriteComponent.class, PositionComponent.class);
        graphicsSystem.setCoordinator(coordinator);
        graphicsSystem.setGraphics(graphics);

        HerbivoreTargetSearchSystem herbivoreTargetSearchSystem = registerSystem(coordinator, HerbivoreTargetSearchSystem.class, CrassMarker.class);
        herbivoreTargetSearchSystem.setCoordinator(coordinator);

        PredatorTargetSearchSystem predatorTargetSearchSystem = registerSystem(coordinator, PredatorTargetSearchSystem.class, HerbivoreMarker.class);
        predatorTargetSearchSystem.setCoordinator(coordinator);

        FindPathSystem findPathSystem = registerSystem(coordinator, FindPathSystem.class, PositionComponent.class);
        findPathSystem.setCoordinator(coordinator);

        HerbivoreMovementSystem herbivoreMovementSystem = registerSystem(coordinator, HerbivoreMovementSystem.class, HerbivoreMarker.class);
        herbivoreMovementSystem.setCoordinator(coordinator);
        herbivoreMovementSystem.setTargetSearchSystem(herbivoreTargetSearchSystem);
        herbivoreMovementSystem.setFindPathSystem(findPathSystem);

        PredatorMovementSystem predatorMovementSystem = registerSystem(coordinator, PredatorMovementSystem.class, PredatorMarker.class);
        predatorMovementSystem.setCoordinator(coordinator);
        predatorMovementSystem.setTargetSearchSystem(predatorTargetSearchSystem);
        predatorMovementSystem.setFindPathSystem(findPathSystem);

        DamageSystem damageSystem = registerSystem(coordinator, DamageSystem.class, DamageRequestComponent.class, HealthComponent.class);
        damageSystem.setCoordinator(coordinator);

        CrassSpawnSystem crassSpawnSystem = registerSystem(coordinator, CrassSpawnSystem.class, CrassMarker.class);
        crassSpawnSystem.setCoordinator(coordinator);
        crassSpawnSystem.setRandom(random);

        HerbivoreSpawnSystem herbivoreSpawnSystem = registerSystem(coordinator, HerbivoreSpawnSystem.class, HerbivoreMarker.class);
        herbivoreSpawnSystem.setCoordinator(coordinator);
        herbivoreSpawnSystem.setRandom(random);

        PredatorSpawnSystem predatorSpawnSystem = registerSystem(coordinator, PredatorSpawnSystem.class, PredatorMarker.class);
        predatorSpawnSystem.setCoordinator(coordinator);
        predatorSpawnSystem.setRandom(random);


        for (int i = 0; i < 2000; i++) {
            crassSpawnSystem.update();
            herbivoreSpawnSystem.update();
            predatorSpawnSystem.update();
            graphicsSystem.update();
            herbivoreMovementSystem.update();
            predatorMovementSystem.update();
            damageSystem.update();
            Thread.sleep(10);

        }

    }

    private static <T extends Systemable> T registerSystem(Coordinator coordinator, Class<T> system, Class<?>... components) {
        T registeredSystem = coordinator.registerSystem(system);

        assert components.length > 0;

        Signature signature = new Signature();
        for (Class<?> component : components) {
            signature.set(coordinator.getComponentType(component).value(), true);
        }

        coordinator.setSystemSignature(system, signature);

        return registeredSystem;
    }

    private static Class<?>[] getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        assert classLoader != null;

        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);

        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = (URL) resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }

        return classes.toArray(new Class[classes.size()]);
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();

        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {

                assert !file.getName().contains(".");

                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}
