package com.cerner.shipit.DynaSites.model;

import java.util.ArrayList;
import java.util.HashMap;

public class AppModelTemplate {
	HashMap<String,String> fieldMap = new HashMap<String,String>();//HashMap<fieldLabel,fieldType> - field type can be text,textarea,select,checkbox,..
	HashMap<String,ArrayList<String>> selectOptionsMap = new HashMap<String,ArrayList<String>>();//if fieldMap has select, then this map is to hold the options list of the select field.
	HashMap<String,String> functionsMap = new HashMap<String,String>();//HashMap<functionName, searchCriteria> searchCriteria will be the fieldLabel.
	
	public HashMap<String, String> getFieldMap() {
		return fieldMap;
	}
	public void setFieldMap(HashMap<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}
	public HashMap<String, ArrayList<String>> getSelectOptionsMap() {
		return selectOptionsMap;
	}
	public void setSelectOptionsMap(HashMap<String, ArrayList<String>> selectOptionsMap) {
		this.selectOptionsMap = selectOptionsMap;
	}
	public HashMap<String, String> getFunctionsMap() {
		return functionsMap;
	}
	public void setFunctionsMap(HashMap<String, String> functionsMap) {
		this.functionsMap = functionsMap;
	}
		
}
