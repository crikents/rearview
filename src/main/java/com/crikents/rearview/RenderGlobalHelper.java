package com.crikents.rearview;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.texture.TextureMap;

/**
 * Created by crikents on 12/19/13.
 */
public final class RenderGlobalHelper {
    public Minecraft mc;
    public RenderGlobal rg, orig;
    public boolean advanced_opengl;
    public boolean fancy_graphics;
    public int ambient_occlusion;
    public String skin = "";

    RenderGlobalHelper() {
        mc = Minecraft.getMinecraft();
        rg = new RenderGlobal(mc);
        rg.registerDestroyBlockIcons((TextureMap) mc.getTextureManager().getTexture(TextureMap.locationBlocksTexture));
        orig = null;
    }

    public void getSettings() {
        advanced_opengl = mc.gameSettings.advancedOpengl;
        fancy_graphics = mc.gameSettings.fancyGraphics;
        ambient_occlusion = mc.gameSettings.ambientOcclusion;
        skin = mc.gameSettings.skin;
    }

    public boolean settingsChanged() {
        return advanced_opengl != mc.gameSettings.advancedOpengl ||
                fancy_graphics != mc.gameSettings.fancyGraphics ||
                ambient_occlusion != mc.gameSettings.ambientOcclusion ||
                skin != mc.gameSettings.skin;
    }

    public void switchTo() {
        if (orig == null)
            orig = mc.renderGlobal;
        if (orig.theWorld != rg.theWorld) {
            rg.setWorldAndLoadRenderers(orig.theWorld);
            getSettings();
        } else if (settingsChanged()) {
            rg.loadRenderers();
            getSettings();
        }
        mc.renderGlobal = rg;
    }

    public void switchFrom() {
        if (orig != null)
            mc.renderGlobal = orig;
        orig = null;
    }
}
