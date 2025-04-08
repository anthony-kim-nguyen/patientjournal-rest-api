package com.patient_journal_rest_api.mappers;

import com.patient_journal_rest_api.models.CarePlan.SimpleJson.SimpleGoal;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Goal;

public class GoalMapper {

    public static Goal mapSimpleToFhir(SimpleGoal simpleGoal) {
        if (simpleGoal == null) return null;

        Goal fhirGoal = new Goal();
        fhirGoal.setId(simpleGoal.getId());
        fhirGoal.setDescription(new CodeableConcept().setText(simpleGoal.getDescription())); // ✅ Fix: use CodeableConcept

        return fhirGoal;
    }

    public static SimpleGoal mapFhirToSimple(Goal fhirGoal) {
        if (fhirGoal == null) return null;

        SimpleGoal simpleGoal = new SimpleGoal();
        simpleGoal.setId(fhirGoal.getIdElement().getIdPart());
        simpleGoal.setDescription(
                fhirGoal.hasDescription() ? fhirGoal.getDescription().getText() : null
        );

        return simpleGoal;
    }
}
