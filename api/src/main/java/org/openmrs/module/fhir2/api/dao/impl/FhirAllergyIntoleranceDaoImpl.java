/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.fhir2.api.dao.impl;

import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.or;

import javax.inject.Inject;
import javax.inject.Named;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.AllergyIntolerance;
import org.openmrs.AllergenType;
import org.openmrs.Allergy;
import org.openmrs.module.fhir2.FhirConstants;
import org.openmrs.module.fhir2.api.FhirGlobalPropertyService;
import org.openmrs.module.fhir2.api.dao.FhirAllergyIntoleranceDao;
import org.springframework.stereotype.Component;

@Component
@Setter(AccessLevel.PACKAGE)
public class FhirAllergyIntoleranceDaoImpl extends BaseDaoImpl implements FhirAllergyIntoleranceDao {
	
	@Inject
	@Named("sessionFactory")
	private SessionFactory sessionFactory;
	
	@Inject
	private FhirGlobalPropertyService globalPropertyService;
	
	private Map<String, String> severityConceptUuids;
	
	@Override
	public Allergy getAllergyIntoleranceByUuid(String uuid) {
		return (Allergy) sessionFactory.getCurrentSession().createCriteria(Allergy.class).add(eq("uuid", uuid))
		        .uniqueResult();
	}
	
	@Override
	public Collection<Allergy> searchForAllergies(ReferenceParam patientReference, TokenOrListParam category,
	        TokenOrListParam allergen, TokenOrListParam severity, TokenOrListParam manifestationCode,
	        TokenOrListParam clinicalStatus) {
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Allergy.class);
		handlePatientReference(criteria, patientReference, "patient");
		handleAllergenCategory(criteria, "allergen.allergenType", category);
		handleAllergen(criteria, allergen);
		handleSeverity(criteria, severity).ifPresent(criteria::add);
		handleManifestation(criteria, manifestationCode);
		handleBoolean("voided", convertStringStatusToBoolean(clinicalStatus)).ifPresent(criteria::add);
		
		return criteria.list();
	}
	
	private void handleManifestation(Criteria criteria, TokenOrListParam code) {
		if (code != null) {
			criteria.createAlias("reactions", "r");
			criteria.createAlias("r.reaction", "rc");
			
			handleResourceCode(criteria, code, "rc", "rcm", "rcrt");
		}
	}
	
	private void handleAllergen(Criteria criteria, TokenOrListParam code) {
		if (code != null) {
			criteria.createAlias("allergen.codedAllergen", "ac");
			
			handleResourceCode(criteria, code, "ac", "acm", "acrt");
		}
	}
	
	private Optional<Criterion> handleSeverity(Criteria criteria, TokenOrListParam severityParam) {
		if (severityParam == null) {
			return Optional.empty();
		}
		severityConceptUuids = globalPropertyService.getGlobalProperties(FhirConstants.GLOBAL_PROPERTY_MILD,
		    FhirConstants.GLOBAL_PROPERTY_MODERATE, FhirConstants.GLOBAL_PROPERTY_SEVERE,
		    FhirConstants.GLOBAL_PROPERTY_OTHER);
		
		criteria.createAlias("severity", "sc");
		
		return handleOrListParam(severityParam, token -> {
			try {
				AllergyIntolerance.AllergyIntoleranceSeverity severity = AllergyIntolerance.AllergyIntoleranceSeverity
				        .fromCode(token.getValue());
				switch (severity) {
					case MILD:
						return Optional.of(eq("sc.uuid", severityConceptUuids.get(FhirConstants.GLOBAL_PROPERTY_MILD)));
					case MODERATE:
						return Optional.of(eq("sc.uuid", severityConceptUuids.get(FhirConstants.GLOBAL_PROPERTY_MODERATE)));
					case SEVERE:
						return Optional.of(eq("sc.uuid", severityConceptUuids.get(FhirConstants.GLOBAL_PROPERTY_SEVERE)));
					case NULL:
						return Optional.of(eq("sc.uuid", severityConceptUuids.get(FhirConstants.GLOBAL_PROPERTY_OTHER)));
				}
			}
			catch (FHIRException ignored) {}
			return Optional.empty();
		});
	}
	
	private Optional<Criterion> handleAllergenCategory(Criteria criteria, String propertyName,
	        TokenOrListParam categoryParam) {
		if (categoryParam != null) {
			
			Collection<Criterion> criterionCollection = new ArrayList<>();
			
			for (TokenParam token : categoryParam.getValuesAsQueryTokens()) {
				AllergyIntolerance.AllergyIntoleranceCategory category = AllergyIntolerance.AllergyIntoleranceCategory
				        .fromCode(token.getValue());
				
				switch (category) {
					case FOOD:
						criteria.add(eq(propertyName, AllergenType.FOOD));
						break;
					case MEDICATION:
						criteria.add(eq(propertyName, AllergenType.DRUG));
						break;
					case ENVIRONMENT:
						criteria.add(eq(propertyName, AllergenType.ENVIRONMENT));
						break;
					default:
						criteria.add(eq(propertyName, AllergenType.OTHER));
				}
				
				criterionCollection.add((Criterion) criteria);
			}
			return Optional.of(or(toCriteriaArray((Stream<Optional<Criterion>>) criterionCollection)));
			
		}
		
		return Optional.empty();
	}
	
}
