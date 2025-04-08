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
public class SimpleHumanName {
    //last name
    private String family;

    //first name(s)
    private String given;

    //prefix
    private String prefix;
    //full name
    private String text;
}
