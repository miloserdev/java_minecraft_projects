package World;

public class Fonts {
	public static String unicodeFont(String input) {
		String returnString = "";
		boolean skip = false;
		boolean skipNext = false;
		for (int t = 0; t < input.length(); t++) {
			if (input.charAt(t) == '§') {
				skip = true;
				skipNext = true;
			}
			if (!skip) {
				if ((input.charAt(t) <= '') && (input.charAt(t) >= ' ')) {
					returnString = returnString + (char) (65248 + input.charAt(t));
				}
			} else {
				returnString = returnString + input.charAt(t);
				if (!skipNext) {
					skip = false;
				} else {
					skipNext = false;
				}
			}
		}
		return returnString;
	}
}
