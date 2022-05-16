package vip.phantom.system.user_interface.screens.main_screen.home;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.Role;
import vip.phantom.system.contract.Contract;
import vip.phantom.system.contract.ContractManager;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.components.Graph;
import vip.phantom.system.user_interface.screens.main_screen.MainScreen;

import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;

public class HomeScreen extends MainScreen {

    public HomeScreen() {
        super("Startseite");
    }

    private Area financeArea, lastContactsArea, currentTasksArea;
    private Graph financeGraph;

    @Override
    public void initScreen() {
        super.initScreen();
        financeArea = new Area(mainWindow.getX() + 5, mainWindow.getY() + headlineFr.getHeight(), mainWindow.getWidth() / 3f * 2f - 15, (mainWindow.getHeight() - headlineFr.getHeight()) / 2f - 5);
        if (crm.currentAccount.getOrganisationRole(crm.selectedOrganisation) == Role.ADMIN || crm.currentAccount.getApplicationRole() == Role.ADMIN) {
            HashMap<LocalDate, Float> entries = new HashMap<>();
            for (Contract contract : ContractManager.INSTANCE.getContractList()) {
                entries.put(contract.getDeliveryDate(), contract.getPrice());
            }
            financeGraph = new Graph(financeArea.getX() + 10, financeArea.getY() + fr.getHeight() + 5, financeArea.getWidth() - 20, financeArea.getHeight() - fr.getHeight() - 15, " in €", "Date", entries);
        }
        lastContactsArea = new Area(financeArea.getX() + financeArea.getWidth() + 5, financeArea.getY(), mainWindow.getWidth() - financeArea.getWidth() - 15, mainWindow.getHeight() - headlineFr.getHeight() - 5);
        currentTasksArea = new Area(financeArea.getX(), financeArea.getY() + financeArea.getHeight() + 5, financeArea.getWidth(), financeArea.getHeight());
    }

    @Override
    public void drawMainArea(int mouseX, int mouseY) {

        headlineFr.drawString("§nWelcome back, " + crm.currentAccount.getName(), mainWindow.getX() + 5, renderY, Color.black);
        renderY += headlineFr.getHeight();

        if (crm.currentAccount.getOrganisationRole(crm.selectedOrganisation) == Role.ADMIN || crm.currentAccount.getApplicationRole() == Role.ADMIN) {
            drawFinanceArea(mouseX, mouseY);
        }
        drawLastContactsArea(mouseX, mouseY);
        drawCurrentTasksArea(mouseX, mouseY);
        super.drawMainArea(mouseX, mouseY);
    }

    private void drawFinanceArea(int mouseX, int mouseY) {
        RenderUtil.drawRect(financeArea.getX(), financeArea.getY(), financeArea.getWidth(), financeArea.getHeight(), Color.green);
        fr.drawString("§nFinanzen", financeArea.getX(), financeArea.getY(), Color.black);
        financeGraph.drawGraph(mouseX, mouseY);
    }

    private void drawLastContactsArea(int mouseX, int mouseY) {
        RenderUtil.drawRect(lastContactsArea.getX(), lastContactsArea.getY(), lastContactsArea.getWidth(), lastContactsArea.getHeight(), Color.red);
        fr.drawString("§nLetzte Kontakte", lastContactsArea.getX(), lastContactsArea.getY(), Color.black);

    }

    private void drawCurrentTasksArea(int mouseX, int mouseY) {
        RenderUtil.drawRect(currentTasksArea.getX(), currentTasksArea.getY(), currentTasksArea.getWidth(), currentTasksArea.getHeight(), Color.orange);
        fr.drawString("§nZu Erledigen", currentTasksArea.getX(), currentTasksArea.getY(), Color.black);
    }
}
