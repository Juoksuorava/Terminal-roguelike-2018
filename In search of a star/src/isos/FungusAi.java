package isos;


public class FungusAi extends CreatureAi {

    private Factory factory;
    private int spreadcount;

    public FungusAi(Creature creature, Factory factory){
        super(creature);
        this.factory = factory;
    }

    @Override
    public void onUpdate(){
        if (spreadcount < 5 && Math.random() < 0.01){
            spread();
        }
    }

    private void spread(){
        int x = creature.x + (int)(Math.random() * 11) - 5;
        int y = creature.y + (int)(Math.random() * 11) - 5;

        if (!creature.canEnter(x, y, creature.z)){
            return;
        }

        Creature child = factory.newFungusChild(creature.z);
        creature.doAction("spawn a child");
        child.x = x;
        child.y = y;
        child.x = creature.z;
        spreadcount++;
    }
}
