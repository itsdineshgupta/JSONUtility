package com.json.utility;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.mapper.GsonMappingProvider;

public class jsonUtility {
	private static List<String> keyValueList = new ArrayList<String>();

	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {

		String filePath = "fileJSON.json";
		String key = "id";

		System.out.println(readJsonFileDynamic(filePath, "$.."+key));

		//parseJson(filePath, key);
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
			//break;
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
				Set<Map.Entry<String, JsonElement>> entrySet = jsonElement.getAsJsonObject().entrySet();
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

	public static List<?> readJsonFileDynamic(String filePath, String jsonPath) {

		System.out.println("jsonpath - "+jsonPath);
		List<?> categories = null;
		try
		{
			String content = new String(Files.readAllBytes(Paths.get(filePath)));
			Configuration conf = Configuration.builder()
					.jsonProvider(new GsonJsonProvider())
					.mappingProvider(new GsonMappingProvider())
					.build();

			DocumentContext context = JsonPath.using(conf).parse(content);
			categories = context.read(jsonPath, List.class);//List 

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return categories;
	}
}