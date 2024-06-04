package com.figstreet.service.rest.v1.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.figstreet.data.users.Users;
import com.figstreet.data.users.UsersID;

import java.sql.Timestamp;

@JacksonXmlRootElement(localName = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersApiData {
    private Users fUser;

    public UsersApiData(Users pUser) {
        this.fUser = pUser;
    }

    public boolean isActive() {
        return this.fUser.isActive();
    }

    public String getEmail() {
        return this.fUser.getEmail();
    }

    public String getDisplayID() {
        return this.fUser.getDisplayID();
    }

    public String getFirstName() {
        return this.fUser.getFirstName();
    }

    public String getMiddleName() {
        return this.fUser.getMiddleName();
    }

    public String getLastName() {
        return this.fUser.getLastName();
    }

    public char[] getPassword() {
        return this.fUser.getPassword();
    }

    public String getPasswordSalt() {
        return this.fUser.getPasswordSalt();
    }

    public Timestamp getPasswordSet() {
        return this.fUser.getPasswordSet();
    }

    public String getConfirmationKey() {
        return this.fUser.getConfirmationKey();
    }


    public Timestamp getConfirmed() {
        return this.fUser.getConfirmed();
    }

    public Timestamp getLastLogin() {
        return this.fUser.getLastLogin();
    }

    public Integer getLoginFailureCount() {
        return this.fUser.getLoginFailureCount();
    }

    public String getInactiveReason() {
        return this.fUser.getInactiveReason();
    }

    public UsersID getRecordID() {
        return this.fUser.getRecordID();
    }

    public Timestamp getAdded() {
        return this.fUser.getAdded();
    }


    public UsersID getAddedBy() {
        return this.fUser.getAddedBy();
    }

    public Timestamp getLastUpdated() {
        return this.fUser.getLastUpdated();
    }

    public UsersID getLastUpdatedBy() {
        return this.fUser.getLastUpdatedBy();
    }

}
