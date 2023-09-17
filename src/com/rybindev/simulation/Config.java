package com.rybindev.simulation;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Config {
    public static int MAX_ENTITIES = 2000;
    public static int HEIGHT = 15;
    public static int WIDTH = 100;

    public static int PREDATOR_HEALTH = 100;
    public static int PREDATOR_ATTACK = 30;
    public static int PREDATOR_VELOCITY = 10;
    public static String PREDATOR_SPRITE = "#";
    public static int PREDATOR_PLUS = 10;
    public static int MAX_PREDATOR = 30;
    public static int MIN_PREDATOR = 3;



    public static int HERBIVORE_HEALTH = 100;
    public static int HERBIVORE_ATTACK = 20;
    public static int HERBIVORE_VELOCITY = 6;
    public static String HERBIVORE_SPRITE = "@";
    public static int HERBIVORE_PLUS = 10;
    public static int MAX_HERBIVORE = 50;
    public static int MIN_HERBIVORE = 10;

    public static String CRASS_SPRITE = "v";
    public static int MAX_CRASS = 150;
    public static int MIN_CRASS = 50;

}
