package com.patient_journal_rest_api.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.patient_journal_rest_api.models.CarePlan.JpaCarePlan;
import com.patient_journal_rest_api.models.CarePlan.SimpleJson.SimpleCarePlanActivity;
import com.patient_journal_rest_api.models.CarePlan.SimpleJson.SimpleGoal;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Reference;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CarePlanMapper {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Convert JpaCarePlan → FHIR CarePlan
    public static CarePlan mapJpaToFhir(JpaCarePlan jpaCarePlan) {
        if (jpaCarePlan == null) {
            return null;
        }

        CarePlan fhirCarePlan = new CarePlan();
        fhirCarePlan.setId(String.valueOf(jpaCarePlan.getId()));
        fhirCarePlan.setStatus(CarePlan.CarePlanStatus.fromCode(jpaCarePlan.getStatus()));
        fhirCarePlan.setIntent(CarePlan.CarePlanIntent.fromCode(jpaCarePlan.getIntent()));

        // Convert Goals JSON to FHIR Goal References
        if (jpaCarePlan.getGoalsJson() != null) {
            try {
                List<SimpleGoal> simpleGoals = objectMapper.readValue(
                        jpaCarePlan.getGoalsJson(), new TypeReference<List<SimpleGoal>>() {}
                );
                List<Reference> goalReferences = simpleGoals.stream()
                        .map(goal -> new Reference("Goal/" + goal.getId()))
                        .collect(Collectors.toList());
                fhirCarePlan.setGoal(goalReferences);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Convert Activities JSON to FHIR Activities
        if (jpaCarePlan.getActivitiesJson() != null) {
            try {
                List<SimpleCarePlanActivity> simpleActivities = objectMapper.readValue(
                        jpaCarePlan.getActivitiesJson(), new TypeReference<List<SimpleCarePlanActivity>>() {}
                );
                fhirCarePlan.setActivity(simpleActivities.stream()
                        .map(ActivityMapper::mapSimpleToFhir)  // ✅ Correct mapper usage
                        .collect(Collectors.toList()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fhirCarePlan;
    }

    // Convert FHIR CarePlan → JpaCarePlan
    public static JpaCarePlan mapFhirToJpa(CarePlan fhirCarePlan) {
        if (fhirCarePlan == null) {
            return null;
        }

        JpaCarePlan jpaCarePlan = new JpaCarePlan();
        jpaCarePlan.setId(fhirCarePlan.hasId() ? Long.parseLong(fhirCarePlan.getIdElement().getIdPart()) : null);
        jpaCarePlan.setStatus(fhirCarePlan.getStatus().toCode());
        jpaCarePlan.setIntent(fhirCarePlan.getIntent().toCode());

        // Convert Goal References → SimpleGoal list
        if (fhirCarePlan.hasGoal()) {
            List<SimpleGoal> simpleGoals = fhirCarePlan.getGoal().stream()
                    .map(ref -> {
                        SimpleGoal simpleGoal = new SimpleGoal();
                        if (ref.hasReference() && ref.getReference() != null) {
                            String refId = ref.getReference();
                            if (refId.contains("/")) {
                                simpleGoal.setId(refId.split("/")[1]);
                            }
                        } else if (ref.getIdentifier() != null) {
                            simpleGoal.setId(ref.getIdentifier().getValue());
                        }
                        return simpleGoal;
                    })
                    .collect(Collectors.toList());

            try {
                jpaCarePlan.setGoalsJson(objectMapper.writeValueAsString(simpleGoals));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        // Convert FHIR Activities to SimpleCarePlanActivity
        if (fhirCarePlan.hasActivity()) {
            List<SimpleCarePlanActivity> simpleActivities = fhirCarePlan.getActivity().stream()
                    .map(ActivityMapper::mapFhirToSimple)  // ✅ Correct mapper usage
                    .collect(Collectors.toList());

            try {
                jpaCarePlan.setActivitiesJson(objectMapper.writeValueAsString(simpleActivities));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jpaCarePlan;
    }
}
