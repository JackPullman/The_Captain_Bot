import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Jack Pullman
 */
public class JSONEditor {

	// Fields
	static JSONObject botData;
	static FileReader fr;
	static FileWriter fw;
	String fileName;

	enum type {
		Number, String
	};

	/**
	 * Default constructor for JSONEditor. Opens FileReader and FileWriter for
	 * botData, creating designated JSON if it does not exist
	 * 
	 * @param file the JSON which is being manipulated by the JSONEditor instance
	 */
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
			System.err.println("Parse Failed");
			e.printStackTrace();
		}
	}

	/**
	 * Function with package-wide static access. Writes the JSON object to file and
	 * then rereads the JSON file to the Object instance
	 */
	void updateJSON() {
		try {
			fw = new FileWriter("botData.json", false);
			fw.write(botData.toJSONString());
			fw.close();
			fr = new FileReader(fileName);
			Object obj = new JSONParser().parse(fr);
			botData = (JSONObject) obj;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Checks for and returns the value corresponding to key key with type dataType
	 * 
	 * @param key      the key whose value is returned once it is checked for and
	 *                 populated if missing
	 * @param dataType the expected value type for casting
	 * @return the value corresponding to key once it is autopopulated if missing
	 */
	@SuppressWarnings("unchecked")
	Object checkField(String key, type dataType) {
		if (!botData.containsKey(key)) {
			if (dataType.equals(type.Number)) {
				botData.put(key, 0);
			} else if (dataType.equals(type.String)) {
				botData.put(key, "");
			}
			updateJSON();
		}
		return botData.get(key);
	}

	/**
	 * Updates the value of key key with value value of type dataType
	 * 
	 * @param key      the key whose value is updated to be value
	 * @param value    the value assigned to key key
	 * @param dataType the expected value type for casting
	 */
	@SuppressWarnings("unchecked")
	void updateField(String key, Object value, type dataType) {
		if (dataType.equals(type.Number)) {
			botData.put(key, value);
		} else if (dataType.equals(type.String)) {
			botData.put(key, (String) value);
		}
	}

	Object getValue(String key) {
		return botData.get(key);
	}

	/**
	 * Creates a txt file named exportFileName at the path specified in env.json
	 * containing the value corresponding to keys (connected by hyphen if multiple
	 * values), preceded by pretext if applicable
	 * 
	 * @param exportFileName the name of the txt file the value(s) are exported to
	 * @param keys           the key(s) whose values are exported
	 * @param pretext        the text that is added to the file before the value(s)
	 */
	void export(String exportFileName, String[] keys, String pretext) {
		if (!TwitchBotMain.export)
			return;
		try {
			File tempFile = new File(TwitchBotMain.exportPath + exportFileName);
			FileWriter tempWriter = new FileWriter(tempFile, false);
			String temp = pretext;
			for (int i = 0; i < keys.length; i++) {
				temp += checkField(keys[i], type.Number);
				if (i < (keys.length - 1)) {
					temp += "-";
				}
			}
			tempWriter.write(temp);
			tempWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Export FileWriter Error");
		}
	}

	/**
	 * Disconnect handling: Writes the current botData to a backup JSON and
	 * gracefully exits the program after notifying the channel chat
	 */
	static void onDisconnect() {
		try {
			fw = new FileWriter(new File(TwitchBotMain.exportPath + "Backup\\backupData.json"), false);
			fw.write(botData.toJSONString());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Disconnect FileWriter Error");
		}
		System.exit(0);
	}
}
