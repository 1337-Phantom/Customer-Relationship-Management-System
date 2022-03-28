/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.buttons.round_buttons;

import vip.phantom.api.resources.ResourceLocation;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.buttons.RoundButton;

import java.awt.*;

public class RoundPictureButton extends RoundButton {
    private int pictureId;

    public RoundPictureButton(int id, int x, int y, int size, String pictureName) {
        super(id, x, y, size);
        this.pictureId = new ResourceLocation("/icons/" + pictureName + ".png").textureId;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        hovered = RenderUtil.isHovered(mouseX, mouseY, x + width / 2f, y + height / 2f, radius);
        RenderUtil.drawCircle(x + width / 2f, y + height / 2f, radius, true, 1, 0, 360, false, hovered ? hoveredColor : normalColor);
        RenderUtil.drawPicture(x + width / 6f, y + height / 6f, width / 3f * 2, height / 3f * 2, pictureId, false);
    }
}
