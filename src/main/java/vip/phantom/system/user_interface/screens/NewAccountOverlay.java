package vip.phantom.system.user_interface.screens;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.Account;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.NormalButton;
import vip.phantom.system.user_interface.interactive_areas.text.TextField;

import java.awt.*;
import java.util.Collections;
import java.util.Locale;

public class NewAccountOverlay extends Overlay {

    private TextField name;
    private String loginName = "";

    @Override
    public void initOverlay() {
        super.initOverlay();
        informationArea = new Area(width / 4f, height / 4f, width / 2f, height / 2f);
        float width = fr.getWidth("Name: ");
        String prevName = "";
        if (name != null) {
            prevName = name.getText();
        }
        name = new TextField(informationArea.getX() + 5 + width + 5, informationArea.getY() + fr.getHeight(), informationArea.getWidth() - width - 15, "Name", fr, "(.)+");
        name.setText(prevName);

        buttonList.add(new NormalButton(0, (int) (informationArea.getX() + 5), (int) (informationArea.getY() + informationArea.getHeight() - 30), (int) (informationArea.getWidth() - 10), 25, "Create Account"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        drawDefaultBackground();
        RenderUtil.drawRect(informationArea.getX(), informationArea.getY(), informationArea.getWidth(), informationArea.getHeight(), new Color(100, 100, 100));
        float renderY = informationArea.getY();
        fr.drawString("Neuer Account", informationArea.getX() + informationArea.getWidth() / 2f - fr.getWidth("Neuer Account") / 2f, renderY, Color.black);
        renderY += fr.getHeight();
        name.drawTextField(mouseX, mouseY);
        renderY += name.getHeight();
        fr.drawString("Ihr Loginname: " + loginName, informationArea.getX() + 10, renderY, Color.black);
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton)) {
            return name.mouseClicked(mouseX, mouseY, mouseButton);
        }
        return true;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        name.mouseReleased(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        name.keyTyped(typedChar, keyCode);
        loginName = "";
        String[] texts = name.getText().toLowerCase().split(" ");
        for (int i = texts.length - 1; i >= 0; i--) {
            loginName += texts[i] + ".";
        }
        loginName = loginName.substring(0, loginName.length() - 1);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void buttonPressed(int buttonId) {
        switch (buttonId) {
            case 0 -> crm.login(new Account(loginName, name.getText()), true);
        }
        super.buttonPressed(buttonId);
    }
}
