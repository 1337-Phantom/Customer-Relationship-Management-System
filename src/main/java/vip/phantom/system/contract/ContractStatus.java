package vip.phantom.system.contract;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContractStatus {

    APPROVAL("Freigabe"), DRAFT("Vorlage"), ACCEPTED("Akzeptiert"), PROCESS("In Bearbeitung"), DELIVERED("Abgeschlossen");

    private final String string;
}
