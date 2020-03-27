/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.fhir2.api.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

import org.apache.commons.lang3.NotImplementedException;
import org.hl7.fhir.r4.model.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FhirConditionServiceImplTest {
	
	private static final String BAD_CONDITION_UUID = "90378769-f1a4-46af-b08b-d9fe8a09034j";
	
	private FhirConditionServiceImpl conditionService;
	
	private Condition condition;
	
	@Before
	public void setup() {
		conditionService = new FhirConditionServiceImpl();
		
		condition = new Condition();
		condition.setId(BAD_CONDITION_UUID);
	}
	
	@Test(expected = NotImplementedException.class)
	public void getConditionByUuid_shouldNotImplementedException() {
		assertThat(conditionService.getConditionByUuid(BAD_CONDITION_UUID), nullValue());
	}
	
	@Test(expected = NotImplementedException.class)
	public void searchConditions_shouldNotImplementedException() {
		assertThat(conditionService.searchConditions(null, null, null, null, null, null, null, null), nullValue());
	}
	
	@Test(expected = NotImplementedException.class)
	public void saveCondition_shouldNotImplementedException() {
		assertThat(conditionService.saveCondition(condition), nullValue());
	}
	
}
