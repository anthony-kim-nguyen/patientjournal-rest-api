package com.patient_journal_rest_api.models.SimpleJson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleAddress {

    private String line;
    private String city;
    private String state;
    private String postalCode;

    private String country;

    /*
    *
    *
    * "address": [
    {
      "line": ["123 Main St"],
      "city": "San Diego",
      "state": "CA",
      "postalCode": "92122",
      "country": "USA"
    }
    * */
}
