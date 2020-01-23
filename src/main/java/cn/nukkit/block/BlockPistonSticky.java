package cn.nukkit.block;

import cn.nukkit.utils.Identifier;

/**
 * @author CreeperFace
 */
public class BlockPistonSticky extends BlockPistonBase {

    public BlockPistonSticky(Identifier id) {
        super(id);
    }
    
    @Override
    protected BlockPistonHead createHead(int damage) {
        return new BlockPistonHeadSticky(damage);
    }
}
