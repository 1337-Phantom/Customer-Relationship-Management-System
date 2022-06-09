package vip.phantom.system.user_interface.interactive_areas.comboboxes;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.user_interface.Area;

import java.awt.*;

public class ComboBox extends Area {

    private float extendedHeight;
    private boolean extended;

    @Getter
    @Setter
    private String currentValue;
    private String[] values;

    private FontRenderer fr = Fonts.Light10;

    public ComboBox(float x, float y, String... values) {
        super(x, y, 0, 0);
        currentValue = values[0];
        this.values = values;
        height = fr.getHeight();
        extendedHeight = fr.getHeight() * values.length;

        float longestValue = 0;
        for (String value : values) {
            final float width = fr.getWidth(value);
            if (width > longestValue) {
                longestValue = width;
            }
        }
        width = longestValue + 10;
    }

    public void drawComboBox(int mouseX, int mouseY) {
        RenderUtil.drawOutline(x, y, width, height, 1, Color.black);
        fr.drawString(currentValue, x, y, Color.black);
        if (extended) {
            RenderUtil.drawRect(x, y + height, width, extendedHeight, new Color(100, 100, 100));
            RenderUtil.drawOutline(x, y + height, width, extendedHeight, 1, Color.black);
            for (int i = 0; i < values.length; i++) {
                fr.drawString(values[i], x, y + height + i * fr.getHeight(), isModeHovered(mouseX, mouseY, i) ? new Color(97, 202, 192) : currentValue.equals(values[i]) ? Color.white : Color.black);
            }
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            extended = !extended;
            return true;
        } else if (extended && isValueBoxHovered(mouseX, mouseY)) {
            for (int i = 0; i < values.length; i++) {
                if (isModeHovered(mouseX, mouseY, i)) {
                    currentValue = values[i];
                    break;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isModeHovered(int mouseX, int mouseY, int index) {
        return RenderUtil.isHovered(mouseX, mouseY, x, y + height + fr.getHeight() * index, width, fr.getHeight());
    }

    private boolean isValueBoxHovered(int mouseX, int mouseY) {
        return RenderUtil.isHovered(mouseX, mouseY, x, y + height, width, extendedHeight);
    }

    public float getHeight() {
        return height + (extended ? extendedHeight : 0);
    }


}
