package vip.phantom.system.user_interface.buttons;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.api.utils.RenderUtil;

public class RoundButton extends Button {
    @Getter @Setter
    protected float radius;

    public RoundButton(int id, int x, int y, int size) {
        super(id, x, y, size, size, "");
        radius = size / 2f;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        hovered = RenderUtil.isHovered(mouseX, mouseY, x + radius, y + radius, radius);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        radius = width / 2f;
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        setWidth(height);
    }
}
