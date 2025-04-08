package com.patient_journal_rest_api.models.Users.Patient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient_journal_rest_api.models.SimpleJson.SimpleAddress;

import com.patient_journal_rest_api.models.CarePlan.JpaCarePlan;
import com.patient_journal_rest_api.models.SimpleJson.SimpleHumanName;
import com.patient_journal_rest_api.models.SimpleJson.SimpleTelecom;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
@Entity
public class JpaPatient {

    //generate unique id's for each patient
    @Id
    @Column(name = "patient_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "human_name", columnDefinition = "jsonb")
    private String humanNameJson;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "gender")
    private String gender;
    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "addresses", columnDefinition = "jsonb")
    private String addressJson;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "telecom", columnDefinition = "jsonb")
    private String telecomJson;

    @Column(name = "care_plans")
    @OneToMany(mappedBy = "patient")
    private List<JpaCarePlan> carePlans;


    private static final ObjectMapper objectMapper = new ObjectMapper();



    // Transient Getter for `SimpleHumanName`
    @Transient
    public SimpleHumanName getHumanName() {
        try {
            return objectMapper.readValue(humanNameJson, SimpleHumanName.class);
        } catch (IOException e) {
            return null;
        }
    }

    // Transient Setter for `SimpleHumanName`
    @Transient
    public void setHumanName(SimpleHumanName humanName) {
        try {
            this.humanNameJson = objectMapper.writeValueAsString(humanName);
        } catch (IOException e) {
            this.humanNameJson = null;
        }
    }
    @Transient
    public List<SimpleAddress> getAddresses() {
        try {
            return objectMapper.readValue(addressJson, new TypeReference<List<SimpleAddress>>() {});
        } catch (IOException e) {
            return null;
        }
    }


    // Convert List<Address> to JSON
    @Transient
    public void setAddresses(List<SimpleAddress> addresses) {
        try {
            this.addressJson = objectMapper.writeValueAsString(addresses);
        } catch (IOException e) {
            this.addressJson = null;
        }
    }


    @Transient
    public List<SimpleTelecom> getTelecoms() {
        try {
            return objectMapper.readValue(telecomJson, new TypeReference<List<SimpleTelecom>>() {});
        } catch (IOException e) {
            return null;
        }
    }

    // Convert List<Telecom> to JSON
    @Transient
    public void setTelecoms(List<SimpleTelecom> telecoms) {
        try {
            this.telecomJson = objectMapper.writeValueAsString(telecoms);
        } catch (IOException e) {
            this.telecomJson = null;
        }
    }



//create
    /*
{
  "resourceType": "Patient",

  "name": [
    {
      "family": "Doe",
      "given": ["John"]
    }
  ],
  "gender": "male",
  "birthDate": "1998-05-15",
  "address": [
    {
      "line": ["123 Main St", "Apt 4B"],
      "city": "San Diego",
      "state": "CA",
      "postalCode": "92122",
      "country": "USA"
    }
  ],
  "telecom": [
    {
      "system": "phone",
      "value": "+1-555-555-5555",
      "use": "home"
    },
    {
      "system": "email",
      "value": "johndoe@example.com",
      "use": "work"
    }
  ]
}
///sample get all
fetch("https://1luu4uw639.execute-api.us-west-1.amazonaws.com/Prod/api/patients", {
  method: "GET",
  headers: {
    "Accept": "application/json"
  }
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error(error));


    */





}
