/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vip.phantom.system.user_interface.screens.Screen;
import vip.phantom.system.user_interface.screens.main_screen.calendar.CalendarScreen;
import vip.phantom.system.user_interface.screens.main_screen.contract.ContractsScreen;
import vip.phantom.system.user_interface.screens.main_screen.contact.ContactsScreen;
import vip.phantom.system.user_interface.screens.main_screen.home.HomeScreen;
import vip.phantom.system.user_interface.screens.main_screen.tasks.TasksScreen;

@AllArgsConstructor
@Getter
public enum Category {
    HOME("HomeIcon", new HomeScreen()),
    CONTACTS("ContactsIcon", new ContactsScreen()),
    CONTRACTS("ContractsIcon", new ContractsScreen()),
    CALENDAR("CalendarIcon", new CalendarScreen()),
    TASKS("TasksIcon", new TasksScreen())
    ;

    private String pictureName;
    private Screen categoryScreen;
}

