package isos.screens;

import isos.Creature;
import isos.Item;

public class ExamineInventoryScreen extends InventoryScreen{

    public ExamineInventoryScreen(Creature player){
        super(player);
    }

    @Override
    protected String getVerb() {
        return "examine";
    }

    @Override
    protected boolean isAcceptable(Item item) {
        return true;
    }

    @Override
    protected Screen use(Item item) {
        String article = "aeiou".contains(item.getName().subSequence(0, 1)) ? "an " : "a ";
        player.notify("It's " + article + item.getName() + "." + item.getDetails());
        return null;
    }
}
