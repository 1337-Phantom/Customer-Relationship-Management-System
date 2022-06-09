package vip.phantom.system.user_interface.screens.main_screen.contract;

import org.lwjgl.input.Keyboard;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.Methods;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.contact.ContactManager;
import vip.phantom.system.contract.Contract;
import vip.phantom.system.contract.ContractManager;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.NormalButton;
import vip.phantom.system.user_interface.interactive_areas.text.TextField;
import vip.phantom.system.user_interface.screens.Overlay;

import java.awt.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AddContractOverlay extends Overlay {

    private LinkedHashMap<String, TextField> entryFields = new LinkedHashMap<>();
    private float textFieldX;

    @Override
    public void initOverlay() {
        super.initOverlay();
        informationArea = new Area(width / 4f, height / 4f, width / 2f, height / 2f);

        if (entryFields.isEmpty()) {
            entryFields.put("§c*§rTitel", new TextField(0, 0, 0, "", fr, "(.)+"));
            entryFields.put("§c*§rKunde", new TextField(0, 0, 0, "", fr, "(.)+"));
            entryFields.put("§c*§rDatum/Zeitraum", new TextField(0, 0, 0, "", fr, "(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})|[0-9]+"));
            entryFields.put("Preis", new TextField(0, 0, 0, "", fr, "()|[0-9]+((\\.|\\,)[0-9]{0,2}|())"));
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
        RenderUtil.drawRect(informationArea.getX(), informationArea.getY(), informationArea.getWidth(), informationArea.getHeight(), new Color(80, 80, 80));
        float renderY = informationArea.getY();
        RenderUtil.beginScissor(informationArea.getX(), informationArea.getY(), informationArea.getWidth(), informationArea.getHeight());
        headlineFr.drawString("§nNeuer Vertrag", informationArea.getX(), renderY, Color.black);
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
            case 0 -> crm.currentScreen.closeOverlay();
            case 1 -> {
                boolean canAdd = true;
                for (TextField value : entryFields.values()) {
                    if (!value.matchesPattern()) {
                        canAdd = false;
                        break;
                    }
                }
                if (canAdd) {
                    String time = entryFields.get("§c*§rDatum/Zeitraum").getText();
                    LocalDate localDate;
                    if (Pattern.matches("[0-9]+", time)) {
                        localDate = LocalDate.now().plusMonths(Integer.parseInt(time));
                    } else {
                        localDate = Methods.getDateFromString(time);
                    }
                    final Contract newContract = new Contract(entryFields.get("§c*§rTitel").getText(), entryFields.get("§c*§rKunde").getText(), localDate);
                    if (!entryFields.get("Preis").getText().equals("")) {
                        newContract.setPrice(Float.parseFloat(entryFields.get("Preis").getText()));
                    }
                    if (!entryFields.get("Beschreibung").getText().equals("")) {
                        newContract.setDescription(entryFields.get("Beschreibung").getText());
                    }
                    ContractManager.INSTANCE.addContract(newContract);
                    crm.currentScreen.closeOverlay();
                }
            }
        }
        super.buttonPressed(buttonId);
    }
}
