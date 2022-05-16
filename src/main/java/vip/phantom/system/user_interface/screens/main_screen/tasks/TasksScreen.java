package vip.phantom.system.user_interface.screens.main_screen.tasks;

import vip.phantom.system.task.TaskManager;
import vip.phantom.system.task.TaskStatus;
import vip.phantom.system.user_interface.interactive_areas.buttons.round_buttons.RoundPictureButton;
import vip.phantom.system.user_interface.screens.main_screen.MainScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TasksScreen extends MainScreen {

    private int lastTaskNumber;

    private RoundPictureButton addContractButton;


    private List<TaskStatusComponent> taskStatusComponents = new ArrayList<>();

    public TasksScreen() {
        super("Aufgaben");
    }

    @Override
    public void initScreen() {
        super.initScreen();
        lastTaskNumber = 0;
        addContractButton = new RoundPictureButton(buttonList.size(), width - 45, height - 45, 38, "plusIcon", Color.black);
    }

    @Override
    public void drawMainArea(int mouseX, int mouseY) {
        drawHeadline(mouseX, mouseY);

        final boolean updateComponents = lastTaskNumber != TaskManager.INSTANCE.getTaskList().size();
        if (updateComponents) {
            System.out.println("Clearing taskcomps");
            taskStatusComponents.clear();
            for (TaskStatus status : TaskStatus.values()) {
                TaskStatusComponent newComp = new TaskStatusComponent(mainWindow.getX(), renderY, mainWindow.getWidth(), status);
                taskStatusComponents.add(newComp);
                renderY += newComp.getHeight();
            }
        }
        taskStatusComponents.forEach(taskStatusComponent -> taskStatusComponent.drawScreen(mouseX, mouseY));

        lastTaskNumber = TaskManager.INSTANCE.getTaskList().size();
        addContractButton.drawScreen(mouseX, mouseY);
        super.drawMainArea(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton) && activeOverlay == null) {
            if (addContractButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                showOverlay(new AddTaskOverlay());
                return true;
            } else {
                for (TaskStatusComponent taskStatusComponent : taskStatusComponents) {
                    if (taskStatusComponent.mouseClicked(mouseX, mouseY, mouseButton)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        taskStatusComponents.forEach(taskStatusComponent -> taskStatusComponent.mouseReleased(mouseX, mouseY, mouseButton));
        super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        taskStatusComponents.forEach(taskStatusComponent -> taskStatusComponent.keyTyped(typedChar, keyCode));
        super.keyTyped(typedChar, keyCode);
    }
}
