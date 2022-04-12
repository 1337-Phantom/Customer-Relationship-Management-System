package vip.phantom.system.user_interface.screens.main_screen.contact;

import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.user_interface.Area;
import vip.phantom.system.user_interface.screens.main_screen.Overlay;

import java.awt.*;

public class ContactOverlay extends Overlay {

    private final Contact shownContact;

    public ContactOverlay(Contact shownContact) {
        this.shownContact = shownContact;
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
        headlineFr.drawString("§n" + shownContact.getFullName(), informationArea.getX(), renderY, Color.black);
        renderY += headlineFr.getHeight();
        fr.drawString("Name: " + shownContact.getName(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Zweitname: " + shownContact.getSecondName(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Nachname: " + shownContact.getFamilyName(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Birthdate: " + shownContact.getBirthdateAsString() + " (" + shownContact.getAge() + ")", informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("E-Mail: " + shownContact.getEMail(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("PhoneNumber: " + shownContact.getPhoneNumber(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Mobile Number: " + shownContact.getMobilePhoneNumber(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Wohnsitz: " + shownContact.getStreetAndNumber(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("PLZ: " + shownContact.getPostalCode(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Stadt: " + shownContact.getCity(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();
        fr.drawString("Land: " + shownContact.getCountry(), informationArea.getX(), renderY, Color.black);
        renderY += fr.getHeight();

        super.drawScreen(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!informationArea.isHovered(mouseX, mouseY) && mouseButton == 0) {
            crm.currentScreen.closeOverlay();
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
