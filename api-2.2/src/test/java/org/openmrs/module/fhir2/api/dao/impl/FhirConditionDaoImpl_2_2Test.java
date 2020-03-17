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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import org.exparity.hamcrest.date.DateMatchers;
import org.hibernate.SessionFactory;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.CodedOrFreeText;
import org.openmrs.Condition;
import org.openmrs.ConditionClinicalStatus;
import org.openmrs.ConditionVerificationStatus;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.module.fhir2.TestFhirSpringConfiguration;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestFhirSpringConfiguration.class, inheritLocations = false)
public class FhirConditionDaoImpl_2_2Test extends BaseModuleContextSensitiveTest {
	
	private static final String CONDITION_UUID = "2cc6880e-2c46-15e4-9038-a6c5e4d22fb7";
	
	private static final String NEW_CONDITION_UUID = "3dd6880e-2c46-15e4-9038-a6c5e4d22gh8";
	
	private static final String EXISTING_CONDITION_UUID = "604953c5-b5c6-4e1e-be95-e37d8f392046";
	
	private static final String WRONG_CONDITION_UUID = "430bbb70-6a9c-4e1e-badb-9d1034b1b5e9";
	
	private static final String CONDITION_INITIAL_DATA_XML = "org/openmrs/module/fhir2/api/dao/impl/FhirConditionDaoImplTest_initial_data.xml";
	
	// This is the UUID for person_id=2.
	private static final String PATIENT_UUID = "da7f524f-27ce-4bb2-86d6-6d1d05312bd5";
	
	private static final String PATIENT_GIVEN_NAME = "Horatio";
	
	private static final String PATIENT_PARTIAL_NAME = "Hor";
	
	private static final String PATIENT_FAMILY_NAME = "Hornblower";
	
	private static final String PATIENT_NOT_FOUND_NAME = "Igor";
	
	private static final String ONSET_DATE_NOT_FOUND = "eq2017-01-13";
	
	private static final String END_REASON = "End reason";
	
	private static final Integer PATIENT_ID = 6;
	
	private static final Integer CONDITION_ID = 9;
	
	// This corresponds to the EXISTING_CONDITION_UUID above.
	private static final String CONDITION_CONCEPT_UUID = "c607c80f-1ea9-4da3-bb88-6276ce8868dd";
	
	@Inject
	@Named("sessionFactory")
	private Provider<SessionFactory> sessionFactoryProvider;
	
	@Inject
	private PatientService patientService;
	
	@Inject
	private ConceptService conceptService;
	
	@Inject
	private FhirConditionDaoImpl_2_2 dao;
	
	@Before
	public void setUp() {
		dao = new FhirConditionDaoImpl_2_2();
		dao.setSessionFactory(sessionFactoryProvider.get());
		executeDataSet(CONDITION_INITIAL_DATA_XML);
	}
	
	@Test
	public void shouldRetrieveConditionByUuid() {
		Condition condition = dao.getConditionByUuid(CONDITION_UUID);
		assertThat(condition, notNullValue());
		assertThat(condition.getUuid(), notNullValue());
		assertThat(condition.getUuid(), equalTo(CONDITION_UUID));
	}
	
	@Test
	public void shouldReturnNullWhenGetConditionByWrongUuid() {
		Condition condition = dao.getConditionByUuid(WRONG_CONDITION_UUID);
		assertThat(condition, nullValue());
	}
	
	public void searchForPatients_shouldReturnConditionByPatientUuid() {
		ReferenceParam patientReference = new ReferenceParam("", PATIENT_UUID);
		ReferenceAndListParam patientList = new ReferenceAndListParam();
		patientList.addValue(new ReferenceOrListParam().add(patientReference));
		Collection<Condition> results = dao.searchForConditions(patientList, null, null, null, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(3));
		assertThat(results.iterator().next().getPatient().getUuid(), equalTo(PATIENT_UUID));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionByPatientGivenName() {
		ReferenceParam patientReference = new ReferenceParam(Patient.SP_GIVEN, PATIENT_GIVEN_NAME);
		ReferenceAndListParam patientList = new ReferenceAndListParam();
		patientList.addValue(new ReferenceOrListParam().add(patientReference));
		Collection<Condition> results = dao.searchForConditions(patientList, null, null, null, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		// TODO during review: Is returning duplicated Conditions the desired outcome?
		assertThat(results.size(), equalTo(6));
		assertThat(results.iterator().next().getPatient().getGivenName(), equalTo(PATIENT_GIVEN_NAME));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionByPatientFamilyName() {
		ReferenceParam patientReference = new ReferenceParam(Patient.SP_FAMILY, PATIENT_FAMILY_NAME);
		ReferenceAndListParam patientList = new ReferenceAndListParam();
		patientList.addValue(new ReferenceOrListParam().add(patientReference));
		Collection<Condition> results = dao.searchForConditions(patientList, null, null, null, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(9));
		assertThat(results.iterator().next().getPatient().getFamilyName(), equalTo(PATIENT_FAMILY_NAME));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionByPatientName() {
		ReferenceParam patientReference = new ReferenceParam(Patient.SP_NAME, PATIENT_PARTIAL_NAME);
		ReferenceAndListParam patientList = new ReferenceAndListParam();
		patientList.addValue(new ReferenceOrListParam().add(patientReference));
		Collection<Condition> results = dao.searchForConditions(patientList, null, null, null, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(9));
		assertThat(results.iterator().next().getPatient().getGivenName(), equalTo(PATIENT_GIVEN_NAME));
		assertThat(results.iterator().next().getPatient().getFamilyName(), equalTo(PATIENT_FAMILY_NAME));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionBySubjectName() {
		ReferenceParam subjectReference = new ReferenceParam(Patient.SP_NAME, PATIENT_PARTIAL_NAME);
		ReferenceAndListParam subjectList = new ReferenceAndListParam();
		subjectList.addValue(new ReferenceOrListParam().add(subjectReference));
		Collection<Condition> results = dao.searchForConditions(null, subjectList, null, null, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(9));
		assertThat(results.iterator().next().getPatient().getGivenName(), equalTo(PATIENT_GIVEN_NAME));
		assertThat(results.iterator().next().getPatient().getFamilyName(), equalTo(PATIENT_FAMILY_NAME));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionByOnsetDate() {
		String testDate = "2017-01-12";
		
		DateParam onsetDate = new DateParam("eq" + testDate);
		Collection<Condition> results = dao.searchForConditions(null, null, null, null, onsetDate, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(3));
		assertThat(results.iterator().next().getOnsetDate().toString(), startsWith(testDate));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionByRecordedDate() {
		String testDate = "2016-01-12";
		
		DateParam recordedDate = new DateParam("eq" + testDate);
		Collection<Condition> results = dao.searchForConditions(null, null, null, null, null, null, recordedDate, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(1));
		assertThat(results.iterator().next().getDateCreated().toString(), startsWith(testDate));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionByClinicalStatusActive() {
		TokenOrListParam listParam = new TokenOrListParam();
		listParam.add(new TokenParam("active"));
		Collection<Condition> results = dao.searchForConditions(null, null, null, listParam, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(5));
		assertThat(results.iterator().next().getClinicalStatus(), equalTo(ConditionClinicalStatus.ACTIVE));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionByClinicalStatusInactive() {
		TokenOrListParam listParam = new TokenOrListParam();
		listParam.add(new TokenParam("inactive"));
		Collection<Condition> results = dao.searchForConditions(null, null, null, listParam, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(1));
		assertThat(results.iterator().next().getClinicalStatus(), equalTo(ConditionClinicalStatus.INACTIVE));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionByClinicalStatusAll() {
		TokenOrListParam listParam = new TokenOrListParam();
		listParam.add(new TokenParam("active"));
		listParam.add(new TokenParam("inactive"));
		Collection<Condition> results = dao.searchForConditions(null, null, null, listParam, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(6));
	}
	
	@Test
	public void searchForPatients_shouldReturnConditionByCode() {
		TokenOrListParam listParam = new TokenOrListParam();
		listParam.add(new TokenParam("CD41003")); // for concept_id=5497
		Collection<Condition> results = dao.searchForConditions(null, null, listParam, null, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(1));
		assertThat(results.iterator().next().getCondition().getCoded().getConceptId(), equalTo(5497));
	}
	
	@Test
	public void searchForPatients_shouldReturnMultipleConditionsByCodeList() {
		TokenOrListParam listParam = new TokenOrListParam();
		listParam.add(new TokenParam("CD41003")); // for concept_id=5497
		listParam.add(new TokenParam("WGT234")); // for concept_id=5089
		Collection<Condition> results = dao.searchForConditions(null, null, listParam, null, null, null, null, null);
		
		assertThat(results, notNullValue());
		assertThat(results, not(empty()));
		assertThat(results.size(), equalTo(2));
	}
	
	public void shouldSaveNewCondition() {
		Condition condition = new Condition();
		condition.setUuid(CONDITION_UUID);
		condition.setOnsetDate(new Date());
		condition.setEndDate(null);
		
		org.openmrs.Patient patient = patientService.getPatient(PATIENT_ID);
		condition.setPatient(patient);
		
		dao.saveCondition(condition);
		Condition result = dao.getConditionByUuid(CONDITION_UUID);
		assertThat(result, notNullValue());
		assertThat(result.getUuid(), equalTo(CONDITION_UUID));
	}
	
	@Test
	public void shouldReturnExistingConditionIfBothAreEquals() throws Exception {
		Condition condition = new Condition();
		condition.setConditionId(CONDITION_ID);
		condition.setUuid(EXISTING_CONDITION_UUID);
		condition.setPatient(patientService.getPatient(PATIENT_ID));
		condition.setClinicalStatus(ConditionClinicalStatus.ACTIVE);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date onsetDate = sdf.parse("2020-03-13 19:00:00");
		condition.setOnsetDate(onsetDate);
		condition.setVerificationStatus(ConditionVerificationStatus.CONFIRMED);
		
		CodedOrFreeText codedOrFreeText = new CodedOrFreeText();
		codedOrFreeText.setCoded(conceptService.getConceptByUuid(CONDITION_CONCEPT_UUID));
		condition.setCondition(codedOrFreeText);
		
		Condition result = dao.saveCondition(condition);
		assertThat(result, notNullValue());
		assertThat(result.getUuid(), equalTo(condition.getUuid()));
		assertThat(result.getClinicalStatus(), equalTo(condition.getClinicalStatus()));
		assertThat(result.getVerificationStatus(), equalTo(condition.getVerificationStatus()));
	}
	
	@Test
	public void shouldUpdateExistingCondition() {
		Condition condition = new Condition();
		condition.setUuid(EXISTING_CONDITION_UUID);
		
		condition.setPatient(patientService.getPatient(PATIENT_ID));
		condition.setOnsetDate(new Date());
		condition.setClinicalStatus(ConditionClinicalStatus.HISTORY_OF);
		
		dao.saveCondition(condition);
		Condition result = dao.getConditionByUuid(EXISTING_CONDITION_UUID);
		
		assertThat(result, notNullValue());
		assertThat(result.getUuid(), equalTo(EXISTING_CONDITION_UUID));
		assertThat(result.getPatient(), equalTo(patientService.getPatient(PATIENT_ID)));
		assertThat(result.getEndDate(), notNullValue());
		assertThat(result.getEndDate(), DateMatchers.sameDay(condition.getOnsetDate()));
	}
	
	@Test
	public void shouldSetConditionVoidedStatusTrue() {
		Condition condition = new Condition();
		condition.setUuid(EXISTING_CONDITION_UUID);
		condition.setPatient(patientService.getPatient(PATIENT_ID));
		condition.setClinicalStatus(ConditionClinicalStatus.ACTIVE);
		condition.setOnsetDate(new Date());
		
		dao.saveCondition(condition);
		Condition result = dao.getConditionByUuid(EXISTING_CONDITION_UUID);
		assertThat(result, notNullValue());
		assertThat(result.getVoided(), is(true));
	}
	
	@Test
	public void shouldSetEndDateIfEndReasonIsNotNull() {
		Condition condition = new Condition();
		condition.setUuid(NEW_CONDITION_UUID);
		condition.setPatient(patientService.getPatient(PATIENT_ID));
		condition.setClinicalStatus(ConditionClinicalStatus.ACTIVE);
		condition.setEndReason(END_REASON);
		
		dao.saveCondition(condition);
		Condition result = dao.getConditionByUuid(NEW_CONDITION_UUID);
		assertThat(result, notNullValue());
		assertThat(result.getEndDate(), notNullValue());
		assertThat(result.getEndDate(), DateMatchers.sameDay(new Date()));
	}
}
