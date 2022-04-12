package vip.phantom.system.contract;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.system.contact.Contact;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Contract {
    @Setter
    private int contractNumber;
    @Getter
    private String headline;
    @Getter
    private Contact customer;
    private ContractStatus status;

    private LocalDate startDate, deliveryDate;

    @Setter @Getter
    private String description = "";

    @Setter
    private float price;

    public Contract(String headline, Contact customer, int timeForContractInMonth) {
        this(headline, customer, LocalDate.now().plusMonths(timeForContractInMonth));
    }

    public Contract(String headline, Contact customer, LocalDate deliveryDate) {
        this.headline = headline;
        this.customer = customer;
        this.status = ContractStatus.APPROVAL;
        this.startDate = LocalDate.now();
        this.deliveryDate = deliveryDate;
    }

    public String getContractNumber() {
        int longestNumberLength = String.valueOf(ContractManager.INSTANCE.getContractList().size()).length();
        int numberLength = String.valueOf(contractNumber).length();
        return "0".repeat(Math.max(0, longestNumberLength - numberLength)) + contractNumber;
    }

    public String getPrice() {
        return String.format("%.2f", price) + "€";
    }

    public String getStatusAsString() {
        return status.getString();
    }

    public String getStartDate() {
        return startDate.toString();
    }

    public String getDeliveryDate() {
        return deliveryDate.toString();
    }

    public String getDaysLeft() {
        return String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), deliveryDate));
    }
}
