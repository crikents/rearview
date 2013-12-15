package com.crikents.rearview;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;

import java.util.logging.Logger;

@Mod(modid = RearviewMod.MODID, version = RearviewMod.VERSION)
public class RearviewMod
{
    public static final String MODID = "rearview";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.crikents.rearview.RearviewClient", serverSide = "com.crikents.rearview.RearviewServer")
    public static Rearview rv;
    public static Configuration conf;
    public static Logger log;

    public static Mirror hm;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        rv.preinit(event);
        conf = new Configuration(event.getSuggestedConfigurationFile());
        log = event.getModLog();
        hm = new Mirror(conf.getItem("helmetMirror", 1024, "The Helmet Mirror").getInt());
        hm.setUnlocalizedName("helmetMirror");
        hm.setTextureName("rearview:helmetMirror");
        LanguageRegistry.addName(hm, "Helmet Mirror");
        GameRegistry.registerItem(hm, "helmetMirror", MODID);

        GameRegistry.addShapedRecipe(new ItemStack(hm), " GG", " GG", "S  ", Character.valueOf('G'), Block.glass, Character.valueOf('S'), Item.stick);
        GameRegistry.addRecipe(new HelmetMirrorRecipe());
        conf.save();
    }
    public void init(FMLInitializationEvent event)       { rv.init(event); }
}
