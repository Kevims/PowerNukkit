package cn.nukkit.entity.impl.projectile;

import cn.nukkit.entity.EntityType;
import cn.nukkit.entity.impl.BaseEntity;
import cn.nukkit.entity.projectile.Egg;
import cn.nukkit.item.ItemEgg;
import cn.nukkit.level.chunk.Chunk;
import cn.nukkit.level.particle.ItemBreakParticle;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.concurrent.ThreadLocalRandom;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EntityEgg extends EntityProjectile implements Egg {

    public EntityEgg(EntityType<Egg> type, Chunk chunk, CompoundTag nbt) {
        this(type, chunk, nbt, null);
    }

    public EntityEgg(EntityType<Egg> type, Chunk chunk, CompoundTag nbt, BaseEntity shootingEntity) {
        super(type, chunk, nbt, shootingEntity);
    }

    @Override
    public float getWidth() {
        return 0.25f;
    }

    @Override
    public float getLength() {
        return 0.25f;
    }

    @Override
    public float getHeight() {
        return 0.25f;
    }

    @Override
    public float getGravity() {
        return 0.03f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        boolean hasUpdate = super.onUpdate(currentTick);

        if (this.age > 1200 || this.isCollided) {
            this.kill();
            hasUpdate = true;
        }

        return hasUpdate;
    }
    
    @Override
    protected void addHitEffect() {
        int particles = ThreadLocalRandom.current().nextInt(10) + 5;
        ItemEgg egg = new ItemEgg();
        for (int i = 0; i < particles; i++) {
            level.addParticle(new ItemBreakParticle(this, egg));
        }
    }
}
