/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.interactive_areas.buttons.round_buttons;

import vip.phantom.api.resources.ResourceLocation;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.interactive_areas.buttons.RoundButton;

import java.awt.*;

public class RoundPictureButton extends RoundButton {
    private int pictureId;

    private Color color;

    public RoundPictureButton(int id, int x, int y, int size, String pictureName) {
        this(id, x, y, size, pictureName, null);
    }

    public RoundPictureButton(int id, int x, int y, int size, String pictureName, Color color) {
        super(id, x, y, size);
        this.pictureId = new ResourceLocation("/icons/" + pictureName + ".png").textureId;
        this.color = color;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        hovered = RenderUtil.isHovered(mouseX, mouseY, x + width / 2f, y + height / 2f, radius);
        RenderUtil.drawCircle(x + width / 2f, y + height / 2f, radius, true, 1, 0, 360, false, hovered ? hoveredColor : normalColor);
        if (color != null) {
            RenderUtil.setGLColor(color);
        }
        RenderUtil.drawPicture(x + width / 6f, y + height / 6f, width / 3f * 2, height / 3f * 2, pictureId, color != null);
    }
}
