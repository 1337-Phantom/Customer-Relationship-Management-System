package vip.phantom.system.contact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Contact {

    private String accountName;

    /* Contact information */
    private Title title;
    private String name, secondName, familyName;
    private String eMail;
    private String phoneNumber, mobilePhoneNumber;

    /* Address information */
    private String streetAndNumber;
    private int postalCode;
    private String city, country;

    public Contact(String familyName) {
        this.familyName = familyName;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (title == Title.DR || title == Title.PROF) {
            sb.append(title.getString()).append(" ");
        }
        if (name != null) {
            sb.append(name).append(" ");
        }
        if (secondName != null) {
            sb.append(secondName).append(" ");
        }
        if (familyName != null) {
            sb.append(familyName);
        }
        return sb.toString();
    }
}
