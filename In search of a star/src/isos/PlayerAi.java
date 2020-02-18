package isos;

import java.util.List;

public class PlayerAi extends CreatureAi {

    private List<String> messages;
    private FOV fov;

    public PlayerAi(Creature creature, List<String> messages, FOV fov){
        super(creature);
        this.messages = messages;
        this.fov = fov;
    }

    @Override
    public void onEnter(int x, int y, int z, Tile tile){
        if (tile.isGround()){
            creature.x = x;
            creature.y = y;
            creature.z = z;
        } else if (tile.isDiggable()){
            creature.dig(x, y, z);
        }
    }

    @Override
    public void onNotify(String message){
        messages.add(message);
    }

    /*
    only creature that needs fov is the player
    so we have to override the canSee method
    */
    @Override
    public boolean canSee(int wx, int wy, int wz){
        return fov.isVisible(wx, wy, wz);
    }

    @Override
    public void onGainLevel() {
    }

    @Override
    public Tile rememberedTile(int wx, int wy, int wz) {
        return fov.tile(wx, wy, wz);
    }
}
