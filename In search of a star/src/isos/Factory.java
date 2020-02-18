package isos;

import asciiPanel.AsciiPanel;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;

import java.util.List;

public class Factory {

    private World world;

    public Factory(World world){
        this.world = world;
    }

    //CREATURES

    public Creature newPlayer(List<String> messages, FOV fov){
        Creature player = new Creature(world, '@', AsciiPanel.brightWhite, 100,  20, 5, 9, "player");
        world.addAtEmptyLocation(player, 0);
        new PlayerAi(player, messages, fov);
        return player;
    }

    public Creature newFungus(int depth){
        Creature fungus = new Creature(world, 'F', AsciiPanel.green, 10,  0, 0, 9, "fungus");
        world.addAtEmptyLocation(fungus, depth);
        new FungusAi(fungus, this);
        return fungus;
    }

    public Creature newFungusChild(int depth){
        Creature fungusChild = new Creature(world, 'f', AsciiPanel.green, 5,  1, 0, 9,"fungus child");
        world.addAtEmptyLocation(fungusChild, depth);
        new FungusChildAi(fungusChild);
        return fungusChild;
    }

    public Creature newBat(int depth){
        Creature bat = new Creature(world, 'b', AsciiPanel.yellow, 15,  10, 0, 10, "bat");
        world.addAtEmptyLocation(bat, depth);
        new BatAi(bat);
        return bat;
    }

    public Creature newZombie(int depth, Creature player){
        Creature zombie = new Creature(world, 'z', AsciiPanel.white, 50,  10, 10, 9, "zombie");
        world.addAtEmptyLocation(zombie, depth);
        new ZombieAi(zombie, player);
        return zombie;
    }

    public Creature newGoblin(int depth, Creature player){
        Creature goblin = new Creature(world, 'g', AsciiPanel.brightGreen, 50,  15, 5, 7, "goblin");
        goblin.equip(randomWeapon(depth));
        goblin.equip(randomArmor(depth));
        world.addAtEmptyLocation(goblin, depth);
        new GoblinAi(goblin, player);
        return goblin;
    }

    public Creature newGoblinKing(int depth, Creature player){
        Creature creature = new Creature(world, 'G', AsciiPanel.brightGreen, 150, 50, 10, 10, "goblin king");
        world.addAtEmptyLocation(creature, depth);
        new GoblinAi(creature, player);
        return creature;
    }

    //ITEMS

    //Other
    public Item newRock(int depth){
        Item rock = new Item(',', AsciiPanel.yellow, "rock");
        world.addAtEmptyLocation(rock, depth);
        rock.modifyThrownAttackValue(5);
        return rock;
    }

    public Item newVictoryItem(int depth){
        Item item = new Item('*', AsciiPanel.brightWhite, "cut brandy");
        world.addAtEmptyLocation(item, depth);
        return item;
    }


    //Potions
    public Item newPotionHealth(int depth){
        Item potion = new Item('!', AsciiPanel.red, "health potion");
        potion.setDrinkEffect(new Effect(1){
            public void start(Creature creature){
                if (creature.hp() == creature.getMaxHp()){
                    return;
                }
                creature.modifyHp(15);
                creature.doAction("feel renewed");
            }
        });
        world.addAtEmptyLocation(potion,depth);
        return potion;
    }

    public Item newPotionRegen(int depth){
        Item potion = new Item('!', AsciiPanel.brightGreen, "regen potion");
        potion.setDrinkEffect(new Effect(10){
            public void update(Creature creature){
                super.update(creature);
                creature.modifyHp(2);
            }
        });
        world.addAtEmptyLocation(potion, depth);
        return potion;
    }

    public Item newPotionPoison(int depth){
        Item poison = new Item('!', AsciiPanel.green, "bottle of poison");
        poison.setDrinkEffect(new Effect(10){
            public void start(Creature creature){
                creature.doAction("look sick");
            }
            public void update(Creature creature){
                super.update(creature);
                creature.modifyHp(-2);
            }
        });
        world.addAtEmptyLocation(poison, depth);
        return poison;
    }
    public Item newPotionInvincibility(int depth){
        Item potion = new Item('!', AsciiPanel.brightYellow, "hero's potion");
        potion.setDrinkEffect(new Effect(3){
            public void start(Creature creature){
                creature.modifyDefenseValue(100);
                creature.modifyAttackValue(100);
                creature.doAction("feel unnaturally strong");
            }
            public void end(Creature creature){
                creature.modifyDefenseValue(-100);
                creature.modifyAttackValue(-100);
                creature.doAction("feel normal again");
            }
        });
        world.addAtEmptyLocation(potion, depth);
        return potion;
    }

    public Item randomPotion(int depth){
        switch ((int)(Math.random() * 2)){
            case 0: return newPotionHealth(depth);
            case 1: return newPotionPoison(depth);
            default: return newPotionRegen(depth);
        }
    }

    //Food
    public Item newMushroom(int depth){
        Item mushroom = new Item('-', AsciiPanel.yellow, "mushroom");
        mushroom.modifyFoodValue(50);
        world.addAtEmptyLocation(mushroom, depth);
        return mushroom;
    }

    public Item newBread(int depth) {
        Item bread = new Item('~', AsciiPanel.yellow, "bread");
        bread.modifyFoodValue(150);
        world.addAtEmptyLocation(bread, depth);
        return bread;
    }

    //Weapons
    public Item randomWeapon(int depth){
        switch ((int)(Math.random() * 3)){
            case 0: return newDagger(depth);
            case 1: return newSword(depth);
            case 2: return newBow(depth);
            default: return newStaff(depth);
        }
    }

    public Item newDagger(int depth){
        Item dagger = new Item(')', AsciiPanel.white, "dagger");
        dagger.modifyAttackValue(5);
        dagger.modifyThrownAttackValue(15);
        dagger.modifyMissChance(0.33);
        world.addAtEmptyLocation(dagger, depth);
        return dagger;
    }

    public Item newStaff(int depth){
        Item staff = new Item(')', AsciiPanel.yellow, "staff");
        staff.modifyAttackValue(5);
        staff.modifyDefenseValue(3);
        world.addAtEmptyLocation(staff, depth);
        return staff;
    }

    public Item newSword(int depth){
        Item sword = new Item(')', AsciiPanel.brightWhite, "sword");
        sword.modifyAttackValue(10);
        sword.modifyThrownAttackValue(20);
        sword.modifyMissChance(0.75);
        world.addAtEmptyLocation(sword, depth);
        return sword;
    }

    public Item newBow(int depth){
        Item bow = new Item(')', AsciiPanel.brightRed, "bow");
        bow.modifyAttackValue(1);
        bow.modifyRangedAttackValue(10);
        bow.modifyMissChance(0.20);
        world.addAtEmptyLocation(bow, depth);
        return bow;
    }


    //Armor
    public Item newLightArmor(int depth){
        Item item = new Item('[', AsciiPanel.green, "tunic");
        item.modifyDefenseValue(2);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item newMediumArmor(int depth){
        Item item = new Item('[', AsciiPanel.blue, "chainmail");
        item.modifyDefenseValue(4);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item newHeavyArmor(int depth){
        Item item = new Item('[', AsciiPanel.magenta, "platemail");
        item.modifyDefenseValue(6);
        world.addAtEmptyLocation(item, depth);
        return item;
    }

    public Item randomArmor(int depth){
        switch ((int)(Math.random() * 3)){
            case 0: return newLightArmor(depth);
            case 1: return newMediumArmor(depth);
            default: return newHeavyArmor(depth);
        }
    }
}
