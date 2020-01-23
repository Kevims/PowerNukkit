package cn.nukkit.entity.impl.passive;

import cn.nukkit.entity.EntityType;
import cn.nukkit.entity.passive.Turtle;
import cn.nukkit.level.chunk.Chunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * Created by PetteriM1
 */
public class EntityTurtle extends Animal implements Turtle {

    public EntityTurtle(EntityType<Turtle> type, Chunk chunk, CompoundTag nbt) {
        super(type, chunk, nbt);
    }

    public String getName() {
        return "Turtle";
    }

    @Override
    public float getWidth() {
        return 1.2f;
    }

    @Override
    public float getHeight() {
        return 0.4f;
    }

    @Override
    public void initEntity() {
        super.initEntity();
        this.setMaxHealth(30);
    }

    public void setBreedingAge(int ticks) {

    }

    public void setHomePos(Vector3 pos) {

    }
}
