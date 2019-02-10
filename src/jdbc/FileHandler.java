package jdbc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import application.StaticVars;

public class FileHandler {
	public void log(String log)
	{
		Thread t = new Thread(()->{
			try {
				String logfile = "log.txt";
				StringBuffer sb = new StringBuffer();
				String readF;
				//Read contents first
				File logF = new File(logfile);
				if(!logF.exists())
				{
					logF.createNewFile();
				}
				FileReader fr = new FileReader(logF);
				BufferedReader br = new BufferedReader(fr);
				while((readF = br.readLine())!=null)
				{
					sb.append(readF);
					sb.append(System.getProperty("line.separator"));
				}
				br.close();
				
				Date date = new Date(System.currentTimeMillis());
				DateFormat dformat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				FileWriter fw = new FileWriter(new File(logfile));
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter pw = new PrintWriter(bw);
				pw.println(log+" at : "+dformat.format(date));
				pw.println(sb);
				pw.close();
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		t.run();
	}
	
	public void log(Throwable ex)
	{
		this.log(ex.getMessage());
	}
}
