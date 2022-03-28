package vip.phantom.system.user_interface;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.api.utils.RenderUtil;

public class Area {

    @Getter
    @Setter
    protected int x, y, width, height;

    public Area(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return RenderUtil.isHovered(mouseX, mouseY, x, y, width, height);
    }
}
