package com.patient_journal_rest_api.models.SimpleJson;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleTelecom {


        // "phone", "email", "fax"
        private String system;

        // The actual phone number or email
        private String value;

        // "home", "work", "mobile"
        private String use;

        // Priority order ( 1 , 2 , 3. 1 being highest prio)
        private Integer rank;


}
