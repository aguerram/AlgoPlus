package application;

public class CheckSum {
	private String folder;
	private String fileName;
	private String md5;
	
	public static final String LIB = "app/lib";
	public static final String RES = "app/res";
	public CheckSum(String file,String md5,String folder)
	{
		this.fileName = file;
		this.md5 = md5;
		this.folder = folder;
	}
}
