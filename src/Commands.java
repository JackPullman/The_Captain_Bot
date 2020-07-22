/**
 * @author Jack Pullman
 *
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

	static void unaryCommand(String fieldName, boolean increment, JSONEditor editor) {
		long var = (long) editor.checkField(fieldName, JSONEditor.type.Number);
		var = increment ? (var + 1) : (var - 1);
		setCommand(fieldName, var, editor, JSONEditor.type.Number);
	}
}
