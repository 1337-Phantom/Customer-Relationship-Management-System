package vip.phantom.system;

import com.google.gson.JsonObject;
import lombok.Getter;
import vip.phantom.api.file.Savable;
import vip.phantom.api.utils.Methods;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.contact.Title;
import vip.phantom.system.contract.Contract;
import vip.phantom.system.task.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Account extends Savable {

    private final String accountName;

    @Getter
    private final String name;
    @Getter
    private final Role applicationRole;

    private long createdAt;

    @Getter
    private final List<Organisation> organisations = new ArrayList<>();
    private final HashMap<Organisation, Role> organisationRole = new HashMap<>();

    public Account(String accountName, String username, Role applicationRole) {
        this.accountName = accountName;
        this.name = username;
        this.applicationRole = applicationRole;
    }

    public void addOrganisation(Organisation organisation, Role role) {
        organisations.add(organisation);
        organisationRole.put(organisation, role);
    }

    public Role getOrganisationRole(Organisation organisation) {
        return organisationRole.get(organisation);
    }

    @Override
    public boolean loadFile() throws IOException {
        JsonObject jsonObject = readFile();
        if (jsonObject != null) {
            if (jsonObject.has("Count")) {
                if (jsonObject.get("Count").getAsInt() != 0) {
                    organisations.clear();
                }
                for (int i = 0; i < jsonObject.get("Count").getAsInt(); i++) {
                    if (jsonObject.has("Organisation#" + i)) {
                        JsonObject organisationJsonObject = jsonObject.getAsJsonObject("Organisation#" + i);
                        Organisation organisation = new Organisation(organisationJsonObject.get("Name").getAsString());
                        if (organisationJsonObject.has("Contacts")) {
                            List<Contact> contacts = new ArrayList<>();
                            JsonObject contactsJsonObject = organisationJsonObject.getAsJsonObject("Contacts");
                            if (contactsJsonObject.has("Count")) {
                                for (int j = 0; j < contactsJsonObject.get("Count").getAsInt(); j++) {
                                    JsonObject contactJO = contactsJsonObject.getAsJsonObject("Contact#" + j);

                                    Title title = Title.getTitleFromString(contactJO.get("Title").getAsString());
                                    String name = contactJO.get("FirstName").getAsString();
                                    String secondName = contactJO.get("SecondName").getAsString();
                                    String familyName = contactJO.get("FamilyName").getAsString();
                                    LocalDate birthdate = Methods.getDateFromString(contactJO.get("Birthdate").getAsString());
                                    String email = contactJO.get("Email").getAsString();
                                    String phoneNumer = contactJO.get("PhoneNumber").getAsString();
                                    String mobilePhoneNumber = contactJO.get("MobilePhoneNumber").getAsString();

                                    String streetAndNumber = contactJO.get("StreetAndNumber").getAsString();
                                    int postalCode = contactJO.get("PostalCode").getAsInt();
                                    String city = contactJO.get("City").getAsString();
                                    String country = contactJO.get("Country").getAsString();

                                    long lastLookedAt = contactJO.get("LastLookedAt").getAsLong();

                                    contacts.add(new Contact(title, name, secondName, familyName, birthdate, email, phoneNumer, mobilePhoneNumber, streetAndNumber, postalCode, city, country, lastLookedAt));
                                }
                            }
                            organisation.setContacts(contacts);
                        }

                        if (organisationJsonObject.has("Contracts")) {
                            JsonObject contractsJsonObject = organisationJsonObject.getAsJsonObject("Contracts");
                            if (contractsJsonObject.has("Count")) {
                                for (int j = 0; j < contractsJsonObject.get("Count").getAsInt(); j++) {

                                }
                            }
                        }
                        if (organisationJsonObject.has("Tasks")) {
                            JsonObject tasksJsonObject = organisationJsonObject.getAsJsonObject("Tasks");
                            if (tasksJsonObject.has("Count")) {
                                for (int j = 0; j < tasksJsonObject.get("Count").getAsInt(); j++) {

                                }
                            }
                        }
                        CRM.getCrm().currentAccount.addOrganisation(organisation, Role.ADMIN);
                        if (i == 0) {
                            CRM.getCrm().setSelectedOrganisation(organisation);
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean saveFile() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Count", organisations.size());
        for (int i = 0; i < organisations.size(); i++) {
            Organisation organisation = organisations.get(i);
            JsonObject organisationJsonObject = new JsonObject();
            organisationJsonObject.addProperty("Name", organisation.getName());
            //Contacts
            {
                JsonObject contacts = new JsonObject();
                contacts.addProperty("Count", organisation.getContacts().size());
                for (int j = 0; j < organisation.getContacts().size(); j++) {
                    Contact contact = organisation.getContacts().get(j);
                    JsonObject contactJsonObject = new JsonObject();

                    contactJsonObject.addProperty("Title", contact.getTitle() != null ? contact.getTitle().getString() : "null");
                    contactJsonObject.addProperty("FirstName", contact.getName());
                    contactJsonObject.addProperty("SecondName", contact.getSecondName());
                    contactJsonObject.addProperty("FamilyName", contact.getFamilyName());
                    contactJsonObject.addProperty("Birthdate", contact.getBirthdateAsString());
                    contactJsonObject.addProperty("Email", contact.getEMail());
                    contactJsonObject.addProperty("PhoneNumber", contact.getPhoneNumber());
                    contactJsonObject.addProperty("MobilePhoneNumber", contact.getMobilePhoneNumber());

                    contactJsonObject.addProperty("StreetAndNumber", contact.getStreetAndNumber());
                    contactJsonObject.addProperty("PostalCode", contact.getPostalCode());
                    contactJsonObject.addProperty("City", contact.getCity());
                    contactJsonObject.addProperty("Country", contact.getCountry());

                    contactJsonObject.addProperty("LastLookedAt", contact.getLastLookedAt());
                    contacts.add("Contact#" + j, contactJsonObject);
                }
                organisationJsonObject.add("Contacts", contacts);
            }
            //Contracts
            {
                JsonObject contracts = new JsonObject();
                contracts.addProperty("Count", organisation.getContracts().size());
                for (int j = 0; j < organisation.getContracts().size(); j++) {
                    Contract contract = organisation.getContracts().get(j);
                    JsonObject contractJsonObject = new JsonObject();
                    contractJsonObject.addProperty("Number", contract.getContractNumber());

                    contractJsonObject.addProperty("Headline", contract.getHeadline());
                    contractJsonObject.addProperty("Customer", contract.getCustomer().getAccountName());
                    contractJsonObject.addProperty("Status", contract.getStatusAsString());
                    contractJsonObject.addProperty("StartDate", contract.getStartDateAsString());
                    contractJsonObject.addProperty("DeliveryDate", contract.getDeliveryDateAsString());
                    contractJsonObject.addProperty("Description", contract.getDescription());
                    contractJsonObject.addProperty("Price", contract.getPrice());

                    contracts.add("Contract#" + j, contractJsonObject);
                }
                organisationJsonObject.add("Contracts", contracts);
            }
            {
                JsonObject tasks = new JsonObject();
                tasks.addProperty("Count", organisation.getTasks().size());
                for (int j = 0; j < organisation.getTasks().size(); j++) {
                    Task task = organisation.getTasks().get(j);
                    JsonObject taskJsonObject = new JsonObject();

                    taskJsonObject.addProperty("Headline", task.getHeadline());
                    taskJsonObject.addProperty("Creator", task.getCreator().accountName);
                    taskJsonObject.addProperty("EntryDate", task.getEntryDate());
                    {
                        JsonObject participants = new JsonObject();
                        participants.addProperty("Count", task.getParticipants().size());
                        for (int j1 = 0; j1 < task.getParticipants().size(); j1++) {
                            Account participant = task.getParticipants().get(j1);
                            participants.addProperty("Participant#" + j1, participant.accountName);
                        }
                        taskJsonObject.add("Participants", participants);
                    }
                    taskJsonObject.addProperty("LatestDate", task.getFinishDate());
                    taskJsonObject.addProperty("Status", task.getStatus().string);
                    taskJsonObject.addProperty("Description", task.getDescription());

                    tasks.add("Task#" + j, taskJsonObject);
                }
                organisationJsonObject.add("Tasks", tasks);
            }
            jsonObject.add("Organisation#" + i, organisationJsonObject);
        }
        return writeFile(jsonObject);
    }

    @Override
    public File getFile() {
        return checkFile(accountName);
    }
}
