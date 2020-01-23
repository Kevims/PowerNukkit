package cn.nukkit.block;

import cn.nukkit.utils.Identifier;

/**
 * @author CreeperFace
 */
public class BlockPiston extends BlockPistonBase {

    public BlockPiston(Identifier id) {
        super(id);
    }
    
    @Override
    protected BlockPistonHead createHead(int damage) {
        return new BlockPistonHead(damage);
    }
}
