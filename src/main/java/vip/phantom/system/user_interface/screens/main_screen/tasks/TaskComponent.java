package vip.phantom.system.user_interface.screens.main_screen.tasks;

import lombok.Getter;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.CRM;
import vip.phantom.system.task.Task;

import java.awt.*;

public class TaskComponent {

    @Getter
    private float x, y, width, height;

    private final Task task;

    private FontRenderer headlineFr = Fonts.Light12, fr = Fonts.Light8;

    public TaskComponent(float x, float y, float width, float height, Task task) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.task = task;
    }

    public void drawScreen(int mouseX, int mouseY) {
        RenderUtil.drawRect(x, y, width, height, isHovered(mouseX, mouseY) ? Color.red : Color.green);
        headlineFr.drawString("§n" + task.headline, x + 5, y + 2, Color.black);
        fr.drawString(task.description, x + 5, y + 2 + headlineFr.getHeight(), Color.black);

        fr.drawString(task.getFinishDate(), x + width - fr.getWidth(task.getFinishDate()) - 5, y + 2, Color.black);
        fr.drawString(task.getDaysLeft() + "d", x + width - fr.getWidth(task.getDaysLeft() + "d") - 5, y - 5 + fr.getHeight(), Color.black);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (isHovered(mouseX, mouseY)) {
            CRM.getCrm().currentScreen.showOverlay(new TaskOverlay(task));
            return true;
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    }

    public void keyTyped(char typedChar, int keyCode) {
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return RenderUtil.isHovered(mouseX, mouseY, x, y, width, height);
    }
}
