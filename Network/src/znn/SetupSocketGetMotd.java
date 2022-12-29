package znn;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SetupSocketGetMotd {

	public static String getMotd(String ip, String port) {
		try {
			Socket socket = new Socket(ip, Integer.parseInt(port));
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			DataInputStream in = new DataInputStream(socket.getInputStream());

			out.write(0xFE);
			int b;
			StringBuffer str = new StringBuffer();
			while ((b = in.read()) != -1) {
				if (b != 0 && b > 16 && b != 255 && b != 23 && b != 24)
					str.append((char) b);
			}
			socket.close();
			return str.toString().split("§")[0];
		} catch (Exception ex) {
			return null;
		}
	}
}
