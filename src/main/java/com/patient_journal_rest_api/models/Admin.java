package com.patient_journal_rest_api.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "admin")
@Entity
public class Admin {
     //generate unique id's for each admin
     @Id
     @Column
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;
}
