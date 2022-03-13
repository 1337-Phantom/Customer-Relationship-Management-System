/*
 * Copyright (c) 2022, for customer relationship management system by Yorck Heilmann.
 * All rights reserved.
 */

package vip.phantom.system.user_interface.screens.main_screen;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Categories {
    CUSTOMERS("CustomersIcon"),
//    CONTACT("ContactIcon")
    ;
    private String pictureName;
}

