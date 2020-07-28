/**
 * @author Jack Pullman
 */
public class Commands {

	static boolean isBroadcaster(String user) {
		return user.equalsIgnoreCase(TwitchBotMain.broadcaster);
	}

	static Object getCommand(String fieldName, JSONEditor editor, JSONEditor.type dataType) {
		return editor.checkField(fieldName, dataType);
	}

	static void setCommand(String fieldName, Object value, JSONEditor editor, JSONEditor.type dataType) {
		editor.updateField(fieldName, value, dataType);
		editor.updateJSON();
	}

	static boolean unaryCommand(String fieldName, boolean increment, int lowerLimit, int upperLimit,
			JSONEditor editor) {
		long var = (long) editor.checkField(fieldName, JSONEditor.type.Number);
		var = increment ? (var + 1) : (var - 1);
		if (((var >= lowerLimit || lowerLimit == -1)) && ((var <= upperLimit || upperLimit == -1))) {
			setCommand(fieldName, var, editor, JSONEditor.type.Number);
			return true;
		} else {
			System.err.println("Unary operation not performed, upper or lower limit condition is not satisfied.");
			return false;
		}
	}

	static String handleCommandMistakes(long numMistakes) {
		if (numMistakes < 5) {
			return "Do better.";
		} else if (numMistakes < 10) {
			return "Come on now, haven't we talked about this?";
		} else if (numMistakes < 15) {
			return "I am convinced you are doing this on purpose.";
		} else if (numMistakes < 20) {
			return "You should probably gift a sub or something cuz you sound pretty gullible.";
		} else if (numMistakes < 25) {
			return "You are breaking my heart, you should go. BibleThump";
		} else {
			return "I am done with you, time to walk the plank.";
		}
	}
}
