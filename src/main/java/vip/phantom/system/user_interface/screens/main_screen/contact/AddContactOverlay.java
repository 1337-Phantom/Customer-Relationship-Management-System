package vip.phantom.system.user_interface.screens.main_screen.contact;

import org.lwjgl.input.Keyboard;
import org.w3c.dom.Text;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.Methods;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.contact.ContactManager;
import vip.phantom.system.contact.Title;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.NormalButton;
import vip.phantom.system.user_interface.interactive_areas.comboboxes.ComboBox;
import vip.phantom.system.user_interface.interactive_areas.text.TextField;
import vip.phantom.system.user_interface.screens.main_screen.Overlay;

import java.awt.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class AddContactOverlay extends Overlay {

    private LinkedHashMap<String, Object> entryFields = new LinkedHashMap<>();
    private float entryFieldX;

    @Override
    public void initOverlay() {
        super.initOverlay();
        informationArea = new Area(width / 4f, height / 4f, width / 2f, height / 2f);
        String[] titles = new String[Title.values().length];
        for (int i = 0; i < Title.values().length; i++) {
            titles[i] = Title.values()[i].getString();
        }

        if (entryFields.isEmpty()) {
            entryFields.put("Title", new ComboBox(0, 0, titles));
            entryFields.put("Name", new TextField(0, 0, 0, "", fr, "()|[a-zA-Z]{2,}"));
            entryFields.put("Zweitname", new TextField(0, 0, 0, "", fr, "()|[a-zA-Z]{2,}"));
            entryFields.put("§c*§rNachname", new TextField(0, 0, 0, "", fr, "[a-zA-Z]{2,}"));
            entryFields.put("Geburtsdatum", new TextField(0, 0, 0, "", fr, "()|(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})"));
            entryFields.put("E-Mail", new TextField(0, 0, 0, "", fr, "()|[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"));
            entryFields.put("Telefon", new TextField(0, 0, 0, "", fr, "()|^(\\+[0-9]{1,3}|0)[0-9]{3}( ){0,1}[0-9]{7,8}"));
            entryFields.put("Mobiltelefon", new TextField(0, 0, 0, "", fr, "()|^(\\+[0-9]{1,3}|0)[0-9]{3}( ){0,1}[0-9]{7,8}"));

            entryFields.put("Straße, Hausnummer", new TextField(0, 0, 0, "", fr, "()|[a-zA-Z]+,( ){0,1}[0-9]{1,3}[a-zA-Z]*"));
            entryFields.put("PLZ", new TextField(0, 0, 0, "", fr, "()|[0-9]{5}"));
            entryFields.put("Stadt", new TextField(0, 0, 0, "", fr));
            entryFields.put("Land", new TextField(0, 0, 0, "", fr));
        }

        entryFieldX = 0;
        for (String s : entryFields.keySet()) {
            float strWidth = fr.getWidth(s) + (s.contains("*") ? 0 : fr.getWidth("*"));
            if (strWidth > entryFieldX) {
                entryFieldX = strWidth;
            }
        }
        entryFieldX += informationArea.getX() + 10;
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
        headlineFr.drawString("§nNeuer Account", informationArea.getX(), renderY, Color.black);
        renderY += headlineFr.getHeight();

        for (String s : entryFields.keySet()) {
            Object entryField = entryFields.get(s);
            fr.drawString(s, informationArea.getX() + (s.contains("*") ? 0 : fr.getWidth("*")), renderY, Color.black);
            if (entryField instanceof TextField textField) {
                textField.setPosition(entryFieldX, renderY);
                textField.setWidth(informationArea.getX() + informationArea.getWidth() - entryFieldX - 10);
                textField.drawTextField(mouseX, mouseY);
                renderY += textField.getHeight() + 2;
            } else if (entryField instanceof ComboBox comboBox) {
                comboBox.setX(entryFieldX);
                comboBox.setY(renderY);
                comboBox.drawComboBox(mouseX, mouseY);
                renderY += comboBox.getHeight() + 2;
            }
        }
        RenderUtil.endScissor();
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton)) {
            for (Object value : entryFields.values()) {
                if (value instanceof TextField textField) {
                    if (textField.mouseClicked(mouseX, mouseY, mouseButton)) {
                        return true;
                    }
                } else if (value instanceof ComboBox comboBox) {
                    if (comboBox.mouseClicked(mouseX, mouseY, mouseButton)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Object value : entryFields.values()) {
            if (value instanceof TextField textField) {
                textField.mouseReleased(mouseX, mouseY, state);
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        for (Object value : entryFields.values()) {
            if (value instanceof TextField textField) {
                textField.keyTyped(typedChar, keyCode);
            }
        }
        if (keyCode == Keyboard.KEY_TAB) {
            for (Iterator<Object> iterator = entryFields.values().iterator(); iterator.hasNext(); ) {
                Object value = iterator.next();
                if (value instanceof TextField textField) {
                    if (textField.isFocused()) {
                        if (iterator.hasNext()) {
                            textField.setFocused(false);
                            Object next = iterator.next();
                            if (next instanceof TextField nexTextField)
                                nexTextField.setFocused(true);
                        }
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
                for (Object value : entryFields.values()) {
                    if (value instanceof TextField textField && !textField.matchesPattern()) {
                        canAdd = false;
                        break;
                    }
                }
                if (canAdd) {
                    String acName = ((TextField) entryFields.get("§c*§rNachname")).getText() + ((TextField) entryFields.get("Name")).getText();
                    Title title = Title.MR;
                    for (Title value : Title.values()) {
                        if (value.getString().equals(((ComboBox) entryFields.get("Title")).getCurrentValue())) {
                            title = value;
                            break;
                        }
                    }
                    String name = ((TextField) entryFields.get("Name")).getText();
                    String secondName = ((TextField) entryFields.get("Zweitname")).getText();
                    String familyName = ((TextField) entryFields.get("§c*§rNachname")).getText();
                    LocalDate birthdate = Methods.getDateFromString(((TextField) entryFields.get("Geburtsdatum")).getText());
                    String email = ((TextField) entryFields.get("E-Mail")).getText();
                    String phoneNumber = ((TextField) entryFields.get("Telefon")).getText();
                    String mobilePhone = ((TextField) entryFields.get("Mobiltelefon")).getText();
                    String street = ((TextField) entryFields.get("Straße, Hausnummer")).getText();
                    int postalCode = ((TextField) entryFields.get("PLZ")).getText().equals("") ? 0 : Integer.parseInt(((TextField) entryFields.get("PLZ")).getText());
                    String city = ((TextField) entryFields.get("Stadt")).getText();
                    String country = ((TextField) entryFields.get("Land")).getText();

                    ContactManager.INSTANCE.addContact(new Contact(acName, title, name, secondName, familyName, birthdate, email, phoneNumber, mobilePhone, street, postalCode, city, country, System.currentTimeMillis()));

                    crm.currentScreen.closeOverlay();
                }
            }
        }
        super.buttonPressed(buttonId);
    }
}
