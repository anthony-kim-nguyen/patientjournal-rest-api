package com.patient_journal_rest_api.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient_journal_rest_api.models.SimpleJson.SimpleAddress;
import com.patient_journal_rest_api.models.Users.Patient.JpaPatient;
import com.patient_journal_rest_api.models.SimpleJson.SimpleHumanName;
import com.patient_journal_rest_api.models.SimpleJson.SimpleTelecom;
import org.hl7.fhir.r4.model.*;

import java.io.IOException;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;


import java.util.List;
import java.util.stream.Collectors;

public class PatientMapper {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    //  Convert JpaPatient → FHIR Patient
    public static Patient mapJpaToFhir(JpaPatient jpaPatient) {
        if (jpaPatient == null) {
            return null;
        }

        Patient fhirPatient = new Patient();

        //  Set ID
        fhirPatient.setId(String.valueOf(jpaPatient.getId()));

        //  Set Name
        if (jpaPatient.getHumanNameJson() != null) {
            try {
                SimpleHumanName simpleHumanName = objectMapper.readValue(jpaPatient.getHumanNameJson(), SimpleHumanName.class);
                fhirPatient.addName(HumanNameMapper.mapSimpleToFhir(simpleHumanName));
            } catch (IOException e) {
                // Handle JSON parsing error
                e.printStackTrace();
            }
        }

        //  Set Birth Date
        if (jpaPatient.getBirthDate() != null) {
            fhirPatient.setBirthDateElement(new DateType(jpaPatient.getBirthDate().toString())); // Converts to YYYY-MM-DD
        }
        //  Set Gender
        if ("male".equalsIgnoreCase(jpaPatient.getGender())) {
            fhirPatient.setGender(Enumerations.AdministrativeGender.MALE);
        } else if ("female".equalsIgnoreCase(jpaPatient.getGender())) {
            fhirPatient.setGender(Enumerations.AdministrativeGender.FEMALE);
        } else {
            fhirPatient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
        }

        // Set Address
        if (jpaPatient.getAddresses() != null) {
            List<Address> fhirAddresses = jpaPatient.getAddresses().stream()
                    .map(AddressMapper::mapSimpleToFhir)
                    .collect(Collectors.toList());
            fhirPatient.setAddress(fhirAddresses);
        }

        //  Convert Telecom (Contact Info) - Now handles all types
        if (jpaPatient.getTelecoms() != null) {

            List<SimpleTelecom> simpleTelecoms = jpaPatient.getTelecoms();

            List<ContactPoint> fhirTelecoms = simpleTelecoms.stream()
                    .map(TelecomMapper::mapSimpleToFhir)
                    .collect(Collectors.toList());

            fhirPatient.setTelecom(fhirTelecoms);

        }

        return fhirPatient;
    }

    //  Convert FHIR Patient → JpaPatient
    public static JpaPatient mapFhirToJpa(Patient fhirPatient) {
        if (fhirPatient == null) {
            return null;
        }

        JpaPatient jpaPatient = new JpaPatient();

        // ✅ Set ID (if exists)
        if (fhirPatient.hasId()) {
            jpaPatient.setId(Long.parseLong(fhirPatient.getIdElement().getIdPart()));
        }

        // ✅ Set Name
        if (fhirPatient.hasName()) {
            SimpleHumanName simpleHumanName = HumanNameMapper.mapFhirToSimple(fhirPatient.getNameFirstRep());
            try {
                jpaPatient.setHumanNameJson(objectMapper.writeValueAsString(simpleHumanName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Set Birth Date
        if (fhirPatient.hasBirthDate()) {
            jpaPatient.setBirthDate(LocalDate.parse(fhirPatient.getBirthDateElement().getValueAsString(), DATE_FORMATTER));
        }

        // Set Gender
        if (fhirPatient.hasGender()) {
            jpaPatient.setGender(fhirPatient.getGender().toCode());
        }

        // Set Addresses
        if (fhirPatient.hasAddress()) {
            List<SimpleAddress> addresses = fhirPatient.getAddress().stream()
                    .map(fhirAddress -> new SimpleAddress(
                            String.join(", ", fhirAddress.getLine().stream().map(org.hl7.fhir.r4.model.StringType::getValue).collect(Collectors.toList())),
                            fhirAddress.getCity(),
                            fhirAddress.getState(),
                            fhirAddress.getPostalCode(),
                            fhirAddress.getCountry()
                    ))
                    .collect(Collectors.toList());

            try {
                jpaPatient.setAddressJson(objectMapper.writeValueAsString(addresses));  // ✅ Store as JSON
            } catch (IOException e) {
                jpaPatient.setAddressJson(null);
            }
        }

        // Set Telecoms
        if (fhirPatient.hasTelecom()) {
            List<SimpleTelecom> simpleTelecoms = fhirPatient.getTelecom().stream()
                    .map(TelecomMapper::mapFhirToSimple)
                    .collect(Collectors.toList());

            try {
                String telecomJson = objectMapper.writeValueAsString(simpleTelecoms);
                jpaPatient.setTelecomJson(telecomJson);  // Store as JSON string
            } catch (IOException e) {
                e.printStackTrace();
                jpaPatient.setTelecomJson(null);  // Handle error case
            }
        }


        return jpaPatient;
    }

    // ✅ Helper: Convert JPA telecom system to FHIR ContactPointSystem


}
