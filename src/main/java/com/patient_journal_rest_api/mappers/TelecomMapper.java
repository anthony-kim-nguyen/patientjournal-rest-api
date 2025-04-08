package com.patient_journal_rest_api.mappers;

import com.patient_journal_rest_api.models.SimpleJson.SimpleAddress;
import com.patient_journal_rest_api.models.SimpleJson.SimpleTelecom;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.ContactPoint;

public class TelecomMapper {
    // Convert SimpleAddress -> FHIR Address
    public static ContactPoint mapSimpleToFhir(SimpleTelecom simpleTelecom) {
        if (simpleTelecom == null) return null;

        ContactPoint telecom = new ContactPoint();
        telecom.setSystem(mapTelecomSystem(simpleTelecom.getSystem()));
        telecom.setValue(simpleTelecom.getValue());
        telecom.setUse(mapTelecomUse(simpleTelecom.getUse()));

        if (simpleTelecom.getRank() != null) {
            telecom.setRank(simpleTelecom.getRank());
        }

        return telecom;
    }

    // Convert FHIR Address -> SimpleAddress
    public static SimpleTelecom mapFhirToSimple(ContactPoint fhirTelecom) {
        if (fhirTelecom == null) return null;

        SimpleTelecom simpleTelecom = new SimpleTelecom();
        simpleTelecom.setSystem(mapTelecomSystemToJpa(fhirTelecom.getSystem()));
        simpleTelecom.setValue(fhirTelecom.getValue());
        simpleTelecom.setUse(mapTelecomUseToJpa(fhirTelecom.getUse()));

        if (fhirTelecom.hasRank()) {
            simpleTelecom.setRank(fhirTelecom.getRank());
        }

        return simpleTelecom;
    }
    private static ContactPoint.ContactPointSystem mapTelecomSystem(String system) {
        if ("phone".equalsIgnoreCase(system)) {
            return ContactPoint.ContactPointSystem.PHONE;
        } else if ("email".equalsIgnoreCase(system)) {
            return ContactPoint.ContactPointSystem.EMAIL;
        } else if ("fax".equalsIgnoreCase(system)) {
            return ContactPoint.ContactPointSystem.FAX;
        }
        return ContactPoint.ContactPointSystem.OTHER;
    }

    // ✅ Helper: Convert JPA telecom use to FHIR ContactPointUse
    private static ContactPoint.ContactPointUse mapTelecomUse(String use) {
        if ("home".equalsIgnoreCase(use)) {
            return ContactPoint.ContactPointUse.HOME;
        } else if ("work".equalsIgnoreCase(use)) {
            return ContactPoint.ContactPointUse.WORK;
        } else if ("mobile".equalsIgnoreCase(use)) {
            return ContactPoint.ContactPointUse.MOBILE;
        }
        return ContactPoint.ContactPointUse.NULL;
    }

    // ✅ Helper: Convert FHIR ContactPointSystem to JPA String
    private static String mapTelecomSystemToJpa(ContactPoint.ContactPointSystem system) {
        switch (system) {
            case PHONE: return "phone";
            case EMAIL: return "email";
            case FAX: return "fax";
            default: return "other";
        }
    }

    // ✅ Helper: Convert FHIR ContactPointUse to JPA String
    private static String mapTelecomUseToJpa(ContactPoint.ContactPointUse use) {
        switch (use) {
            case HOME: return "home";
            case WORK: return "work";
            case MOBILE: return "mobile";
            default: return "other";
        }
    }
}
