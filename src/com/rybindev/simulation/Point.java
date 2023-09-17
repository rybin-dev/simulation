package com.rybindev.simulation;

public record Point(int x, int y) {

    public static int distance(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(b.y - a.y);
    }
}
