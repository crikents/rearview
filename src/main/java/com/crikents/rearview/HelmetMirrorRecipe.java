package com.crikents.rearview;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

import java.util.ArrayList;

/**
 * Created by crikents on 12/13/13.
 */
public class HelmetMirrorRecipe extends ShapelessRecipes {
    public ItemArmor helmet;
    public byte side;

    HelmetMirrorRecipe(final byte side, final Mirror m, final ItemArmor helmet) {
        super(addMirror(new ItemStack(helmet), side), new ArrayList<ItemStack>(){{
            add(new ItemStack(helmet));
            add(new ItemStack(m, 1, side));
        }});
        this.side = side;
        this.helmet = helmet;
    }

    public static ItemStack addMirror(ItemStack helmet, byte side) {
        ItemStack newIs = helmet.copy();
        newIs.setTagInfo(RearviewMod.MODID, new NBTTagByte("mirrorSide", side));

        NBTTagCompound display = newIs.getTagCompound().getCompoundTag("display");
        if (display == null) display = new NBTTagCompound();
        NBTTagList lore = display.getTagList("Lore");
        if (lore == null) lore = new NBTTagList();
        lore.appendTag(new NBTTagString("Lore", "With " + (side == 0 ? "left" : "right") +
                " helmet mirror."));
        display.setTag("Lore", lore);
        newIs.setTagInfo("display", display);
        return newIs;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        ItemStack theHelmet = null;
        for (int i = 0; i < inventorycrafting.getSizeInventory(); i++) {
            ItemStack is = inventorycrafting.getStackInSlot(i);
            if (is == null) continue;
            Item item = is.getItem();
            if (item instanceof ItemArmor) {
                theHelmet = is;
            }
        }

        return addMirror(theHelmet, this.side);
    }
}
