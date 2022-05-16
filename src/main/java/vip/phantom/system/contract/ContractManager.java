package vip.phantom.system.contract;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.system.CRM;
import vip.phantom.system.contact.Contact;
import vip.phantom.system.contact.ContactManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ContractManager {
    public static ContractManager INSTANCE = new ContractManager();

    public ContractManager() {
        final Contract contract = new Contract("Facharbeit", new Contact("Unknown"), LocalDate.of(2022, 6, 15));
        contract.setDescription("Eine Facharbeit über Customer Relationship Management System Schreiben");
        contract.setPrice(1000);
        addContract(contract);
        final Contract contract1 = new Contract("Programm AB", new Contact("Unknown"), LocalDate.of(2022, 5, 7));
        contract1.setPrice(200);
        addContract(contract1);
        final Contract contract2 = new Contract("Programm XY", new Contact("Unknown"), LocalDate.of(2022, 5, 10));
        contract2.setPrice(350);
        addContract(contract2);
        contract2.setDescription("Hallo. Ich bin ein kleiner Blindtext. Und zwar schon so lange ich denken kann. Es war nicht leicht zu verstehen, was es bedeutet, ein blinder Text zu sein: Man ergibt keinen Sinn. Wirklich keinen Sinn. Man wird zusammenhangslos eingeschoben und rumgedreht – und oftmals gar nicht erst gelesen. Aber bin ich allein deshalb ein schlechterer Text als andere? Na gut, ich werde nie in den Bestsellerlisten stehen. Aber andere Texte schaffen das auch nicht. Und darum stört es mich nicht besonders blind zu sein. Und sollten Sie diese Zeilen noch immer lesen, so habe ich als kleiner Blindtext etwas geschafft, wovon all die richtigen und wichtigen Texte meist nur träumen.");
    }

    public List<Contract> getContractList() {
        return CRM.getCrm().selectedOrganisation.getContracts();
    }

    public List<Contract> getContractsForDay(LocalDate day) {
        return getContractList().stream().filter(contract -> contract.getDeliveryDate().equals(day)).collect(Collectors.toList());
    }

    public void addContract(Contract contract) {
        getContractList().add(contract);
        contract.setContractNumber(getContractList().size());
    }
}
