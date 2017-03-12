package org.openmrs.module.annotation.fragment.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

/**
 * Created by abhij on 3/9/2017.
 */
public class StatusBarFragmentController {
	
	Log log = LogFactory.getLog(getClass());
	
	// TODO: 3/9/2017 Customize for view only
	public void controller(FragmentModel model, @FragmentParam(value = "mode", required = false) String mode)
	        throws Exception {
		log.error(getClass().getSimpleName() + ".controller()");
		model.addAttribute("mode", mode);
	}
}