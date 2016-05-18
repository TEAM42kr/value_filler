package net.team42.valuefiller;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ValueFillerHelper {
	public static String findFileContent(String path, String charset) {
		FileInputStream fis = null;
		try {
			byte[] buffer = new byte[1024];
			fis = new FileInputStream(path);
			int readed = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((readed = fis.read(buffer)) != -1) {
				baos.write(buffer, 0, readed);
			}
			return baos.toString(charset);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}
	public static String findHTTPContent(String url, String charset) {
		InputStream is = null;
		HttpURLConnection con = null;
		try {
			byte[] buffer = new byte[1024];
			con = (HttpURLConnection) new URL(url).openConnection();
			is = con.getInputStream();
			int readed = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((readed = is.read(buffer)) != -1) {
				baos.write(buffer, 0, readed);
			}
			return baos.toString(charset);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				is.close();
				con.disconnect();
			} catch (Exception e) {
			}
		}
	}

}
