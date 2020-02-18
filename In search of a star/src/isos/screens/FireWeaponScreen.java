package isos.screens;

import isos.Creature;
import isos.Line;
import isos.Point;

public class FireWeaponScreen extends TargetScreen {

    public FireWeaponScreen(Creature player, int sx, int sy){
        super(player, "Fire " + player.getWeapon().getName() + " at?", sx, sy);
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
        Creature other = player.creature(x, y, player.z);
        if (other == null){
            player.notify("There's nothing there to fire at.");
        } else {
            player.rangedWeaponAttack(other);
        }
    }
}
