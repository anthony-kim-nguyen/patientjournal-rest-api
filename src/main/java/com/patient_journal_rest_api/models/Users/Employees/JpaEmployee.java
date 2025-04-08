package com.patient_journal_rest_api.models.Users.Employees;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient_journal_rest_api.models.SimpleJson.SimpleAddress;
import com.patient_journal_rest_api.models.SimpleJson.SimpleHumanName;
import com.patient_journal_rest_api.models.SimpleJson.SimpleTelecom;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "employees")
public class JpaEmployee {
    @Id
    @Column(name = "employee_id")
    private long employeeId;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "human_name", columnDefinition = "jsonb")
    private String humanNameJson;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "addresses", columnDefinition = "jsonb")
    private String addressJson;

    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "telecom", columnDefinition = "jsonb")
    private String telecomJson;

    private static final ObjectMapper objectMapper = new ObjectMapper();


    // Transient Getter for `SimpleHumanName`
    @Transient
    public SimpleHumanName getHumanName() {
        try {
            return objectMapper.readValue(humanNameJson, SimpleHumanName.class);
        } catch (IOException e) {
            return null;
        }
    }

    // Transient Setter for `SimpleHumanName`
    @Transient
    public void setHumanName(SimpleHumanName humanName) {
        try {
            this.humanNameJson = objectMapper.writeValueAsString(humanName);
        } catch (IOException e) {
            this.humanNameJson = null;
        }
    }
    @Transient
    public List<SimpleAddress> getAddresses() {
        try {

            return objectMapper.readValue(addressJson, new TypeReference<List<SimpleAddress>>() {});
        } catch (IOException e) {
            return null;
        }
    }


    // Convert List<Address> to JSON
    @Transient
    public void setAddresses(List<SimpleAddress> addresses) {
        try {
            this.addressJson = objectMapper.writeValueAsString(addresses);
        } catch (IOException e) {
            this.addressJson = null;
        }
    }


    @Transient
    public List<SimpleTelecom> getTelecoms() {
        try {
            return objectMapper.readValue(telecomJson, new TypeReference<List<SimpleTelecom>>() {});
        } catch (IOException e) {
            return null;
        }
    }

    // Convert List<Telecom> to JSON
    @Transient
    public void setTelecoms(List<SimpleTelecom> telecoms) {
        try {
            this.telecomJson = objectMapper.writeValueAsString(telecoms);
        } catch (IOException e) {
            this.telecomJson = null;
        }
    }


}
