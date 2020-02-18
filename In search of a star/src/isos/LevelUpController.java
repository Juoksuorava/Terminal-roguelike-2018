package isos;

import java.util.ArrayList;
import java.util.List;

public class LevelUpController {

    private static LevelUpOption[] options = new LevelUpOption[] {
            new LevelUpOption("Increased hit points"){
                public void invoke(Creature creature) { creature.modifyMaxHp(10); }
                },
            new LevelUpOption("Increased attack value"){
                public void invoke(Creature creature) { creature.modifyAttackValue(2); }
                },
            new LevelUpOption("Increased defense value") {
                public void invoke(Creature creature) { creature.modifyDefenseValue(2); }
                },
            new LevelUpOption("Increased vision radius"){
                public void invoke(Creature creature) { creature.modifyVisionRadius(1); }
                }
    };

    public void autoLevelUp(Creature creature){
        options[(int)(Math.random() * options.length)].invoke(creature);
    }

    public List<String> getLevelUpOptions() {
        List<String> names = new ArrayList<String>();
        for (LevelUpOption option : options){
            names.add(option.name());
        }
        return names;
    }

    public LevelUpOption getLevelUpOption(String name) {
        for (LevelUpOption option : options){
            if (option.name().equals(name)) return option;
        }
        return null;
    }
}
