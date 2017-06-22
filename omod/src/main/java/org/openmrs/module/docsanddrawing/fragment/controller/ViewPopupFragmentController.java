/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.docsanddrawing.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Encounter;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.module.docsanddrawing.ModuleContext;
import org.openmrs.module.appui.UiSessionContext;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.InjectBeans;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;

/**
 *  * Controller for a popup view fragment  
 */
public class ViewPopupFragmentController {
	
	Log log = LogFactory.getLog(getClass());
	
	public void controller(PageRequest pageRequest, FragmentModel model,
	        @RequestParam(value = "patientId", required = false) Patient patient,
	        @RequestParam(value = "encounterId", required = false) Encounter encounter,
	        @RequestParam(value = "drawingUuid", required = false) String drawingUuid, UiSessionContext sessionContext,
	        UiUtils ui, @InjectBeans ModuleContext moduleContext) throws Exception {
		log.debug(getClass().getSimpleName() + ".controller()");
		model.addAttribute("patientId", patient.getId());
		model.addAttribute("encounterId", encounter.getId());
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
		model.addAttribute("encounterTime", sdf.format(encounter.getEncounterDatetime()));
		sdf = new SimpleDateFormat("EEE, d MMM yyyy");
		model.addAttribute("encounterDate", sdf.format(encounter.getEncounterDatetime()));
		model.addAttribute("providerId", sessionContext.getCurrentProvider().getId());
		model.addAttribute("drawingUuid", drawingUuid);
		model.addAttribute("returnUrl",
		    "/openmrs/coreapps/patientdashboard/patientDashboard.page?patientId=" + patient.getId());
	}
	
	public void submit(PageRequest pageRequest, FragmentModel model,
	        @RequestParam(value = "patientId", required = false) Patient patient,
	        @RequestParam(value = "visitId", required = false) Visit visit, UiSessionContext sessionContext, UiUtils ui,
	        @InjectBeans ModuleContext moduleContext) throws Exception {
		log.debug("submit triggered");
	}
}