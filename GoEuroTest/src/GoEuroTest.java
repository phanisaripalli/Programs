import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class responsible to access the GoEuro Test API with the user input, 
 * get the JSON content, decode (using json-simple)
 * <a href="https://code.google.com/p/json-simple/">json-simple</a>
 * and write the results to a CSV (comma separated) file.
 *  
 * @author phanisaripalli
 */
public class GoEuroTest {
	
	/**
	 * Keys of JSON whose values are non-JSONObject (for e.g. String, Long) 
	 */
	private static final String[] KEYS = new String[]{"_type", "_id", "name", "type"};
    private static final String CSV_HEADER = "_type, _id, name, type, latitude, longitude\n"; 
    private static final String BASE_URL = "http://pre.dev.goeuro.de:12345/api/v1/suggest/position/en/name/";
    private static final String WORK_PATH = System.getProperty("user.dir") + File.separator; 
    
    private FileWriter writer = null;
    
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Invalid input/location.");
		} else {
			String place = args[0];
			if (place.contains(" ")) {
				place = place.replace(" ", "%20");
			}
			GoEuroTest goEuro = new GoEuroTest();
			String json = goEuro.getJSON(BASE_URL + place);
			JSONArray results = goEuro.getJSONResults(json);
			
			String csvRecordValue = goEuro.getCSVRecord(results);
			
			goEuro.createCSVFile();
			//Even if the results are empty, the csv form is always written
			goEuro.writeCSVRecords(CSV_HEADER + csvRecordValue);
			
		}
	}

	/**
	 * More than a getter. Decodes the JSON, and get the records to 
	 * be added to the CSV file as a String.
	 * @param results The JSON results array.
	 * @return Returns records to be added to CSV as a String.
	 */
	private String getCSVRecord(JSONArray results) {
		String csvRecordValue = "";
		for (int i = 0; i < results.size(); i++) {
			JSONObject jsonObject = (JSONObject) results.get(i);
			
			for (int j = 0; j < KEYS.length; j++) {
				csvRecordValue += this.getCVSField(jsonObject.get(KEYS[j])) + ",";
			}
			//Querying for geo_position
			JSONObject geoPosition = (JSONObject) jsonObject.get("geo_position");
			if (null != geoPosition) {
				csvRecordValue += this.getCVSField(geoPosition.get("latitude")) + "," 
						+ this.getCVSField(geoPosition.get("longitude"));
			} 
			
			csvRecordValue += "\n";
		}
		return csvRecordValue;
	}
	
	/**
	 * Accesses the URL, and gets the content of the URL.
	 * @param baseURL
	 * @return The content of the URL
	 */
	private String getJSON(String baseURL) {
		URL url;
		String json = "";
		try {
			url = new URL(baseURL);
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(url.openStream()));
			String tmp;
			while ((tmp = in.readLine()) != null) {
				json = tmp;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 return json;
	}
	
	/**
	 * Parses the JSON string and returns the results.
	 * @param str	
	 */
	private JSONArray getJSONResults(String str) {
		JSONParser parser = new JSONParser();
		Object obj = null;;
		try {
			obj = parser.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray results = (JSONArray) jsonObject.get("results");
		
		return results;
	}
	
	
	/**
	 * Method that generates a value for the CSV field of a record. If the value has a comma, 
	 * the value is wrapped in double-quotes.  
	 * @param obj An object.
	 * @return Returns a field to be added to a CSV record.
	 */
	private String getCVSField(Object obj) {
		String result = "";
		
		if (null != obj) {
			result = obj.toString();
			if (result.contains(",")) {
				result = "\"" + result + "\"";
			}
		} 
		return result;
		
	}
	
	/**
	 * Creates a CSV file.
	 */
	private void createCSVFile() {
		try {
			this.writer = new FileWriter(WORK_PATH + "GoEuro.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Writes CSV records. The records are input as a single string.
	 * @param str CSV Records as a string.
	 */
	private void writeCSVRecords(String str) {
		try {
			this.writer.append(str);
			this.writer.flush();
			this.writer.close();
			if (str.length() > CSV_HEADER.length()) {
				System.out.println("Content written to CSV located at: " + WORK_PATH + "GoEuro.csv");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}


