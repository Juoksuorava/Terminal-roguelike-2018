package isos.screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class HelpScreen implements Screen {

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.clear();
        terminal.writeCenter("In search of a star -help", 1);
        terminal.write("Explore the caves deep under Turku to find the hidden treasure", 1, 3);
        terminal.write("that is marked with a star. Use everything you find to survive", 1, 4);

        int y = 6;
        terminal.write("[,] to pick up items", 2, y++);
        terminal.write("[i] to open up your inventory", 2, y++);
        terminal.write("[d] to drop items from your inventory", 2, y++);
        terminal.write("[e] to eat something supposedly edible", 2, y++);
        terminal.write("[c] to consume potions", 2, y++);
        terminal.write("[w] to wield or wear loot you may find", 2, y++);
        terminal.write("[;] to look at around", 2, y++);
        terminal.write("[t] to throw something", 2, y++);
        terminal.write("[f] to fire your weapon", 2, y++);
        terminal.write("[<] to go up stairs or", 2, y++);
        terminal.write("[>] to go down stairs", 2, y++);
        terminal.write("[h] for help", 2, y++);

        terminal.writeCenter("-- press any key to continue --", 22);
    }

    @Override
    public Screen respondToInput(KeyEvent key) {
        return null;
    }
}
