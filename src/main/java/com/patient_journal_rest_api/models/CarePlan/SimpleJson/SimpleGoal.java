package com.patient_journal_rest_api.models.CarePlan.SimpleJson;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleGoal {

    // Ex: "goal-1"
    private String id;

    // Ex: "Lower blood sugar to normal range"
    private String description;
}
/*
*
*
[
  {
    "id": "goal-1",
    "description": "Lower blood sugar to normal range"
  },
  {
    "id": "goal-2",
    "description": "Improve cardiovascular health"
  }
]

* */