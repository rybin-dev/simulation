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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

public class SimulationRunner extends JPanel implements ActionListener {


    private static HerbivoreTargetSearchSystem herbivoreTargetSearchSystem;
    private static PredatorTargetSearchSystem predatorTargetSearchSystem;
    private static FindPathSystem findPathSystem;
    private static HerbivoreMovementSystem herbivoreMovementSystem;
    private static PredatorMovementSystem predatorMovementSystem;
    private static DamageSystem damageSystem;
    private static CrassSpawnSystem crassSpawnSystem;
    private static HerbivoreSpawnSystem herbivoreSpawnSystem;
    private static PredatorSpawnSystem predatorSpawnSystem;

    private final int DELAY = 300;

    private Timer timer;
    private Image crass;
    private Image herbivore;
    private Image predator;
    private GraphicsSystem graphicsSystem;
    private Coordinator coordinator;

    public SimulationRunner() {
        init();
    }

    void init() {
        Random random = new Random();

        Graphics graphics = new Graphics(Config.WIDTH, Config.HEIGHT);

        coordinator = new Coordinator(Config.MAX_ENTITIES);


        try {
            for (Class<?> clazz : getClasses("com.rybindev.simulation.component")) {
                coordinator.registerComponent(clazz);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

        graphicsSystem = registerSystem(coordinator, GraphicsSystem.class, SpriteComponent.class, PositionComponent.class);
        graphicsSystem.setCoordinator(coordinator);
        graphicsSystem.setGraphics(graphics);

        herbivoreTargetSearchSystem = registerSystem(coordinator, HerbivoreTargetSearchSystem.class, CrassMarker.class);
        herbivoreTargetSearchSystem.setCoordinator(coordinator);

        predatorTargetSearchSystem = registerSystem(coordinator, PredatorTargetSearchSystem.class, HerbivoreMarker.class);
        predatorTargetSearchSystem.setCoordinator(coordinator);

        findPathSystem = registerSystem(coordinator, FindPathSystem.class, PositionComponent.class);
        findPathSystem.setCoordinator(coordinator);

        herbivoreMovementSystem = registerSystem(coordinator, HerbivoreMovementSystem.class, HerbivoreMarker.class);
        herbivoreMovementSystem.setCoordinator(coordinator);
        herbivoreMovementSystem.setTargetSearchSystem(herbivoreTargetSearchSystem);
        herbivoreMovementSystem.setFindPathSystem(findPathSystem);

        predatorMovementSystem = registerSystem(coordinator, PredatorMovementSystem.class, PredatorMarker.class);
        predatorMovementSystem.setCoordinator(coordinator);
        predatorMovementSystem.setTargetSearchSystem(predatorTargetSearchSystem);
        predatorMovementSystem.setFindPathSystem(findPathSystem);

        damageSystem = registerSystem(coordinator, DamageSystem.class, DamageRequestComponent.class, HealthComponent.class);
        damageSystem.setCoordinator(coordinator);

        crassSpawnSystem = registerSystem(coordinator, CrassSpawnSystem.class, CrassMarker.class);
        crassSpawnSystem.setCoordinator(coordinator);
        crassSpawnSystem.setRandom(random);

        herbivoreSpawnSystem = registerSystem(coordinator, HerbivoreSpawnSystem.class, HerbivoreMarker.class);
        herbivoreSpawnSystem.setCoordinator(coordinator);
        herbivoreSpawnSystem.setRandom(random);

        predatorSpawnSystem = registerSystem(coordinator, PredatorSpawnSystem.class, PredatorMarker.class);
        predatorSpawnSystem.setCoordinator(coordinator);
        predatorSpawnSystem.setRandom(random);

        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(Config.WIDTH*10, Config.HEIGHT*10));
        loadImages();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        crass = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        herbivore = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        predator = iih.getImage();
    }


    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        graphicsSystem.getEntities().forEach(entity -> {
            PositionComponent position = coordinator.getComponent(entity, PositionComponent.class);
            SpriteComponent sprite = coordinator.getComponent(entity, SpriteComponent.class);

            switch (sprite.getSprite()){
                case "v" -> g.drawImage(crass,position.getX()*10,position.getY()*10,this);
                case "@"-> g.drawImage(herbivore,position.getX()*10,position.getY()*10,this);
                case "#"-> g.drawImage(predator,position.getX()*10,position.getY()*10,this);
            }
            Toolkit.getDefaultToolkit().sync();
        });
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame ex = new JFrame();
            ex.add(new SimulationRunner());

            ex.setResizable(false);
            ex.pack();

            ex.setTitle("Simulation");
            ex.setLocationRelativeTo(null);
            ex.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ex.setVisible(true);
        });


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

    @Override
    public void actionPerformed(ActionEvent e) {
        crassSpawnSystem.update();
        herbivoreSpawnSystem.update();
        predatorSpawnSystem.update();
        herbivoreMovementSystem.update();
        predatorMovementSystem.update();
        damageSystem.update();

        repaint();

    }
}
