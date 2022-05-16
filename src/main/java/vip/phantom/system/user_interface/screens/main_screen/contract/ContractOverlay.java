package vip.phantom.system.user_interface.screens.main_screen.contract;

import vip.phantom.api.utils.Methods;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contract.Contract;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.square_buttons.EditButton;
import vip.phantom.system.user_interface.interactive_areas.text.TextField;
import vip.phantom.system.user_interface.screens.main_screen.Overlay;

import java.awt.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;

public class ContractOverlay extends Overlay {

    private final Contract shownContract;

    private TextField editingField;
    private int fieldEditing;

    private LinkedHashMap<String, String> data = new LinkedHashMap<>();

    public ContractOverlay(Contract shownContract) {
        this.shownContract = shownContract;
    }

    @Override
    public void initOverlay() {
        super.initOverlay();
        informationArea = new Area(width / 4f, height / 4f, width / 2f, height / 2f);

        data.put("Vertragsnummer", shownContract.getContractNumber());
        data.put("Auftraggeber", shownContract.getCustomer().getFullName());
        data.put("Status", shownContract.getStatusAsString());
        data.put("Preis", shownContract.getPriceAsString());
        data.put("Vertragsbeginn", shownContract.getStartDateAsString());
        data.put("Vertragsende", shownContract.getDeliveryDateAsString() + " (in " + shownContract.getDaysLeft() + " Tagen)");
        data.put("Beschreibung", "");

        for (int i = 0; i < 4; i++) {
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
        headlineFr.drawString("§n" + shownContract.getHeadline(), informationArea.getX(), renderY, Color.black);
        renderY += headlineFr.getHeight();
//        editingField.setY(renderY + fr.getHeight() * (fi);

        int i = 0;
        for (String s : data.keySet()) {
            String content = data.get(s);
            float renderX = informationArea.getX();
            fr.drawString(s + ": ", renderX, renderY, Color.black);
            renderX += fr.getWidth(s + ": ") + 5;
            if (editingField.isShown() && fieldEditing + 3 == i) {
                editingField.setPosition(renderX, renderY);
                renderX += editingField.getWidth() + 5;
            } else {
                fr.drawString(content, renderX, renderY, Color.black);
                renderX += fr.getWidth(content) + 5;
            }
            if (i >= 3) {
                buttonList.get(i - 3).setX(renderX);
                buttonList.get(i - 3).setY(renderY - 3);
            }
            renderY += fr.getHeight();
            i++;
        }

        String[] words = shownContract.getDescription().split(" ");
        float beginX = informationArea.getX() + 10;
        float maxX = beginX + informationArea.getWidth() - 20;
        float renderX = beginX;
        for (String word : words) {
            word += " ";
            float wordWidth = fr.getWidth(word);
            if (renderX + wordWidth > maxX) {
                renderY += fr.getHeight();
                renderX = beginX;
            }
            fr.drawString(word, renderX, renderY, Color.black);
            renderX += wordWidth;
        }

        editingField.drawTextField(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
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
        if (buttonList.get(buttonId) instanceof EditButton editButton) {
            switch (buttonId) {
                case 0 -> {
                    if (!editButton.isEditing()) {
                        shownContract.setPrice(Float.parseFloat(editingField.getText().replace(",", ".")));
                        data.replace("Preis", shownContract.getPriceAsString());
                    } else {
                        editingField.setText(shownContract.getPriceAsString().substring(0, shownContract.getPriceAsString().length() - 1));
                        editingField.setRegex("()|[0-9]+((\\.|\\,)[0-9]{0,2}|())");
                    }
                }
                case 1 -> {
                    if (!editButton.isEditing()) {
                        if (editingField.matchesPattern()) {
                            shownContract.setStartDate(Methods.getDateFromString(editingField.getText()));
                            data.replace("Vertragsbeginn", shownContract.getStartDateAsString());
                        } else {
                            editButton.setEditing(true);
                        }
                    } else {
                        editingField.setText(shownContract.getStartDateAsString());
                        editingField.setRegex("(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})");
                    }
                }
                case 2 -> {
                    if (!editButton.isEditing()) {
                        if (editingField.matchesPattern()) {
                            shownContract.setDeliveryDate(Methods.getDateFromString(editingField.getText()));
                            data.replace("Vertragsende", shownContract.getDeliveryDateAsString() + " (in " + shownContract.getDaysLeft() + " Tagen)");
                        } else {
                            editButton.setEditing(true);
                        }
                    } else {
                        editingField.setText(shownContract.getDeliveryDateAsString());
                        editingField.setRegex("(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})");
                    }
                }
            }
            fieldEditing = buttonId;
            editingField.setShown(editButton.isEditing());
        }
        super.buttonPressed(buttonId);
    }
}
