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

import org.hl7.fhir.r4.model.ServiceRequest;
import org.openmrs.TestOrder;

public interface ServiceRequestTranslator<T> extends ToFhirTranslator<T, ServiceRequest> {
	
	/**
	 * Maps a {@link TestOrder} to a {@link ServiceRequest}
	 * 
	 * @param order the OpenMRS Order to translate
	 * @return the corresponding FHIR ServiceRequest
	 */
	@Override
	ServiceRequest toFhirResource(T order);
}
