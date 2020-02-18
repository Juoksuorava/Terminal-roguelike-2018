package isos.screens;

import isos.Creature;
import isos.Item;
import isos.Line;
import isos.Point;

public class ThrowAtScreen extends TargetScreen{

    private Item item;

    public ThrowAtScreen(Creature player, int sx, int sy, Item item) {
        super(player, "Throw " + item.getName() + " at?", sx, sy);
        this.item = item;
    }

    @Override
    public boolean isAcceptable(int x, int y) {
        if (!player.canSee(x, y, player.z)) return false;

        for (Point p : new Line(player.x, player.y, x, y)){
            if (!player.realTile(p.x, p.y, player.z).isGround()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void selectWorldCoordinate(int x, int y, int screenX, int screenY) {
        player.throwItem(item, x, y, player.z);
    }
}
