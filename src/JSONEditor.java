import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONEditor {

	//Fields
	static JSONObject botData;
	FileReader fr;
	static FileWriter fw;
	String fileName;
	
	enum type {
		Number, String
	};
	
		public JSONEditor(String file) throws IOException {
			fileName = file;
			try {
				fr = new FileReader(fileName);
				Object obj = new JSONParser().parse(fr);
				botData = (JSONObject) obj;
				fw = new FileWriter("botData.json", false);
			} catch (FileNotFoundException e) {
				botData = new JSONObject();
				File botDataFile = new File("botData.json");
				botDataFile.createNewFile();
				fw = new FileWriter("botData.json");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		void updateJSON() {
			try {
				fw = new FileWriter("botData.json", false);
				fw.write(botData.toJSONString());
				fw.flush();
				fw.close();
				fr = new FileReader(fileName);
				Object obj = new JSONParser().parse(fr);
				botData = (JSONObject) obj;
			} catch (IOException | ParseException e) {
				e.printStackTrace();
			}
		}
		
		@SuppressWarnings("unchecked")
		Object checkField(String key, type dataType) {
			if (!botData.containsKey(key)) {
				if (dataType.equals(type.Number)) {
					botData.put(key, 0);
				}
				else if (dataType.equals(type.String)) {
					botData.put(key, "");
				}
				updateJSON();
			}
			return botData.get(key);
		}
		
		@SuppressWarnings("unchecked")
		void updateField(String key, Object value, type dataType) {
			if (dataType.equals(type.Number)) {
				botData.put(key, value);
			}
			else if (dataType.equals(type.String)) {
				botData.put(key, (String) value);
			}
		}
		
		Object getValue(String key) {
			return botData.get(key);
		}

		public static void onDisconnect() {
			try {
				fw = new FileWriter("botData.json", false);
				fw.write(botData.toJSONString());
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
}