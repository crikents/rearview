package com.crikents.rearview;

import net.minecraft.item.Item;

/**
 * Created by crikents on 12/13/13.
 */
public class Mirror extends Item {
    public boolean onLeft;
    public Mirror(int par1, boolean onLeft) {
        super(par1);
        this.onLeft = onLeft;
    }
}
