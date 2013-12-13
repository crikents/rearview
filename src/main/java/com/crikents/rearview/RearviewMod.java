package com.crikents.rearview;

import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;

@Mod(modid = RearviewMod.MODID, version = RearviewMod.VERSION)
public class RearviewMod
{
    public static final String MODID = "rearview";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.crikents.rearview.RearviewClient", serverSide = "com.crikents.rearview.RearviewServer")
    public static Rearview rv;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) { rv.preinit(event); }
    public void init(FMLInitializationEvent event)       { rv.init(event); }
}
