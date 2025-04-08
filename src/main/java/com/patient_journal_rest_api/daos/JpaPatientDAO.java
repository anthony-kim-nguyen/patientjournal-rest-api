package com.patient_journal_rest_api.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.patient_journal_rest_api.models.Users.Patient.JpaPatient;

@Repository
public interface JpaPatientDAO extends CrudRepository<JpaPatient, Long>{
    
}
