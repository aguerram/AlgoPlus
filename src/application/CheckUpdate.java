package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.*;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import jdbc.FileHandler;

public class CheckUpdate {
	private String jsonText;
	private JSONObject json;
	
	private String version = "";
	public CheckUpdate() {
		try {
			if(StaticVars.CountVersion < this.getVersion())
			{
				System.out.println("There is a newer version("+version+")\nYour current version is : "+StaticVars.AlgoPlusVersion);
				System.out.println("Please visit : "+StaticVars.SiteCheckUpdateDownloadPage+" to download the newer version");
			}
		} catch (IOException e) {
			/*e.printStackTrace();
			new FileHandler().log(e);*/
		}
	}

	private int getVersion() throws MalformedURLException, IOException {
		int count_version = 0;
		InputStream in = new URL(StaticVars.SiteCheckUpdate).openStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
		this.jsonText = readAll(rd);
		this.json = new JSONObject(this.jsonText);
		count_version = Integer.parseInt((String) this.json.get("count_version"));
		version = json.getString("version");
		return count_version;
	}

	private String readAll(BufferedReader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
