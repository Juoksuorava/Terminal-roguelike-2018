package isos;

import asciiPanel.AsciiPanel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Creature {

    //Attributes
    private World world;

    //Location data
    public int x;
    public int y;
    public int z;

    //Custom AI for every type of creature
    private CreatureAi ai;
    public void setCreatureAi(CreatureAi ai) {
        this.ai = ai;
    }

    //Creatures need to appear in a certain way on the PlayScreen
    private char glyph;
    public char glyph() {
        return glyph;
    }

    private Color color;
    public Color color() {
        return color;
    }

    //Creatures have stats
    //A lot of them
    private String name;
    public String name() {
        return name;
    }

    private Inventory inventory;
    public Inventory getInventory() {
        return inventory;
    }

    private int maxHp;
    public int getMaxHp() { return maxHp; }
    public void modifyMaxHp(int amount) {
        maxHp += amount;
        doAction("look healthier");
    }

    private int hp;
    public int hp() { return hp; }
    public void modifyHp(int amount) {
        hp += amount;
        if (hp < 1) {
            doAction("die");
            leaveCorpse();
            world.remove(this);
        }
        if (hp > getMaxHp()) {
            hp = maxHp;
        }
    }

    private int attackValue;
    public int getAttackValue() {
        return attackValue
                + (weapon == null ? 0 : weapon.getAttackValue())
                + (armor == null ? 0 : armor.getAttackValue());
    }
    public void modifyAttackValue(int amount) {
        attackValue += amount;
        doAction("feel stronger");
    }

    private int defenseValue;
    public int getDefenseValue() {
        return defenseValue
                + (armor == null ? 0 : armor.getDefenseValue())
                + (weapon == null ? 0 : weapon.getDefenseValue());
    }
    public void modifyDefenseValue(int amount) {
        defenseValue += amount;
        doAction("seem tougher");
    }

    private int visionRadius;
    public int getVisionRadius() { return visionRadius; }
    public void modifyVisionRadius(int amount) {
        visionRadius += amount;
        doAction("see better");
    }

    private int maxFood;
    public int getMaxFood() {
        return maxFood;
    }

    private int food;
    public int getFood() {
        return food;
    }

    public void modifyFood(int amount) {
        food += amount;
        if (food > maxFood) {
            maxFood = maxFood + food / 2;
            food = maxFood;
            notify("Your stomach streches!");
            modifyHp(-1);
        } else if (food < 1 && isPlayer()) {
            modifyHp(-1000);
        }
    }

    private Item weapon;
    public Item getWeapon() { return weapon; }

    private Item armor;
    public Item getArmor() { return armor; }

    private int regenHpCooldown;
    private int regenHpPer1000;

    private List<Effect> effects;

    private int xp;
    public void modifyXp(int amount) {
        xp += amount;

        notify("You %s %d xp.", amount < 0 ? "lose" : "gain", amount);

        while (xp > (int) (Math.pow(getLevel(), 1.5) * 20)) {
            level++;
            doAction("advance to level %d", level);
            ai.onGainLevel();
            modifyHp(level * 2);
        }
    }
    public void gainXp(Creature other) {
        int amount = other.maxHp + other.getAttackValue() + other.getDefenseValue() - level * 2;

        if (amount > 0) {
            modifyXp(amount);
        }
    }

    private int level;
    public int getLevel() { return level; }

    public Creature(World world, char glyph, Color color, int maxHp, int attackValue, int defenseValue, int visionRadius, String name) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attackValue = attackValue;
        this.defenseValue = defenseValue;
        this.visionRadius = visionRadius;
        this.name = name;
        this.inventory = new Inventory(20);
        this.maxFood = 1000;
        this.food = maxFood / 3 * 2;
        this.level = 1;
        this.regenHpPer1000 = 10;
        this.effects = new ArrayList<Effect>();
    }

    public boolean canSee(int wx, int wy, int wz) {
        return ai.canSee(wx, wy, wz);
    }

    public Tile realTile(int wx, int wy, int wz) {
        return world.tile(wx, wy, wz);
    }

    public Tile tile(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz)) {
            return world.tile(wx, wy, wz);
        } else {
            return ai.rememberedTile(wx, wy, wz);
        }
    }

    public Creature creature(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz)) {
            return world.creature(wx, wy, wz);
        } else {
            return null;
        }
    }

    public Item item(int wx, int wy, int wz) {
        if (canSee(wx, wy, wz)) {
            return world.item(wx, wy, wz);
        } else {
            return null;
        }
    }


    //moveBy method is used to move creatures around the world
    public void moveBy(int mx, int my, int mz) {
        if (mx == 0 && my == 0 && mz == 0) {
            return;
        }

        Tile tile = world.tile(x + mx, y + my, z + mz);

        if (mz == -1) {
            if (tile == Tile.STAIRS_DOWN) {
                doAction("walk up the stairs to level %d", z + mz + 1);
            } else {
                doAction("try to go up, but are stopped by the cave ceiling");
                return;
            }
        } else if (mz == 1) {
            if (tile == Tile.STAIRS_UP) {
                doAction("walk down the stairs to level %d", z + mz + 1);
            } else {
                doAction("try to go down but are stopped by the cave floor");
                return;
            }
        }

        Creature other = world.creature(x + mx, y + my, z + mz);

        if (other == null) {
            modifyFood(-1);
            ai.onEnter(x + mx, y + my, z + mz, tile);
        } else {
            attack(other);
        }
    }

    public void update() {
        if (isPlayer()) {
            modifyFood(-1);
        }
        regenerateHealth();
        updateEffects();
        ai.onUpdate();
    }

    private void updateEffects() {
        List<Effect> done = new ArrayList<Effect>();
        for (Effect effect : effects) {
            effect.update(this);
            if (effect.isDone()) {
                effect.end(this);
                done.add(effect);
            }
        }
        effects.removeAll(done);
    }

    /*
    Method to notify other creatures of events around them
    Also used to notify the player of events around them
    */
    public void doAction(String message, Object... params) {
        int r = getVisionRadius();
        for (int ox = -r; ox < r + 1; ox++) {
            for (int oy = -r; oy < r + 1; oy++) {
                if (ox * ox + oy * oy > r * r) {
                    continue;
                }
                Creature other = world.creature(x + ox, y + oy, z);

                if (other == null) {
                    continue;
                }
                if (other == this) {
                    other.notify("You " + message + ".", params);
                } else if (other.canSee(x, y, z)) {
                    other.notify(String.format("The '%s' %s.", name, makeSecondPerson(message)), params);
                }
            }
        }
    }

    /*
    String manipulation method to make messages grammatically correct.
    Assumes the first word is a verb
    Please do not use in any other project because language issues
    */
    private String makeSecondPerson(String text) {
        String[] words = text.split(" ");
        words[0] = words[0] + "s";

        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(" ");
            builder.append(word);
        }

        return builder.toString().trim();
    }

    public boolean canEnter(int wx, int wy, int wz) {
        return (world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null);
    }

    public void attack(Creature other) {
        int amount = Math.max(0, getAttackValue() - other.getDefenseValue());

        amount = (int) (Math.random() * amount + 1);

        other.modifyHp(-amount);

        notify("You attack the '%s' for %d damage!", other.name, amount);
        other.notify("The '%s' attacks you for %d damage!", name, amount);

        if (other.hp < 1) {
            gainXp(other);
        }
    }

    public void throwItem(Item item, int wx, int wy, int wz) {
        Point end = new Point(x, y, 0);

        for (Point p : new Line(x, y, wx, wy)) {
            if (!realTile(p.x, p.y, z).isGround()) {
                break;
            }
            end = p;
        }
        wx = end.x;
        wy = end.y;

        Creature c = creature(wx, wy, wz);

        if (c != null) {
            thrownAttack(item, c);
            if (item.getDrinkEffect() != null) {
                getRidOf(item);
            }
        } else {
            doAction("throw a %s", item.getName());
        }
        unequip(item);
        inventory.remove(item);
        world.addAtEmptySpace(item, wx, wy, wz);
    }

    public void thrownAttack(Item item, Creature other) {
        modifyFood(-1);

        int amount = Math.max(0, item.getThrownAttackValue() - other.getDefenseValue());

        amount = (int) (Math.random() * amount) + 1;

        boolean hit;

        if (Math.random() - item.getMissChance() > 0) {
            hit = true;
        } else {
            hit = false;
        }

        if (hit) {
            doAction("throw a %s at the %s for %d damage", item.getName(), other.name, amount);
            other.modifyHp(-amount);
            other.addEffect(item.getDrinkEffect());

            if (other.hp < 1) {
                gainXp(other);
            }
        } else {
            doAction("throw a %s at the %s, but it's a miss!", item.getName(), other.name);
        }


    }

    public void rangedWeaponAttack(Creature other) {
        modifyFood(-1);

        int amount = Math.max(0, weapon.getRangedAttackValue() - other.getDefenseValue());

        amount = (int) (Math.random() * amount) + 1;

        boolean hit;

        if (Math.random() - weapon.getMissChance() > 0) {
            hit = true;
        } else {
            hit = false;
        }

        if (hit) {
            doAction("fire a %s at the %s for %d damage", weapon.getName(), other.name, amount);
            other.modifyHp(-amount);

            if (other.hp < 1) {
                gainXp(other);
            }
        } else {
            doAction("fire a %s at the %s, but it's a miss!", weapon.getName(), other.name);
        }
    }

    public void pickUp() {
        Item item = world.item(x, y, z);

        if (item == null) {
            doAction("grab at the ground");
            return;
        }
        if (inventory.isFull()) {
            doAction("realize inventory is full");
            return;
        } else {
            doAction("pick up a %s", item.getName());
            world.remove(x, y, z);
            inventory.add(item);
        }
    }

    public void drop(Item item) {
        doAction("drop a " + item.getName());
        inventory.remove(item);
        unequip(item);
        world.addAtEmptySpace(item, x, y, z);
    }

    public void eat(Item item) {
        doAction("eat a " + item.getName());
        consume(item);
    }

    public void drink(Item item) {
        doAction("drink a " + item.getName());
        consume(item);
    }

    public void consume(Item item) {
        if (item.getFoodValue() < 0) {
            notify("Gross!");
        }

        addEffect(item.getDrinkEffect());

        modifyFood(item.getFoodValue());
        if (item.getGlyph() == 'f' || item.getGlyph() == 'F' || item.getGlyph() == 'z') {
            visionRadius -= 1;
            notify("The food was poisonous!");
        }
        getRidOf(item);
    }

    public void addEffect(Effect effect) {
        if (effect == null) {
            return;
        }

        effect.start(this);
        effects.add(effect);
    }

    public void equip(Item item) {

        if (item.getAttackValue() == 0 && item.getRangedAttackValue() == 0 && item.getDefenseValue() == 0) {
            return;
        }
        if (item.getAttackValue() + item.getRangedAttackValue() >= item.getDefenseValue()) {
            unequip(weapon);
            doAction("wield a " + item.getName());
            weapon = item;
        } else {
            unequip(armor);
            doAction("put on a " + item.getName());
            armor = item;
        }
    }

    public void unequip(Item item) {
        if (item == null) {
            return;
        }

        if (item == armor) {
            doAction("remove a " + item.getName());
            armor = null;
        } else if (item == weapon) {
            doAction("put away a " + item.getName());
            weapon = null;
        }
    }

    private void leaveCorpse() {
        Item corpse = new Item(glyph, AsciiPanel.brightYellow, name + " corpse");
        corpse.modifyFoodValue(maxHp * 3);
        world.addAtEmptySpace(corpse, x, y, z);
        for (Item item : inventory.getItems()) {
            if (item != null) {
                drop(item);
            }
        }
    }

    public boolean isPlayer() {
        return glyph == '@';
    }

    public void notify(String message, Object... params) {
        ai.onNotify(String.format(message, params));
    }

    public void dig(int wx, int wy, int wz) {
        modifyFood(-10);
        world.dig(wx, wy, wz);
        doAction("dig");
    }

    public String getDetails() {
        return String.format("    level:%d    attack:%d    defense%d    hp:%d", level, getAttackValue(), getDefenseValue(), hp);
    }

    private void regenerateHealth() {
        regenHpCooldown -= regenHpPer1000;
        if (regenHpCooldown < 0) {
            modifyHp(1);
            modifyFood(-1);
            regenHpCooldown += 1000;
        }
    }

    private void getRidOf(Item item) {
        inventory.remove(item);
        unequip(item);
    }
}
