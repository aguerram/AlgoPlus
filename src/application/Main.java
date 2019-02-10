package application;

import java.io.File;


import compiler.Interpreter.RunTheProgram;
import jdbc.Jdbc;
import system.GUI;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class Main {
	public static void main(String[] args) {
		/*ExpressionParser parser = new SpelExpressionParser(); 
		Expression exp = parser.parseExpression("(5+6)*(8-6)");
		Object message = (Object) exp.getValue();
		System.out.println(message);*/
		//System.out.println(System.getProperty("java.io.tmpdir"));
		inis_value();
		GUI window = new GUI();
		window.initial(args);
	}

	private final static void inis_value() {
		//Check For Updates
		CheckUpdate checkupdate = new CheckUpdate();
		Jdbc db = new Jdbc();
		// Config
		StaticVars.lastWorkingDirectory = db.getConfig("lastWorkingDirectory");
		if(!new File(StaticVars.lastWorkingDirectory).exists())
		{
			StaticVars.lastWorkingDirectory = System.getProperty("user.home");
		}
		StaticVars.systemLanguage = db.getConfig("lang");
		RunTheProgram.waitingUserInput = false;
		// Translate
		StaticVars.tabText = db.getTranslate("tab");
		StaticVars.file = db.getTranslate("file");
		StaticVars.newfile = db.getTranslate("newfile");
		StaticVars.newprojecet = db.getTranslate("newprojecet");
		StaticVars.opendfolder = db.getTranslate("opendfolder");
		StaticVars.openfile = db.getTranslate("openfile");
		
		StaticVars.saving = db.getTranslate("saving");
		StaticVars.aboutme = db.getTranslate("aboutme");
		StaticVars.quit = db.getTranslate("quit");
		StaticVars.copy = db.getTranslate("copy");
		StaticVars.cut = db.getTranslate("cut");
		StaticVars.clear = db.getTranslate("clear");
		StaticVars.paste = db.getTranslate("paste");
		StaticVars.undo = db.getTranslate("undo");
		StaticVars.redo = db.getTranslate("redo");
		StaticVars.save = db.getTranslate("save");
		StaticVars.run = db.getTranslate("run");
		StaticVars.find = db.getTranslate("find");
		StaticVars.closeAll = db.getTranslate("closeAll");
		StaticVars.findAndReplace = db.getTranslate("findAndReplace");
		StaticVars.compileRun = db.getTranslate("compileRun");
	}

	private static CheckSum checkFiles[] = {
			// LIB Content
			new CheckSum("controlsfx-8.40.14.jar", "76c85051ab80fe9097f9b3eb525ef1f4", CheckSum.LIB),
			new CheckSum("fontawesomefx-8.9.jar", "10e9dcadbf584ecc0945faeb1d62f11f", CheckSum.LIB),
			new CheckSum("jfoenix-8.0.7.jar", "950a43a85bec48a3fda74ce4306cfb55", CheckSum.LIB),
			new CheckSum("spring-aop-5.0.7.RELEASE.jar", "cd592093caba2866661a095786f1ed11", CheckSum.LIB),
			new CheckSum("spring-aspects-5.0.7.RELEASE.jar", "b718a837d81f22ce4d99f88de27dbbb4", CheckSum.LIB),
			new CheckSum("spring-beans-5.0.7.RELEASE.jar", "c850badbb984cda6983da22c8672a59f", CheckSum.LIB),
			new CheckSum("spring-context-5.0.7.RELEASE.jar", "3848a1c130365332ce874d92844c9dbb", CheckSum.LIB),
			new CheckSum("spring-context-indexer-5.0.7.RELEASE.jar", "b2169284ece57a12ce14d7a5ab382cdc", CheckSum.LIB),
			new CheckSum("spring-context-support-5.0.7.RELEASE.jar", "db0012a78e8d0edf8b03fbe0ecf2632e", CheckSum.LIB),
			new CheckSum("spring-core-5.0.7.RELEASE.jar", "913b95eac3078834b038d0d0c2fa90ff", CheckSum.LIB),
			new CheckSum("spring-expression-5.0.7.RELEASE.jar", "c73cd0160b0194e91bcda6f59394d2b9", CheckSum.LIB),
			new CheckSum("spring-instrument-5.0.7.RELEASE.jar", "625680634c3e7df4de9b815657c91186", CheckSum.LIB),
			new CheckSum("spring-jcl-5.0.7.RELEASE.jar", "93fa5dfcd237ca91845e0ee598f061c9", CheckSum.LIB),
			new CheckSum("spring-jdbc-5.0.7.RELEASE.jar", "5e1612ae8c02e5a8562bf4377a4615e4", CheckSum.LIB),
			new CheckSum("spring-jms-5.0.7.RELEASE.jar", "2dbb7896e951694ad5c2de5353cc519d", CheckSum.LIB),
			new CheckSum("spring-messaging-5.0.7.RELEASE.jar", "616a6a97284bcc37f81256b39a2caaf6", CheckSum.LIB),
			new CheckSum("spring-orm-5.0.7.RELEASE.jar", "420a5fb8997f02756c1ca3f39fdcdc6f", CheckSum.LIB),
			new CheckSum("spring-oxm-5.0.7.RELEASE.jar", "ad33422070493ea1647c594786719e4f", CheckSum.LIB),
			new CheckSum("spring-test-5.0.7.RELEASE.jar", "44ae008b4c746a73b8233446b6dc1eb1", CheckSum.LIB),
			new CheckSum("spring-tx-5.0.7.RELEASE.jar", "987d0c60a14dc9d1bca709d167ba2d9b", CheckSum.LIB),
			new CheckSum("spring-web-5.0.7.RELEASE.jar", "cdb97ca6e419ea429244db6b01ea9d09", CheckSum.LIB),
			new CheckSum("spring-webflux-5.0.7.RELEASE.jar", "c70804199d88b3c350a98a82aa1fdbd1", CheckSum.LIB),
			new CheckSum("spring-webmvc-5.0.7.RELEASE.jar", "d8031b3014b2a88a49bcf22eae95fa1a", CheckSum.LIB),
			new CheckSum("spring-websocket-5.0.7.RELEASE.jar", "b4fdd6b6ed3cbe92ba4e47543c3a1db0", CheckSum.LIB),
			new CheckSum("sqlite-jdbc-3.23.1.jar", "76c9c25de0f8603e5706adab1cc18009", CheckSum.LIB)
	};
}
