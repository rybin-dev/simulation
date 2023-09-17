package com.rybindev.simulation.system;

import com.rybindev.ecs.Coordinator;
import com.rybindev.ecs.Entity;
import com.rybindev.ecs.Systemable;
import com.rybindev.simulation.Astar;
import com.rybindev.simulation.Point;
import com.rybindev.simulation.component.PositionComponent;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
public class FindPathSystem extends Systemable {
    private Coordinator coordinator;

    @Override
    public void update() {

    }

    public List<Point> findPath(PositionComponent source, PositionComponent target) {
        Set<Point> obstacles = getEntities().stream()
                .map(this::getCoordinates)
                .collect(Collectors.toSet());

        return Astar.findPath(toPoint(source), toPoint(target),obstacles::contains);
    }

    private  Point getCoordinates(Entity entity) {
        PositionComponent position = coordinator.getComponent(entity, PositionComponent.class);
        return toPoint(position);
    }

    private static Point toPoint(PositionComponent position){
        return new Point(position.getX(), position.getY());
    }
}
