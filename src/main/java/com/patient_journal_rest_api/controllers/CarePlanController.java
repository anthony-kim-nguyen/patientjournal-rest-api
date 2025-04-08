package com.patient_journal_rest_api.controllers;

import com.patient_journal_rest_api.services.CarePlanService;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/careplans")
public class CarePlanController {

    private final CarePlanService carePlanService;
    private final FhirContext fhirContext;

    public CarePlanController(CarePlanService carePlanService, FhirContext fhirContext) {
        this.carePlanService = carePlanService;
        this.fhirContext = fhirContext;
    }

    // Create a new CarePlan
    @PostMapping(produces = "application/json")
    public ResponseEntity<String> createCarePlan(@RequestBody String fhirJson) {
        IParser parser = fhirContext.newJsonParser();
        CarePlan fhirCarePlan = parser.parseResource(CarePlan.class, fhirJson);

        // Extract patientId from CarePlan.subject.reference
        String subjectReference = fhirCarePlan.getSubject().getReference(); // e.g., "Patient/123"
        if (subjectReference == null || !subjectReference.startsWith("Patient/")) {
            return ResponseEntity.badRequest().body("Invalid or missing patient reference");
        }

        // Parse ID part (after "Patient/")
        String patientIdStr = subjectReference.split("/")[1];
        Long patientId = Long.parseLong(patientIdStr);

        CarePlan savedCarePlan = carePlanService.createFhirCarePlan(fhirCarePlan, patientId);
        return ResponseEntity.ok(parser.encodeResourceToString(savedCarePlan));
    }


    // Retrieve a CarePlan by ID
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> getCarePlanById(@PathVariable Long id) {
        CarePlan fhirCarePlan = carePlanService.getFhirCarePlanById(id);
        if (fhirCarePlan == null) {
            return ResponseEntity.notFound().build();
        }

        IParser parser = fhirContext.newJsonParser().setPrettyPrint(true);
        return ResponseEntity.ok(parser.encodeResourceToString(fhirCarePlan));
    }

    // Get all CarePlans (as a FHIR Bundle)
    @GetMapping(produces = "application/json")
    public ResponseEntity<String> getAllCarePlans() {
        List<CarePlan> carePlans = carePlanService.getAllFhirCarePlans();
        IParser parser = fhirContext.newJsonParser().setPrettyPrint(true);

        // Create a FHIR Bundle for multiple CarePlans
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);
        carePlans.forEach(plan -> bundle.addEntry().setResource(plan));

        return ResponseEntity.ok(parser.encodeResourceToString(bundle));
    }

    // Update a CarePlan
    @PutMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> updateCarePlan(@PathVariable Long id, @RequestBody String fhirJson) {
        IParser parser = fhirContext.newJsonParser();
        CarePlan updatedFhirCarePlan = parser.parseResource(CarePlan.class, fhirJson);

        CarePlan updatedCarePlan = carePlanService.updateFhirCarePlan(id, updatedFhirCarePlan);
        return ResponseEntity.ok(parser.encodeResourceToString(updatedCarePlan));
    }

    // Delete a CarePlan
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCarePlan(@PathVariable Long id) {
        boolean deleted = carePlanService.deleteCarePlan(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
