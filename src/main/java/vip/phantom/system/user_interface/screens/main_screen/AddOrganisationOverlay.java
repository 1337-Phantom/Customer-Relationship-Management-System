package vip.phantom.system.user_interface.screens.main_screen;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.Account;
import vip.phantom.system.Organisation;
import vip.phantom.system.Role;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.NormalButton;
import vip.phantom.system.user_interface.interactive_areas.text.TextField;
import vip.phantom.system.user_interface.screens.Overlay;

import java.awt.*;
import java.time.temporal.Temporal;

public class AddOrganisationOverlay extends Overlay {

    private TextField name;

    @Override
    public void initOverlay() {
        super.initOverlay();
        informationArea = new Area(width / 4f, height / 4f, width / 2f, height / 2f);
        float width = fr.getWidth("Name der Organisation:");
        String prevName = "";
        if (name != null) {
            prevName = name.getText();
        }
        name = new TextField(informationArea.getX() + 5 + width + 5, informationArea.getY() + fr.getHeight(), informationArea.getWidth() - width - 15, "Name", fr, "(.)+");
        name.setText(prevName);

        buttonList.add(new NormalButton(0, (int) (informationArea.getX() + 5), (int) (informationArea.getY() + informationArea.getHeight() - 30), (int) (informationArea.getWidth() - 10), 25, "Hinzufügen"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        drawDefaultBackground();
        RenderUtil.drawRect(informationArea.getX(), informationArea.getY(), informationArea.getWidth(), informationArea.getHeight(), new Color(100, 100, 100));
        float renderY = informationArea.getY();
        fr.drawString("Neue Organisation", informationArea.getX() + informationArea.getWidth() / 2f - fr.getWidth("Neue Organisation") / 2f, renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Name der Organisation: ", informationArea.getX(), renderY, Color.black);
        name.drawTextField(mouseX, mouseY);
        renderY += name.getHeight();
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
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void buttonPressed(int buttonId) {
        switch (buttonId) {
            case 0 -> {
                if (name.matchesPattern()) {
                    crm.currentAccount.addOrganisation(new Organisation(name.getText()), Role.ADMIN);
                    crm.currentScreen.closeOverlay();
                }
            }
        }
        super.buttonPressed(buttonId);
    }
}
