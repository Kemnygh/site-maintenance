package models;

import java.sql.Timestamp;
import java.util.Objects;

public class Engineer {
    private int id;
    private String first_name;
    private String last_name;
    private String eng_no;
    private String phone_no;
    private String email;
    private Timestamp created;
    private Timestamp updated;
    private String deleted;

    public Engineer(String firstName, String lastName, String engNo, String phoneNo, String email){
    this.first_name = firstName;
    this.last_name = lastName;
    this.email = email;
    this.eng_no = engNo;
    this.phone_no = phoneNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Engineer)) return false;
        Engineer engineer = (Engineer) o;
        return getId() == engineer.getId() && getFirst_name().equals(engineer.getFirst_name()) && getLast_name().equals(engineer.getLast_name()) && getEng_no().equals(engineer.getEng_no()) && getPhone_no().equals(engineer.getPhone_no()) && getEmail().equals(engineer.getEmail()) && getCreated().equals(engineer.getCreated()) && Objects.equals(getUpdated(), engineer.getUpdated()) && getDeleted().equals(engineer.getDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirst_name(), getLast_name(), getEng_no(), getPhone_no(), getEmail(), getCreated(), getUpdated(), getDeleted());
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEng_no() {
        return eng_no;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getEmail() {
        return email;
    }

    public Timestamp getCreated() {
        return created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public String getDeleted() {
        return deleted;
    }
}
