package com.example.technicaltestinc.util;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;


public class JsonUtil {

	public static String retrieveValueFromPath(String json, String path) {
		if (!doesPathExist(json, path)) {
			return null;
		}
		DocumentContext context = JsonPath.parse(json);
		JsonPath jsonPath = JsonPath.compile(path);
		return context.read(jsonPath).toString();
	}

	public static boolean doesPathExist(String json, String path) {
		DocumentContext context = JsonPath.parse(json,
				Configuration.defaultConfiguration().addOptions(Option.SUPPRESS_EXCEPTIONS));
		return context.read(JsonPath.compile(path)) != null;
	}
}
