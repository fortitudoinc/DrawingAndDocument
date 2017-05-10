package org.openmrs.module.annotation.web.controller;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openmrs.module.web.WebModuleUtil;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.v1_0.controller.MainResourceController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
@RequestMapping(value = "/rest/" + RestConstants.VERSION_1 + "/annotation/template")
public class TemplatesController extends MainResourceController {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@ResponseBody
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public String onGetAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		File file = getTemplateDir();
		JSONArray jsonArray = new JSONArray();
		if (file.exists() && file.isDirectory()) {
			for (File f : file.listFiles()) {
				jsonArray.put(f.getName());
			}
		}
		return jsonArray.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/get/{fileName:.+}", method = RequestMethod.GET)
	public void onGetTemplate(@PathVariable(value = "fileName") String fileName, HttpServletResponse response)
	        throws IOException {
		File file = new File(getTemplateDir(), fileName);
		
		if (file.exists()) {
			InputStream is = new FileInputStream(file);
			IOUtils.copy(is, response.getOutputStream());
			response.flushBuffer();
		} else
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete/{fileName:.+}", method = RequestMethod.GET)
	public String onDeleteTemplate(@PathVariable(value = "fileName") String fileName, HttpServletResponse response)
	        throws IOException, JSONException {
		File file = new File(getTemplateDir(), fileName);
		
		if (file.exists() && file.delete()) {
			return new JSONObject().put("result", "success").toString();
		} else
			return new JSONObject().put("result", "failed").toString();
		
	}
	
	@ResponseBody
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String onUploadTemplate(HttpServletRequest request, @RequestParam(value = "filename") String fileName,
	        @RequestParam(value = "file") String file, HttpServletResponse response) throws IOException, JSONException {
		log.error(getClass().getName() + " filename received : " + fileName);
		
		if (!file.isEmpty()) {
			try {
				File templateDir = getTemplateDir();
				File templateFile = new File(templateDir, fileName);
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(templateFile));
				stream.write(Base64.decodeBase64(file.getBytes()));
				stream.close();
				return new JSONObject().put("result", "success").toString();
			}
			catch (Exception e) {
				return new JSONObject().put("result", "failed").put("description", "Exception occurred").toString();
			}
		} else {
			return new JSONObject().put("result", "failed").put("description", "File is empty").toString();
		}
	}
	
	@Override
	public String getNamespace() {
		return "v1/annotation/template";
	}
	
	private File getTemplateDir() {
		String rootFolderPath = WebModuleUtil.getModuleWebFolder("annotation");
		return new File(rootFolderPath, "templates");
	}
	
}
