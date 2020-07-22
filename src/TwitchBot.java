import org.jibble.pircbot.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;

import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * @author Jack Pullman
 * @version 1.0
 */
public class TwitchBot extends PircBot {

	// Fields
	FileReader reader;
	String fileName = "botData.json";
	JSONObject botData;
	FileWriter fw;
	public static final String broadcaster = "CaptainJack99";

	public TwitchBot() throws IOException {
		this.setName("the_captain_bot");
		try {
			reader = new FileReader(fileName);
			Object obj = new JSONParser().parse(reader);
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

	public void onDisconnect() {
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

	private void updateJSON() {
		try {
			fw = new FileWriter("botData.json", false);
			fw.write(botData.toJSONString());
			fw.flush();
			fw.close();
			reader = new FileReader(fileName);
			Object obj = new JSONParser().parse(reader);
			botData = (JSONObject) obj;
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.substring(0, 1).equals("!")) {
			String command = message.substring(1);
			switch (command.toLowerCase()) {
			case "disconnect": // Broadcaster Only
				if (sender.equalsIgnoreCase(broadcaster)) {
					this.sendMessage(TwitchBotMain.twitchChannel, this.getName() + " has disconnected");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.disconnect();
				}
				break;
			case "plank":
				long plank = 0;
				if (botData.containsKey("plank")) {
					plank = (long) botData.get("plank");
				} else {
					botData.put("plank", plank);
					updateJSON();
					plank = (long) botData.get("plank");
				}
				plank = plank + 1;
				sendMessage(channel, "@" + sender + ", CaptainJack has planked " + plank + " times.");
				botData.put("plank", plank);
				updateJSON();
				break;
			case "+win": // Broadcaster Only
				if (sender.equalsIgnoreCase(broadcaster)) {
					long wins = 0;
					if (botData.containsKey("wins")) {
						wins = (long) botData.get("wins");
					} else {
						botData.put("wins", wins);
						updateJSON();
						wins = (long) botData.get("wins");
					}
					sendMessage(channel, "Win has been added to the record.");
					botData.put("wins", wins + 1);
					updateJSON();
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}
			case "-win": // Broadcaster Only
				if (sender.equalsIgnoreCase(broadcaster)) {
					long wins = 0;
					if (botData.containsKey("wins")) {
						wins = (long) botData.get("wins");
					} else {
						botData.put("wins", wins);
						updateJSON();
						wins = (long) botData.get("wins");
					}
					sendMessage(channel, "Win has been subtracted from the record.");
					botData.put("wins", wins - 1);
					updateJSON();
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}
			case "+loss": // Broadcaster Only
				if (sender.equalsIgnoreCase(broadcaster)) {
					long losses = 0;
					if (botData.containsKey("losses")) {
						losses = (long) botData.get("losses");
					} else {
						botData.put("losses", losses);
						updateJSON();
						losses = (long) botData.get("losses");
					}
					sendMessage(channel, "Loss has been added to the record.");
					botData.put("losses", losses + 1);
					updateJSON();
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}
			case "-loss": // Broadcaster Only
				if (sender.equalsIgnoreCase(broadcaster)) {
					long losses = 0;
					if (botData.containsKey("losses")) {
						losses = (long) botData.get("losses");
					} else {
						botData.put("losses", losses);
						updateJSON();
						losses = (long) botData.get("losses");
					}
					sendMessage(channel, "Loss has been subtracted from the record.");
					botData.put("losses", losses - 1);
					updateJSON();
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}
			case "record":
				if (!botData.containsKey("wins")) {
					botData.put("wins", 0);
				}
				if (!botData.containsKey("losses")) {
					botData.put("losses", 0);
				}
				updateJSON();
				long wins = (long) botData.get("wins");
				long losses = (long) botData.get("losses");
				sendMessage(channel, "@" + sender + ", " + wins + "-" + losses);
				break;
			case "reset": // Broadcaster Only
				if (sender.equalsIgnoreCase(broadcaster)) {
					botData.put("wins", 0);
					botData.put("losses", 0);
					updateJSON();
					sendMessage(channel, "The record has been reset.");
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}
			default:
				sendMessage(channel, "@" + sender + ", this is not a supported command.");
				break;
			}
		}
	}
}
