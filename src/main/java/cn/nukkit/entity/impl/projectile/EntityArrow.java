package cn.nukkit.entity.impl.projectile;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBell;
import cn.nukkit.block.BlockCampfire;
import cn.nukkit.block.BlockFire;
import cn.nukkit.block.BlockID;
import cn.nukkit.block.BlockTNT;
import cn.nukkit.entity.EntityType;
import cn.nukkit.entity.impl.BaseEntity;
import cn.nukkit.entity.projectile.Arrow;
import cn.nukkit.level.Sound;
import cn.nukkit.level.chunk.Chunk;
import cn.nukkit.nbt.tag.CompoundTag;

import java.util.concurrent.ThreadLocalRandom;

import static cn.nukkit.entity.data.EntityFlag.CRITICAL;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class EntityArrow extends EntityProjectile implements Arrow {

    public static final int PICKUP_NONE = 0;
    public static final int PICKUP_ANY = 1;
    public static final int PICKUP_CREATIVE = 2;

    protected int pickupMode;
    protected float gravity = 0.05f;
    protected float drag = 0.01f;

    public EntityArrow(EntityType<Arrow> type, Chunk chunk, CompoundTag nbt) {
        this(type, chunk, nbt, null);
    }

    public EntityArrow(EntityType<Arrow> type, Chunk chunk, CompoundTag nbt, BaseEntity shootingEntity) {
        this(type, chunk, nbt, shootingEntity, false);
    }

    public EntityArrow(EntityType<Arrow> type, Chunk chunk, CompoundTag nbt, BaseEntity shootingEntity, boolean critical) {
        super(type, chunk, nbt, shootingEntity);
        this.setCritical(critical);
    }

    @Override
    public float getWidth() {
        return 0.5f;
    }

    @Override
    public float getLength() {
        return 0.5f;
    }

    @Override
    public float getHeight() {
        return 0.5f;
    }

    @Override
    public float getGravity() {
        return 0.05f;
    }

    @Override
    public float getDrag() {
        return 0.01f;
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        this.damage = namedTag.contains("damage") ? namedTag.getDouble("damage") : 2;
        this.pickupMode = namedTag.contains("pickup") ? namedTag.getByte("pickup") : PICKUP_ANY;
    }

    public void setCritical() {
        this.setCritical(true);
    }

    public boolean isCritical() {
        return this.getFlag(CRITICAL);
    }

    public void setCritical(boolean value) {
        this.setFlag(CRITICAL, value);
    }

    @Override
    public int getResultDamage() {
        int base = super.getResultDamage();

        if (this.isCritical()) {
            base += ThreadLocalRandom.current().nextInt(base / 2 + 2);
        }

        return base;
    }

    @Override
    protected double getBaseDamage() {
        return 2;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        if (this.closed) {
            return false;
        }

        this.timing.startTiming();

        boolean hasUpdate = super.onUpdate(currentTick);

        if (this.onGround || this.hadCollision) {
            this.setCritical(false);
        }

        if (this.age > 1200) {
            this.close();
            hasUpdate = true;
        }

        this.timing.stopTiming();

        return hasUpdate;
    }

    @Override
    protected void addHitEffect() {
        this.level.addSound(this, Sound.RANDOM_BOWHIT);
    }

    @Override
    protected boolean onCollideWithBlock(Block collisionBlock) {
        if (super.onCollideWithBlock(collisionBlock)) {
            if (collisionBlock instanceof BlockBell && isOnFire() && level.getBlock(this).getId() == BlockID.AIR) {
                level.setBlock(this, new BlockFire(), true, true);
            }
            return true;
        }

        if (isOnFire()) {
            if (collisionBlock instanceof BlockCampfire) {
                BlockCampfire campfire = (BlockCampfire) collisionBlock;
                if (campfire.isExtinguished()) {
                    campfire.setExtinguished(false);
                    level.setBlock(collisionBlock, collisionBlock, true, true);
                }
                return true;
            } else if (collisionBlock instanceof BlockTNT) {
                ((BlockTNT) collisionBlock).prime(80, this);
                return true;
            }
        }

        return false;
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        this.namedTag.putByte("pickup", this.pickupMode);
    }

    public int getPickupMode() {
        return this.pickupMode;
    }

    public void setPickupMode(int pickupMode) {
        this.pickupMode = pickupMode;
    }
}
