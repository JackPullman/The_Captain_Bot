import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Jack Pullman
 *
 */
public class TwitchBotMain {

	private static String oauth;
	static String twitchChannel;
	static String broadcaster;
	static String hostName;
	static long port;
	static boolean export = false;
	static String exportPath;

	public static void main(String[] args) throws Exception {
		FileReader reader = new FileReader("env.json");
		Object obj = new JSONParser().parse(reader);
		JSONObject envData = (JSONObject) obj;
		TwitchBot bot = new TwitchBot();
		oauth = (String) envData.get("oauth");
		twitchChannel = (String) envData.get("twitchChannel");
		broadcaster = (String) envData.get("broadcaster");
		hostName = (String) envData.get("hostName");
		port = (long) envData.get("port");
		export = (boolean) envData.get("export");
		exportPath = (String) envData.get("exportPath");
		bot.setVerbose(true);
		bot.connect(hostName, Math.toIntExact(port), oauth);
		bot.joinChannel(twitchChannel);
		if (!bot.isConnected()) {
			System.err.println("BOT FAILED TO CONNECT");
			System.exit(1);
		}
		bot.sendMessage(twitchChannel, bot.getName() + " is connected");
	}
}
