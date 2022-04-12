package vip.phantom.system.contact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
public class Contact {

    private final String noInformation = "N/A";

    private String accountName;

    /* Contact information */
    private Title title;
    private String name, secondName, familyName;
    private LocalDate birthdate;
    private String eMail;
    private String phoneNumber, mobilePhoneNumber;

    /* Address information */
    private String streetAndNumber;
    private int postalCode;
    private String city, country;

    public Contact(String familyName) {
        this.familyName = familyName;
    }

    public Contact(String accountName, Title title, String name, String secondName, String familyName, LocalDate birthdate, String eMail, String phoneNumber, String mobilePhoneNumber, String streetAndNumber, int postalCode, String city, String country) {
        this.accountName = accountName;
        this.title = title;
        this.name = name;
        this.secondName = secondName;
        this.familyName = familyName;
        this.birthdate = birthdate;
        this.eMail = eMail;
        this.phoneNumber = phoneNumber;
        this.mobilePhoneNumber = mobilePhoneNumber;
        this.streetAndNumber = streetAndNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        if (title == Title.DR || title == Title.PROF) {
            sb.append(title.getString()).append(" ");
        }
        if (name != null && !name.equals("")) {
            sb.append(name).append(" ");
        }
        if (secondName != null && !secondName.equals("")) {
            sb.append(secondName).append(" ");
        }
        if (familyName != null) {
            sb.append(familyName);
        }
        return sb.toString();
    }

    public void setBirthdate(String birthdate) {
        String[] dates = birthdate.split("\\.");
        setBirthdate(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
    }

    public void setBirthdate(int day, int month, int year) {
        birthdate = LocalDate.of(year, month, day);
    }

    public String getName() {
        return name != null && !name.equals("") ? name : noInformation;
    }

    public String getSecondName() {
        return secondName != null && !secondName.equals("") ? secondName : noInformation;
    }

    public String getEMail() {
        return eMail != null && !eMail.equals("") ? eMail : noInformation;
    }

    public String getMobilePhoneNumber() {
        return mobilePhoneNumber != null && !mobilePhoneNumber.equals("") ? mobilePhoneNumber : noInformation;
    }

    public String getStreetAndNumber() {
        return streetAndNumber != null && !streetAndNumber.equals("") ? streetAndNumber : noInformation;
    }

    public String getPostalCode() {
        return postalCode != 0 ? String.valueOf(postalCode) : noInformation;
    }

    public String getCity() {
        return city != null && !city.equals("") ? city : noInformation;
    }

    public String getCountry() {
        return country != null && !country.equals("") ? country : noInformation;
    }

    public String getBirthdateAsString() {
        if (birthdate != null) {
            return birthdate.toString();
        }
        return noInformation;
    }

    public String getAge() {
        if (birthdate != null) {
            return String.valueOf(ChronoUnit.YEARS.between(birthdate, LocalDate.now()));
        }
        return noInformation;
    }

    public String getPhoneNumber() {
        return phoneNumber != null && !phoneNumber.equals("") ? phoneNumber : noInformation;
    }
}
