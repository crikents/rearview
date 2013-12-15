package com.crikents.rearview;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

/**
 * Created by crikents on 12/13/13.
 */
public class HelmetMirrorRecipe implements IRecipe {
    @Override
    public boolean matches(InventoryCrafting inventorycrafting, World world) {
        boolean foundMirror = false, foundHelmet = false;
        int i = 0;
        for (; i < inventorycrafting.getSizeInventory(); i++) {
            ItemStack is = inventorycrafting.getStackInSlot(i);
            if (is == null) continue;
            Item item = is.getItem();
            if (item instanceof Mirror && !foundMirror) foundMirror = true;
            else if (item instanceof ItemArmor && ((ItemArmor)item).armorType == 0) {
                NBTTagCompound nbt = is.getTagCompound();
                if (nbt == null || !nbt.hasKey(RearviewMod.MODID))
                    foundHelmet = true;
                else
                    return false;
            }
            else return false;
        }
        return foundMirror && foundHelmet;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
        ItemStack theHelmet = null;
        Mirror theMirror = null;
        for (int i = 0; i < inventorycrafting.getSizeInventory(); i++) {
            ItemStack is = inventorycrafting.getStackInSlot(i);
            if (is == null) continue;
            Item item = is.getItem();
            if (item instanceof ItemArmor) {
                theHelmet = is;
            } else {
                theMirror = (Mirror)is.getItem();
            }
        }

        if (theHelmet == null || theMirror == null) return null;

        ItemStack newIs = theHelmet.copy();
        newIs.setTagInfo(RearviewMod.MODID, new NBTTagByte("mirrorSide", (byte)(theMirror.onLeft ? 1 : 0)));

        NBTTagCompound display = newIs.getTagCompound().getCompoundTag("display");
        if (display == null) display = new NBTTagCompound();
        NBTTagList lore = display.getTagList("Lore");
        if (lore == null) lore = new NBTTagList();
        lore.appendTag(new NBTTagString("Lore", "\u00A1con espejo! " + (theMirror.onLeft ? "LEFT" : "RIGHT")));
        display.setTag("Lore", lore);
        newIs.setTagInfo("display", display);
        return newIs;
    }

    @Override
    public int getRecipeSize() {
        return 4;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }
}
