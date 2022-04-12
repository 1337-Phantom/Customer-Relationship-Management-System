package vip.phantom.system.user_interface.screens.main_screen.contract;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contract.Contract;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.screens.main_screen.Overlay;

import java.awt.*;

public class ContractOverlay extends Overlay {

    private final Contract shownContract;

    public ContractOverlay(Contract shownContract) {
        this.shownContract = shownContract;
    }

    @Override
    public void initOverlay() {
        super.initOverlay();
        informationArea = new Area(width / 4f, height / 4f, width / 2f, height / 2f);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        drawDefaultBackground();
        RenderUtil.drawRect(informationArea.getX(), informationArea.getY(), informationArea.getWidth(), informationArea.getHeight(), Color.green);

        float renderY = informationArea.getY();
        headlineFr.drawString("§n" + shownContract.getHeadline(), informationArea.getX(), renderY, Color.black);
        renderY += headlineFr.getHeight();
        fr.drawString("Vertragsnummer: " + shownContract.getContractNumber(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Auftraggeber: " + shownContract.getCustomer().getFullName(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Status: " + shownContract.getStatusAsString(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Preis: " + shownContract.getPrice(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Vertragsbeginn: " + shownContract.getStartDate(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Vertragsende: " + shownContract.getDeliveryDate() + " (in " + shownContract.getDaysLeft() + " Tagen)", informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Beschreibung:", informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();

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
        super.drawScreen(mouseX, mouseY);
    }
}
