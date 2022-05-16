package vip.phantom.system.user_interface.screens.main_screen.tasks;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.Account;
import vip.phantom.system.task.Task;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.EditButton;
import vip.phantom.system.user_interface.interactive_areas.text.TextField;
import vip.phantom.system.user_interface.screens.main_screen.Overlay;

import java.awt.*;
import java.util.LinkedHashMap;

public class TaskOverlay extends Overlay {

    private final Task shownTask;

    private TextField editingField;
    private int fieldEditing;

    private LinkedHashMap<String, String> data = new LinkedHashMap<>();

    public TaskOverlay(Task shownTask) {
        this.shownTask = shownTask;
    }

    @Override
    public void initOverlay() {
        super.initOverlay();
        informationArea = new Area(width / 4f, height / 4f, width / 2f, height / 2f);

        data.put("Überschrift", shownTask.headline);
        data.put("Status", shownTask.getStatus().string);
        data.put("Ersteller", shownTask.creator.getName());
        StringBuilder participants = new StringBuilder();
        for (Account participant : shownTask.participants) {
            participants.append(participant.getName());
        }
        data.put("Teilnehmer", participants.toString());
        data.put("Bis", shownTask.getFinishDate() + "(" + shownTask.getDaysLeft() + " Tage)");

        for (int i = 0; i < 5; i++) {
            buttonList.add(new EditButton(i, 0, 0, (int) fr.getHeight(), (int) fr.getHeight(), Color.black));
        }
        editingField = new TextField(0, 0, 200, "", fr);
        editingField.setShown(false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        drawDefaultBackground();
        RenderUtil.drawRect(informationArea.getX(), informationArea.getY(), informationArea.getWidth(), informationArea.getHeight(), Color.green);
        float renderY = informationArea.getY();
        headlineFr.drawString("§n" + shownTask.getHeadline(), informationArea.getX(), renderY, Color.black);
        renderY += headlineFr.getHeight();
        editingField.setY(renderY + fr.getHeight() * fieldEditing);

        int i = 0;
        for (String s : data.keySet()) {
            String content = data.get(s);
            float renderX = informationArea.getX();
            fr.drawString(s + ": ", renderX, renderY, Color.black);
            renderX += fr.getWidth(s + ": ") + 5;
            if (editingField.isShown() && fieldEditing == i) {
                editingField.setPosition(renderX, renderY);
                renderX += editingField.getWidth() + 5;
            } else {
                fr.drawString(content, renderX, renderY, Color.black);
                renderX += fr.getWidth(content) + 5;
            }
            buttonList.get(i).setX(renderX);
            buttonList.get(i).setY(renderY - 3);
            renderY += fr.getHeight();
            i++;
        }
        editingField.drawTextField(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
//        if (!informationArea.isHovered(mouseX, mouseY) && mouseButton == 0) {
//            crm.currentScreen.closeOverlay();
//        }
        if (!super.mouseClicked(mouseX, mouseY, mouseButton)) {
            return editingField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        editingField.mouseReleased(mouseX, mouseY, state);
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        editingField.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void buttonPressed(int buttonId) {

        super.buttonPressed(buttonId);
    }
}
