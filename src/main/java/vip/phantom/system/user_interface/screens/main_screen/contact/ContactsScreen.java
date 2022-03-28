package vip.phantom.system.user_interface.screens.main_screen.contact;

import vip.phantom.api.font.FontRenderer;
import vip.phantom.api.font.Fonts;
import vip.phantom.api.utils.RenderUtil;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.contact.ContactManager;
import vip.phantom.system.user_interface.buttons.square_buttons.TextAsButton;
import vip.phantom.system.user_interface.screens.main_screen.MainScreen;

import java.awt.*;
import java.util.HashMap;

public class ContactsScreen extends MainScreen {

    public ContactsScreen() {
        super("Kontakte");
    }

    private FontRenderer fr = Fonts.VERDANA10;

    private HashMap<Integer, Contact> buttonContactConnection = new HashMap<>();

    @Override
    public void initScreen() {
        super.initScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {
        RenderUtil.drawRect(mainWindow.getX(), renderY, mainWindow.getWidth(), mainWindow.getHeight() - (renderY - mainWindow.getY()), Color.green);
        super.drawScreen(mouseX, mouseY);
        for (Contact contact : ContactManager.INSTANCE.getContactList()) {
            fr.drawString(contact.getFullName(), mainWindow.getX() + spacerSize, renderY + spacerSize / 2f, Color.black);
            renderY += fr.getHeight() + spacerSize;
        }
    }
}
