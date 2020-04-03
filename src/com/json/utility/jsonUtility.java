package com.json.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class jsonUtility {
	private static List<String> keyValueList = new ArrayList<String>();

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

		String filePath = "fileJSON.json";
		String key = "id";
		parseJson(filePath, key);
	}

	private static void parseJson(String filePath, String key) throws FileNotFoundException, IOException, ParseException {

		//Read from JSON File
		JSONParser simpleParser = new JSONParser();
		JSONObject jsonObject =  (JSONObject) simpleParser.parse(new FileReader(filePath));
		//Store as JSON String
		String jsonString = jsonObject.toJSONString();
		//System.out.println(data);

		JsonParser gsonParser = new JsonParser();
		readKey(key, gsonParser.parse(jsonString));
		//Print all the found values
		for(String keyValue:keyValueList) {
			System.out.println(keyValue.toString().replaceAll("\"", ""));
			break;
		}
	}

	private static void readKey(String keyName, JsonElement jsonElement) {

		if (jsonElement.isJsonArray()) {
			for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
				readKey(keyName, jsonElement1);
			}
		} 
		else {
			if (jsonElement.isJsonObject()) {
				Set<Map.Entry<String, JsonElement>> entrySet = jsonElement
						.getAsJsonObject().entrySet();
				for (Map.Entry<String, JsonElement> entry : entrySet) {
					String key1 = entry.getKey();
					if (key1.equals(keyName)) {
						keyValueList.add(entry.getValue().toString());
					}
					readKey(keyName, entry.getValue());
				}
			} else {
				if (jsonElement.toString().equals(keyName)) {
					keyValueList.add(jsonElement.toString());
				}
			}
		}
	}
}