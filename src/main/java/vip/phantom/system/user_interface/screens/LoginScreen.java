/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.screens;

import org.lwjgl.input.Keyboard;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.resources.ResourceLocation;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.Account;
import vip.phantom.system.Category;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.NormalButton;
import vip.phantom.system.user_interface.interactive_areas.text.TextField;

import java.awt.*;
import java.time.LocalDate;

public class LoginScreen extends Screen {

    private TextField username;

    private FontRenderer fr = Fonts.Light12;

    private boolean showErrorString = false;

    @Override
    public void initScreen() {
        super.initScreen();
        int x = (int) (width / 2f - 100),
                y = (int) (height / 2f - 12);
        String name = "";
        if (username != null) {
            name = username.getText();
        }
        username = new TextField(x, y, 200, "Username", Fonts.Light12);
        username.setText(name);
        y += username.getHeight() + 5;
//        String pw = "";
//        if (password != null) {
//            pw = password.getText();
//        }
//        password = new TextField(x, y, 200, "Password", Fonts.Light12);
//        password.setText(pw);
//        password.setObfuscated(true);
//        y += password.getHeight() + 5;
        buttonList.add(new NormalButton(0, x, y, 200, 25, "Login"));
        buttonList.add(new NormalButton(1, width - 205, height - 40, 200, 35, "Neuer Account"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        RenderUtil.drawPicture(0, 0, width, height, new ResourceLocation("/backgrounds/LoginBackground.png").textureId, false);

        RenderUtil.drawRoundedRect(username.getX() - 7, username.getY() - 7, username.getWidth() + 14, getButtonById(0).getY() + getButtonById(0).getHeight() - username.getY() + 14, 5, Color.white);
        if (showErrorString) {
            fr.drawString("Account couldn't be found", width / 2f - fr.getWidth("Account couldn't be found") / 2f, username.getY() - 7 - fr.getHeight(), Color.red);
        }
        username.drawTextField(mouseX, mouseY);
//        password.drawTextField(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton)) {
            if (username.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
//            if (password.mouseClicked(mouseX, mouseY, mouseButton)) {
//                return true;
//            }
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        username.mouseReleased(mouseX, mouseY, mouseButton);
//        password.mouseReleased(mouseX, mouseY, mouseButton);
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_TAB) {
//            if (username.isFocused()) {
//                username.setFocused(false);
//                password.setFocused(true);
//            } else {
//                password.setFocused(false);
//                username.setFocused(true);
//            }
        } else if (keyCode == Keyboard.KEY_RETURN) {
            buttonPressed(0);
        }
        username.keyTyped(typedChar, keyCode);
//        password.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void buttonPressed(int buttonId) {
        switch (buttonId) {
            case 0 -> {
                crm.login(new Account(username.getText()), false);
                showErrorString = true;
            }
            case 1 -> showOverlay(new NewAccountOverlay());
        }
        super.buttonPressed(buttonId);
    }
}
