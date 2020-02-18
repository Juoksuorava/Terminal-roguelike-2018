package isos.screens;

import asciiPanel.AsciiPanel;
import isos.Creature;
import isos.Line;
import isos.Point;

import java.awt.event.KeyEvent;

public abstract class TargetScreen implements Screen {

    protected Creature player;
    protected String caption;
    private int sx;
    private int sy;
    private int x;
    private int y;

    public TargetScreen(Creature player, String caption, int sx, int sy){
        this.player = player;
        this.caption = caption;
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    public void displayOutput(AsciiPanel terminal) {
        for (Point p : new Line(sx, sy, sx + x, sy + y)){
            if (p.x < 0 || p.x >= 80 || p.y < 0 || p.y >= 24){
                continue;
            }
            terminal.write('o', p.x, p.y, AsciiPanel.brightMagenta);
        }

        terminal.clear(' ', 0, 23, 80, 1);
        terminal.write(caption, 0, 23);
    }

    @Override
    public Screen respondToInput(KeyEvent key) {
        int px = x;
        int py = y;

        switch (key.getKeyCode()){
            case KeyEvent.VK_LEFT:
                x--;
                break;
            case KeyEvent.VK_RIGHT:
                x++;
                break;
            case KeyEvent.VK_UP:
                y--;
                break;
            case KeyEvent.VK_DOWN:
                y++;
                break;
            case KeyEvent.VK_ENTER:
                selectWorldCoordinate(player.x + x, player.y + y, sx + x, sy + y);
                return null;
            case KeyEvent.VK_ESCAPE:
                return null;
        }
        if (!isAcceptable(player.x + x, player.y + y)){
            x = px;
            y = py;
        }

        enterWorldCoordinate(player.x + x, player.y +y, sx + x, sy + y);

        return this;
    }

    public void selectWorldCoordinate(int x, int y, int screenX, int screenY){ }

    public boolean isAcceptable(int x, int y){ return true; }

    public void enterWorldCoordinate(int x, int y, int screenX, int screenY){ }
}
