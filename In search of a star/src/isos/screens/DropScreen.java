package isos.screens;

import isos.Creature;
import isos.Item;

public class DropScreen extends InventoryScreen {

    public DropScreen(Creature player){
        super(player);
    }

    @Override
    protected String getVerb() {
        return "drop";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return true;
    }

    @Override
    protected Screen use(Item item) {
        player.drop(item);
        return null;
    }
}
