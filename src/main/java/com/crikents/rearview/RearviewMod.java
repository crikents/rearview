package com.crikents.rearview;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.logging.Logger;

@Mod(modid = RearviewMod.MODID, version = RearviewMod.VERSION)
public class RearviewMod {
    public static final String MODID = "rearview";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "com.crikents.rearview.RearviewClient", serverSide = "com.crikents.rearview.RearviewServer")
    public static Rearview rv;
    public static Configuration conf;
    public static Logger log;

    public static Mirror mir;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        rv.preinit(event);
        conf = new Configuration(event.getSuggestedConfigurationFile());
        log = event.getModLog();
        mir = new Mirror(conf.getItem("Helmet Mirror", 3841, "The Helmet Mirror").getInt());
        LanguageRegistry.instance().addStringLocalization("item.helmetMirror.left.name", "Helmet Mirror (Left)");
        LanguageRegistry.instance().addStringLocalization("item.helmetMirror.right.name", "Helmet Mirror (Right)");
        GameRegistry.registerItem(mir, "helmetMirror", MODID);

        String patLeft[] = {" IG", " IG", "S  "};
        String patRight[] = {"GI ", "GI ", "  S"};
        final IRecipe rightRecipe = new ShapedOreRecipe(new ItemStack(mir, 1, 1), patRight, 'G', Block.glass,
                'I', Item.ingotIron, 'S', "stickWood");
        IRecipe leftRecipe = new ShapedOreRecipe(new ItemStack(mir, 1, 0), patLeft, 'G', Block.glass,
                'I', Item.ingotIron, 'S', "stickWood") {
            @Override
            public ItemStack getCraftingResult(InventoryCrafting inventorycrafting) {
                if (inventorycrafting.getStackInSlot(0) == null)
                    return super.getCraftingResult(inventorycrafting);
                else
                    return rightRecipe.getCraftingResult(inventorycrafting);
            }

            ;
        };
        GameRegistry.addRecipe(leftRecipe);
        GameRegistry.addRecipe(rightRecipe);

        conf.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        rv.init(event);
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        for (Item i : Item.itemsList) {
            if (!(i instanceof ItemArmor)) continue;
            ItemArmor armor = (ItemArmor) i;
            if (armor.armorType != 0) continue;
            GameRegistry.addRecipe(new HelmetMirrorRecipe((byte) 0, mir, armor));
            GameRegistry.addRecipe(new HelmetMirrorRecipe((byte) 1, mir, armor));
        }
    }
}
