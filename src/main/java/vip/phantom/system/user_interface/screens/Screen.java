/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.screens;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.CRM;
import vip.phantom.system.user_interface.interactive_areas.buttons.Button;
import vip.phantom.system.user_interface.screens.main_screen.Overlay;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Screen {

    public CRM crm = CRM.getCrm();

    public List<Button> buttonList = new ArrayList<>();

    public int width, height;

    public Overlay activeOverlay = null;

    public void initScreen() {
        buttonList.clear();
    }

    public void showOverlay(Overlay overlay) {
        overlay.setResolution(width, height);
        activeOverlay = overlay;
    }

    public void closeOverlay() {
        activeOverlay = null;
        initScreen();
    }

    public void drawScreen(int mouseX, int mouseY) {
        int realMouseX = mouseX, realMouseY = mouseY;
        if (activeOverlay != null) {
            mouseX = mouseY = -1;
        }
        for (Button button : buttonList) {
            button.drawScreen(mouseX, mouseY);
        }
        if (activeOverlay != null) {
            activeOverlay.drawScreen(realMouseX, realMouseY);
        }
    }

    public void drawDefaultBackground() {
        RenderUtil.drawRect(0, 0, width, height, Color.white);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (activeOverlay != null) {
            return activeOverlay.mouseClicked(mouseX, mouseY, mouseButton);
        } else {
            for (Button button : buttonList) {
                if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
                    buttonPressed(button.buttonId);
                    return true;
                }
            }
        }
        return false;
    }

    public void buttonPressed(int buttonId) {

    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (activeOverlay != null) {
            activeOverlay.mouseReleased(mouseX, mouseY, mouseButton);
        } else {
            for (Button button : buttonList) {
                if (button.mouseReleased(mouseX, mouseY, mouseButton)) {
                    buttonReleased(button.buttonId);
                    break;
                }
            }
        }
    }

    public void buttonReleased(int buttonId) {

    }

    public void handleMouseInput(int mouseX, int mouseY) {
        if (activeOverlay != null) {
            activeOverlay.handleMouseInput(mouseX, mouseY);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (activeOverlay != null) {
            activeOverlay.keyTyped(typedChar, keyCode);
        }
    }

    public void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
        initScreen();
        if (activeOverlay != null) {
            activeOverlay.setResolution(width, height);
            activeOverlay.initOverlay();
        }
    }

    public Button getButtonById(int buttonId) {
        for (Button button : buttonList) {
            if (buttonId == button.buttonId) {
                return button;
            }
        }
        return null;
    }
}
