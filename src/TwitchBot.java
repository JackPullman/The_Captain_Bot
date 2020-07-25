import org.jibble.pircbot.*;

import java.io.IOException;

/**
 * @author Jack Pullman
 * @version 1.1
 */
public class TwitchBot extends PircBot {

	// Fields
	String fileName = "botData.json";
	JSONEditor editor;
	String[] recordKeys = { "wins", "losses" };

	public TwitchBot() throws IOException {
		setName("the_captain_bot");
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
					sendMessage(TwitchBotMain.twitchChannel, getName() + " has disconnected");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					disconnect();
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

					editor.export("record.txt", recordKeys, "RECORD: ");
					break;
				}
				unauthorized(channel, sender);
				break;
			case "-win":
				if (Commands.isBroadcaster(sender)) {
					Commands.unaryCommand("wins", false, editor);
					sendMessage(channel, "Win has been subtracted from the record.");
					editor.export("record.txt", recordKeys, "RECORD: ");
					break;
				}
				unauthorized(channel, sender);
				break;

			case "+loss":
				if (Commands.isBroadcaster(sender)) {
					Commands.unaryCommand("losses", true, editor);
					sendMessage(channel, "Loss has been added to the record.");
					editor.export("record.txt", recordKeys, "RECORD: ");
					break;
				}
				unauthorized(channel, sender);
				break;

			case "-loss":
				if (Commands.isBroadcaster(sender)) {
					Commands.unaryCommand("losses", false, editor);
					sendMessage(channel, "Loss has been subtracted from the record.");
					editor.export("record.txt", recordKeys, "RECORD: ");
					break;
				}
				unauthorized(channel, sender);
				break;

			/**
			 * @AccessLevel Broadcaster
			 * @Description Sets both wins and losses to be 0
			 */
			case "reset":
				if (Commands.isBroadcaster(sender)) {
					Commands.setCommand("wins", 0, editor, JSONEditor.type.Number);
					Commands.setCommand("losses", 0, editor, JSONEditor.type.Number);
					sendMessage(channel, "The record has been reset.");
					editor.export("record.txt", recordKeys, "RECORD: ");
					break;
				}
				unauthorized(channel, sender);
				break;

			/**
			 * @AccessLevel Everyone
			 * @Description Increments the 'plank' counter and sends the current plank count
			 */
			case "plank":
				Commands.unaryCommand("plank", true, editor);
				sendMessage(channel, sender + ", CaptainJack has planked " + editor.getValue("plank") + " times.");
				break;

			/**
			 * @AccessLevel Everyone
			 * @Description Sends the current record (wins-losses)
			 */
			case "record":
				long wins = (long) Commands.getCommand("wins", editor, JSONEditor.type.Number);
				long losses = (long) Commands.getCommand("losses", editor, JSONEditor.type.Number);
				sendMessage(channel, sender + ", " + wins + "-" + losses);
				break;
				
			/**
			 * @AccessLevel Everyone
			 * @Description Predefined responses to commands, no back end
			 */
			case "spotify":
				sendMessage(channel, "Listen along to my liked songs with me: https://spoti.fi/2Ec8B0g");
				break;
				
			case "socials":
				sendMessage(channel, "Follow me on Social Media! https://twitter.com/jpullman13 https://www.instagram.com/jpullman13/");
				break;
				
			case "rank":
				sendMessage(channel, "GOLD 1");
				break;
				
			case "help":
				sendMessage(channel, sender + " The_Captain_Bot Commands: https://bit.ly/3eYCa27");
				break;

			default:
				Commands.unaryCommand(sender + "_mistakes", true, editor);
				long mistakes = (long) Commands.getCommand(sender + "_mistakes", editor, JSONEditor.type.Number);
				sendMessage(channel, sender + ", " + "you have attempted " + mistakes + " failed commands. "
						+ Commands.handleMistakes(mistakes));
				break;
			}
		}
	}

	public void onDisconnect() {
		JSONEditor.onDisconnect();
	}

	private void unauthorized(String channel, String sender) {
		sendMessage(channel, sender + " HACKER DETECTED. PERMISSION DENIED.");
	}
}
