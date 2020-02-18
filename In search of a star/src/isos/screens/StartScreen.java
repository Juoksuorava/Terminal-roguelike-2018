package isos.screens;

import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;

public class StartScreen implements Screen{

    @Override
    public void displayOutput(AsciiPanel terminal){
        terminal.write("In search of a star", 1, 1);
        terminal.writeCenter("-- press [enter] to start --", 22);
        terminal.writeCenter("-- press [h] in game to open help --", 23);
    }

    @Override
    public Screen respondToInput(KeyEvent key) {
        return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
    }
}
