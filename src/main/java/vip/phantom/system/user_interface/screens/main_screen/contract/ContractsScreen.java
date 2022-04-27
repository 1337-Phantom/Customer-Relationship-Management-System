package vip.phantom.system.user_interface.screens.main_screen.contract;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contact.ContactManager;
import vip.phantom.system.contract.ContractManager;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.interactive_areas.buttons.round_buttons.RoundPictureButton;
import vip.phantom.system.user_interface.screens.main_screen.MainScreen;
import vip.phantom.system.user_interface.screens.main_screen.contact.AddContactOverlay;
import vip.phantom.system.user_interface.screens.main_screen.contact.ContactOverlay;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ContractsScreen extends MainScreen {

    public ContractsScreen() {
        super("Verträge");
    }

    private HashMap<Integer, Area> areaByIndex = new HashMap<>();

    private RoundPictureButton addContractButton;

    private LinkedHashMap<String, List<String>> table = new LinkedHashMap<>();

    @Override
    public void initScreen() {
        super.initScreen();

        List<String> numberList = new ArrayList<>();
        ContractManager.INSTANCE.getContractList().forEach(contract -> numberList.add(contract.getContractNumber()));
        table.put("Vertragsnummer", numberList);
        List<String> headlineList = new ArrayList<>();
        ContractManager.INSTANCE.getContractList().forEach(contract -> headlineList.add(contract.getHeadline()));
        table.put("Überschrift", headlineList);
        List<String> contactList = new ArrayList<>();
        ContractManager.INSTANCE.getContractList().forEach(contract -> contactList.add(contract.getCustomer().getFullName()));
        table.put("Auftraggeber", contactList);
        List<String> stages = new ArrayList<>();
        ContractManager.INSTANCE.getContractList().forEach(contract -> stages.add(contract.getStatusAsString()));
        table.put("Status", stages);
        List<String> prices = new ArrayList<>();
        ContractManager.INSTANCE.getContractList().forEach(contract -> prices.add(contract.getPrice()));
        table.put("Preis", prices);
        List<String> daysLeft = new ArrayList<>();
        ContractManager.INSTANCE.getContractList().forEach(contract -> daysLeft.add(contract.getDaysLeft()));
        table.put("Tage übrig", daysLeft);
        List<String> beginDates = new ArrayList<>();
        ContractManager.INSTANCE.getContractList().forEach(contract -> beginDates.add(contract.getStartDate()));
        table.put("Vertragsbeginn", beginDates);
        List<String> deliveryDates = new ArrayList<>();
        ContractManager.INSTANCE.getContractList().forEach(contract -> deliveryDates.add(contract.getDeliveryDate()));
        table.put("Vertragsende", deliveryDates);

        addContractButton = new RoundPictureButton(buttonList.size(), width - 45, height - 45, 38, "plusIcon", Color.black);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        drawDefaultBackground();
        drawSidebar(mouseX, mouseY);
        drawHeadline(mouseX, mouseY);
        RenderUtil.drawRect(mainWindow.getX(), renderY, mainWindow.getWidth(), mainWindow.getHeight() - (renderY - mainWindow.getY()), Color.blue);

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

        addContractButton.drawScreen(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY);
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
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (activeOverlay == null) {
            if (addContractButton.mouseClicked(mouseX, mouseY, mouseButton)) {
                showOverlay(new AddContractOverlay());
            } else {
                areaByIndex.forEach((integer, area) -> {
                    if (area.isHovered(mouseX, mouseY)) {
                        showOverlay(new ContractOverlay(ContractManager.INSTANCE.getContractList().get(integer)));
                    }
                });
            }
        }
    }


}
