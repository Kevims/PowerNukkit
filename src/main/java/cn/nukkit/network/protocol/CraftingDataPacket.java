package cn.nukkit.network.protocol;

import cn.nukkit.inventory.*;
import cn.nukkit.item.Item;
import cn.nukkit.registry.ItemRegistry;
import cn.nukkit.utils.Binary;
import io.netty.buffer.ByteBuf;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Nukkit Project Team
 */
@ToString
public class CraftingDataPacket extends DataPacket {

    public static final short NETWORK_ID = ProtocolInfo.CRAFTING_DATA_PACKET;

    public static final String CRAFTING_TAG_CRAFTING_TABLE = "crafting_table";
    public static final String CRAFTING_TAG_CARTOGRAPHY_TABLE = "cartography_table";
    public static final String CRAFTING_TAG_STONECUTTER = "stonecutter";
    public static final String CRAFTING_TAG_FURNACE = "furnace";
    public static final String CRAFTING_TAG_CAMPFIRE = "campfire";
    public static final String CRAFTING_TAG_BLAST_FURNACE = "blast_furnace";
    public static final String CRAFTING_TAG_SMOKER = "smoker";

    private final List<Recipe> entries = new ArrayList<>();
    private final List<BrewingRecipe> brewingEntries = new ArrayList<>();
    private final List<ContainerRecipe> containerEntries = new ArrayList<>();
    public boolean cleanRecipes;

    public void addRecipes(Recipe... recipe) {
        Collections.addAll(entries, recipe);
    }
    
    public void addStonecutterRecipe(StonecutterRecipe... recipes) {
        Collections.addAll(entries, recipes);
    }
    
    public void addCartographyRecipe(CartographyRecipe... recipe) {
        Stream.of(recipe).filter(r -> r.getRecipeId() != null).forEachOrdered(r -> entries.add(r));
    }

    public void addRecipes(Collection<? extends Recipe> recipes) {
        entries.addAll(recipes);
    }

    public void addSmokerRecipe(SmokerRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    public void addBlastFurnaceRecipe(BlastFurnaceRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    public void addCampfireRecipeRecipe(CampfireRecipe... recipe) {
        Collections.addAll(entries, recipe);
    }

    public void addBrewingRecipes(Collection<BrewingRecipe> brewingRecipes) {
        brewingEntries.addAll(brewingRecipes);
    }

    public void addContainerRecipes(Collection<ContainerRecipe> containerRecipes) {
        containerEntries.addAll(containerRecipes);
    }

    @Override
    protected void decode(ByteBuf buffer) {

    }

    @Override
    protected void encode(ByteBuf buffer) {
        Binary.writeUnsignedVarInt(buffer, entries.size());

        for (Recipe recipe : entries) {
            Binary.writeVarInt(buffer, recipe.getType().networkType);
            switch (recipe.getType()) {
                case STONECUTTER:
                    StonecutterRecipe stonecutter = (StonecutterRecipe) recipe;
                    this.putString(stonecutter.getRecipeId());
                    this.putUnsignedVarInt(1);
                    this.putRecipeIngredient(stonecutter.getIngredient());
                    this.putUnsignedVarInt(1);
                    this.putSlot(stonecutter.getResult());
                    this.putUUID(stonecutter.getId());
                    this.putString(CRAFTING_TAG_STONECUTTER);
                    this.putVarInt(stonecutter.getPriority());
                    break;
                case SHAPELESS:
                case SHULKER_BOX:
                case SHAPED_CHEMISTRY:
                case CARTOGRAPHY:
                    ShapelessRecipe shapeless = (ShapelessRecipe) recipe;
                    Binary.writeString(buffer, shapeless.getRecipeId());
                    List<Item> ingredients = shapeless.getIngredientList();
                    Binary.writeUnsignedVarInt(buffer, ingredients.size());
                    for (Item ingredient : ingredients) {
                        Binary.writeRecipeIngredient(buffer, ingredient);
                    }
                    Binary.writeUnsignedVarInt(buffer, 1);
                    Binary.writeItem(buffer, shapeless.getResult());
                    Binary.writeUuid(buffer, shapeless.getId());
                    Binary.writeString(buffer, recipe.getType() == RecipeType.CARTOGRAPHY ? CRAFTING_TAG_CARTOGRAPHY_TABLE : CRAFTING_TAG_CRAFTING_TABLE);
                    Binary.writeVarInt(buffer, shapeless.getPriority());
                    break;
                case SHAPED:
                case SHAPELESS_CHEMISTRY:
                    ShapedRecipe shaped = (ShapedRecipe) recipe;
                    Binary.writeString(buffer, shaped.getRecipeId());
                    Binary.writeVarInt(buffer, shaped.getWidth());
                    Binary.writeVarInt(buffer, shaped.getHeight());

                    for (int z = 0; z < shaped.getHeight(); ++z) {
                        for (int x = 0; x < shaped.getWidth(); ++x) {
                            Binary.writeRecipeIngredient(buffer, shaped.getIngredient(x, z));
                        }
                    }
                    List<Item> outputs = new ArrayList<>();
                    outputs.add(shaped.getResult());
                    outputs.addAll(shaped.getExtraResults());
                    Binary.writeUnsignedVarInt(buffer, outputs.size());
                    for (Item output : outputs) {
                        Binary.writeItem(buffer, output);
                    }
                    Binary.writeUuid(buffer, shaped.getId());
                    Binary.writeString(buffer, CRAFTING_TAG_CRAFTING_TABLE);
                    Binary.writeVarInt(buffer, shaped.getPriority());
                    break;
                case FURNACE:
                case FURNACE_DATA:
                case SMOKER:
                case SMOKER_DATA:
                case BLAST_FURNACE:
                case BLAST_FURNACE_DATA:
                case CAMPFIRE:
                case CAMPFIRE_DATA:
                    SmeltingRecipe smelting = (SmeltingRecipe) recipe;
                    Item input = smelting.getInput();
                    Binary.writeVarInt(buffer, ItemRegistry.get().getRuntimeId(input.getId()));
                    if (recipe.getType().name().endsWith("_DATA")) {
                        Binary.writeVarInt(buffer, input.getDamage());
                    }
                    Binary.writeItem(buffer, smelting.getResult());
                    switch (recipe.getType()) {
                        case FURNACE:
                        case FURNACE_DATA:
                            Binary.writeString(buffer, CRAFTING_TAG_FURNACE);
                            break;
                        case SMOKER:
                        case SMOKER_DATA:
                            Binary.writeString(buffer, CRAFTING_TAG_SMOKER);
                            break;
                        case BLAST_FURNACE:
                        case BLAST_FURNACE_DATA:
                            Binary.writeString(buffer, CRAFTING_TAG_BLAST_FURNACE);
                            break;
                        case CAMPFIRE:
                        case CAMPFIRE_DATA:
                            Binary.writeString(buffer, CRAFTING_TAG_CAMPFIRE);
                            break;
                    }
                    break;
                case MULTI:
                    throw new UnsupportedOperationException();
            }
        }

        ItemRegistry registry = ItemRegistry.get();

        Binary.writeVarInt(buffer, 0);
        Binary.writeVarInt(buffer, 0);
        Binary.writeVarInt(buffer, this.brewingEntries.size());
        for (BrewingRecipe recipe : brewingEntries) {
            Binary.writeVarInt(buffer, recipe.getInput().getDamage());
            Binary.writeVarInt(buffer, registry.getRuntimeId(recipe.getIngredient().getId()));
            Binary.writeVarInt(buffer, recipe.getResult().getDamage());
        }

        Binary.writeVarInt(buffer, this.containerEntries.size());
        for (ContainerRecipe recipe : containerEntries) {
            Binary.writeVarInt(buffer, registry.getRuntimeId(recipe.getInput().getId()));
            Binary.writeVarInt(buffer, registry.getRuntimeId(recipe.getIngredient().getId()));
            Binary.writeVarInt(buffer, registry.getRuntimeId(recipe.getResult().getId()));
        }

        buffer.writeBoolean(cleanRecipes);
    }

    @Override
    public short pid() {
        return NETWORK_ID;
    }

}
