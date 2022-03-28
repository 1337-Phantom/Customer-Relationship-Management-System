/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.screens.main_screen;

import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.Category;
import vip.phantom.system.user_interface.buttons.Button;
import vip.phantom.system.user_interface.buttons.round_buttons.CategoryButton;
import vip.phantom.system.user_interface.screens.Screen;

import java.awt.*;

public class MainScreen extends Screen {

    public int spacerSize = 3;

    public Area sideBar, mainWindow;

    protected float renderY;

    protected FontRenderer headlineFr = Fonts.VERDANA12;
    protected Color headlineColor = Color.black;
    protected String headline;

    public MainScreen(String headline) {
        this.headline = headline;
    }

    @Override
    public void initScreen() {
        super.initScreen();
        sideBar = new Area(0, 0, Math.min(width / 12, 60), height);
        mainWindow = new Area(sideBar.getWidth(), 0, width - sideBar.getWidth(), height);

        int yOffset = spacerSize;
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            CategoryButton categoryButton = new CategoryButton(i, spacerSize, yOffset, sideBar.getWidth() - spacerSize * 2, category.getPictureName(), category);
            buttonList.add(categoryButton);
            yOffset += categoryButton.getHeight() + spacerSize;
        }


    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        renderY = mainWindow.getY() + spacerSize;
        drawDefaultBackground();
        /* sidebar */
        RenderUtil.drawRoundedRect(0, 0, sideBar.getWidth(), sideBar.getHeight(), 5, Color.white.darker(), true, true, false, false);
        /* headline */
        headlineFr.drawString("§n" + headline, spacerSize + mainWindow.getX(), renderY, headlineColor);
        renderY += headlineFr.getHeight() + spacerSize;
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public void buttonPressed(int buttonId) {
        Button button = buttonList.get(buttonId);
        if (button instanceof CategoryButton categoryButton) {
            crm.displayScreen(categoryButton.getCategory().getCategoryScreen());
        }
        super.buttonPressed(buttonId);
    }
}
