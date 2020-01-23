package cn.nukkit.item;

import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Identifier;

public class ItemPotionLingering extends ProjectileItem {

    public ItemPotionLingering(Identifier id) {
        super(id);
    }
    
    @Override
    public int getMaxStackSize() {
        return 1;
    }
    
    @Override
    public boolean canBeActivated() {
        return true;
    }
    
    @Override
    public String getProjectileEntityType() {
        return "LingeringPotion";
    }
    
    @Override
    public float getThrowForce() {
        return 0.5f;
    }
    
    @Override
    protected void correctNBT(CompoundTag nbt) {
        nbt.putInt("PotionId", this.meta);
    }
}
