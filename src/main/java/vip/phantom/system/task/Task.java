package vip.phantom.system.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vip.phantom.system.Account;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Task {

    public String headline;

    public Account creator;
    public LocalDate entryDate;

    public List<Account> participants = new ArrayList<>();
    public LocalDate latestDate;

    public TaskStatus status;

    public String description;

    public void addParticipant(Account account) {
        participants.add(account);
    }

    public String getEntryDate() {
        return entryDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public String getFinishDate() {
        return latestDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    public Long getDaysLeft() {
        return ChronoUnit.DAYS.between(LocalDate.now(), latestDate);
    }


    public Task() {

    }

    public Task(String headline, Account creator, LocalDate entryDate, LocalDate latestDate, TaskStatus status, String description) {
        this.headline = headline;
        this.creator = creator;
        this.entryDate = entryDate;
        this.latestDate = latestDate;
        this.status = status;
        this.description = description;
    }

}
