/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.fhir2.api.translators.impl;

import org.hl7.fhir.r4.model.Address;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Location;
import org.openmrs.module.fhir2.FhirConstants;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LocationAddressTranslatorImplTest {
	
	private static final String CITY = "Test city";
	
	private static final String STATE_PROVINCE = "Test province";
	
	private static final String COUNTRY = "Test country";
	
	private static final String POSTAL_CODE = "Test postal code";
	
	private LocationAddressTranslatorImpl translator;
	
	private Location omrsLocation;
	
	@Before
	public void setUp() {
		translator = new LocationAddressTranslatorImpl();
		omrsLocation = new Location();
	}
	
	@Test
	public void shouldTranslateLocationCityVillageToAddressCity() {
		omrsLocation.setCityVillage(CITY);
		org.hl7.fhir.r4.model.Address address = translator.toFhirResource(omrsLocation);
		assertNotNull(address);
		assertNotNull(address.getCity());
		assertEquals(address.getCity(), CITY);
	}
	
	@Test
	public void shouldTranslateLocationProvinceToAddressProvince() {
		omrsLocation.setStateProvince(STATE_PROVINCE);
		org.hl7.fhir.r4.model.Address address = translator.toFhirResource(omrsLocation);
		assertNotNull(address);
		assertNotNull(address.getState());
		assertEquals(address.getState(), STATE_PROVINCE);
		
	}
	
	@Test
	public void shouldTranslateLocationCountryToAddressCountry() {
		omrsLocation.setCountry(COUNTRY);
		org.hl7.fhir.r4.model.Address address = translator.toFhirResource(omrsLocation);
		assertNotNull(address);
		assertNotNull(address.getCountry());
		assertEquals(address.getCountry(), COUNTRY);
		
	}
	
	@Test
	public void shouldTranslateLocationPostalCodeToAddressCode() {
		omrsLocation.setPostalCode(POSTAL_CODE);
		org.hl7.fhir.r4.model.Address address = translator.toFhirResource(omrsLocation);
		assertNotNull(address);
		assertNotNull(address.getPostalCode());
		assertEquals(address.getPostalCode(), POSTAL_CODE);
		
	}
	
	@Test
	public void shouldTranslateAddressCityToLocationCity() {
		org.hl7.fhir.r4.model.Address address = new Address();
		address.setCity(CITY);
		omrsLocation = translator.toOpenmrsType(address);
		assertNotNull(omrsLocation);
		assertNotNull(omrsLocation.getCityVillage());
		assertEquals(omrsLocation.getCityVillage(), CITY);
	}
	
	@Test
	public void shouldTranslateAddressStateToLocationState() {
		org.hl7.fhir.r4.model.Address address = new Address();
		address.setState(STATE_PROVINCE);
		omrsLocation = translator.toOpenmrsType(address);
		assertNotNull(omrsLocation);
		assertNotNull(omrsLocation.getStateProvince());
		assertEquals(omrsLocation.getStateProvince(), STATE_PROVINCE);
	}
	
	@Test
	public void shouldTranslateAddressCountryToLocationCountry() {
		org.hl7.fhir.r4.model.Address address = new Address();
		address.setCountry(COUNTRY);
		omrsLocation = translator.toOpenmrsType(address);
		assertNotNull(omrsLocation);
		assertNotNull(omrsLocation.getCountry());
		assertEquals(omrsLocation.getCountry(), COUNTRY);
	}
	
	@Test
	public void shouldTranslateAddressPostalCodeToLocationPostalCode() {
		org.hl7.fhir.r4.model.Address address = new Address();
		address.setPostalCode(POSTAL_CODE);
		omrsLocation = translator.toOpenmrsType(address);
		assertNotNull(omrsLocation);
		assertNotNull(omrsLocation.getPostalCode());
		assertEquals(omrsLocation.getPostalCode(), POSTAL_CODE);
	}
	
	@Test
	public void shouldAddEtexnsionForAddess1() {
		Location location = new Location();
		location.setAddress1("address 1");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address1"),
		    hasProperty("value", hasProperty("value", equalTo("address 1"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess2() {
		Location location = new Location();
		location.setAddress2("address 2");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address2"),
		    hasProperty("value", hasProperty("value", equalTo("address 2"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess3() {
		Location location = new Location();
		location.setAddress3("address 3");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address3"),
		    hasProperty("value", hasProperty("value", equalTo("address 3"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess4() {
		Location location = new Location();
		location.setAddress4("address 4");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address4"),
		    hasProperty("value", hasProperty("value", equalTo("address 4"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess5() {
		Location location = new Location();
		location.setAddress5("address 5");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address5"),
		    hasProperty("value", hasProperty("value", equalTo("address 5"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess6() {
		Location location = new Location();
		location.setAddress6("address 6");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address6"),
		    hasProperty("value", hasProperty("value", equalTo("address 6"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess7() {
		Location location = new Location();
		location.setAddress7("address 7");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address7"),
		    hasProperty("value", hasProperty("value", equalTo("address 7"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess8() {
		Location location = new Location();
		location.setAddress8("address 8");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address8"),
		    hasProperty("value", hasProperty("value", equalTo("address 8"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess9() {
		Location location = new Location();
		location.setAddress9("address 9");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address9"),
		    hasProperty("value", hasProperty("value", equalTo("address 9"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess10() {
		Location location = new Location();
		location.setAddress10("address 10");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address10"),
		    hasProperty("value", hasProperty("value", equalTo("address 10"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess11() {
		Location location = new Location();
		location.setAddress11("address 11");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address11"),
		    hasProperty("value", hasProperty("value", equalTo("address 11"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess12() {
		Location location = new Location();
		location.setAddress12("address 12");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address12"),
		    hasProperty("value", hasProperty("value", equalTo("address 12"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess13() {
		Location location = new Location();
		location.setAddress13("address 13");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address13"),
		    hasProperty("value", hasProperty("value", equalTo("address 13"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess14() {
		Location location = new Location();
		location.setAddress14("address 14");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address14"),
		    hasProperty("value", hasProperty("value", equalTo("address 14"))));
	}
	
	@Test
	public void shouldAddEtexnsionForAddess15() {
		Location location = new Location();
		location.setAddress15("address 15");
		
		assertThat(translator.toFhirResource(location).getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS)
		        .getExtensionByUrl(FhirConstants.OPENMRS_FHIR_EXT_ADDRESS + "#address15"),
		    hasProperty("value", hasProperty("value", equalTo("address 15"))));
	}
	
}
