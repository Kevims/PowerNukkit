package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.utils.BlockColor;
import cn.nukkit.utils.Identifier;

import static cn.nukkit.block.BlockIds.DOUBLE_STONE_SLAB2;

/**
 * Created by CreeperFace on 26. 11. 2016.
 */
public class BlockSlabRedSandstone extends BlockSlab {

    public static final int RED_SANDSTONE = 0;
    public static final int PURPUR = 1;
    public static final int PRISMARINE = 2;
    public static final int PRISMARINE_BRICKS = 3;
    public static final int DARK_PRISMARINE = 4;
    public static final int MOSSY_COBBLESTONE = 5;
    public static final int SMOOTH_SANDSTONE = 6;
    public static final int RED_NETHER_BRICK = 7;

    public BlockSlabRedSandstone(Identifier id) {
        super(id);
    }

    @Override
    public BlockColor getColor() {
        switch (this.getDamage() & 0x07) {
            case RED_SANDSTONE:
                return BlockColor.ORANGE_BLOCK_COLOR;
            case PURPUR:
                return BlockColor.MAGENTA_BLOCK_COLOR;
            case PRISMARINE:
                return BlockColor.CYAN_BLOCK_COLOR;
            case PRISMARINE_BRICKS:
            case DARK_PRISMARINE:
                return BlockColor.DIAMOND_BLOCK_COLOR;
            default:
            case MOSSY_COBBLESTONE:
                return BlockColor.STONE_BLOCK_COLOR;
            case SMOOTH_SANDSTONE:
                return BlockColor.SAND_BLOCK_COLOR;
            case RED_NETHER_BRICK:
                return BlockColor.NETHERRACK_BLOCK_COLOR;
        }
    }

    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            return new Item[]{
                    toItem()
            };
        } else {
            return new Item[0];
        }
    }

    @Override
    public Item toItem() {
        return Item.get(id, this.getDamage() & 0x07);
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    protected Identifier getDoubleSlab() {
        return DOUBLE_STONE_SLAB2;
    }
}
