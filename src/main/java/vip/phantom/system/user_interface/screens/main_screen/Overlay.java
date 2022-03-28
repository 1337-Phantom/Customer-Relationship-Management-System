package vip.phantom.system.user_interface.screens.main_screen;

import org.lwjgl.input.Keyboard;
import vip.phantom.system.CRM;
import vip.phantom.system.user_interface.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class Overlay {

    public CRM crm = CRM.getCrm();

    public int width, height;

    public List<Button> buttonList = new ArrayList<>();

    public void initOverlay() {
        buttonList.clear();
    }

    public void drawScreen(int mouseX, int mouseY) {
        for (Button button : buttonList) {
            button.drawScreen(mouseX, mouseY);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Button button : buttonList) {
            if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
                buttonPressed(button.buttonId);
                break;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Button button : buttonList) {
            if (button.mouseReleased(mouseX, mouseY, state)) {
                buttonReleased(button.buttonId);
            }
        }
    }

    public void buttonPressed(int buttonId) {

    }

    public void buttonReleased(int buttonId) {

    }

    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            crm.currentScreen.closeOverlay();
        }
    }

    public void setResolution(int width, int height) {
        this.width = width;
        this.height = height;
        initOverlay();
    }
}
