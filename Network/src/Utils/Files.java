package Utils;

import java.io.File;

public class Files {

	public static String random(String str) {
		String s = null;
		final File dir = new File(str);
		int size = dir.listFiles().length;
		String[] myArray = new String[size];
		File[] files = dir.listFiles();
		for (int i = 0; i < size; i++) {
			int idx = (int) (Math.random() * size);
			myArray[i] = files[idx].getName();
		}
		for (String x : myArray)
			s = x;
		return str + "\\" + s;
	}
}
