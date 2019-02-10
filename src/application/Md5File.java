package application;

import java.io.FileInputStream;
import java.io.InputStream;

import java.security.MessageDigest;

public class Md5File {
	public final static int MD5 = 0;
	public final static int SHA1 = 1;
	public final static int SHA256 = 2;

	public static byte[] createChecksum(String filename, int algorithm) throws Exception {
		String al = "MD5";
		switch (algorithm) {
		case 0:
			al = "MD5";
			break;
		case 1:
			al = "SHA-1";
			break;
		case 2:
			al = "SHA-256";
			break;
		default:
			al = "MD5";
			break;
		}
		InputStream fis = new FileInputStream(filename);

		byte[] buffer = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance(al);
		int numRead;

		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);

		fis.close();
		return complete.digest();
	}

	public static String getMD5Checksum(String filename, int algorithm) throws Exception {
		byte[] b = createChecksum(filename, algorithm);
		StringBuffer result = new StringBuffer();

		for (int i = 0; i < b.length; i++) {
			result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
		}
		return new String(result);
	}

	public static boolean checkMD5Checksum(String md5, String file, int algorithm) throws Exception {
		return md5.equals(getMD5Checksum(file, algorithm));
	}

}