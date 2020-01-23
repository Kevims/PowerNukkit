package cn.nukkit.item;

import cn.nukkit.block.BlockSweetBerryBush;

import cn.nukkit.utils.Identifier;

public class ItemSweetBerries extends ItemEdible {

    public ItemSweetBerries(Identifier id) {
        super(id);
        this.block = new BlockSweetBerryBush();
    }
}
