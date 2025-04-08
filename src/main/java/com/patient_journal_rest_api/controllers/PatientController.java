package com.patient_journal_rest_api.controllers;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.patient_journal_rest_api.services.PatientService;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")  // FHIR-compliant endpoint
public class PatientController {

    private final PatientService patientService;
    private final FhirContext fhirContext = FhirContext.forR4();  // FHIR Parser

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // CREATE Patient
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createPatient(@RequestBody String fhirJson) {
        // initialize parser
        IParser parser = fhirContext.newJsonParser();
        // initialize patient by parsing json request
        Patient fhirPatient = parser.parseResource(Patient.class, fhirJson);
        // use create patient service -> save the fhir representation of our model
        Patient savedPatient = patientService.createFhirPatient(fhirPatient);
        // return fhir response
        return ResponseEntity.ok(parser.setPrettyPrint(true).encodeResourceToString(savedPatient));
    }

    // READ: Get All Patients
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllPatients() {
        List<Patient> patients = patientService.getAllFhirPatients();
        IParser parser = fhirContext.newJsonParser().setPrettyPrint(true);

        // Create a FHIR Bundle
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);

        for (IBaseResource resource : patients) {
            bundle.addEntry().setResource((Resource) resource);
        }

        // Now you can serialize the Bundle
        return ResponseEntity.ok(parser.encodeResourceToString(bundle));
    }


    // READ: Get a Patient by ID
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getPatientById(@PathVariable Long id) {
        Patient fhirPatient = patientService.getFhirPatientById(id);

        if (fhirPatient == null) {
            return ResponseEntity.notFound().build();
        }

        IParser parser = fhirContext.newJsonParser().setPrettyPrint(true);
        return ResponseEntity.ok(parser.encodeResourceToString(fhirPatient));
    }


    // UPDATE Patient
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePatient(@PathVariable Long id, @RequestBody String fhirJson) {
        IParser parser = fhirContext.newJsonParser();

        // Parse the incoming FHIR JSON into a `Patient` object
        Patient updatedFhirPatient = parser.parseResource(Patient.class, fhirJson);

        // Call service layer to update the patient
        Patient updatedPatient = patientService.updateFhirPatient(id, updatedFhirPatient);

        // Convert updated FHIR Patient back to JSON and return response
        return ResponseEntity.ok(parser.setPrettyPrint(true).encodeResourceToString(updatedPatient));
    }


    // DELETE Patient
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {
        patientService.deleteFhirPatient(id);
        return ResponseEntity.ok("{\"message\":\"Patient deleted successfully\"}");
    }
}
