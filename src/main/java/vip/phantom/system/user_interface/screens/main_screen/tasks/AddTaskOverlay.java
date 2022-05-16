package vip.phantom.system.user_interface.screens.main_screen.tasks;

import org.lwjgl.input.Keyboard;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.Methods;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.Account;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.contact.ContactManager;
import vip.phantom.system.contact.Title;
import vip.phantom.system.task.Task;
import vip.phantom.system.task.TaskManager;
import vip.phantom.system.task.TaskStatus;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.NormalButton;
import vip.phantom.system.user_interface.interactive_areas.text.TextField;
import vip.phantom.system.user_interface.screens.main_screen.Overlay;

import java.awt.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class AddTaskOverlay extends Overlay {

    private LinkedHashMap<String, TextField> entryFields = new LinkedHashMap<>();
    private float textFieldX;

    @Override
    public void initOverlay() {
        super.initOverlay();
        informationArea = new Area(width / 4f, height / 4f, width / 2f, height / 2f);
        if (entryFields.isEmpty()) {
            entryFields.put("§c*§rTitel", new TextField(0, 0, 0, "", fr, "(.)+"));
            entryFields.put("Teilnehmer", new TextField(0, 0, 0, "", fr));
            entryFields.put("§c*§rAbgabedatum", new TextField(0, 0, 0, "", fr, "(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})"));
            entryFields.put("Beschreibung", new TextField(0, 0, 0, "", fr));
        }

        for (TextField value : entryFields.values()) {
            value.setWidth(0);
        }

        textFieldX = 0;
        for (String s : entryFields.keySet()) {
            float strWidth = fr.getWidth(s) + (s.contains("*") ? 0 : fr.getWidth("*"));
            if (strWidth > textFieldX) {
                textFieldX = strWidth;
            }
        }
        textFieldX += informationArea.getX() + 10;
        int buttonHeight = (int) Fonts.Light12.getHeight();
        buttonList.add(new NormalButton(0, (int) informationArea.getX() + 10, (int) (informationArea.getY() + informationArea.getHeight() - buttonHeight - 5), (int) (informationArea.getWidth() / 2f - 20), buttonHeight, "Abbrechen"));
        buttonList.add(new NormalButton(1, (int) (informationArea.getX() + informationArea.getWidth() / 2f + 5), (int) (informationArea.getY() + informationArea.getHeight() - buttonHeight - 5), (int) (informationArea.getWidth() / 2f - 20), buttonHeight, "Hinzufügen"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        drawDefaultBackground();
        RenderUtil.drawRect(informationArea.getX(), informationArea.getY(), informationArea.getWidth(), informationArea.getHeight(), Color.green);
        float renderY = informationArea.getY();
        RenderUtil.beginScissor(informationArea.getX(), informationArea.getY(), informationArea.getWidth(), informationArea.getHeight());
        headlineFr.drawString("§nNeue Aufgabe", informationArea.getX(), renderY, Color.black);
        renderY += headlineFr.getHeight();

        for (String s : entryFields.keySet()) {
            TextField textField = entryFields.get(s);
            fr.drawString(s, informationArea.getX() + (s.contains("*") ? 0 : fr.getWidth("*")), renderY, Color.black);
            if (textField.getWidth() == 0) {
                textField.setPosition(textFieldX, renderY);
                textField.setWidth(informationArea.getX() + informationArea.getWidth() - textFieldX - 10);
            }
            textField.drawTextField(mouseX, mouseY);
            renderY += textField.getHeight() + 2;
        }
        RenderUtil.endScissor();
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton)) {
            for (TextField value : entryFields.values()) {
                if (value.mouseClicked(mouseX, mouseY, mouseButton)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (TextField value : entryFields.values()) {
            value.mouseReleased(mouseX, mouseY, state);
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (TextField value : entryFields.values()) {
            value.keyTyped(typedChar, keyCode);
        }
        if (keyCode == Keyboard.KEY_TAB) {
            for (Iterator<TextField> iterator = entryFields.values().iterator(); iterator.hasNext(); ) {
                TextField value = iterator.next();
                if (value.isFocused()) {
                    if (iterator.hasNext()) {
                        value.setFocused(false);
                        iterator.next().setFocused(true);
                    }
                }
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void buttonPressed(int buttonId) {
        switch (buttonId) {
            case 0 -> {
                crm.currentScreen.closeOverlay();
                System.out.println("hallo");
            }
            case 1 -> {
                boolean canAdd = true;
                for (TextField value : entryFields.values()) {
                    if (!value.matchesPattern()) {
                        canAdd = false;
                        break;
                    }
                }
                if (canAdd) {
                    String headline = entryFields.get("§c*§rTitel").getText();
                    Account creator = crm.currentAccount;
                    LocalDate entryDate = LocalDate.now();
                    LocalDate latestDate = Methods.getDateFromString(entryFields.get("§c*§rAbgabedatum").getText());
                    TaskStatus taskStatus = TaskStatus.PENDING;
                    String description = entryFields.get("Beschreibung").getText();

                    Task task = new Task(headline, creator, entryDate, latestDate, taskStatus, description);

                    TaskManager.INSTANCE.addTask(task);

                    crm.currentScreen.closeOverlay();
                }
            }
        }
        super.buttonPressed(buttonId);
    }

}
