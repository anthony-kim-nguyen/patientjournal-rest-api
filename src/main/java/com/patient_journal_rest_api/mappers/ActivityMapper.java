package com.patient_journal_rest_api.mappers;

import com.patient_journal_rest_api.models.CarePlan.SimpleJson.SimpleCarePlanActivity;
import org.hl7.fhir.r4.model.CarePlan;

public class ActivityMapper {

    public static CarePlan.CarePlanActivityComponent mapSimpleToFhir(SimpleCarePlanActivity simpleActivity) {
        if (simpleActivity == null) return null;

        CarePlan.CarePlanActivityComponent fhirActivity = new CarePlan.CarePlanActivityComponent();

        // Create and set the detail
        CarePlan.CarePlanActivityDetailComponent detail = new CarePlan.CarePlanActivityDetailComponent();

        if (simpleActivity.getId() != null) {
            detail.setId(simpleActivity.getId());
        }

        detail.setDescription(simpleActivity.getDescription());

        if (simpleActivity.getStatus() != null) {
            detail.setStatus(CarePlan.CarePlanActivityStatus.fromCode(simpleActivity.getStatus()));
        }

        fhirActivity.setDetail(detail);

        return fhirActivity;
    }

    public static SimpleCarePlanActivity mapFhirToSimple(CarePlan.CarePlanActivityComponent fhirActivity) {
        if (fhirActivity == null) return null;

        SimpleCarePlanActivity simpleActivity = new SimpleCarePlanActivity();

        if (fhirActivity.hasDetail()) {
            CarePlan.CarePlanActivityDetailComponent detail = fhirActivity.getDetail();

            if (detail.hasId()) {
                simpleActivity.setId(detail.getId());
            }

            simpleActivity.setDescription(detail.getDescription());

            if (detail.hasStatus()) {
                simpleActivity.setStatus(detail.getStatus().toCode());
            }
        }

        return simpleActivity;
    }
}
