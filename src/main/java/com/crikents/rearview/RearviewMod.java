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
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

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

    public static Mirror hml;
    public static Mirror hmr;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        rv.preinit(event);
        conf = new Configuration(event.getSuggestedConfigurationFile());
        log = event.getModLog();
        hml = new Mirror(conf.getItem("helmetMirrorLeft", 1024, "The Helmet Mirror").getInt(), true);
        hmr = new Mirror(conf.getItem("helmetMirrorRight", 1025, "The Helmet Mirror").getInt(), false);
        hml.setUnlocalizedName("helmetMirrorLeft");
        hmr.setUnlocalizedName("helmetMirrorRight");
        hml.setTextureName("rearview:helmetMirrorLeft");
        hmr.setTextureName("rearview:helmetMirrorRight");
        LanguageRegistry.addName(hml, "Helmet Mirror (Left)");
        LanguageRegistry.addName(hmr, "Helmet Mirror (Right)");
        GameRegistry.registerItem(hml, "helmetMirrorLeft", MODID);
        GameRegistry.registerItem(hmr, "helmetMirrorRight", MODID);

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hml), " IG", " IG", "S  ",
                'I', Item.ingotIron, 'G', Block.glass, 'S', "stickWood"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(hmr), "GI ", "GI ", "  S",
                'I', Item.ingotIron, 'G', Block.glass, 'S', "stickWood"));

        GameRegistry.addRecipe(new HelmetMirrorRecipe());
        conf.save();
    }
    public void init(FMLInitializationEvent event)       { rv.init(event); }
}
