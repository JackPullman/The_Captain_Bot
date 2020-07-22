import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * @author Jack Pullman
 *
 */
public class TwitchBotMain {

	public static final String twitchChannel = "#captainjack99";

	public static void main(String[] args) throws Exception {
		FileReader reader = new FileReader("env.json");
		Object obj = new JSONParser().parse(reader);
		JSONObject envData = (JSONObject) obj;
		TwitchBot bot = new TwitchBot();
		bot.setVerbose(true);
		bot.connect("irc.twitch.tv", 6667, (String)envData.get("oath"));
		bot.joinChannel(twitchChannel);
		if (bot.isConnected()) {
			bot.sendMessage(twitchChannel, bot.getName() + " is connected");
		}
	}
}
