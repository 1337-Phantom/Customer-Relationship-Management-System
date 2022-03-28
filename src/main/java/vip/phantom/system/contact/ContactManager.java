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
        addContact(new Contact("Voss"));
        addContact(new Contact("Teuschler"));
        System.out.println("adding");
    }

    public void addContact(Contact contact) {
        contactList.add(contact);
    }

}
