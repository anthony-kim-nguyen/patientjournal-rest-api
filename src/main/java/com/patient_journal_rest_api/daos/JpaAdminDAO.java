package com.patient_journal_rest_api.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.patient_journal_rest_api.models.Users.Employees.JpaAdmin;

@Repository
public interface JpaAdminDAO extends CrudRepository<JpaAdmin, Long>{
    
}
