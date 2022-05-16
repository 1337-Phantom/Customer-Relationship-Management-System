package vip.phantom.system.user_interface.screens.main_screen.tasks;

import lombok.Getter;
import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.task.Task;
import vip.phantom.system.task.TaskManager;
import vip.phantom.system.task.TaskStatus;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TaskStatusComponent {

    @Getter
    private float x, y, width, height;

    private final TaskStatus status;

    private final List<TaskComponent> tasks = new ArrayList<>();

    private FontRenderer fr = Fonts.Light12;

    public TaskStatusComponent(float x, float y, float width, TaskStatus status) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.status = status;

        float renderY = y + fr.getHeight();
        for (Task task : TaskManager.INSTANCE.getTasksWithStatus(status)) {
            TaskComponent newComp = new TaskComponent(x, renderY, width, 50, task);
            tasks.add(newComp);
            renderY += newComp.getHeight();
        }
        this.height = renderY - y;
//        this.height = height == 0 ? 2 : height;
    }

    public void drawScreen(int mouseX, int mouseY) {
        RenderUtil.drawRect(x, y, width, fr.getHeight(), Color.gray);
        fr.drawString(status.string, x + 10, y, Color.black);

        for (TaskComponent task : tasks) {
            task.drawScreen(mouseX, mouseY);
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (TaskComponent task : tasks) {
            if (task.mouseClicked(mouseX, mouseY, mouseButton)) {
                return true;
            }
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        tasks.forEach(taskComponent -> taskComponent.mouseReleased(mouseX, mouseY, mouseButton));
    }

    public void keyTyped(char typedChar, int keyCode) {
        tasks.forEach(taskComponent -> taskComponent.keyTyped(typedChar, keyCode));
    }

}
