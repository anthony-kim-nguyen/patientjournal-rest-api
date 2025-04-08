package com.patient_journal_rest_api.controllers;

import com.patient_journal_rest_api.mappers.PractitionerMapper;
import com.patient_journal_rest_api.models.Users.Employees.JpaPractitioner;
import com.patient_journal_rest_api.services.PractitionerService;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/practitioners")
public class PractitionerController {

    private final PractitionerService practitionerService;
    private final FhirContext fhirContext;

    public PractitionerController(PractitionerService practitionerService, FhirContext fhirContext) {
        this.practitionerService = practitionerService;
        this.fhirContext = fhirContext;
    }

    // ✅ Get all Practitioners
    @GetMapping(produces = "application/json")
    public ResponseEntity<String> getAllPractitioners() {
        List<Practitioner> practitioners = practitionerService.getAllPractitioners()
                .stream()
                .map(PractitionerMapper::mapJpaToFhir)
                .collect(Collectors.toList());

        // ✅ Create a FHIR Bundle
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);

        // ✅ Add each practitioner to the bundle
        for (Practitioner practitioner : practitioners) {
            bundle.addEntry().setResource(practitioner);
        }

        // ✅ Convert to FHIR JSON
        IParser parser = fhirContext.newJsonParser().setPrettyPrint(true);
        return ResponseEntity.ok(parser.encodeResourceToString(bundle));
    }


    // ✅ Get Practitioner by ID
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> getPractitionerById(@PathVariable Long id) {
        JpaPractitioner practitioner = practitionerService.getPractitionerById(id);
        if (practitioner == null) {
            return ResponseEntity.notFound().build();
        }

        IParser parser = fhirContext.newJsonParser().setPrettyPrint(true);
        return ResponseEntity.ok(parser.encodeResourceToString(PractitionerMapper.mapJpaToFhir(practitioner)));
    }

    // ✅ Create Practitioner
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> createPractitioner(@RequestBody String fhirJson) {
        IParser parser = fhirContext.newJsonParser();
        Practitioner fhirPractitioner = parser.parseResource(Practitioner.class, fhirJson);

        JpaPractitioner createdPractitioner = practitionerService.createPractitioner(fhirPractitioner);

        return ResponseEntity.ok(parser.setPrettyPrint(true).encodeResourceToString(PractitionerMapper.mapJpaToFhir(createdPractitioner)));
    }

    // ✅ Update Practitioner
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> updatePractitioner(@PathVariable Long id, @RequestBody String fhirJson) {
        IParser parser = fhirContext.newJsonParser();
        Practitioner fhirPractitioner = parser.parseResource(Practitioner.class, fhirJson);

        JpaPractitioner updatedPractitioner = practitionerService.updatePractitioner(id, PractitionerMapper.mapFhirToJpa(fhirPractitioner));

        return ResponseEntity.ok(parser.setPrettyPrint(true).encodeResourceToString(PractitionerMapper.mapJpaToFhir(updatedPractitioner)));
    }

    // ✅ Delete Practitioner
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePractitioner(@PathVariable Long id) {
        practitionerService.deletePractitioner(id);
        return ResponseEntity.noContent().build();
    }
}
