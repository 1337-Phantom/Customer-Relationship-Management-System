/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.screens;

import vip.phantom.api.resources.ResourceLocation;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.buttons.Button;
import vip.phantom.system.user_interface.screens.main_screen.MainScreen;

import java.awt.*;

public class LoginScreen extends Screen {

    @Override
    public void initScreen() {
        super.initScreen();
        buttonList.add(new Button(0, (int) (width / 2f - 100), (int) (height / 2f - 12), 200, 30, "Login"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        RenderUtil.drawPicture(0, 0, width, height, new ResourceLocation("/backgrounds/LoginBackground.png").textureId, false);
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public void buttonPressed(int buttonId) {
        switch (buttonId) {
            case 0 -> crm.displayScreen(new MainScreen());
        }
        super.buttonPressed(buttonId);
    }
}
