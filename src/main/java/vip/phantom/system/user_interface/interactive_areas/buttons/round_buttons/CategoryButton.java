package vip.phantom.system.user_interface.interactive_areas.buttons.round_buttons;

import lombok.Getter;
import org.lwjgl.Sys;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.Category;

import java.awt.*;

public class CategoryButton extends RoundPictureButton {

    @Getter
    private final Category category;

    private long hoveredSince;

    public CategoryButton(int id, int x, int y, int size, String pictureName, Category category) {
        super(id, x, y, size, pictureName, new Color(97, 202, 192));
        this.category = category;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        super.drawScreen(mouseX, mouseY);
        if (!hovered) {
            hoveredSince = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - hoveredSince > 1000) {
            RenderUtil.drawRect(mouseX, mouseY - fr.getHeight(), fr.getWidth(category.getString()) + 3, fr.getHeight(), Color.gray);
            fr.drawString(category.getString(), mouseX, mouseY - fr.getHeight(), Color.black);
        }
    }
}
