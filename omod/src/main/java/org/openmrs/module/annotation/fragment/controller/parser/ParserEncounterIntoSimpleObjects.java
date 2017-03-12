package org.openmrs.module.annotation.fragment.controller.parser;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.api.LocationService;
import org.openmrs.module.annotation.obs.data.ModuleComplexData;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.emrapi.disposition.DispositionService;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ParserEncounterIntoSimpleObjects {
	
	private Encounter encounter;
	
	private UiUtils uiUtils;
	
	private EmrApiProperties emrApiProperties;
	
	private LocationService locationService;
	
	private DispositionService dispositionService;
	
	public ParserEncounterIntoSimpleObjects(Encounter encounter, UiUtils uiUtils, EmrApiProperties emrApiProperties,
	    LocationService locationService, DispositionService dispositionService) {
		this.encounter = encounter;
		this.uiUtils = uiUtils;
		this.emrApiProperties = emrApiProperties;
		this.locationService = locationService;
		this.dispositionService = dispositionService;
	}
	
	//	public List<SimpleObject> parseOrders() {
	//		List<SimpleObject> orders = new ArrayList<SimpleObject>();
	//
	//		// hacky reflection to allow core 1.9 compatibility, which doesn't support order.orderNumber
	//		Method getOrderNumber = null;
	//		for (Method m : new Order().getClass().getMethods()) {
	//			if (m.getName().equals("getOrderNumber")) {
	//				getOrderNumber = m;
	//				break;
	//			}
	//		}
	//
	//		for (Order order : encounter.getOrders()) {
	//			try {
	//				orders.add(SimpleObject.create("concept", uiUtils.format(order.getConcept()), "orderNumber",
	//				    uiUtils.format(getOrderNumber != null ? getOrderNumber.invoke(order) : order.getAccessionNumber()))); // prior to 1.10 we display accession number, 1.10+ we display order number
	//			}
	//			// fail softly for now
	//			catch (IllegalAccessException e) {}
	//			catch (InvocationTargetException e) {}
	//		}
	//
	//		return orders;
	//	}
	
	public ParsedObs parseObservations(Locale locale) {
		//		DiagnosisMetadata diagnosisMetadata = emrApiProperties.getDiagnosisMetadata();
		//		DispositionDescriptor dispositionDescriptor = null;
		//		try {
		//			dispositionDescriptor = dispositionService.getDispositionDescriptor();
		//		}
		//		catch (IllegalStateException ex) {
		//			// No problem. We do not require dispositions to be configured here
		//		}
		
		ParsedObs parsedObs = new ParsedObs();
		
		for (Obs obs : encounter.getObsAtTopLevel(false)) {
			//			if (diagnosisMetadata.isDiagnosis(obs)) {
			//				parsedObs.getDiagnoses().add(parseDiagnosis(diagnosisMetadata, obs));
			//			} else if (dispositionDescriptor != null && dispositionDescriptor.isDisposition(obs)) {
			//				parsedObs.getDispositions().add(parseDisposition(dispositionDescriptor, obs, locale));
			//			} else {
			if (obs.getComment().contains("svg")) {
				parsedObs.setDrawing(parseObs(obs, locale));
				continue;
			}
			SimpleObject simpleObject = parseObs(obs, locale);
			parsedObs.getObs().add(simpleObject);
			//			}
		}
		
		// just sort the obs by obsId--not perfect, but a decent "natural" object
		Collections.sort(parsedObs.getObs(), new Comparator<SimpleObject>() {
			
			@Override
			public int compare(SimpleObject o1, SimpleObject o2) {
				return (Integer) o1.get("obsId") < (Integer) o2.get("obsId") ? -1 : 1;
			}
		});
		
		//		Collections.sort(parsedObs.getDiagnoses(), new Comparator<SimpleObject>() {
		//
		//			@Override
		//			public int compare(SimpleObject o1, SimpleObject o2) {
		//				Integer order1 = (Integer) o1.get("order");
		//				Integer order2 = (Integer) o2.get("order");
		//				return order1 - order2;
		//			}
		//		});
		
		return parsedObs;
	}
	
	private SimpleObject parseObs(Obs obs, Locale locale) {
		//		if ("org.openmrs.Location".equals(obs.getComment())) {
		//			return (parseObsWithLocationAnswer(obs, locationService.getLocation(Integer.valueOf(obs.getValueText()))));
		//		} else {
		SimpleObject simpleObject = SimpleObject.create("obsId", obs.getObsId());
		simpleObject.put("uuid", obs.getUuid());
		//            simpleObject.put("question", capitalizeString(uiUtils.format(obs.getConcept())));
		//            simpleObject.put("answer", uiUtils.format(obs));
		simpleObject.put("name", obs.getComment());
		if (obs.getComplexData() instanceof ModuleComplexData) {
			simpleObject.put("obsType", ((ModuleComplexData) obs.getComplexData()).getObsType());
			simpleObject.put("mimeType", ((ModuleComplexData) obs.getComplexData()).getMimeType());
		}
		return simpleObject;
		//		}
	}
	
	//	private SimpleObject parseDisposition(DispositionDescriptor dispositionDescriptor, Obs obs, Locale locale) {
	//		Obs dispositionObs = dispositionDescriptor.getDispositionObs(obs);
	//		Obs admissionLocationObs = dispositionDescriptor.getAdmissionLocationObs(obs);
	//		Obs internalTransferLocationObs = dispositionDescriptor.getInternalTransferLocationObs(obs);
	//		Obs dateOfDeathObs = dispositionDescriptor.getDateOfDeathObs(obs);
	//		List<Obs> additionalObs = dispositionDescriptor.getAdditionalObs(obs);
	//
	//		SimpleObject simpleObject = SimpleObject.create("obsId", obs.getObsId());
	//		simpleObject.put("disposition", dispositionObs.getValueAsString(locale));
	//
	//		List<SimpleObject> simplifiedAdditionalObs = new ArrayList<SimpleObject>();
	//
	//		if (admissionLocationObs != null) {
	//			simplifiedAdditionalObs.add(parseObsWithLocationAnswer(admissionLocationObs,
	//			    dispositionDescriptor.getAdmissionLocation(obs, locationService)));
	//		}
	//
	//		if (internalTransferLocationObs != null) {
	//			simplifiedAdditionalObs.add(parseObsWithLocationAnswer(internalTransferLocationObs,
	//			    dispositionDescriptor.getInternalTransferLocation(obs, locationService)));
	//		}
	//
	//		if (dateOfDeathObs != null) {
	//			simplifiedAdditionalObs.add(parseObs(dateOfDeathObs, locale));
	//		}
	//
	//		for (Obs additional : additionalObs) {
	//			simplifiedAdditionalObs.add(parseObs(additional, locale));
	//		}
	//		simpleObject.put("additionalObs", simplifiedAdditionalObs);
	//
	//		return simpleObject;
	//	}
	
	//	private SimpleObject parseDiagnosis(DiagnosisMetadata diagnosisMetadata, Obs obs) {
	//		Diagnosis diagnosis = diagnosisMetadata.toDiagnosis(obs);
	//
	//		String answer = "(" + uiUtils.message("coreapps.Diagnosis.Certainty." + diagnosis.getCertainty()) + ") ";
	//		answer += diagnosis.getDiagnosis().formatWithCode(uiUtils.getLocale(),
	//		    emrApiProperties.getConceptSourcesForDiagnosisSearch());
	//
	//		SimpleObject simpleObject = SimpleObject.fromObject(obs, uiUtils, "obsId");
	//		simpleObject.put("question", formatDiagnosisQuestion(diagnosis.getOrder()));
	//		simpleObject.put("answer", answer);
	//		simpleObject.put("order", diagnosis.getOrder().ordinal());
	//		return simpleObject;
	//	}
	
	//	private SimpleObject parseObsWithLocationAnswer(Obs obs, Location location) {
	//
	//		SimpleObject simpleObject = SimpleObject.create("obsId", obs.getObsId());
	//
	//		simpleObject.put("question", capitalizeString(uiUtils.format(obs.getConcept())));
	//		simpleObject.put("answer", uiUtils.format(location));
	//		return simpleObject;
	//
	//	}
	
	//	private String formatDiagnosisQuestion(Diagnosis.Order order) {
	//		return uiUtils.message("coreapps.patientDashBoard.diagnosisQuestion." + order);
	//	}
	//
	//	private String capitalizeString(String name) {
	//		return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
	//	}
	
}