package com.rybindev.simulation.system;

import com.rybindev.ecs.Coordinator;
import com.rybindev.ecs.Systemable;
import com.rybindev.simulation.Graphics;
import com.rybindev.simulation.Point;
import com.rybindev.simulation.component.PositionComponent;
import com.rybindev.simulation.component.SpriteComponent;
import lombok.Setter;
@Setter
public class GraphicsSystem extends Systemable {

    private Coordinator coordinator;
    private Graphics graphics;


    public void update(){
        graphics.clear();
        getEntities().stream().forEach(entity -> {
            PositionComponent position = coordinator.getComponent(entity, PositionComponent.class);
            String sprite = coordinator.getComponent(entity, SpriteComponent.class).getSprite();
            graphics.draw(sprite,new Point(position.getX(),position.getY()));

        });
        graphics.print();
    }
}
