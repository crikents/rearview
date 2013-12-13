package com.crikents.rearview;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by crikents on 12/12/13.
 */
public interface Rearview {
    public void preinit(FMLPreInitializationEvent event);
    public void init(FMLInitializationEvent event);
}
