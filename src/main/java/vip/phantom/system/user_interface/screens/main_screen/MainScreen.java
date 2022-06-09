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
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.NormalButton;
import vip.phantom.system.user_interface.interactive_areas.comboboxes.ComboBox;
import vip.phantom.system.user_interface.screens.LoginScreen;
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
    protected Color headlineColor = Color.white;
    protected String headline;

    private List<Button> navigationButtons = new ArrayList<>();

    public MainScreen(String headline) {
        this.headline = headline;
    }

    @Override
    public void initScreen() {
        super.initScreen();
        navigationButtons.clear();
        sideBar = new Area(0, 0, Math.min(width / 12, 60), height);
        topBar = new Area(sideBar.getWidth(), 0, width - sideBar.getWidth(), fr.getHeight() + 10);
        String[] organisations = new String[crm.currentAccount.getOrganisations().size()];
        for (int i = 0; i < crm.currentAccount.getOrganisations().size(); i++) {
            organisations[i] = crm.currentAccount.getOrganisations().get(i).getName();
        }
        organisationBox = new ComboBox(topBar.getX() + fr.getWidth("Organisation:") + 5, topBar.getY() + topBar.getHeight() / 2f - fr.getHeight() / 2f, organisations);
        organisationBox.setCurrentValue(crm.selectedOrganisation.getName());
        float stringWidth = fr.getWidth("Neue Organisation") + 60;
        navigationButtons.add(new NormalButton(1337, (int) (organisationBox.getX() + organisationBox.getWidth() + 10), (int) (topBar.getY() + 2), (int) stringWidth, (int) topBar.getHeight() - 4, "Neue Organisation"));
        navigationButtons.add(new NormalButton(1339, (int) (organisationBox.getX() + organisationBox.getWidth() + 10 + navigationButtons.get(0).getWidth() + 5), (int) (topBar.getY() + 2), (int) fr.getWidth("Löschen") + 30, (int) topBar.getHeight() - 4, "Löschen"));
        float logoutWidth = fr.getWidth("Logout") + 20;
        navigationButtons.add(new NormalButton(1338, (int) (topBar.getX() + topBar.getWidth() - logoutWidth - 2), (int) (topBar.getY() + 2), (int) logoutWidth, (int) topBar.getHeight() - 4, "Logout"));

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

    public void drawNavigationBar(int mouseX, int mouseY) {
        RenderUtil.drawRoundedRect(sideBar.getX(), sideBar.getY(), sideBar.getWidth(), sideBar.getHeight(), 5, new Color(100, 100, 100), false, true, false, false);
        RenderUtil.drawRoundedRect(topBar.getX(), topBar.getY(), topBar.getWidth(), topBar.getHeight(), 5, new Color(100, 100, 100), false, true, false, false);
        fr.drawString("Organisation:", topBar.getX(), topBar.getY() + topBar.getHeight() / 2f - fr.getHeight() / 2f, Color.white);
        organisationBox.drawComboBox(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton)) {
            for (Button navigationButton : navigationButtons) {
                if (navigationButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                    if (navigationButton instanceof CategoryButton categoryButton) {
                        crm.displayScreen(categoryButton.getCategory().getCategoryScreen());
                    } else if (navigationButton.buttonId == 1337) {
                        showOverlay(new AddOrganisationOverlay());
                    } else if (navigationButton.buttonId == 1338) {
                        crm.currentAccount = null;
                        crm.displayScreen(new LoginScreen());
                    } else if (navigationButton.buttonId == 1339) {
                        if (crm.currentAccount.getOrganisations().size() > 1) {
                            int tmp = crm.currentAccount.getOrganisations().indexOf(crm.selectedOrganisation);
                            crm.currentAccount.deleteOrganisation(crm.selectedOrganisation);
                            crm.selectedOrganisation = crm.currentAccount.getOrganisations().get(tmp > crm.currentAccount.getOrganisations().size() - 1 ? 0 : tmp);
                            initScreen();
                        }
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
}
