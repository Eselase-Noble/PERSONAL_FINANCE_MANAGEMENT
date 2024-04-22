package org.nobleson.pfms.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;


@Entity
@Table
public class AppUsers {

    @Id
    private Long userID;
    private String surname;
    private String otherName;
    private String username;
    private  String email;
    private Date createdAt;

    private String password;
}
