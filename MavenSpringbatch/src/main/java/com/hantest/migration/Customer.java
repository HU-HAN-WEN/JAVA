package com.hantest.migration;

import java.util.Date;

// --- 來源 Table 1: Customer ---
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private Date birthday;
    // Getters and Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public Date getBirthday() { return birthday; }
    public void setBirthday(Date birthday) { this.birthday = birthday; }
}