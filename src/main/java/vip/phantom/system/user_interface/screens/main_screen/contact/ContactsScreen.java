package vip.phantom.system.user_interface.screens.main_screen.contact;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contact.ContactManager;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.round_buttons.RoundPictureButton;
import vip.phantom.system.user_interface.screens.main_screen.MainScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ContactsScreen extends MainScreen {

    public ContactsScreen() {
        super("Kontakte");
    }

    private HashMap<Integer, Area> areaByIndex = new HashMap<>();

    private RoundPictureButton addContactButton;

    private LinkedHashMap<String, List<String>> table = new LinkedHashMap<>();

    @Override
    public void initScreen() {
        super.initScreen();
        areaByIndex.clear();
        List<String> nameList = new ArrayList<>();
        ContactManager.INSTANCE.getContactList().forEach(contact -> nameList.add(contact.getFullName()));
        table.put("Name", nameList);
        List<String> mailList = new ArrayList<>();
        ContactManager.INSTANCE.getContactList().forEach(contact -> mailList.add(contact.getEMail()));
        table.put("E-Mail", mailList);
        List<String> phoneList = new ArrayList<>();
        ContactManager.INSTANCE.getContactList().forEach(contact -> phoneList.add(contact.getPhoneNumber()));
        table.put("Telefon", phoneList);
        List<String> ageList = new ArrayList<>();
        ContactManager.INSTANCE.getContactList().forEach(contact -> ageList.add(contact.getAge()));
        table.put("Alter", ageList);
        List<String> cityList = new ArrayList<>();
        ContactManager.INSTANCE.getContactList().forEach(contact -> cityList.add(contact.getCity() + " (" + contact.getCountry() + ")"));
        table.put("Wohnort", cityList);
        List<String> addressList = new ArrayList<>();
        ContactManager.INSTANCE.getContactList().forEach(contact -> addressList.add(contact.getStreetAndNumber()));
        table.put("Adresse", addressList);

        addContactButton = new RoundPictureButton(buttonList.size(), width - 45, height - 45, 38, "plusIcon", Color.black);
    }

    @Override
    public void drawMainArea(int mouseX, int mouseY) {
        drawHeadline(mouseX, mouseY);
//        RenderUtil.drawRect(mainWindow.getX(), renderY, mainWindow.getWidth(), mainWindow.getHeight() - (renderY - mainWindow.getY()), Color.blue);

        renderY += spacerSize;
        Object[] keyArray = table.keySet().toArray();
        float[] columnSizes = new float[]{mainWindow.getX(), 0, spacerSize, 0};
        for (int i = 0; i < keyArray.length; i++) {
            String headline = String.valueOf(keyArray[i]);
            List<String> entries = table.get(headline);
            columnSizes = drawStringColumnAt(mouseX, mouseY, columnSizes[0] + columnSizes[2], renderY, headline, entries);
            if (i == 0) {
                RenderUtil.drawRect(columnSizes[0], columnSizes[1], 1, columnSizes[3], Color.black);
            }
            RenderUtil.drawRect(columnSizes[0] + columnSizes[2] - 1, columnSizes[1], 1, columnSizes[3], Color.black);
        }

        addContactButton.drawScreen(mouseX, mouseY);
        super.drawMainArea(mouseX, mouseY);
    }

    private float[] drawStringColumnAt(int mouseX, int mouseY, float x, float y, String columnName, List<String> valueList) {
        float originalY = y;
        /* determining the columnWidth */
        float columnWidth = fr.getWidth(columnName);
        for (String str : valueList) {
            float strWidth = fr.getWidth(str) + 5;
            if (strWidth > columnWidth) {
                columnWidth = strWidth;
            }
        }
        columnWidth += 20;
        /* rendering the Column */
        RenderUtil.drawRect(x, y, columnWidth, 1, Color.black);
        fr.drawString(columnName, x + columnWidth / 2f - fr.getWidth(columnName) / 2f, y, Color.black);
        y += fr.getHeight();
        RenderUtil.drawRect(x, y - 2, columnWidth, 1, Color.black);
        for (int i = 0; i < valueList.size(); i++) {
            String str = valueList.get(i);
            final Area area = new Area(mainWindow.getX(), y - spacerSize / 2f, mainWindow.getWidth(), fr.getHeight() + spacerSize);
            if (!areaByIndex.containsKey(i) || !areaByIndex.get(0).equals(area)) {
                areaByIndex.put(i, area);
            }
            if (area.isHovered(mouseX, mouseY) && activeOverlay == null) {
                RenderUtil.drawRect(x, y - spacerSize / 2f, columnWidth, fr.getHeight() + spacerSize, Color.gray);
            }
            fr.drawString(str, x + columnWidth / 2f - fr.getWidth(str) / 2f - 1, y + spacerSize / 2f, Color.black);
            y += fr.getHeight() + spacerSize;
        }
        RenderUtil.drawRect(x, y - 1, columnWidth, 1, Color.black);
        return new float[]{x, originalY, columnWidth, y - originalY};
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton) && activeOverlay == null) {
            if (addContactButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                showOverlay(new AddContactOverlay());
                return true;
            } else {
                for (Integer integer : areaByIndex.keySet()) {
                    Area area = areaByIndex.get(integer);
                    if (area.isHovered(mouseX, mouseY)) {
                        showOverlay(new ContactOverlay(ContactManager.INSTANCE.getContactList().get(integer)));
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
