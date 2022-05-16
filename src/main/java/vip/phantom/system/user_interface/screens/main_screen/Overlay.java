package vip.phantom.system.user_interface.screens.main_screen;

import org.lwjgl.input.Keyboard;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.CRM;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.Button;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Overlay {

    public CRM crm = CRM.getCrm();

    protected Area informationArea;

    public int width, height;

    protected FontRenderer headlineFr = Fonts.Light12,
            fr = Fonts.Light10;

    public List<Button> buttonList = new ArrayList<>();

    public void initOverlay() {
        buttonList.clear();
    }

    public void drawScreen(int mouseX, int mouseY) {
        for (Button button : buttonList) {
            button.drawScreen(mouseX, mouseY);
        }
    }

    public void drawDefaultBackground() {
        RenderUtil.drawRect(0, 0, width, height, new Color(15, 15, 15, 180));
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Button button : buttonList) {
            if (button.mouseClicked(mouseX, mouseY, mouseButton)) {
                buttonPressed(button.buttonId);
                return true;
            }
        }
        return false;
    }

    public void buttonPressed(int buttonId) {

    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Button button : buttonList) {
            if (button.mouseReleased(mouseX, mouseY, state)) {
                buttonReleased(button.buttonId);
            }
        }
    }

    public void buttonReleased(int buttonId) {

    }

    public void handleMouseInput(int mouseX, int mouseY) {

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
