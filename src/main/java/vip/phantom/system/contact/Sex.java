package vip.phantom.system.contact;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum Sex {

    MALE("Männlich"), FEMALE("Weiblich"), DIVERS("Divers");

    private String string;
}
