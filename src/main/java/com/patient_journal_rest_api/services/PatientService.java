package com.patient_journal_rest_api.services;

import com.patient_journal_rest_api.daos.JpaPatientDAO;
import com.patient_journal_rest_api.mappers.PatientMapper;
import com.patient_journal_rest_api.models.Users.Patient.JpaPatient;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {

    private final JpaPatientDAO patientDAO;

    public PatientService(JpaPatientDAO patientDAO) {
        this.patientDAO = patientDAO;
    }

    //  CREATE Patient
    public Patient createFhirPatient(Patient fhirPatient) {
        //convert to our model
        JpaPatient savedPatient = PatientMapper.mapFhirToJpa(fhirPatient);
        //save to db
        patientDAO.save(savedPatient);
        //return in FHIR format
        return PatientMapper.mapJpaToFhir(savedPatient);
    }

    //  READ: Get All Patients
    public List<Patient> getAllFhirPatients() {
        // Get all Patients
        Iterable<JpaPatient> jpaPatients = patientDAO.findAll();
        // Create a Fhir list
        List<Patient> fhirPatients = new ArrayList<>();

        // Convert entity to fhir
        for (JpaPatient jpaPatient : jpaPatients) {
            fhirPatients.add(PatientMapper.mapJpaToFhir(jpaPatient));
        }
        //return fhir list
        return fhirPatients;
    }


    //  READ: Get a Patient by ID
    public Patient getFhirPatientById(Long id) {
        // Get Patient
        Optional<JpaPatient> optionalPatient = patientDAO.findById(id);
        //  Ensure Patient exists before mapping
        return optionalPatient.map(PatientMapper::mapJpaToFhir).orElse(null);
    }


    //  UPDATE Patient
    public Patient updateFhirPatient(Long id, Patient updatedFhirPatient) {
        // Retrieve existing patient
        JpaPatient existingJpaPatient = patientDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        // Convert FHIR Patient → JPA Patient (Merge Changes)
        JpaPatient updatedJpaPatient = PatientMapper.mapFhirToJpa(updatedFhirPatient);

        // Keep original ID (prevent overwriting)
        updatedJpaPatient.setId(existingJpaPatient.getId());

        // Save and convert back to FHIR
        JpaPatient savedPatient = patientDAO.save(updatedJpaPatient);
        return PatientMapper.mapJpaToFhir(savedPatient);
    }


    //  DELETE Patient
    public void deleteFhirPatient(Long id) {
        if (!patientDAO.existsById(id)) {
            throw new RuntimeException("Patient not found");
        }
        patientDAO.deleteById(id);
    }
}
