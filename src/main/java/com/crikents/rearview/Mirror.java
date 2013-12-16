package com.crikents.rearview;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import java.util.List;

/**
 * Created by crikents on 12/13/13.
 */
public class Mirror extends Item {
    public Icon[] icons;
    public Mirror(int id) {
        super(id);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return stack.getItemDamage() == 0 ? "item.helmetMirror.left" : "item.helmetMirror.right";
    }

    @Override
    public void getSubItems(int id, CreativeTabs tabs, List list) {
        list.add(new ItemStack(id, 1, 0));
        list.add(new ItemStack(id, 1, 1));
    }

    public void registerIcons(IconRegister register) {
        this.icons = new Icon[2];
        icons[0] = register.registerIcon("rearview:helmetMirrorLeft");
        icons[1] = register.registerIcon("rearview:helmetMirrorRight");
    }

    @Override
    public Icon getIconFromDamage(int par1) {
        return par1 == 0 ? icons[0] : icons[1];
    }
}
