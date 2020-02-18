package isos.screens;

import asciiPanel.AsciiPanel;

import java.awt.event.KeyEvent;

public class WinScreen extends PlayScreen implements Screen{

    @Override
    public void displayOutput(AsciiPanel terminal) {
        terminal.write("You escaped with the hidden treasure!", 1, 1);
        terminal.writeCenter("-- press [Enter] to play again --", 22);
    }

    @Override
    public Screen respondToInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen() : this;
    }
}
