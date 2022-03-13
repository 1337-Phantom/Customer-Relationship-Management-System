/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.screens.main_screen;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.buttons.CategoryButton;
import vip.phantom.system.user_interface.screens.Screen;

import java.awt.*;

public class MainScreen extends Screen {

    @Override
    public void initScreen() {
        super.initScreen();
        int yOffset = 0;
        for (Categories value : Categories.values()) {
            CategoryButton categoryButton = new CategoryButton(0, 0, yOffset, (int) getSideBarWidth(), (int) getSideBarWidth(), (int) (getSideBarWidth() / 2f - 5), value.getPictureName());
            buttonList.add(categoryButton);
            yOffset += categoryButton.getHeight();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        RenderUtil.drawRect(0, 0, width, height, Color.white);

        /* sidebar */
        RenderUtil.drawRoundedRect(0, 0, getSideBarWidth(), height, 5, Color.white.darker(), true, true, false, false);

        //        RenderUtil.drawRect(0, 0, Math.min(width / 12f, 40), height, Color.white.darker());

        super.drawScreen(mouseX, mouseY);
    }

    private float getSideBarWidth() {
        return Math.min(width / 12f, 60);
    }
}
