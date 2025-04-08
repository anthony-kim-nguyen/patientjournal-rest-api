package com.patient_journal_rest_api.services;

import com.patient_journal_rest_api.daos.JpaCarePlanDAO;
import com.patient_journal_rest_api.daos.JpaPatientDAO;
import com.patient_journal_rest_api.mappers.CarePlanMapper;
import com.patient_journal_rest_api.models.CarePlan.JpaCarePlan;
import com.patient_journal_rest_api.models.Users.Patient.JpaPatient;
import org.hl7.fhir.r4.model.CarePlan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CarePlanService {

    private final JpaCarePlanDAO carePlanDAO;
    private final JpaPatientDAO patientDAO;

    public CarePlanService(JpaCarePlanDAO carePlanDAO, JpaPatientDAO patientDAO) {
        this.carePlanDAO = carePlanDAO;
        this.patientDAO = patientDAO;
    }

    // ✅ Create a new CarePlan
    public CarePlan createFhirCarePlan(CarePlan fhirCarePlan, Long patientId) {
        JpaCarePlan jpaCarePlan = CarePlanMapper.mapFhirToJpa(fhirCarePlan);
        JpaPatient patient = patientDAO.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        jpaCarePlan.setPatient(patient);
        JpaCarePlan savedCarePlan = carePlanDAO.save(jpaCarePlan); // Save JSONB goals & activities correctly
        return CarePlanMapper.mapJpaToFhir(savedCarePlan);
    }

    // ✅ Get a CarePlan by ID
    public CarePlan getFhirCarePlanById(Long id) {
        return carePlanDAO.findById(id)
                .map(CarePlanMapper::mapJpaToFhir)
                .orElse(null);
    }

    // ✅ Get all CarePlans
    public List<CarePlan> getAllFhirCarePlans() {
        return StreamSupport.stream(carePlanDAO.findAll().spliterator(), false)
                .map(CarePlanMapper::mapJpaToFhir)
                .collect(Collectors.toList());
    }

    // ✅ Update an existing CarePlan
    public CarePlan updateFhirCarePlan(Long id, CarePlan updatedFhirCarePlan) {
        Optional<JpaCarePlan> existingCarePlanOpt = carePlanDAO.findById(id);
        if (existingCarePlanOpt.isEmpty()) {
            throw new RuntimeException("CarePlan not found");
        }

        JpaCarePlan existingCarePlan = existingCarePlanOpt.get();
        JpaCarePlan updatedJpaCarePlan = CarePlanMapper.mapFhirToJpa(updatedFhirCarePlan);
        updatedJpaCarePlan.setId(existingCarePlan.getId());

        return CarePlanMapper.mapJpaToFhir(carePlanDAO.save(updatedJpaCarePlan));
    }

    // ✅ Delete a CarePlan
    public boolean deleteCarePlan(Long id) {
        if (carePlanDAO.existsById(id)) {
            carePlanDAO.deleteById(id);
            return true;
        }
        return false;
    }
}
