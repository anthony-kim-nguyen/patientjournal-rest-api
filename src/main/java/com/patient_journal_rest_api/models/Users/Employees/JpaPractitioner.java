package com.patient_journal_rest_api.models.Users.Employees;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "practitioners")
@Entity
public class JpaPractitioner extends JpaEmployee {

    @Column(name = "gender")
    private String gender;
    @Column(name = "birth_date")
    private LocalDate birthDate;

    /*
    //create
{
  "resourceType": "Practitioner",
  "id": "12345",
  "identifier": [
    {
      "system": "http://hospital-system.com/employee-id",
      "value": "EMP-6789"
    }
  ],
  "name": [
    {
      "family": "Doe",
      "given": ["John"],
      "prefix": ["Dr."],
      "text": "Dr. John Doe"
    }
  ],
  "birthDate": "1985-08-15",
  "gender": "male",
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
      "use": "work"
    },
    {
      "system": "email",
      "value": "johndoe@example.com",
      "use": "work"
    }
  ]
}

//sample get all
fetch("https://1luu4uw639.execute-api.us-west-1.amazonaws.com/Prod/api/practitioners", {
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
