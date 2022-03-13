/*
 * Copyright (c) 2022, for DirtyMod by Dirt, Deleteboys, Phantom.
 * All rights reserved.
 *
 * Copyright (c) for Minecraft by Mojang.
 * (This license is not in contact with Mojangs)
 */

package vip.phantom.api.utils;

import org.lwjgl.opengl.GL11;

public class OpenGLProfile {
    private boolean blend;
    private boolean alpha;
    private boolean depth;
    private boolean texture2D;
    private boolean lighting;

    public OpenGLProfile save() {
        blend = GL11.glGetBoolean(GL11.GL_BLEND);
        alpha = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);
        depth = GL11.glGetBoolean(GL11.GL_DEPTH_TEST);
        texture2D = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
        lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
        return this;
    }

    public OpenGLProfile restore() {
        if (blend) {
            GL11.glEnable(GL11.GL_BLEND);
        } else {
            GL11.glDisable(GL11.GL_BLEND);
        }
        if (alpha) {
            GL11.glEnable(GL11.GL_ALPHA_TEST);
        } else {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
        }
        if (depth) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } else {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
        if (texture2D) {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        } else {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
        if (lighting) {
            GL11.glEnable(GL11.GL_LIGHTING);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
        }
        return this;
    }

}
