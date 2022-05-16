/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.interactive_areas.buttons;

import lombok.Getter;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.Area;

import java.awt.*;

public class Button extends Area {

    public int buttonId;
    @Getter
    protected String text;

    protected FontRenderer fr = Fonts.Light12;

    protected boolean hovered = false, pressed = false;

    protected Color normalColor = Color.red, hoveredColor = Color.green;

    public Button(int id, int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.buttonId = id;
        this.text = text;
    }

    public void drawScreen(int mouseX, int mouseY) {
        hovered = RenderUtil.isHovered(mouseX, mouseY, x, y, width, height);
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
