package isos.screens;

import isos.Creature;
import isos.Item;

public class EatScreen extends InventoryScreen{

    public EatScreen(Creature player){
        super(player);
    }

    protected String getVerb(){
        return "eat";
    }

    protected boolean isAcceptable(Item item) {
        return item.getFoodValue() != 0;
    }

    @Override
    protected Screen use(Item item) {
        player.eat(item);
        return null;
    }
}
