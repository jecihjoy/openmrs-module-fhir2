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

import javax.inject.Inject;
import javax.inject.Named;

import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
@Setter(AccessLevel.PACKAGE)
public abstract class FhirBaseDaoImpl {
	
	@Inject
	@Named("sessionFactory")
	private SessionFactory sessionFactory;
	
	public Object getValueByUuid(Object object, String uuid) {
		return (Object) sessionFactory.getCurrentSession().createCriteria(Object.class).add(eq("uuid", uuid)).uniqueResult();
		
	}
}
