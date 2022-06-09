/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system;

import lombok.Getter;
import org.lwjgl.opengl.Display;
import vip.phantom.api.file.FileManager;
import vip.phantom.api.font.Fonts;
import vip.phantom.system.user_interface.screens.Screen;

import java.io.File;

public class CRM {

    private static CRM crm;

    public Account currentAccount;

    public Organisation selectedOrganisation;

    public Screen currentScreen = null;

    public int width, height, lastWidth, lastHeight;

    public void startup() {
        Fonts.getINSTANCE();
    }

    public void setSelectedOrganisation(Organisation organisation) {
        selectedOrganisation = organisation;
        if (currentScreen != null) {
            currentScreen.initScreen();
        }
    }

    public boolean login(Account account, boolean force) {
        boolean doesAccountExist = force;
        for (File file : FileManager.INSTANCE.getRootDirectory().listFiles()) {
            if (file.getName().equals(account.getAccountName())) {
                doesAccountExist = true;
            }
        }
        if (doesAccountExist) {
            currentAccount = account;
            Organisation orga = new Organisation("Standard");
            currentAccount.addOrganisation(orga, Role.ADMIN);
            selectedOrganisation = orga;

            FileManager.INSTANCE.addFile(CRM.getCrm().currentAccount);
            FileManager.INSTANCE.loadAllFiles();
            crm.displayScreen(Category.HOME.getCategoryScreen());
        }
        return doesAccountExist;
    }

    public void drawScreen(int mouseX, int mouseY) {
        width = Display.getWidth();
        height = Display.getHeight();

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

    public void shutdown() {
        FileManager.INSTANCE.saveAllFiles();
    }

    public static CRM getCrm() {
        if (crm == null) {
            crm = new CRM();
        }
        return crm;
    }
}
