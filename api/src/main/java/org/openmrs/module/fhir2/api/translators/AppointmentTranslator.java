/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.fhir2.api.translators;

import org.hl7.fhir.r4.model.Appointment;

public interface AppointmentTranslator<T> extends OpenmrsFhirUpdatableTranslator<T, Appointment> {
	
	/**
	 * Maps OpenMRS appointment to {@link org.hl7.fhir.r4.model.Appointment}
	 *
	 * @param appointment the OpenMRS data element to translate
	 * @return the corresponding FHIR Appointment resource
	 */
	@Override
	Appointment toFhirResource(T appointment);
	
	/**
	 * Maps {@link org.hl7.fhir.r4.model.Appointment} to OpenMRS appointment object
	 *
	 * @param existingAppointment
	 * @param appointment the resource to map
	 * @return the updated OpenMRS appointment object
	 */
	@Override
	T toOpenmrsType(T existingAppointment, Appointment appointment);
}
