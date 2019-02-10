package compiler.lexical;

import compiler.global.Global;

public class Token {
	private int id;
	private int order;
	private String line;
	private String word;
	public Token(int nbr,String line,String word)
	{
		this.id = nbr;
		this.line = line;
		this.word = word;
		this.order = Global.tokens.size();
	}
	public String getWord()
	{
		return word;
	}
	public int getId() {
		return id;
	}
	public void setId(int number) {
		this.id = number;
	}
	public String getLine() {
		try {
			return String.valueOf(Integer.parseInt(line.split(":")[0].trim())+1);
		}
		catch(Exception e)
		{
			return "";
		}
	}
	public void setLine(String line) {
		this.line = line;
	}
	public int getLineNumber()
	{
		int ii = Integer.parseInt(this.line.split(":")[0].replace(" ", ""));
		return ii;
	}
	public String getColonNumber()
	{
		int ii = Integer.parseInt(this.line.split(":")[1].replace(" ", ""));
		return String.valueOf(ii);
	}
	public int getOrderNumber()
	{
		return this.order;
	}
	@Override
	public String toString()
	{
		return this.word+" = "+this.id;
	}
}
