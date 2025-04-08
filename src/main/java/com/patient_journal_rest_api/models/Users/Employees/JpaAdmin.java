package com.patient_journal_rest_api.models.Users.Employees;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admins")
@Entity
public class JpaAdmin extends JpaEmployee {

    @Column
    private String hired;

}
