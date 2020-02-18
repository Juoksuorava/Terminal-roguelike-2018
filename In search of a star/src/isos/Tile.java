package isos;

import asciiPanel.AsciiPanel;
import java.awt.*;

public enum Tile {
    FLOOR((char)250, AsciiPanel.yellow, "A dirt and rock cave floor."),
    WALL((char)177, AsciiPanel.yellow, "A dirt and rock cave wall."),
    BOUNDS('x', AsciiPanel.brightBlack, "When you gaze into the abyss, the abyss gazes into you"),
    STAIRS_DOWN('>', AsciiPanel.white, "A stone staircase leading further into the depths"),
    STAIRS_UP('<', AsciiPanel.white, "A stone staircase that goes up, closer to the sun"),
    UNKNOWN(' ', AsciiPanel.white, "(unknown)");

    private String details;
    public String getDetails() { return details; }

    private char glyph;
    public char glyph() { return glyph; }

    private Color color;
    public Color color() { return color; }

    Tile(char glyph, Color color, String details){
        this.glyph = glyph;
        this.color = color;
        this.details = details;
    }

    public boolean isDiggable() {
        return this == Tile.WALL;
    }

    public boolean isGround() {
        return this != WALL && this != BOUNDS;
    }


}
