package org.openmrs.module.annotation.fragment.controller.parser;

import org.openmrs.ui.framework.SimpleObject;

import java.util.ArrayList;
import java.util.List;

public class ParsedObs {
	
	private List<SimpleObject> obs = new ArrayList<SimpleObject>();
	
	private SimpleObject drawing;
	
	public ParsedObs() {
		
	}
	
	public List<SimpleObject> getObs() {
		return obs;
	}
	
	public SimpleObject getDrawing() {
		return drawing;
	}
	
	public void setDrawing(SimpleObject drawing) {
		this.drawing = drawing;
	}
	
}
