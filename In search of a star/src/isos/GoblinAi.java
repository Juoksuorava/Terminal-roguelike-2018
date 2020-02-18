package isos;

import java.util.List;

public class GoblinAi extends CreatureAi {

    private Creature player;
    private Point playerLocation;

    public GoblinAi(Creature creature, Creature player){
        super(creature);
        this.player = player;
    }

    @Override
    public void onUpdate() {
        if (canRangedWeaponAttack(player)){
            creature.rangedWeaponAttack(player);
        }else if (creature.canSee(player.x,player.y,player.z)) {
            hunt(player);
            playerLocation = new Point(player.x, player.y, player.z);
        } else if (playerLocation != null) {
            moveTo(playerLocation);
        } else {
            wander();
        }
    }

    private boolean canRangedWeaponAttack(Creature player) {
        return creature.getWeapon() != null
                && creature.getWeapon().getRangedAttackValue() > 0
                && creature.canSee(player.x, player.y, player.z);
    }

    public void hunt(Creature target){
        List<Point> points = new Path(creature, target.x, target.y).points();

        int mx = points.get(0).x - creature.x;
        int my = points.get(0).y - creature.y;

        creature.moveBy(mx, my, 0);
    }

    public void moveTo(Point location){
        List<Point> points = new Path(creature, location.x, location.y).points();

        int mx = points.get(0).x - creature.x;
        int my = points.get(0).y - creature.y;

        creature.moveBy(mx, my, 0);

        if (creature.x == playerLocation.x && creature.y == playerLocation.y){
            playerLocation = null;
        }
    }
}
