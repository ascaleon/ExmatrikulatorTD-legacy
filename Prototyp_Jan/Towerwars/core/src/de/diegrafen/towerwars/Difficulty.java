package de.diegrafen.towerwars;

import com.badlogic.gdx.graphics.Color;

public enum Difficulty {
    EASY("Easy", Assets.COLOR_GREEN), MEDIUM("Medium", Assets.COLOR_YELLOW), HARD("Hard", Assets.COLOR_RED);

    private String name;
    private final Color color;

    Difficulty(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
