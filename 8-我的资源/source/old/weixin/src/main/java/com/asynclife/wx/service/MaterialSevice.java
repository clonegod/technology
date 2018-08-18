package com.asynclife.wx.service;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.asynclife.wx.domain.MaterialRepository;
import com.asynclife.wx.enums.MediaType;
import com.asynclife.wx.util.HttpClient;

@Service
public class MaterialSevice {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MaterialRepository materialRepository;
	
	@Autowired
	HttpClient httpClient;
	
	@Value("${POST_UPLOAD_TEMP_MATERIAL}")
	String POST_UPLOAD_TEMP_MATERIAL;
	
	public String uploadTempMaterial(File materialPath, MediaType mediaType) {
		
		MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
		FileSystemResource f1 = new FileSystemResource(materialPath);
		
		parts.add("media", f1);
		
		Map<String,String> urlVariables = new HashMap<String,String>();
		urlVariables.put("TYPE", mediaType.name());
		
		String response = httpClient.doPost(POST_UPLOAD_TEMP_MATERIAL, parts, urlVariables);
		
		return response;
	}
	
	public void getTempMaterial() {
		
	}
	
	public void uploadPermanentMaterial() {
		
	}
	
	public void getPermanentMaterial() {
		
	}
	
	public void deletePermanentMaterial() {
		
	}
	
	public void updatePermanentMaterial() {
		
	}
	
	public void getMaterialCount() {
		
	}
	
	public void getMaterialList() {
		
	}
}
