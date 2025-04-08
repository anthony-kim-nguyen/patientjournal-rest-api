package com.patient_journal_rest_api.models.CarePlan.SimpleJson;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleCarePlanActivity {

    // Ex: "goal-1"
    private String id;

    // Ex: "Lower blood sugar to normal range"
    private String description;

    // Ex: "in-progress"
    private String status;
}
