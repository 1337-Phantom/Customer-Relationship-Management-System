package vip.phantom.system.user_interface.screens.main_screen.home;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.Role;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.contact.ContactManager;
import vip.phantom.system.contract.Contract;
import vip.phantom.system.contract.ContractManager;
import vip.phantom.system.task.Task;
import vip.phantom.system.task.TaskManager;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.components.Graph;
import vip.phantom.system.user_interface.screens.main_screen.MainScreen;

import java.awt.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class HomeScreen extends MainScreen {

    public HomeScreen() {
        super("Startseite");
    }

    private Area financeArea, lastContactsArea, currentTasksArea;
    private Graph financeGraph;

    private List<Contact> contactList;
    private List<Task> taskList;

    @Override
    public void initScreen() {
        super.initScreen();
        financeArea = new Area(mainWindow.getX() + 5, mainWindow.getY() + headlineFr.getHeight(), mainWindow.getWidth() / 3f * 2f - 15, (mainWindow.getHeight() - headlineFr.getHeight()) / 2f - 5);
        if (crm.currentAccount.getOrganisationRole(crm.selectedOrganisation) == Role.ADMIN) {
            HashMap<LocalDate, Float> entries = new HashMap<>();
            for (Contract contract : ContractManager.INSTANCE.getContractList()) {
                entries.put(contract.getDeliveryDate(), contract.getPrice());
            }
            financeGraph = new Graph(financeArea.getX() + 10, financeArea.getY() + fr.getHeight() + 5, financeArea.getWidth() - 20, financeArea.getHeight() - fr.getHeight() - 15, " in €", "Date", entries);
        }
        lastContactsArea = new Area(financeArea.getX() + financeArea.getWidth() + 5, financeArea.getY(), mainWindow.getWidth() - financeArea.getWidth() - 15, mainWindow.getHeight() - headlineFr.getHeight() - 5);
        contactList = ContactManager.INSTANCE.getContactList();
        contactList.sort(Comparator.comparing(Contact::getLastLookedAt).reversed());

        currentTasksArea = new Area(financeArea.getX(), financeArea.getY() + financeArea.getHeight() + 5, financeArea.getWidth(), financeArea.getHeight());
        taskList = TaskManager.INSTANCE.getTaskList();
        taskList.sort(Comparator.comparing(Task::getLatestDate).reversed());
    }

    @Override
    public void drawMainArea(int mouseX, int mouseY) {
        headlineFr.drawString("§nWelcome back, " + crm.currentAccount.getName(), mainWindow.getX() + 5, renderY, Color.white);
        renderY += headlineFr.getHeight();

        if (crm.currentAccount.getOrganisationRole(crm.selectedOrganisation) == Role.ADMIN) {
            drawFinanceArea(mouseX, mouseY);
        }
        drawLastContactsArea(mouseX, mouseY);
        drawCurrentTasksArea(mouseX, mouseY);
        super.drawMainArea(mouseX, mouseY);
    }

    private void drawFinanceArea(int mouseX, int mouseY) {
//        RenderUtil.drawRect(financeArea.getX(), financeArea.getY(), financeArea.getWidth(), financeArea.getHeight(), Color.green);
        RenderUtil.drawOutline(financeArea.getX(), financeArea.getY(), financeArea.getWidth(), financeArea.getHeight(), 1, Color.black);
        fr.drawString("§nFinanzen", financeArea.getX(), financeArea.getY(), Color.white);
        financeGraph.drawGraph(mouseX, mouseY);
    }

    private void drawLastContactsArea(int mouseX, int mouseY) {
//        RenderUtil.drawRect(lastContactsArea.getX(), lastContactsArea.getY(), lastContactsArea.getWidth(), lastContactsArea.getHeight(), Color.red);
        RenderUtil.drawOutline(lastContactsArea.getX(), lastContactsArea.getY(), lastContactsArea.getWidth(), lastContactsArea.getHeight(), 1, Color.black);
        fr.drawString("§nLetzte Kontakte", lastContactsArea.getX(), lastContactsArea.getY(), Color.white);

        float contactY = lastContactsArea.getY() + fr.getHeight();
        for (Contact contact : contactList) {
            RenderUtil.drawOutline(lastContactsArea.getX() + 5, contactY, lastContactsArea.getWidth() - 10, 5 + fr.getHeight() * 2, 2, Color.black);
            fr.drawString(contact.getFullName(), lastContactsArea.getX() + 10, contactY + 5, Color.white);
            fr.drawString("Email: " + contact.getEMail(), lastContactsArea.getX() + 10, contactY + 5 + fr.getHeight(), Color.white);
            contactY += 5 + fr.getHeight() * 2 + 5;
        }
    }

    private void drawCurrentTasksArea(int mouseX, int mouseY) {
//        RenderUtil.drawRect(currentTasksArea.getX(), currentTasksArea.getY(), currentTasksArea.getWidth(), currentTasksArea.getHeight(), Color.orange);
        RenderUtil.drawOutline(currentTasksArea.getX(), currentTasksArea.getY(), currentTasksArea.getWidth(), currentTasksArea.getHeight(), 1, Color.black);
        fr.drawString("§nZu Erledigen", currentTasksArea.getX(), currentTasksArea.getY(), Color.white);

        float taskY = currentTasksArea.getY() + fr.getHeight();
        for (Task task : taskList) {
            RenderUtil.drawOutline(currentTasksArea.getX() + 5, taskY, currentTasksArea.getWidth() - 10, 5 + fr.getHeight() * 2, 2, Color.black);
            fr.drawString(task.getHeadline() + " (" + task.getStatus().string + ")", currentTasksArea.getX() + 10, taskY + 5, Color.white);
            fr.drawString("Abgabedatum: " + task.getFinishDate(), currentTasksArea.getX() + 10, taskY + 5 + fr.getHeight(), Color.white);
            taskY += 5 + fr.getHeight() * 2 + 5;
        }
    }
}
