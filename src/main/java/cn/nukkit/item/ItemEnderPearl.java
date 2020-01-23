package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityEnderPearl;

import cn.nukkit.entity.EntityType;
import cn.nukkit.entity.EntityTypes;
import cn.nukkit.utils.Identifier;

public class ItemEnderPearl extends ProjectileItem {

    public ItemEnderPearl(Identifier id) {
        super(id);
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    @Override
    public EntityType<?> getProjectileEntityType() {
        return EntityTypes.ENDER_PEARL;
    }

    @Override
    public float getThrowForce() {
        return 1.5f;
    }

    @Override
    protected Entity correctProjectile(Player player, Entity projectile) {
        if (projectile instanceof EntityEnderPearl) {
            if (player.getServer().getTick() - player.getLastEnderPearlThrowingTick() < 20) {
                projectile.kill();
                return null;
            }
            return projectile;
        }
        return null;
    }
}
