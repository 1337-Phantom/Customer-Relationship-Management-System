package vip.phantom.system.task;

import lombok.AllArgsConstructor;
import vip.phantom.system.contact.Title;

@AllArgsConstructor
public enum TaskStatus {
    PENDING("Ausstehend"), INPROGRESS("In Bearbeitung"), COMPLETED("Abgeschlossen");

    public final String string;

    public static TaskStatus getTitleFromString(String string) {
        for (TaskStatus value : values()) {
            if (value.string.equals(string)){
                return value;
            }
        }
        return PENDING;
    }
}
