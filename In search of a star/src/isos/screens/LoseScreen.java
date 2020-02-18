package isos.screens;

import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;

public class LoseScreen implements Screen{

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You failed to retrieve the treasure.", 1, 1);
        terminal.writeCenter("-- press [Enter] to restart --", 22);
    }

    @Override
    public Screen respondToInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen() : this;
    }
}