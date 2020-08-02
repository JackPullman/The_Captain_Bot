/**
 * @author Jack Pullman
 */
public class Commands {

	/**
	 * Determines if message sender is broadcaster as defined by env.json
	 * 
	 * @param user the user that is determined whether they are broadcaster
	 * @return whether the calling user is the broadcaster
	 */
	static boolean isBroadcaster(String user) {
		return user.equalsIgnoreCase(TwitchBotMain.broadcaster);
	}

	/**
	 * Returns the value corresponding to fieldName
	 * 
	 * @param fieldName the key whose value is returned
	 * @param editor    the bot's editor instance
	 * @param dataType  the expected value type for casting
	 * @return the value corresponding to fieldName
	 */
	static Object getCommand(String fieldName, JSONEditor editor, JSONEditor.type dataType) {
		return editor.checkField(fieldName, dataType);
	}

	/**
	 * Sets the value of the key fieldName to be value
	 * 
	 * @param fieldName the key whose value is assigned
	 * @param value     the value to be assigned to fieldName
	 * @param editor    the bot's editor instance
	 * @param dataType  the expected value type for casting
	 */
	static void setCommand(String fieldName, Object value, JSONEditor editor, JSONEditor.type dataType) {
		editor.updateField(fieldName, value, dataType);
		editor.updateJSON();
	}

	/**
	 * Increments or decrements the value of the numeric key corresponding to
	 * fieldName
	 * 
	 * @param fieldName  the key whose value is incremented or decremented
	 * @param increment  true for increment, false for decrement
	 * @param lowerLimit the lower limit to which the value can be decremented to
	 * @param upperLimit the upper limit to which the value can be incremented to
	 * @param editor     the bot's editor instance
	 * @return whether the increment/decrement was successful due to limits
	 */
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
