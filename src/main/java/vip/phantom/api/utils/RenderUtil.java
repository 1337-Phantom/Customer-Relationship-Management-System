/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.api.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class RenderUtil {

    public static void drawRect(float x, float y, float width, float height, Color color) {
        if (width < 0) {
            width = Math.abs(width);
            x -= width;
        }
        if (height < 0) {
            height = Math.abs(height);
            y -= height;
        }
        glPushMatrix();
        final boolean texture2DFlag = glIsEnabled(GL_TEXTURE_2D);
        final boolean blendFlag = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        setGLColor(color);
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x, y + height);
        glVertex2f(x + width, y + height);
        glVertex2f(x + width, y);
        glEnd();
        if (texture2DFlag) {
            glEnable(GL_TEXTURE_2D);
        }
        if (!blendFlag) {
            glDisable(GL_BLEND);
        }
        resetGLColor();
        glPopMatrix();
    }

    public static void drawVerticalLine(float y, float endY, float x, float lineWidth, boolean rounded, Color color) {
        float radius = lineWidth / 2f;
        if (rounded) {
            drawRect(x - radius, y + radius, x + radius, endY - radius, color);
            drawCircleAsPoint(x, y + radius, radius, color);
            drawCircleAsPoint(x, endY - radius, radius, color);
        } else {
            drawRect(x - radius, y, x + radius, endY, color);
        }
    }

    public static void drawHorizontalLine(float x, float endX, float y, float lineWidth, boolean rounded, Color color) {
        if (endX - x < lineWidth) {
            endX = x + lineWidth;
        }
        float radius = lineWidth / 2f;
        if (rounded) {
            drawRect(x + radius / 2f, y - radius, endX - radius / 2f, y + radius, color);
            drawCircleAsPoint(x + radius / 2f, y, Math.floor(radius), color);
            drawCircleAsPoint(endX - radius / 2f, y, Math.floor(radius), color);
        } else {
            drawRect(x, y - radius, endX, y + radius, color);
        }
    }

    public static void drawOutline(float x, float y, float width, float height, float lineWidth, Color color) {
        if (width < 0) {
            width = Math.abs(width);
            x -= width;
        }
        if (height < 0) {
            height = Math.abs(height);
            y -= height;
        }
        glPushMatrix();
        final boolean texture2DFlag = glIsEnabled(GL_TEXTURE_2D);
        final boolean blendFlag = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        setGLColor(color);
        glLineWidth(lineWidth);
        glBegin(GL_LINE_LOOP);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glVertex2f(x, y);
        glEnd();
        if (texture2DFlag) {
            glEnable(GL_TEXTURE_2D);
        }
        if (!blendFlag) {
            glDisable(GL_BLEND);
        }
        resetGLColor();
        glPopMatrix();
    }

    public static void drawCircle(double x, double y, double radius, boolean fill, float lineWidth, float circleStart, float circleStop, boolean smooth, Color color) {
        if (lineWidth <= 1) {
            lineWidth = 1;
        }
        glPushMatrix();
        final boolean texture2DFlag = glIsEnabled(GL_TEXTURE_2D);
        final boolean blendFlag = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);

        glLineWidth(lineWidth * 2);
        setGLColor(color);
        if (smooth) {
            if (fill) {
                glEnable(GL_POLYGON_SMOOTH);
            } else {
                glEnable(GL_LINE_SMOOTH);
                glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
            }
        }

        if (fill) {
            glBegin(GL_POLYGON);
            glVertex2d(x, y);
        } else {
            glBegin(GL_LINE_STRIP);
        }
        for (int i = (int) circleStart; i <= circleStop; i++) {
            glVertex2d(x + Math.sin(Math.PI * i / 180f) * radius, y - Math.cos(Math.PI * i / 180f) * radius);
        }
        glEnd();
        if (smooth) {
            if (fill) {
                glDisable(GL_POLYGON_SMOOTH);
            } else {
                glDisable(GL_LINE_SMOOTH);
            }
        }
        if (texture2DFlag) {
            glEnable(GL_TEXTURE_2D);
        }
        if (!blendFlag) {
            glDisable(GL_BLEND);
        }
        resetGLColor();
        glPopMatrix();
    }

    /* the circle is much smoother using this method because opengl antialiases/smoothens the rendering */
    public static void drawCircleAsPoint(double x, double y, double radius, Color color) {
        glPushMatrix();
        final boolean texture2DFlag = glIsEnabled(GL_TEXTURE_2D);
        final boolean blendFlag = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        setGLColor(color);
        glPointSize((float) (radius * 4f));

        glEnable(GL_POINT_SMOOTH);
        glBegin(GL_POINTS);
        glVertex2d(x, y);
        glEnd();

        if (texture2DFlag) {
            glEnable(GL_TEXTURE_2D);
        }
        if (!blendFlag) {
            glDisable(GL_BLEND);
        }
        resetGLColor();
        glPopMatrix();
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float cornerRadius, Color color) {
        drawRoundedRect(x, y, width, height, cornerRadius, color, true, true, true, true);
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float cornerRadius, Color color, boolean topRight, boolean bottomRight, boolean bottomLeft, boolean topLeft) {
        if (width < 0) {
            width = Math.abs(width);
            x -= width;
        }
        if (height < 0) {
            height = Math.abs(height);
            y -= height;
        }

        glPushMatrix();
        final boolean texture2DFlag = glIsEnabled(GL_TEXTURE_2D);
        final boolean blendFlag = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        setGLColor(color);
        glBegin(GL_POLYGON);
        float cornerX = x + width - cornerRadius,
                cornerY = y + cornerRadius;
        if (topRight) {
            for (int i = 0; i < 90; i++) {
                glVertex2d(cornerX + Math.sin(Math.PI * i / 180f) * cornerRadius, cornerY - Math.cos(Math.PI * i / 180f) * cornerRadius);
            }
        } else {
            glVertex2d(x + width, y);
        }
        cornerY = y + height - cornerRadius;
        if (bottomRight) {
            for (int i = 90; i < 180; i++) {
                glVertex2d(cornerX + Math.sin(Math.PI * i / 180f) * cornerRadius, cornerY - Math.cos(Math.PI * i / 180f) * cornerRadius);
            }
        } else {
            glVertex2d(x + width, y + height);
        }
        cornerX = x + cornerRadius;
        if (bottomLeft) {
            for (int i = 180; i < 270; i++) {
                glVertex2d(cornerX + Math.sin(Math.PI * i / 180f) * cornerRadius, cornerY - Math.cos(Math.PI * i / 180f) * cornerRadius);
            }
        } else {
            glVertex2d(x, y + height);
        }
        cornerY = y + cornerRadius;
        if (topLeft) {
            for (int i = 270; i < 360; i++) {
                glVertex2d(cornerX + Math.sin(Math.PI * i / 180f) * cornerRadius, cornerY - Math.cos(Math.PI * i / 180f) * cornerRadius);
            }
        } else {
            glVertex2d(x, y);
        }

        glEnd();
        if (texture2DFlag) {
            glEnable(GL_TEXTURE_2D);
        }
        if (!blendFlag) {
            glDisable(GL_BLEND);
        }
        resetGLColor();
        glPopMatrix();
    }

    public static void drawPicture(double x, double y, double width, double height, BufferedImage image, boolean color) {
        drawPicture(x, y, width, height, getTextureIdOf(image), color);
    }

    public static void drawPicture(double x, double y, double width, double height, int resource, boolean color) {
        glPushMatrix();
        final boolean texture2DFlag = glIsEnabled(GL_TEXTURE_2D);
        glEnable(GL_TEXTURE_2D);
        final boolean blendFlag = glIsEnabled(GL_BLEND);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //scale down to make picture look better
//        glScalef(0.5f, 0.5f, 1);
//        x *= 2;
//        y *= 2;
//        width *= 2;
//        height *= 2;

        if (!color) {
            setGLColor(Color.white);
        }

        //bind the texture
        glBindTexture(GL_TEXTURE_2D, resource);

        glBegin(GL_QUADS);
        glTexCoord2f(0, 1);
        glVertex2d(x, y + height);
        glTexCoord2f(1, 1);
        glVertex2d(x + width, y + height);
        glTexCoord2f(1, 0);
        glVertex2d(x + width, y);
        glTexCoord2f(0, 0);
        glVertex2d(x, y);
        glEnd();

        glBindTexture(GL_TEXTURE_2D, 0);
        if (!blendFlag) {
            glDisable(GL_BLEND);
        }
        if (!texture2DFlag) {
            glDisable(GL_TEXTURE_2D);
        }
        resetGLColor();
        glPopMatrix();
    }

    public static int getTextureIdOf(BufferedImage image) {
        // Array of all the colors in the image.
        int[] pixels = new int[image.getWidth() * image.getHeight()];

        // Fetches all the colors in the image.
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());

        // Buffer that will store the texture data.
        ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4); //4 for RGBA, 3 for RGB

        // Puts all the pixel data into the buffer.
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                // The pixel in the image.
                int pixel = pixels[y * image.getWidth() + x];

                // Puts the data into the byte buffer.
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }

        // Flips the byte buffer, not sure why this is needed.
        buffer.flip();

        int textureId = GL11.glGenTextures();
        // Binds the opengl texture by the texture id.
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // Sets the texture parameters.
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        // Uploads the texture to opengl.
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

        // Binds the opengl texture 0.
        return textureId;
    }

    public static float[] getCurrentScissorBox() {
        final IntBuffer intBuffer = BufferUtils.createIntBuffer(16);
        glGetInteger(GL_SCISSOR_BOX, intBuffer);
        final float[] currentScissorBox = new float[4];
        for (int i = 0; i < 4; i++) {
            currentScissorBox[i] = (float) (intBuffer.get(i));
        }
        currentScissorBox[1] = Display.getHeight() - (currentScissorBox[1] + currentScissorBox[3]);
        return currentScissorBox;
    }

    public static void beginScissor(double x, double y, double width, double height) {
        if (glIsEnabled(GL_SCISSOR_TEST)) {
            final float[] scissor = getCurrentScissorBox();
            x = Math.max(scissor[0], x);
            if (scissor[0] + scissor[2] < x + width) {
                width = Math.max(0, scissor[0] + scissor[2] - x);
            }
            y = Math.max(scissor[1], y);
            if (scissor[1] + scissor[3] < y + height) {
                height = Math.max(0, scissor[1] + scissor[3] - y);
            }
        }

        glPushAttrib(GL_SCISSOR_BIT);
        glEnable(GL_SCISSOR_TEST);
        glScissor((int) x, (int) (Display.getHeight() - (y + height)), (int) width, (int) height);
    }

    public static void endScissor() {
        glDisable(GL_SCISSOR_TEST);
        glPopAttrib();
    }

    public static boolean isHovered(int mouseX, int mouseY, float x, float y, float width, float height) {
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }

    public static boolean isHovered(int mouseX, int mouseY, float x, float y, float radius) {
        return ((mouseX - x) * (mouseX - x) + (mouseY - y) * (mouseY - y)) <= radius * radius;
    }

    public static void setGLColor(Color color) {
        setGLColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static void setGLColor(float red, float green, float blue, float alpha) {
        glColor4f(red / 255f, green / 255f, blue / 255f, alpha / 255f);
    }

    public static void resetGLColor() {
        setGLColor(255, 255, 255, 255);
    }
}
