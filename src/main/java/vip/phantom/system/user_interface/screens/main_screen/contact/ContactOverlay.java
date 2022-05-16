package vip.phantom.system.user_interface.screens.main_screen.contact;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.EditButton;
import vip.phantom.system.user_interface.interactive_areas.text.TextField;
import vip.phantom.system.user_interface.screens.main_screen.Overlay;

import java.awt.*;
import java.util.LinkedHashMap;

public class ContactOverlay extends Overlay {

    private final Contact shownContact;

    private TextField editingField;
    private int fieldEditing;

    private LinkedHashMap<String, String> data = new LinkedHashMap<>();

    public ContactOverlay(Contact shownContact) {
        this.shownContact = shownContact;
    }

    @Override
    public void initOverlay() {
        super.initOverlay();
        informationArea = new Area(width / 4f, height / 4f, width / 2f, height / 2f);

        shownContact.lookedAtContact();

        data.put("Name", shownContact.getName());
        data.put("Zweitname", shownContact.getSecondName());
        data.put("Nachname", shownContact.getFamilyName());
        data.put("Geburtstag", shownContact.getBirthdateAsString());
        data.put("E-Mail", shownContact.getEMail());
        data.put("Telefonnummer", shownContact.getPhoneNumber());
        data.put("Handynummer", shownContact.getMobilePhoneNumber());
        data.put("Wohnsitz", shownContact.getStreetAndNumber());
        data.put("PLZ", shownContact.getPostalCodeAsString());
        data.put("Stadt", shownContact.getCity());
        data.put("Land", shownContact.getCountry());

        for (int i = 0; i < 11; i++) {
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
        headlineFr.drawString("§n" + (shownContact.getTitle() != null ? shownContact.getTitle().getString() + " " : "") + shownContact.getFullName(), informationArea.getX(), renderY, Color.black);
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
        if (!super.mouseClicked(mouseX, mouseY, mouseButton)) {
            if (!informationArea.isHovered(mouseX, mouseY) && mouseButton == 0) {
                crm.currentScreen.closeOverlay();
                return true;
            }
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
        if (buttonList.get(buttonId) instanceof EditButton editButton) {
            switch (buttonId) {
                case 0 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setName(editingField.getText());
                        data.replace("Name", shownContact.getName());
                    } else {
                        editingField.setText(shownContact.getName());
                        editingField.setRegex("()|[a-zA-Z]{2,}");
                    }
                }
                case 1 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setSecondName(editingField.getText());
                        data.replace("Zweitname", shownContact.getSecondName());
                    } else {
                        editingField.setText(shownContact.getSecondName());
                        editingField.setRegex("()|[a-zA-Z]{2,}");
                    }
                }
                case 2 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setFamilyName(editingField.getText());
                        data.replace("Nachname", shownContact.getFamilyName());
                    } else {
                        editingField.setText(shownContact.getFamilyName());
                        editingField.setRegex("[a-zA-Z]{2,}");
                    }
                }
                case 3 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setBirthdate(editingField.getText());
                        data.replace("Geburtstag", shownContact.getBirthdateAsString());
                    } else {
                        editingField.setText(shownContact.getBirthdateAsString());
                        editingField.setRegex("()|(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})");
                    }
                }
                case 4 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setEMail(editingField.getText());
                        data.replace("E-Mail", shownContact.getEMail());
                    } else {
                        editingField.setText(shownContact.getEMail());
                        editingField.setRegex("()|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
                    }
                }
                case 5 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setPhoneNumber(editingField.getText());
                        data.replace("Telefonummer", shownContact.getPhoneNumber());
                    } else {
                        editingField.setText(shownContact.getPhoneNumber());
                        editingField.setRegex("()|^(+[0-9]{1,3}|0)[0-9]{3}( ){0,1}[0-9]{7,8}");
                    }
                }
                case 6 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setMobilePhoneNumber(editingField.getText());
                        data.replace("Handynummer", shownContact.getMobilePhoneNumber());
                    } else {
                        editingField.setText(shownContact.getMobilePhoneNumber());
                        editingField.setRegex("()|^(+[0-9]{1,3}|0)[0-9]{3}( ){0,1}[0-9]{7,8}");
                    }
                }
                case 7 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setStreetAndNumber(editingField.getText());
                        data.replace("Wohnsitz", shownContact.getStreetAndNumber());
                    } else {
                        editingField.setText(shownContact.getStreetAndNumber());
                        editingField.setRegex("()|[a-zA-Z]+,( ){0,1}[0-9]{1,3}[a-zA-Z]*");
                    }
                }
                case 8 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setPostalCode(Integer.parseInt(editingField.getText()));
                        data.replace("PLZ", shownContact.getPostalCodeAsString());
                    } else {
                        editingField.setText(shownContact.getPostalCodeAsString());
                        editingField.setRegex("()|[0-9]{5}");
                    }
                }
                case 9 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setCity(editingField.getText());
                        data.replace("Stadt", shownContact.getCity());
                    } else {
                        editingField.setText(shownContact.getCity());
                        editingField.setRegex("");
                    }
                }
                case 10 -> {
                    if (!editButton.isEditing()) {
                        shownContact.setCountry(editingField.getText());
                        data.replace("Land", shownContact.getCountry());
                    } else {
                        editingField.setText(shownContact.getCountry());
                        editingField.setRegex("");
                    }
                }
            }
            fieldEditing = buttonId;
            editingField.setShown(editButton.isEditing());
        }
        super.buttonPressed(buttonId);
    }
}
