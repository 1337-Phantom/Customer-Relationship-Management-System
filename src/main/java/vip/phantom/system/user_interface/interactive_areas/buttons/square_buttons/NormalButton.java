package vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.interactive_areas.buttons.Button;

import java.awt.*;

public class NormalButton extends Button {

    public NormalButton(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        RenderUtil.drawRoundedRect(x, y, width, height, 5, hovered ? hoveredColor : normalColor);
        fr.drawString(text, x + width / 2f - fr.getWidth(text) / 2f, y + height / 2f - fr.getHeight() / 2f, Color.black);
    }
}
