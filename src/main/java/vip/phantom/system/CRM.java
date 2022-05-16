/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system;

import lombok.Getter;
import org.lwjgl.opengl.Display;
import vip.phantom.api.file.FileManager;
import vip.phantom.api.font.Fonts;
import vip.phantom.system.contact.ContactManager;
import vip.phantom.system.contract.ContractManager;
import vip.phantom.system.task.TaskManager;
import vip.phantom.system.user_interface.screens.Screen;

public class CRM {

    private static CRM crm;

    public Account currentAccount;

    public Organisation selectedOrganisation;

    public Screen currentScreen = null;

    public int width, height, lastWidth, lastHeight;

    @Getter
    private int debugFps;
    private int fpsCounter;
    private long fpsUpdateTime = 0;

    public void startup() {
        Fonts.getINSTANCE();
        currentAccount = new Account("heilmann.yorck", "Yorck Heilmann", Role.ADMIN);
        Organisation orga = new Organisation("Standard");
        currentAccount.addOrganisation(orga, Role.ADMIN);
//        selectedOrganisation = orga;
//        currentAccount.addOrganisation(new Organisation("Mojang"), Role.ADMIN);
//        currentAccount.addOrganisation(new Organisation("Phantom.vip"), Role.ADMIN);
//        currentAccount.addOrganisation(new Organisation("Uni"), Role.ADMIN);

        FileManager.INSTANCE.loadAllFiles();
    }

    public void setSelectedOrganisation(Organisation organisation) {
        selectedOrganisation = organisation;
        if (currentScreen != null) {
            currentScreen.initScreen();
        }
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

    public void handleMouseInput(int mouseX, int mouseY) {
        if (currentScreen != null) {
            currentScreen.handleMouseInput(mouseX, mouseY);
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
        FileManager.INSTANCE.saveAllFiles();
    }

    public static CRM getCrm() {
        if (crm == null) {
            crm = new CRM();
        }
        return crm;
    }
}
