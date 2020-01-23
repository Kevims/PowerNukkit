package cn.nukkit.item;


import cn.nukkit.block.Block;
import cn.nukkit.block.BlockBeehive;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3f;
import cn.nukkit.player.Player;
import cn.nukkit.utils.Identifier;

import static cn.nukkit.block.BlockIds.FLOWING_WATER;
import static cn.nukkit.block.BlockIds.WATER;
import static cn.nukkit.item.ItemIds.POTION;

public class ItemGlassBottle extends Item {

    public ItemGlassBottle(Identifier id) {
        super(id);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, Vector3f clickPos) {
        Item filled = null;
        if (target.getId() == WATER || target.getId() == FLOWING_WATER) {
            filled = Item.get(POTION);
        } else if (target instanceof BlockBeehive && ((BlockBeehive) target).isFull()) {
            filled = Item.get(HONEY_BOTTLE);
            ((BlockBeehive) target).honeyCollected(player);
            level.addSound(player, Sound.BUCKET_FILL_WATER);
        }
        
        if (filled != null) {
            if (this.getCount() == 1) {
                player.getInventory().setItemInHand(filled);
            } else if (this.getCount() > 1) {
                this.decrementCount();
                player.getInventory().setItemInHand(this);
                if (player.getInventory().canAddItem(filled)) {
                    player.getInventory().addItem(filled);
                } else {
                    player.getLevel().dropItem(player.add(0, 1.3, 0), filled, player.getDirectionVector().multiply(0.4));
                }
            }
        }
        
        return false;
    }
}
