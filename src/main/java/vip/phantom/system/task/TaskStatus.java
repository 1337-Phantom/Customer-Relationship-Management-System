package vip.phantom.system.task;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TaskStatus {
    PENDING("Ausstehend"), INPROGRESS("In Bearbeitung"), COMPLETED("Abgeschlossen");

    public final String string;
}
