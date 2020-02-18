package isos.screens;

import isos.Creature;
import isos.Item;

public class PotionScreen extends InventoryScreen {

    public PotionScreen(Creature player){
        super(player);
    }

    @Override
    protected String getVerb() {
        return "drink";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return item.getDrinkEffect() != null;
    }

    @Override
    protected Screen use(Item item) {
        player.drink(item);
        return null;
    }
}
