package vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons;

import lombok.Getter;
import vip.phantom.api.resources.ResourceLocation;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.interactive_areas.buttons.Button;

import java.awt.*;

public class EditButton extends Button {

    @Getter
    private boolean editing = false;

    private int pictureId;
    private Color color;

    public EditButton(int id, int x, int y, int width, int height, Color color) {
        super(id, x, y, width, height, "");
        pictureId = new ResourceLocation("/icons/EditIcon.png").textureId;
        this.color = color;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            editing = !editing;
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        if (color != null) {
            RenderUtil.setGLColor(hovered ? Color.red : color);
        }
        RenderUtil.drawPicture(x + width / 6f, y + height / 6f, width / 3f * 2, height / 3f * 2, pictureId, color != null);
    }
}
