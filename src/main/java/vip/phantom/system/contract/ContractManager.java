package vip.phantom.system.contract;

import lombok.Getter;
import vip.phantom.system.contact.ContactManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContractManager {
    public static ContractManager INSTANCE = new ContractManager();

    @Getter
    private List<Contract> contractList = new ArrayList<>();

    public ContractManager() {
        final Contract contract = new Contract("Facharbeit", ContactManager.INSTANCE.getContactList().get(0), LocalDate.of(2022, 6, 15));
        contract.setDescription("Eine Facharbeit über Customer Relationship Management System Schreiben");
        addContract(contract);
        final Contract contract1 = new Contract("Programm AB", ContactManager.INSTANCE.getContactList().get(0), LocalDate.of(2022, 5, 7));
        addContract(contract1);
        contract1.setDescription("Hallo. Ich bin ein kleiner Blindtext. Und zwar schon so lange ich denken kann. Es war nicht leicht zu verstehen, was es bedeutet, ein blinder Text zu sein: Man ergibt keinen Sinn. Wirklich keinen Sinn. Man wird zusammenhangslos eingeschoben und rumgedreht – und oftmals gar nicht erst gelesen. Aber bin ich allein deshalb ein schlechterer Text als andere? Na gut, ich werde nie in den Bestsellerlisten stehen. Aber andere Texte schaffen das auch nicht. Und darum stört es mich nicht besonders blind zu sein. Und sollten Sie diese Zeilen noch immer lesen, so habe ich als kleiner Blindtext etwas geschafft, wovon all die richtigen und wichtigen Texte meist nur träumen.");
    }

    public void addContract(Contract contract) {
        contractList.add(contract);
        contract.setContractNumber(contractList.size());
    }
}
