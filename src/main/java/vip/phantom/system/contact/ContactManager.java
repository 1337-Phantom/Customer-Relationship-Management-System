package vip.phantom.system.contact;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.system.CRM;

import java.util.ArrayList;
import java.util.List;

public class ContactManager {
    public static ContactManager INSTANCE = new ContactManager();

    public List<Contact> getContactList() {
        return CRM.getCrm().selectedOrganisation.getContacts();
    }

    public void addContact(Contact contact) {
        getContactList().add(contact);
    }

}
