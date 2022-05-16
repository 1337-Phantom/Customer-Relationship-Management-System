package vip.phantom.system.task;

import lombok.Getter;
import lombok.Setter;
import vip.phantom.system.CRM;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {


    public static TaskManager INSTANCE = new TaskManager();

    public TaskManager() {
        final Task task = new Task();
        task.setCreator(CRM.getCrm().currentAccount);
        task.setEntryDate(LocalDate.now());
        task.setLatestDate(LocalDate.now().plusWeeks(2));
        task.setStatus(TaskStatus.COMPLETED);
        task.setHeadline("Test");
        task.setDescription("Einfach nur ein einfacher Test");
        addTask(task);

        final Task task1 = new Task();
        task1.setCreator(CRM.getCrm().currentAccount);
        task1.setEntryDate(LocalDate.now());
        task1.setLatestDate(LocalDate.now().plusWeeks(2));
        task1.setStatus(TaskStatus.PENDING);
        task1.setHeadline("Test");
        task1.setDescription("Einfach nur ein einfacher Test");
        addTask(task1);

        final Task task2 = new Task();
        task2.setCreator(CRM.getCrm().currentAccount);
        task2.setEntryDate(LocalDate.now());
        task2.setLatestDate(LocalDate.now().plusWeeks(2));
        task2.setStatus(TaskStatus.INPROGRESS);
        task2.setHeadline("Test");
        task2.setDescription("Einfach nur ein einfacher Test");
        addTask(task2);
    }

    public List<Task> getTaskList() {
        return CRM.getCrm().selectedOrganisation.getTasks();
    }

    public void addTask(Task task) {
        getTaskList().add(task);
    }

    public List<Task> getTasksWithStatus(TaskStatus status) {
        return getTaskList().stream().filter(task -> task.status == status).collect(Collectors.toList());
    }

    public List<Task> getTasksForDay(LocalDate localDate) {
        return getTaskList().stream().filter(task -> localDate.equals(task.getLatestDate())).collect(Collectors.toList());
    }
}
