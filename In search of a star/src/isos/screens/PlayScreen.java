package isos.screens;

import asciiPanel.AsciiPanel;
import isos.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class PlayScreen implements Screen {

    private World world;
    private Creature player;
    private int screenWidth;
    private int screenHeight;
    private List<String> messages;
    private FOV fov;
    private Screen subScreen;

    public PlayScreen(){
        screenHeight = 21;
        screenWidth = 80;
        messages = new ArrayList<String>();
        createWorld();
        fov = new FOV(world);

        Factory factory = new Factory(world);
        createCreatures(factory);
        createItems(factory);
    }

    private void createWorld(){
        world = new WorldBuilder(90, 32, 5).makeCaves().build();
    }

    private void createCreatures(Factory factory){
        player = factory.newPlayer(messages, fov);

        for (int z = 0; z < world.getDepth(); z++) {
            for (int i = 0; i < 6; i++) {
                factory.newFungus(z);
            }
            for (int i = 0; i < 15; i++) {
                factory.newBat(z);
            }
            for (int i = 0; i < z + 3; i++){
                factory.newZombie(z, player);
            }
            if (z > 0) {
                for (int i = 0; i < z * 2 + 1; i++){
                    factory.newGoblin(z, player);
                }
            }
        }
        factory.newGoblinKing(world.getDepth() - 1, player);
    }

    private void createItems(Factory factory){

        for (int z = 0; z < world.getDepth(); z++){
            for (int i = 0; i < world.getWidth() * world.getHeight() / 20; i++){
                factory.newRock(z);
            }
            for (int i = 0; i < 3; i++){
                factory.randomPotion(z);
            }
        }
        factory.newPotionInvincibility(world.getDepth() - 2);
        factory.newVictoryItem(world.getDepth() - 1);

        for (int z = 0; z < world.getDepth(); z++){
            for (int i = 0; i < 10; i++){
                factory.newMushroom(z);
            }
            for (int i = 0; i < 5; i++){
                factory.newBread(z);
            }
        }

        for (int z = 0; z < 2; z++){
            for (int i = 0; i < 5; i++) {
                factory.newDagger(z);
            }
        }
        for (int z = 1; z < 3; z++) {
            for (int i = 0; i < 3; i++) {
                factory.newSword(z);
                factory.newStaff(z);
                factory.newLightArmor(z);
            }
        }
        for (int z = 2; z < 4; z++){
            for (int i = 0; i < 3; i++){
                factory.newMediumArmor(z);
                factory.newBow(z);
            }
        }
        for (int i = 0; i < 3; i++){
            factory.newHeavyArmor(world.getDepth() - 2);
        }
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        int left = getScrollX();
        int top = getScrollY();

        displayTiles(terminal, left, top);
        displayMessages(terminal, messages);

        terminal.write(player.glyph(), player.x - left, player.y - top, player.color());

        String stats = String.format("%3d/%3d hp   %8s", player.hp(), player.getMaxHp(), hunger());
        terminal.write(stats, 1, 23);

        if (subScreen != null){
            subScreen.displayOutput(terminal);
        }
    }

    private String hunger() {
        if (player.getFood() < player.getMaxFood() * 0.1){
            return "Starving";
        } else if (player.getFood() < player.getMaxFood() * 0.2){
            return "Hungry";
        } else if (player.getFood() < player.getMaxFood() * 0.5) {
            return "Normal";
        } else if (player.getFood() < player.getMaxFood() * 0.9) {
            return "Stuffed";
        } else if (player.getFood() > player.getMaxFood() * 0.8) {
            return "Full";
        } else {
            return "";
        }
    }

    @Override
    public Screen respondToInput(KeyEvent key) {
        int level = player.getLevel();

        if (subScreen != null){
            subScreen = subScreen.respondToInput(key);
        } else {
            switch (key.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    player.moveBy(-1, 0, 0);
                    break;
                case KeyEvent.VK_RIGHT:
                    player.moveBy(1, 0, 0);
                    break;
                case KeyEvent.VK_UP:
                    player.moveBy(0, -1, 0);
                    break;
                case KeyEvent.VK_DOWN:
                    player.moveBy(0, 1, 0);
                    break;
                case KeyEvent.VK_D:
                    subScreen = new DropScreen(player);
                    break;
                case KeyEvent.VK_E:
                    subScreen = new EatScreen(player);
                    break;
                case KeyEvent.VK_W:
                    subScreen = new EquipScreen(player);
                    break;
                case KeyEvent.VK_H:
                    subScreen = new HelpScreen();
                    break;
                case KeyEvent.VK_I:
                    subScreen = new ExamineInventoryScreen(player);
                    break;
                case KeyEvent.VK_T:
                    subScreen = new ThrowScreen(player,
                            player.x - getScrollX(),
                            player.y - getScrollY());
                    break;
                case KeyEvent.VK_F:
                    if (player.getWeapon() == null || player.getWeapon().getRangedAttackValue() == 0){
                        player.notify("You don't have a ranged weapon equipped.");
                    } else {
                        subScreen = new FireWeaponScreen(player,
                                player.x - getScrollX(),
                                player.y - getScrollY());
                        break;
                    }
                case KeyEvent.VK_C:
                    subScreen = new PotionScreen(player);
                    break;
            }
            switch (key.getKeyChar()) {
                case ',':
                    player.pickUp();
                    break;
                case '<':
                    if (playerIsTryingToExit()){
                        return playerExit();
                    } else {
                        player.moveBy(0, 0, -1);
                    }
                    break;
                case '>':
                    player.moveBy(0, 0, 1);
                    break;
                case ';':
                    subScreen = new LookScreen(player, "Looking",
                            player.x - getScrollX(),
                            player.y - getScrollY());
                    break;
            }
        }
        if (subScreen == null) {
            world.update();
        }
        if (player.getLevel() > level){
            subScreen = new LevelUpScreen(player, player.getLevel() - level);
        }
        if (player.hp() < 1){
            return new LoseScreen();
        }
        return this;
    }

    public int getScrollX() {
        return Math.max(0, Math.min(player.x - screenWidth / 2, world.getWidth() - screenWidth));
    }

    public int getScrollY() {
        return Math.max(0, Math.min(player.y - screenHeight / 2, world.getHeight() - screenHeight));
    }

    private void displayTiles(AsciiPanel terminal, int left, int top) {
        fov.update(player.x, player.y, player.z, player.getVisionRadius());

        for (int x = 0; x < screenWidth; x++){
            for (int y = 0; y < screenHeight; y++){
                int wx = x + left;
                int wy = y + top;

                if (player.canSee(wx, wy, player.z)){
                    terminal.write(world.glyph(wx, wy, player.z), x, y, world.color(wx, wy, player.z));
                } else {
                    terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, Color.darkGray);
                }
            }
        }
    }

    private void displayMessages(AsciiPanel terminal, List<String> messages){
        int top = screenHeight - messages.size();
        for (int i = 0; i < messages.size(); i++){
            terminal.writeCenter(messages.get(i), top + i);
        }
        messages.clear();
    }

    private boolean playerIsTryingToExit(){
        return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
    }

    private Screen playerExit(){
        for (Item item : player.getInventory().getItems()){
            if (item.getName().equals("cut brandy")){
                return new WinScreen();
            }
        }
        return new LoseScreen();
    }
}
