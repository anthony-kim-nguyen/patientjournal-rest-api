package com.patient_journal_rest_api.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient_journal_rest_api.models.Users.Employees.JpaPractitioner;
import com.patient_journal_rest_api.models.SimpleJson.SimpleAddress;
import com.patient_journal_rest_api.models.SimpleJson.SimpleHumanName;
import com.patient_journal_rest_api.models.SimpleJson.SimpleTelecom;
import org.hl7.fhir.r4.model.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PractitionerMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // ✅ Convert JpaPractitioner → FHIR Practitioner
    public static Practitioner mapJpaToFhir(JpaPractitioner jpaPractitioner) {
        if (jpaPractitioner == null) {
            return null;
        }

        Practitioner fhirPractitioner = new Practitioner();
        fhirPractitioner.setId(String.valueOf(jpaPractitioner.getEmployeeId()));

        // Name
        HumanName name = new HumanName();
        if (jpaPractitioner.getHumanNameJson() != null) {
            try {
                SimpleHumanName simpleHumanName = objectMapper.readValue(jpaPractitioner.getHumanNameJson(), SimpleHumanName.class);
                fhirPractitioner.addName(HumanNameMapper.mapSimpleToFhir(simpleHumanName));
            } catch (IOException e) {
                // Handle JSON parsing error
                e.printStackTrace();
            }
        }

        // Birthdate
        if (jpaPractitioner.getBirthDate() != null) {
            fhirPractitioner.setBirthDate(java.sql.Date.valueOf(jpaPractitioner.getBirthDate()));
        }

        // Gender
        fhirPractitioner.setGender(org.hl7.fhir.r4.model.Enumerations.AdministrativeGender.fromCode(jpaPractitioner.getGender()));

        // Address
        if (jpaPractitioner.getAddresses() != null) {
            List<Address> fhirAddresses = jpaPractitioner.getAddresses().stream()
                    .map(AddressMapper::mapSimpleToFhir)
                    .collect(Collectors.toList());
            fhirPractitioner.setAddress(fhirAddresses);
        }
        //  Convert Telecom (Contact Info) - Now handles all types
        if (jpaPractitioner.getTelecoms() != null) {

            List<SimpleTelecom> simpleTelecoms = jpaPractitioner.getTelecoms();

            List<ContactPoint> fhirTelecoms = simpleTelecoms.stream()
                    .map(TelecomMapper::mapSimpleToFhir)
                    .collect(Collectors.toList());

            fhirPractitioner.setTelecom(fhirTelecoms);

        }

        return fhirPractitioner;
    }

    // ✅ Convert FHIR Practitioner → JpaPractitioner
    public static JpaPractitioner mapFhirToJpa(Practitioner fhirPractitioner) {
        if (fhirPractitioner == null) {
            return null;
        }

        JpaPractitioner jpaPractitioner = new JpaPractitioner();

        jpaPractitioner.setEmployeeId(Long.parseLong(fhirPractitioner.getIdElement().getIdPart()));

        // ✅ Set Name
        if (fhirPractitioner.hasName()) {
            SimpleHumanName simpleHumanName = HumanNameMapper.mapFhirToSimple(fhirPractitioner.getNameFirstRep());
            try {
                jpaPractitioner.setHumanNameJson(objectMapper.writeValueAsString(simpleHumanName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (fhirPractitioner.hasBirthDate()) {
            jpaPractitioner.setBirthDate(fhirPractitioner.getBirthDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
        }

        if (fhirPractitioner.hasGender()) {
            jpaPractitioner.setGender(fhirPractitioner.getGender().toCode());
        }
        // Set Addresses
        if (fhirPractitioner.hasAddress()) {
            List<SimpleAddress> addresses = fhirPractitioner.getAddress().stream()
                    .map(fhirAddress -> new SimpleAddress(
                            String.join(", ", fhirAddress.getLine().stream().map(org.hl7.fhir.r4.model.StringType::getValue).collect(Collectors.toList())),
                            fhirAddress.getCity(),
                            fhirAddress.getState(),
                            fhirAddress.getPostalCode(),
                            fhirAddress.getCountry()
                    ))
                    .collect(Collectors.toList());

            try {
                jpaPractitioner.setAddressJson(objectMapper.writeValueAsString(addresses));  // ✅ Store as JSON
            } catch (IOException e) {
                jpaPractitioner.setAddressJson(null);
            }
        }

        // Set Telecoms
        if (fhirPractitioner.hasTelecom()) {
            List<SimpleTelecom> simpleTelecoms = fhirPractitioner.getTelecom().stream()
                    .map(TelecomMapper::mapFhirToSimple)
                    .collect(Collectors.toList());

            try {
                String telecomJson = objectMapper.writeValueAsString(simpleTelecoms);
                jpaPractitioner.setTelecomJson(telecomJson);  // Store as JSON string
            } catch (IOException e) {
                e.printStackTrace();
                jpaPractitioner.setTelecomJson(null);  // Handle error case
            }
        }

        return jpaPractitioner;
    }
}
