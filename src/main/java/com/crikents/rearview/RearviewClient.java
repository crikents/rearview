package com.crikents.rearview;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;

import java.util.EnumSet;

/**
 * Created by crikents on 12/12/13.
 */
public class RearviewClient implements Rearview, ITickHandler {

    public Minecraft mc;
    public int mirrorFBO;
    public int mirrorTex;
    public int mirrorDepth;

    @Override
    public void preinit(FMLPreInitializationEvent event) {
        TickRegistry.registerTickHandler(this, Side.CLIENT);
        mc = Minecraft.getMinecraft();
        mirrorFBO = ARBFramebufferObject.glGenFramebuffers();
        mirrorTex = GL11.glGenTextures();
        mirrorDepth = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mirrorTex);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8, 320, 180, 0, GL11.GL_RGBA, GL11.GL_INT,
                (java.nio.IntBuffer)null);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mirrorDepth);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_R, 320, 180, 0, GL11.GL_RGBA, GL11.GL_INT,
                (java.nio.IntBuffer)null);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void tickStart(EnumSet<TickType> tickTypes, Object... objects) {

    }

    @Override
    public void tickEnd(EnumSet<TickType> tickTypes, Object... objects) {
        if (mc.theWorld != null && mc.currentScreen == null) {
            int w, h;
            float y;
            boolean hide;
            w = mc.displayWidth;
            h = mc.displayHeight;
            y = mc.renderViewEntity.rotationYaw;
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER, mirrorFBO);
            ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER,
                    ARBFramebufferObject.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D,
                    mirrorTex, 0);
            ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER,
                    ARBFramebufferObject.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D,
                    mirrorDepth, 0);
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            mc.displayHeight = 180;
            mc.displayWidth = 320;
            mc.renderViewEntity.rotationYaw = 180;
            mc.entityRenderer.updateCameraAndRender(Math.ulp(0f));
            mc.displayHeight = h;
            mc.displayWidth = w;
            mc.thePlayer.rotationYaw = y;
            ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER, 0);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mirrorTex);
            Tessellator tes = Tessellator.instance;
            tes.startDrawing(GL11.GL_QUADS);
            tes.addVertexWithUV(0, 0, 0, 0, 1);
            tes.addVertexWithUV(0, 180, 0, 0, 0);
            tes.addVertexWithUV(320, 180, 0, 1, 0);
            tes.addVertexWithUV(320, 0, 0, 1, 1);
            tes.draw();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.RENDER);
    }

    @Override
    public String getLabel() {
        return "rearview-render";
    }
}
