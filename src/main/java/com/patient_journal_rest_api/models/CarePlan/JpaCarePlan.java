package com.patient_journal_rest_api.models.CarePlan;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient_journal_rest_api.models.CarePlan.SimpleJson.SimpleCarePlanActivity;
import com.patient_journal_rest_api.models.CarePlan.SimpleJson.SimpleGoal;
import com.patient_journal_rest_api.models.Users.Patient.JpaPatient;
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
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "care_plans")  // Updated table name for clarity
@Entity
public class JpaCarePlan {

    //  Unique ID for the CarePlan
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  CarePlan status (e.g., "active", "completed")
    @Column(name = "status", nullable = false)
    private String status;

    //  Intent (e.g., "order", "proposal", "plan")
    @Column(name = "intent", nullable = false)
    private String intent;

    //  Date the CarePlan was created
    @Temporal(TemporalType.DATE)
    @Column(name = "created_date")
    private Date createdDate;

    //  Reference to the Patient
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private JpaPatient patient;

    //  Practitioner (Doctor/Nurse) who authored the CarePlan
    @Column(name = "author_id")
    private Long authorId;

    // Category of the CarePlan (e.g., "diabetes-management")
    @Column(name = "category")
    private String category;

    // Store Goals for the careplan
    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "goals", columnDefinition = "jsonb")
    private String goalsJson;

    // Store Activities as JSON
    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "care_plan_activities", columnDefinition = "jsonb")
    private String activitiesJson;


    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Getter for Goals (Convert JSON String → List)
    // ✅ Getter for Goals (Convert JSON String → List<SimpleGoal>)
    @Transient
    public List<SimpleGoal> getGoals() {
        try {
            return objectMapper.readValue(goalsJson, new TypeReference<List<SimpleGoal>>() {});
        } catch (IOException e) {
            return null;
        }
    }

    // ✅ Setter for Goals (Convert List<SimpleGoal> → JSON String)
    @Transient
    public void setGoals(List<SimpleGoal> goals) {
        try {
            this.goalsJson = objectMapper.writeValueAsString(goals);
        } catch (IOException e) {
            this.goalsJson = null;
        }
    }

    // ✅ Getter for Activities (Convert JSON String → List<SimpleCarePlanActivity>)
    @Transient
    public List<SimpleCarePlanActivity> getActivities() {
        try {
            return objectMapper.readValue(activitiesJson, new TypeReference<List<SimpleCarePlanActivity>>() {});
        } catch (IOException e) {
            return null;
        }
    }

    // ✅ Setter for Activities (Convert List<SimpleCarePlanActivity> → JSON String)
    @Transient
    public void setActivities(List<SimpleCarePlanActivity> activities) {
        try {
            this.activitiesJson = objectMapper.writeValueAsString(activities);
        } catch (IOException e) {
            this.activitiesJson = null;
        }
    }

    /*



    {
  "resourceType": "CarePlan",
  "status": "active",
  "intent": "plan",
  "category": [
    {
      "coding": [
        {
          "system": "http://example.org/fhir/careplan-category",
          "code": "diabetes-management",
          "display": "Diabetes Management"
        }
      ]
    }
  ],
  "subject": {
    "reference": "Patient/1",
    "display": "John Doe"
  },
  "author": {
    "reference": "Practitioner/456",
    "display": "Dr. Smith"
  },
  "created": "2025-03-25",
  "activity": [
    {
      "detail": {
        "description": "Daily exercise routine",
        "status": "in-progress"
      }
    },
    {
      "detail": {
        "description": "Monitor blood sugar levels",
        "status": "completed"
      }
    }
  ],
  "goal": [
    {
      "id": "goal-1",
      "description": "Lower blood sugar to normal range"
    },
    {
      "id": "goal-2",
      "description": "Improve cardiovascular health"
    }
  ]
}


//sample get all
fetch("https://1luu4uw639.execute-api.us-west-1.amazonaws.com/Prod/api/careplans", {
  method: "GET",
  headers: {
    "Accept": "application/json"
  }
})
.then(response => response.json())
.then(data => console.log(data))
.catch(error => console.error(error));

    * */
}
