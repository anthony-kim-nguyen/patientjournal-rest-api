package com.patient_journal_rest_api.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.patient_journal_rest_api.models.Patient;

@Repository
public interface PatientDAO extends CrudRepository<Patient, Long>{
    
}
