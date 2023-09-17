package com.rybindev.simulation;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Predicate;

@UtilityClass
public class Astar {


    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(of = "coordinates")
    private static class Node {
        Point coordinates;
        int distanceToStart;
        int distanceToEnd;
        int cost;
        Node parent;

    }

    private static final int[] shiftX = new int[]{1, -1, 0, 0};
    private static final int[] shiftY = new int[]{0, 0, 1, -1};
    private static final int neighborsCount = 4;

    public static List<Point> findPath(Point start, Point end, Predicate<Point> obstacles) {

        Node startNode = new Node().setCoordinates(start);
        Node endNode = new Node().setCoordinates(end);

        PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparing(Node::getCost));
        openList.add(startNode);

        Set<Node> closedSet = new HashSet<>();

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();

            if (currentNode.equals(endNode)) {
                ArrayList<Point> path = new ArrayList<>();

                while (currentNode != null) {
                    path.add(currentNode.getCoordinates());
                    currentNode = currentNode.getParent();
                }
                Collections.reverse(path);
                return path;
            }

            closedSet.add(currentNode);

            ArrayList<Node> neighbors = new ArrayList<>(neighborsCount);

            for (int i = 0; i < neighborsCount; i++) {
                Point coordinates = currentNode.getCoordinates();
                int x = coordinates.x() + shiftX[i];
                int y = coordinates.y() + shiftY[i];

                if (x < 0 || y < 0 || x > Config.WIDTH - 1 || y > Config.HEIGHT - 1) {
                    continue;
                }

                Point point = new Point(x, y);

                if (obstacles.test(point) && !point.equals(endNode.coordinates)) {
                    continue;
                }

                neighbors.add(new Node().setCoordinates(point));
            }

            for (Node neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int distance = currentNode.getDistanceToStart() + 1;

                openList.removeIf(e -> e.equals(neighbor) && e.getDistanceToStart() > distance);

                if (openList.contains(neighbor)) {
                    continue;
                }

                neighbor.setDistanceToStart(distance)
                        .setDistanceToEnd(distance(endNode.coordinates, neighbor.coordinates))
                        .setCost(neighbor.getDistanceToStart() + neighbor.getDistanceToEnd())
                        .setParent(currentNode);

                openList.add(neighbor);

            }

        }

        return Collections.emptyList();
    }

    public static int distance(Point a, Point b) {
        return Math.abs(a.x() - b.x()) + Math.abs(b.y() - a.y());
    }

}
