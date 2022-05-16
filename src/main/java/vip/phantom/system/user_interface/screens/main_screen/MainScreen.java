/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.screens.main_screen;

import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.Organisation;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.Category;
import vip.phantom.system.user_interface.interactive_areas.buttons.Button;
import vip.phantom.system.user_interface.interactive_areas.buttons.round_buttons.CategoryButton;
import vip.phantom.system.user_interface.interactive_areas.comboboxes.ComboBox;
import vip.phantom.system.user_interface.screens.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MainScreen extends Screen {

    public int spacerSize = 3;

    public Area sideBar, topBar, mainWindow;
    public ComboBox organisationBox;

    protected FontRenderer fr = Fonts.Light10;

    protected float renderY;

    protected FontRenderer headlineFr = Fonts.Light12;
    protected Color headlineColor = Color.black;
    protected String headline;

    private List<Button> navigationButtons = new ArrayList<>();

    public MainScreen(String headline) {
        this.headline = headline;
    }

    @Override
    public void initScreen() {
        super.initScreen();
        sideBar = new Area(0, 0, Math.min(width / 12, 60), height);
        topBar = new Area(sideBar.getWidth(), 0, width - sideBar.getWidth(), fr.getHeight() + 2);
        String[] organisations = new String[crm.currentAccount.getOrganisations().size()];
        for (int i = 0; i < crm.currentAccount.getOrganisations().size(); i++) {
            organisations[i] = crm.currentAccount.getOrganisations().get(i).getName();
        }
        organisationBox = new ComboBox(topBar.getX() + fr.getWidth("Organisation:") + 5, topBar.getY(), organisations);
        organisationBox.setCurrentValue(crm.selectedOrganisation.getName());

        mainWindow = new Area(sideBar.getWidth(), topBar.getHeight(), width - sideBar.getWidth(), height - topBar.getHeight());

        int yOffset = spacerSize;
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            Category category = categories[i];
            CategoryButton categoryButton = new CategoryButton(i, spacerSize, yOffset, (int) sideBar.getWidth() - spacerSize * 2, category.getPictureName(), category);
            navigationButtons.add(categoryButton);
            yOffset += categoryButton.getHeight() + spacerSize;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        drawDefaultBackground();
        drawMainArea(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY);
        drawNavigationBar(mouseX, mouseY);
        navigationButtons.forEach(button -> button.drawScreen(mouseX, mouseY));
    }

    public void drawMainArea(int mouseX, int mouseY) {
        renderY = mainWindow.getY() + spacerSize;
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton)) {
            for (Button navigationButton : navigationButtons) {
                if (navigationButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                    if (navigationButton instanceof CategoryButton categoryButton) {
                        crm.displayScreen(categoryButton.getCategory().getCategoryScreen());
                    }
                    return true;
                }
            }
            if (organisationBox.mouseClicked(mouseX, mouseY, mouseButton)) {
                Organisation organisation = crm.selectedOrganisation;
                for (Organisation orga : crm.currentAccount.getOrganisations()) {
                    if (orga.getName().equals(organisationBox.getCurrentValue())) {
                        organisation = orga;
                        break;
                    }
                }
                if (organisation != crm.selectedOrganisation) {
                    crm.setSelectedOrganisation(organisation);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        navigationButtons.forEach(button -> button.mouseReleased(mouseX, mouseY, mouseButton));
    }

    public void drawHeadline(int mouseX, int mouseY) {
        headlineFr.drawString("§n" + headline, spacerSize + mainWindow.getX(), renderY, headlineColor);
        renderY += headlineFr.getHeight() + spacerSize;
    }

    public void drawNavigationBar(int mouseX, int mouseY) {
        RenderUtil.drawRoundedRect(sideBar.getX(), sideBar.getY(), sideBar.getWidth(), sideBar.getHeight(), 5, Color.white.darker(), false, true, false, false);
        RenderUtil.drawRoundedRect(topBar.getX(), topBar.getY(), topBar.getWidth(), topBar.getHeight(), 5, Color.white.darker(), false, true, false, false);
        fr.drawString("Organisation:", topBar.getX(), topBar.getY(), Color.black);
        organisationBox.drawComboBox(mouseX, mouseY);
    }
}
