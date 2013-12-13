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
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_DEPTH_COMPONENT, 320, 180, 0, GL11.GL_DEPTH_COMPONENT,
                          GL11.GL_INT, (java.nio.IntBuffer)null);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void tickStart(EnumSet<TickType> tickTypes, Object... objects) {

    }

    private void switchToFB() {
        ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER, mirrorFBO);
        ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER,
                ARBFramebufferObject.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D,
                mirrorTex, 0);
        ARBFramebufferObject.glFramebufferTexture2D(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER,
                ARBFramebufferObject.GL_DEPTH_ATTACHMENT, GL11.GL_TEXTURE_2D,
                mirrorDepth, 0);
    }

    private void switchFromFB() {
        ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_DRAW_FRAMEBUFFER, 0);
    }

    @Override
    public void tickEnd(EnumSet<TickType> tickTypes, Object... objects) {
        Tessellator tes = Tessellator.instance;
        if (mc.theWorld == null || mc.currentScreen != null) return;

        int w, h;
        float y, py, p, pp;
        boolean hide;
        int view, limit;
        w = mc.displayWidth;
        h = mc.displayHeight;
        y = mc.renderViewEntity.rotationYaw;
        py = mc.renderViewEntity.prevRotationYaw;
        p = mc.renderViewEntity.rotationPitch;
        pp = mc.renderViewEntity.prevRotationPitch;
        hide = mc.gameSettings.hideGUI;
        view = mc.gameSettings.thirdPersonView;
        limit = mc.gameSettings.limitFramerate;

        switchToFB();

        mc.displayHeight = 180;
        mc.displayWidth = 320;
        mc.gameSettings.hideGUI = true;
        mc.gameSettings.thirdPersonView = 0;
        mc.gameSettings.limitFramerate = 0;
        mc.renderViewEntity.rotationYaw += 180;
        mc.renderViewEntity.prevRotationYaw += 180;
        mc.renderViewEntity.rotationPitch = -p;
        mc.renderViewEntity.prevRotationPitch = -pp;
        mc.entityRenderer.updateCameraAndRender(0);
        mc.renderViewEntity.rotationYaw = y;
        mc.renderViewEntity.prevRotationYaw = py;
        mc.renderViewEntity.rotationPitch = p;
        mc.renderViewEntity.prevRotationPitch = pp;
        mc.gameSettings.limitFramerate = limit;
        mc.gameSettings.thirdPersonView = view;
        mc.gameSettings.hideGUI = hide;
        mc.displayWidth = w;
        mc.displayHeight = h;

        switchFromFB();
        GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
        mc.entityRenderer.setupOverlayRendering();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mirrorTex);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3ub((byte) 24, (byte) 24, (byte) 24);
        tes.startDrawing(GL11.GL_QUADS);
        tes.addVertex(0, mc.displayHeight / 30, 0);
        tes.addVertex(0, mc.displayHeight/20, 0);
        tes.addVertex(mc.displayWidth / 20, mc.displayHeight / 15, 0);
        tes.addVertex(mc.displayWidth/20, mc.displayHeight/25, 0);
        tes.draw();
        GL11.glColor3ub((byte) 255, (byte) 255, (byte) 255);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        tes.startDrawing(GL11.GL_QUADS);
        tes.addVertexWithUV(mc.displayWidth/60, mc.displayHeight/60, 0, 0, 1);
        tes.addVertexWithUV(mc.displayWidth/60, mc.displayHeight/6, 0, 0, 0);
        tes.addVertexWithUV(mc.displayWidth/6, mc.displayHeight/7, 0, 1, 0);
        tes.addVertexWithUV(mc.displayWidth/6, mc.displayHeight/40, 0, 1, 1);
        tes.draw();

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
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
