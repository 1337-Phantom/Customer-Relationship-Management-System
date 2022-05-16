package vip.phantom.system.contact;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Title {

    MR("Herr"), MS("Frau"), DIVERS("Divers"), DR("Doktor"), PROF("Professor");
    private final String string;

    public static Title getTitleFromString(String string) {
        for (Title value : values()) {
            if (value.string.equals(string)){
                return value;
            }
        }
        return DIVERS;
    }
}
