package vip.phantom.system.user_interface.screens.main_screen.contact;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.contact.ContactManager;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.round_buttons.RoundPictureButton;
import vip.phantom.system.user_interface.interactive_areas.comboboxes.ComboBox;
import vip.phantom.system.user_interface.screens.main_screen.MainScreen;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ContactsScreen extends MainScreen {

    public ContactsScreen() {
        super("Kontakte");
    }

    private HashMap<Integer, Area> areaByIndex = new HashMap<>();

    private RoundPictureButton addContactButton;

    private LinkedHashMap<String, List<String>> table = new LinkedHashMap<>();

    private ComboBox sortBox;

    @Override
    public void initScreen() {
        super.initScreen();
        areaByIndex.clear();
        if (sortBox == null) {
            sortBox = new ComboBox(0, 0, "Letzte", "Name", "Alter");
        }
        sortBox.setX(mainWindow.getX() + 5 + fr.getWidth("Sorted After: ") + 5);
        sortBox.setY(mainWindow.getY() + headlineFr.getHeight() + spacerSize);
        List<Contact> contactList = ContactManager.INSTANCE.getContactList();
        switch (sortBox.getCurrentValue()) {
            case "Letzte" -> contactList.sort(Comparator.comparing(Contact::getLastLookedAt).reversed());
            case "Name" -> contactList.sort(Comparator.comparing(Contact::getFamilyName));
            case "Alter" -> contactList.sort(Comparator.comparing(Contact::getAge));
        }
        List<String> nameList = new ArrayList<>();
        contactList.forEach(contact -> nameList.add(contact.getFullName()));
        table.put("Name", nameList);
        List<String> mailList = new ArrayList<>();
        contactList.forEach(contact -> mailList.add(contact.getEMail()));
        table.put("E-Mail", mailList);
        List<String> phoneList = new ArrayList<>();
        contactList.forEach(contact -> phoneList.add(contact.getPhoneNumber()));
        table.put("Telefon", phoneList);
        List<String> ageList = new ArrayList<>();
        contactList.forEach(contact -> ageList.add(contact.getAge()));
        table.put("Alter", ageList);
        List<String> cityList = new ArrayList<>();
        contactList.forEach(contact -> cityList.add(contact.getCity() + " (" + contact.getCountry() + ")"));
        table.put("Wohnort", cityList);
        List<String> addressList = new ArrayList<>();
        contactList.forEach(contact -> addressList.add(contact.getStreetAndNumber()));
        table.put("Adresse", addressList);

        addContactButton = new RoundPictureButton(buttonList.size(), width - 45, height - 45, 38, "plusIcon", new Color(97, 202, 192));
    }

    @Override
    public void drawMainArea(int mouseX, int mouseY) {
        drawHeadline(mouseX, mouseY);
//        RenderUtil.drawRect(mainWindow.getX(), renderY, mainWindow.getWidth(), mainWindow.getHeight() - (renderY - mainWindow.getY()), Color.blue);

        renderY += spacerSize;
        fr.drawString("Sortiert nach: ", mainWindow.getX() + 5, renderY - 3, Color.white);
        sortBox.drawComboBox(mouseX, mouseY);
        renderY += sortBox.getHeight();
        Object[] keyArray = table.keySet().toArray();
        float[] columnSizes = new float[]{mainWindow.getX(), 0, spacerSize, 0};
        for (int i = 0; i < keyArray.length; i++) {
            String headline = String.valueOf(keyArray[i]);
            List<String> entries = table.get(headline);
            columnSizes = drawStringColumnAt(mouseX, mouseY, columnSizes[0] + columnSizes[2], renderY, headline, entries);
            if (i == 0) {
                RenderUtil.drawRect(columnSizes[0], columnSizes[1], 1, columnSizes[3], Color.white);
            }
            RenderUtil.drawRect(columnSizes[0] + columnSizes[2] - 1, columnSizes[1], 1, columnSizes[3], Color.white);
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
        RenderUtil.drawRect(x, y, columnWidth, 1, Color.white);
        fr.drawString(columnName, x + columnWidth / 2f - fr.getWidth(columnName) / 2f, y, Color.white);
        y += fr.getHeight();
        RenderUtil.drawRect(x, y - 2, columnWidth, 1, Color.white);
        for (int i = 0; i < valueList.size(); i++) {
            String str = valueList.get(i);
            final Area area = new Area(mainWindow.getX(), y - spacerSize / 2f, mainWindow.getWidth(), fr.getHeight() + spacerSize);
            if (!areaByIndex.containsKey(i) || !areaByIndex.get(0).equals(area)) {
                areaByIndex.put(i, area);
            }
            if (area.isHovered(mouseX, mouseY) && activeOverlay == null) {
                RenderUtil.drawRect(x, y - spacerSize / 2f, columnWidth, fr.getHeight() + spacerSize, Color.gray);
            }
            fr.drawString(str, x + columnWidth / 2f - fr.getWidth(str) / 2f - 1, y + spacerSize / 2f, Color.white);
            y += fr.getHeight() + spacerSize;
        }
        RenderUtil.drawRect(x, y - 1, columnWidth, 1, Color.white);
        return new float[]{x, originalY, columnWidth, y - originalY};
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!super.mouseClicked(mouseX, mouseY, mouseButton) && activeOverlay == null) {
            if (addContactButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                showOverlay(new AddContactOverlay());
                return true;
            } else if (sortBox.mouseClicked(mouseX, mouseY, mouseButton)) {
                initScreen();
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
