package jdbc;

import java.io.File;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import application.StaticVars;

public class Jdbc {
	public Connection connect()
	{
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Connection conn = null;
		
		//String url = "jdbc:sqlite:"+getClass().getClassLoader().getResource("db/AlgoPlusDB.db").toString();
		
		try {
			String url = "jdbc:sqlite:"+new File("./db/AlgoPlusDB.db").toURI().toURL();
			conn = DriverManager.getConnection(url);
			return conn;
		}
		catch(Exception ex)
		{
			new FileHandler().log(ex.getMessage());
			ex.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	public boolean setConfig(String name,String value)
	{
		Connection conn=null ;
		try {
			conn = this.connect();
			if(conn == null)
				return false;
			String sql = "insert or REPLACE  into config(name,value) values(?,?)";
			PreparedStatement pstm = conn.prepareStatement(sql);
			pstm.setString(1, name);
			pstm.setString(2, value);
			boolean t = pstm.executeUpdate() > 0;
			conn.close();
			return t;
		}
		catch(Exception ex)
		{
			new FileHandler().log(ex.getMessage());
		}
		try {
			conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			new FileHandler().log(ex.getMessage());
		}
		return false;
	}
	public String getConfig(String name)
	{
		Connection conn=null;
		try {
			conn = this.connect();
			if(conn != null)
			{
				String sql = "select value from config where name=?";
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setString(1, name);
				ResultSet rs= pstm.executeQuery();
				if(rs!=null)
				{
					String ss = rs.getString("value");
					conn.close();
					return ss;
				}			
			}
		}
		catch(Exception ex)
		{
			new FileHandler().log(ex.getMessage());
		}
		try {
			if(conn != null)
				conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			new FileHandler().log(ex.getMessage());
		}
		return "";
	}
	public String getTranslate(String name)
	{
		Connection conn=null;
		try {
			conn = this.connect();
			if(conn != null)
			{
				String sql = "select value from translate where name=? and lang = ?";
				PreparedStatement pstm = conn.prepareStatement(sql);
				pstm.setString(1, name);
				pstm.setString(2, StaticVars.systemLanguage);
				ResultSet rs= pstm.executeQuery();
				if(rs!=null)
				{
					String ss = rs.getString("value");
					conn.close();
					return ss;
				}			
			}
		}
		catch(Exception ex)
		{
			new FileHandler().log(ex.getMessage());
			ex.printStackTrace();
		}
		try {
			if(conn != null)
				conn.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			new FileHandler().log(ex.getMessage());
		}
		return name;
	}
}
