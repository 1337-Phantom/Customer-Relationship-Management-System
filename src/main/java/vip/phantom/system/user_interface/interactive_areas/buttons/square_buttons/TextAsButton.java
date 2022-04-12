package vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons;

import vip.phantom.system.user_interface.interactive_areas.buttons.Button;

import java.awt.*;

public class TextAsButton extends Button {

    public TextAsButton(int id, int x, int y, String text) {
        super(id, x, y, 0, 0, text);
        setWidth((int) fr.getWidth(text) + 2);
        setHeight((int) fr.getHeight() + 2);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        fr.drawString((hovered ? "§n" : "") + text, x, y, Color.black);
    }
}
