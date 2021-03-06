package org.openmrs.module.docsanddrawing.fragment.controller;

import org.openmrs.Encounter;
import org.openmrs.module.docsanddrawing.web.controller.parser.ParsedObs;
import org.openmrs.module.docsanddrawing.web.controller.parser.Parser;
import org.openmrs.ui.framework.SimpleObject;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * get encounter details JSON in fragment
 */
public class DrawingDetailsFragmentController {
	
	public static final String OBSERVATIONS = "obs";
	
	public static final String DRAWING = "drawing";
	
	public SimpleObject getEncounterDetails(@RequestParam("encounterId") Encounter encounter) {
		ParsedObs parsedObs = Parser.parseObservations(encounter);
		
		return SimpleObject.create(DRAWING, parsedObs.getDrawing(), OBSERVATIONS, parsedObs.getObs());
	}
}
