/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.buttons;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.api.resources.ResourceLocation;
import vip.phantom.api.utils.RenderUtil;

import java.awt.*;

public class CategoryButton extends Button {

    @Getter
    @Setter
    private int radius;
    private int pictureId;

    public CategoryButton(int id, int x, int y, int width, int height, int radius, String pictureName) {
        super(id, x, y, width, height, "");
        this.radius = radius;
        this.pictureId = new ResourceLocation("/icons/" + pictureName + ".png").textureId;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        hovered = RenderUtil.isHovered(mouseX, mouseY, x + width / 2f, y + height / 2f, radius);
        RenderUtil.drawCircle(x + width / 2f, y + height / 2f, radius, true, 1, 0, 360, false, hovered ? Color.red : Color.gray);
        RenderUtil.drawPicture(x + width / 4f, y + height / 4f, width / 2f, height / 2f, pictureId, false);
    }
}
