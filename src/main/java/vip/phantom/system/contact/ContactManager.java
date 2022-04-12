package vip.phantom.system.contact;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    public static ContactManager INSTANCE = new ContactManager();

    @Getter
    private List<Contact> contactList = new ArrayList<>();

    public ContactManager() {
        addContact(new Contact("Heilmann"));
        getContactList().get(0).setName("Yorck");
        getContactList().get(0).setSecondName("Immanuel");
        getContactList().get(0).setEMail("han.youke@gmx.de");
        getContactList().get(0).setBirthdate("03.04.2004");
        getContactList().get(0).setMobilePhoneNumber("0176 54498758");
        addContact(new Contact("Heilmann"));
        getContactList().get(1).setName("Sebastian");
        getContactList().get(1).setBirthdate("14.3.1965");
    }

    public void addContact(Contact contact) {
        contactList.add(contact);
    }

}
