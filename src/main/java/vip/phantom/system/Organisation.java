package vip.phantom.system;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.contract.Contract;
import vip.phantom.system.task.Task;

import java.util.ArrayList;
import java.util.List;

public class Organisation {
    @Getter
    private String name;

    public Organisation(String name) {
        this.name = name;
    }

    @Getter
    @Setter
    private List<Contact> contacts = new ArrayList<>();
    @Getter
    @Setter
    private List<Contract> contracts = new ArrayList<>();
    @Getter
    @Setter
    private List<Task> tasks = new ArrayList<>();

}
