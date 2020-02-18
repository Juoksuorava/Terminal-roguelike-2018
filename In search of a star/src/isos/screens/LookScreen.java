package isos.screens;

import isos.Creature;
import isos.Item;
import isos.Tile;

public class LookScreen  extends TargetScreen {

    public LookScreen(Creature player, String caption, int sx, int sy){
        super(player, caption, sx, sy);
    }

    @Override
    public void enterWorldCoordinate(int x, int y, int screenX, int screenY) {
        Creature creature = player.creature(x, y, player.z);
        if (creature != null){
            caption = creature.glyph() + " "    + creature.name() + creature.getDetails();
            return;
        }

        Item item = player.item(x, y, player.z);
        if (item != null) {
            caption = item.getGlyph() + " "    + item.getName() + item.getDetails();
            return;
        }

        Tile tile = player.tile(x, y, player.z);
        caption = tile.glyph() + " " + tile.getDetails();
    }
}
