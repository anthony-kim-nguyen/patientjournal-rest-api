package com.patient_journal_rest_api.services;

import com.patient_journal_rest_api.daos.JpaPractitionerDAO;
import com.patient_journal_rest_api.mappers.PractitionerMapper;
import com.patient_journal_rest_api.models.Users.Employees.JpaPractitioner;

import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PractitionerService {

    private final JpaPractitionerDAO practitionerDAO;

    public PractitionerService(JpaPractitionerDAO practitionerDAO) {
        this.practitionerDAO = practitionerDAO;
    }

    // ✅ Get all Practitioners
    public List<JpaPractitioner> getAllPractitioners() {
        Iterable<JpaPractitioner> practitionersIterable = practitionerDAO.findAll();
        return StreamSupport.stream(practitionersIterable.spliterator(), false)
                .collect(Collectors.toList());
    }


    // ✅ Get Practitioner by ID
    public JpaPractitioner getPractitionerById(Long id) {
        return practitionerDAO.findById(id).orElse(null);
    }

    // ✅ Create Practitioner
    @Transactional
    public JpaPractitioner createPractitioner(Practitioner fhirPractitioner) {
        JpaPractitioner jpaPractitioner = PractitionerMapper.mapFhirToJpa(fhirPractitioner);
        return practitionerDAO.save(jpaPractitioner);
    }

    // ✅ Update Practitioner
    @Transactional
    public JpaPractitioner updatePractitioner(Long id, JpaPractitioner updatedPractitioner) {
        Optional<JpaPractitioner> existing = practitionerDAO.findById(id);
        if (existing.isPresent()) {
            JpaPractitioner practitioner = existing.get();
            practitioner.setHumanName(updatedPractitioner.getHumanName());
            practitioner.setGender(updatedPractitioner.getGender());
            practitioner.setBirthDate(updatedPractitioner.getBirthDate());
            return practitionerDAO.save(practitioner);
        }
        return null;
    }

    // ✅ Delete Practitioner
    @Transactional
    public void deletePractitioner(Long id) {
        practitionerDAO.deleteById(id);
    }
}
