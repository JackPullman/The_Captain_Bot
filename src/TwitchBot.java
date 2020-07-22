import org.jibble.pircbot.*;

import java.io.IOException;

/**
 * @author Jack Pullman
 * @version 1.0
 */
public class TwitchBot extends PircBot {

	// Fields
	String fileName = "botData.json";
	JSONEditor editor;
	public static final String broadcaster = "CaptainJack99";

	public TwitchBot() throws IOException {
		this.setName("the_captain_bot");
		editor = new JSONEditor(fileName);
	}

	public void onMessage(String channel, String sender, String login, String hostname, String message) {
		if (message.substring(0, 1).equals("!")) {
			String command = message.substring(1);
			switch (command.toLowerCase()) {
			/**
			 * @AccessLevel Broadcaster
			 * @Description Disconnects the TwitchBot from Twitch, sends a confirmation
			 *              message, and exits the program
			 */
			case "disconnect":
				if (Commands.isBroadcaster(sender)) {
					this.sendMessage(TwitchBotMain.twitchChannel, this.getName() + " has disconnected");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					this.disconnect();
				}
				break;

			/**
			 * @AccessLevel Broadcaster
			 * @Description Increments or decrements the win or loss counter and sends
			 *              confirmation
			 */
			case "+win":
				if (Commands.isBroadcaster(sender)) {
					Commands.unaryCommand("wins", true, editor);
					sendMessage(channel, "Win has been added to the record.");
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}
			case "-win":
				if (Commands.isBroadcaster(sender)) {
					Commands.unaryCommand("wins", false, editor);
					sendMessage(channel, "Win has been subtracted from the record.");
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}
			case "+loss":
				if (Commands.isBroadcaster(sender)) {
					Commands.unaryCommand("losses", true, editor);
					sendMessage(channel, "Loss has been added to the record.");
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}
			case "-loss":
				if (Commands.isBroadcaster(sender)) {
					Commands.unaryCommand("losses", false, editor);
					sendMessage(channel, "Loss has been subtracted from the record.");
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}

				/**
				 * @AccessLevel Broadcaster
				 * @Description Sets both wins and losses to be 0
				 */
			case "reset":
				if (Commands.isBroadcaster(sender)) {
					Commands.setCommand("wins", 0, editor, JSONEditor.type.Number);
					Commands.setCommand("losses", 0, editor, JSONEditor.type.Number);
					sendMessage(channel, "The record has been reset.");
					break;
				} else {
					sendMessage(channel, "@" + sender + ", you are not authorized to use this command.");
					break;
				}

				/**
				 * @AccessLevel Everyone
				 * @Description Increments the 'plank' counter and sends the current plank count
				 */
			case "plank":
				Commands.unaryCommand("plank", true, editor);
				sendMessage(channel,
						"@" + sender + ", CaptainJack has planked " + editor.getValue("plank") + " times.");
				break;

			/**
			 * @AccessLevel Everyone
			 * @Description Sends the current record (wins-losses)
			 */
			case "record":
				long wins = (long) Commands.getCommand("wins", editor, JSONEditor.type.Number);
				long losses = (long) Commands.getCommand("losses", editor, JSONEditor.type.Number);
				sendMessage(channel, "@" + sender + ", " + wins + "-" + losses);
				break;

			default:
				sendMessage(channel, "@" + sender + ", this is not a supported command.");
				break;
			}
		}
	}

	public void onDisconnect() {
		JSONEditor.onDisconnect();
	}
}
