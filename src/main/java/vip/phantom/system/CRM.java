/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system;

import lombok.Getter;
import org.lwjgl.opengl.Display;
import vip.phantom.api.font.Fonts;
import vip.phantom.system.user_interface.screens.Screen;

public class CRM {

    private static CRM crm;

    public Screen currentScreen = null;

    public int width, height, lastWidth, lastHeight;

    @Getter
    private int debugFps;
    private int fpsCounter;
    private long fpsUpdateTime = 0;

    public void startup() {
        Fonts.getINSTANCE();
    }

    public void drawScreen(int mouseX, int mouseY) {
        width = Display.getWidth();
        height = Display.getHeight();
        fpsCounter += 1;
        while (System.currentTimeMillis() >= fpsUpdateTime + 1000) {
            debugFps = fpsCounter;
            fpsCounter = 0;
            fpsUpdateTime = System.currentTimeMillis();
            Display.setTitle("Customer Relationship System | Yorck Heilmann | FPS: " + getDebugFps());
        }

        if (currentScreen != null) {
            if (lastWidth != width || lastHeight != height) {
                currentScreen.setResolution(width, height);
            }
            currentScreen.drawScreen(mouseX, mouseY);
        }

        lastWidth = width;
        lastHeight = height;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (currentScreen != null) {
            currentScreen.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (currentScreen != null) {
            currentScreen.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (currentScreen != null) {
            currentScreen.keyTyped(typedChar, keyCode);
        }
    }

    public void displayScreen(Screen screen) {
        if (screen != currentScreen) {
            screen.setResolution(width, height);
            currentScreen = screen;
            screen.initScreen();
        }
    }

    public void shutdownHook() {

    }

    public static CRM getCrm() {
        if (crm == null) {
            crm = new CRM();
        }
        return crm;
    }
}
