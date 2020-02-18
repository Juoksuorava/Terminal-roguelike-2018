package isos;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Item {

    private char glyph;
    public char getGlyph() { return glyph; }

    private Color color;
    public Color getColor() { return color; }

    private String name;
    public String getName() { return name; }

    private int foodValue;
    public int getFoodValue() { return foodValue; }

    public void modifyFoodValue(int amount){
        foodValue += amount;
    }
    private int attackValue;
    public int getAttackValue() { return attackValue; }
    public void modifyAttackValue(int amount){
        attackValue += amount;
    }

    private int thrownAttackValue;
    public int getThrownAttackValue() { return thrownAttackValue; }
    public void modifyThrownAttackValue(int amount) {
        thrownAttackValue += amount;
    }

    private double missChance;
    public double getMissChance() { return missChance; }
    public void modifyMissChance(double percent) {
        missChance += percent;
    }

    private int defenseValue;
    public int getDefenseValue() { return defenseValue; }
    public void modifyDefenseValue(int amount) {
        defenseValue = amount;
    }

    private int rangedAttackValue;
    public int getRangedAttackValue() { return rangedAttackValue; }
    public void modifyRangedAttackValue(int amount) {
        rangedAttackValue += amount;
    }

    private Effect drinkEffect;
    public Effect getDrinkEffect() { return drinkEffect; }
    public void setDrinkEffect(Effect effect) { this.drinkEffect = effect; }

    public Item(char glyph, Color color, String name){
        this.glyph = glyph;
        this.color = color;
        this.name = name;
        this.thrownAttackValue = 1;
    }

    public String getDetails() {
        String details = "";
        if (attackValue != 0){
            details += "    attack:" + attackValue;
        }
        if (defenseValue != 0){
            details += "    defense:" + defenseValue;
        }
        if (rangedAttackValue > 0) {
            details += "  ranged:" + rangedAttackValue;
        }
        if (foodValue != 0) {
            details += "    food:" + foodValue;
        }
        return details;
    }
}
