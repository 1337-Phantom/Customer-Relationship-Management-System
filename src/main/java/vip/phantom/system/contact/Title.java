package vip.phantom.system.contact;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum Title {

    MR("Mr."), MS("Ms."), DR("Dr."), PROF("Prof.");
    private final String string;
}
