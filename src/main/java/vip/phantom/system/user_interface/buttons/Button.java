/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.buttons;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;

import java.awt.*;

public class Button {

    public int buttonId;
    @Getter
    @Setter
    protected int x, y, width, height;
    private String text;

    FontRenderer fr = Fonts.VERDANA20;

    protected boolean hovered = false, pressed = false;

    public Button(int id, int x, int y, int width, int height, String text) {
        this.buttonId = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public void drawScreen(int mouseX, int mouseY) {
        hovered = RenderUtil.isHovered(mouseX, mouseY, x, y, width, height);
        RenderUtil.drawRect(x, y, width, height, hovered ? Color.green : Color.red);
        fr.drawString(text, x + width / 2f - fr.getWidth(text) / 2f, y + height / 2f - fr.getHeight(text) / 2f, Color.white);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovered) {
            pressed = true;
        }
        return pressed;
    }

    public boolean mouseReleased(int mouseX, int mouseY, int mouseButton) {
        if (pressed) {
            pressed = false;
            return true;
        } else {
            return false;
        }
    }

}
