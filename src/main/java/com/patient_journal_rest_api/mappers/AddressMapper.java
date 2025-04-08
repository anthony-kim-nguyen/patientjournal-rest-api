package com.patient_journal_rest_api.mappers;

import com.patient_journal_rest_api.models.SimpleJson.SimpleAddress;
import org.hl7.fhir.r4.model.Address;

public class AddressMapper {

    // Convert SimpleAddress -> FHIR Address
    public static Address mapSimpleToFhir(SimpleAddress simpleAddress) {
        if (simpleAddress == null) return null;

        Address fhirAddress = new Address();
        fhirAddress.addLine(simpleAddress.getLine());
        fhirAddress.setCity(simpleAddress.getCity());
        fhirAddress.setState(simpleAddress.getState());
        fhirAddress.setPostalCode(simpleAddress.getPostalCode());
        fhirAddress.setCountry(simpleAddress.getCountry());

        return fhirAddress;
    }

    // Convert FHIR Address -> SimpleAddress
    public static SimpleAddress mapFhirToSimple(Address fhirAddress) {
        if (fhirAddress == null) return null;

        SimpleAddress simpleAddress = new SimpleAddress();
        simpleAddress.setLine(fhirAddress.hasLine() ? fhirAddress.getLine().get(0).getValue() : null);
        simpleAddress.setCity(fhirAddress.getCity());
        simpleAddress.setState(fhirAddress.getState());
        simpleAddress.setPostalCode(fhirAddress.getPostalCode());
        simpleAddress.setCountry(fhirAddress.getCountry());

        return simpleAddress;
    }

}
