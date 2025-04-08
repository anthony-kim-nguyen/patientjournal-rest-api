package com.patient_journal_rest_api.mappers;

import com.patient_journal_rest_api.models.SimpleJson.SimpleAddress;
import com.patient_journal_rest_api.models.SimpleJson.SimpleHumanName;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.StringType;

import java.util.List;
import java.util.stream.Collectors;

public class HumanNameMapper {
    // Convert simple Address -> Fhir
    public static HumanName mapSimpleToFhir(SimpleHumanName simpleHumanName) {
        if (simpleHumanName == null) return null;

        HumanName fhirHumanName = new HumanName();
        fhirHumanName.setFamily(simpleHumanName.getFamily());

        // Convert CSV string of `given` names to string
        if (simpleHumanName.getGiven() != null && !simpleHumanName.getGiven().isEmpty()) {
            fhirHumanName.addGiven(simpleHumanName.getGiven());
        }

        // prefix
        if (simpleHumanName.getPrefix() != null) {
            fhirHumanName.addPrefix(simpleHumanName.getPrefix());
        }

        // full name
        fhirHumanName.setText(simpleHumanName.getText());

        return fhirHumanName;
    }

    // Convert FHIR Address -> SimpleAddress
    public static SimpleHumanName mapFhirToSimple(HumanName fhirHumanName) {
        if (fhirHumanName == null) return null;

        SimpleHumanName simpleHumanName = new SimpleHumanName();
        simpleHumanName.setFamily(fhirHumanName.getFamily());

        // Convert String of `given` names to CSV string
        if (fhirHumanName.hasGiven()) {
            simpleHumanName.setGiven(
                    fhirHumanName.getGiven().stream()
                            .map(StringType::getValue)
                            .collect(Collectors.joining(", "))
            );
        }

        // Prefix
        if (fhirHumanName.hasPrefix()) {
            simpleHumanName.setPrefix(fhirHumanName.getPrefixAsSingleString());
        }

        // Full Name
        simpleHumanName.setText(fhirHumanName.getText());


        return simpleHumanName;
    }
}
