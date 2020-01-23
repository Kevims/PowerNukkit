package cn.nukkit.block;

import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityTypes;
import cn.nukkit.entity.misc.FallingBlock;
import cn.nukkit.level.Level;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.registry.BlockRegistry;
import cn.nukkit.registry.EntityRegistry;
import cn.nukkit.utils.Identifier;

import static cn.nukkit.block.BlockIds.AIR;
import cn.nukkit.nbt.tag.Tag;


/**
 * author: rcsuperman
 * Nukkit Project
 */
public abstract class BlockFallable extends BlockSolid {

    public BlockFallable(Identifier id) {
        super(id);
    }

    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            Block down = this.down();
            if (down.getId() == AIR || down instanceof BlockLiquid || down.getLevelBlockAtLayer(1) instanceof BlockLiquid) {
                this.level.setBlock(this, Block.get(AIR), true, true);
                FallingBlock fall = createFallingEntity(new CompoundTag());

                fall.spawnToAll();
            }
        }
        return type;
    }

    protected FallingBlock createFallingEntity(CompoundTag customNbt) {
        CompoundTag nbt = new CompoundTag()
                .putList(new ListTag<DoubleTag>("Pos")
                        .add(new DoubleTag("", this.x + 0.5))
                        .add(new DoubleTag("", this.y))
                        .add(new DoubleTag("", this.z + 0.5)))
                .putList(new ListTag<DoubleTag>("Motion")
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0))
                        .add(new DoubleTag("", 0)))

                .putList(new ListTag<FloatTag>("Rotation")
                        .add(new FloatTag("", 0))
                        .add(new FloatTag("", 0)))
                .putInt("TileID", BlockRegistry.get().getLegacyId(this.getId()))
                .putByte("Data", this.getDamage());

        for (Tag customTag : customNbt.getAllTags()) {
            nbt.put(customTag.getName(), customTag.copy());
        }

        FallingBlock fall = EntityRegistry.get().newEntity(EntityTypes.FALLING_BLOCK,
                this.getLevel().getChunk(this.getChunkX(), this.getChunkZ()), nbt);

        if (fall != null) {
            fall.spawnToAll();
        }

        return fall;
    }
}
