package runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class DataUtil {
	
	public Map<String,String> loadClassMethods() throws FileNotFoundException, IOException, ParseException {
		Map<String,String> classMethodMap = new HashMap<String,String>();
		String path = System.getProperty("user.dir")+"//src//test//resources//jsons//classmethods.json";
		JSONParser parser = new JSONParser();
		JSONObject json = (JSONObject)parser.parse(new FileReader(new File(path)));
		JSONArray classDetails=(JSONArray)json.get("classdetails"); //return a jsonarray
		for(int i=0;i<classDetails.size();i++) {
			JSONObject classDetail = (JSONObject)classDetails.get(i);
			String className = (String) classDetail.get("class");
			JSONArray methods = (JSONArray)classDetail.get("methods");
			//System.out.println(className);
			for(int j=0;j<methods.size();j++) {
				String method = (String)methods.get(j);
				classMethodMap.put(method, className);
			}
			
		}
		System.out.println(classMethodMap);
		return classMethodMap;
	}
	
public int getTestDataSets(String pathFile,String dataFlag) throws FileNotFoundException, IOException, ParseException {
		
		JSONParser parser = new JSONParser();
		JSONObject json=(JSONObject)parser.parse(new FileReader(new File(pathFile)));
		JSONArray testDataSets = (JSONArray)json.get("testdata");
		for(int dSetId=0;dSetId<testDataSets.size();dSetId++) {
			JSONObject testData = (JSONObject)testDataSets.get(dSetId);
			String flag = (String)testData.get("flag");
			if(dataFlag.equals(flag)) {
				JSONArray dataSets = (JSONArray)testData.get("data");
				return dataSets.size();
			}
		}
		return -1;
		
	}
	
	public JSONObject getTestData(String pathFile,String dataFlag,int iteration) throws FileNotFoundException, IOException, ParseException{
		JSONParser parser = new JSONParser();
		JSONObject json=(JSONObject)parser.parse(new FileReader(new File(pathFile)));
		JSONArray testDataSets = (JSONArray)json.get("testdata");
		for(int dSetId=0;dSetId<testDataSets.size();dSetId++) {
			JSONObject testData = (JSONObject)testDataSets.get(dSetId);
			String flag = (String)testData.get("flag");
			if(dataFlag.equals(flag)) {
				JSONArray dataSets = (JSONArray)testData.get("data");
				JSONObject data = (JSONObject)dataSets.get(iteration);
				return data;
			}
		}
		return null;
	}

}
