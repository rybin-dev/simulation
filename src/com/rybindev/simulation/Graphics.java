package com.rybindev.simulation;

import java.util.Arrays;

public class Graphics {
    private final String[][] canvas;
    int x;
    int y;

    public Graphics(int x, int y) {
        this.x = x;
        this.y = y;

        canvas = new String[y][x];
        clear();
    }

    public void clear() {
        for (String[] arr : canvas) {
            Arrays.fill(arr, " ");
        }
    }

    public void draw(String sprite, Point point) {
        if (point.x() < 0 || point.y() < 0 || point.x() >= x || point.y() >= y) return;
        canvas[point.y()][point.x()] = sprite;
    }

    public void print() {
        for (int i = 0; i < canvas.length; i++) {
            for (int j = 0; j < canvas[i].length; j++) {
                System.out.append(canvas[i][j]);
            }
            System.out.append('\n');
        }
        System.out.println();
    }
}
