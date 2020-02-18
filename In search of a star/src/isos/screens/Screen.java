package isos.screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;


//Screen interface is used, because sometimes there is additional information
// that has to be shown to the player
public interface Screen {

    void displayOutput(AsciiPanel terminal);

    Screen respondToInput(KeyEvent key);
}
