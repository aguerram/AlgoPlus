package compiler.syntaxe;

import java.util.ArrayList;

import compiler.error.ErrorHandler;
import compiler.exceptions.CantGetEndNumber;
import compiler.global.Function;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Token;
import compiler.lexical.Tokens;
import jdbc.FileHandler;

public abstract class Keyword {
	protected int start;
	protected int end;
	protected Token currentToken;
	protected int functionArguments;
	public Keyword(int j)
	{
		this.start = j;
		this.functionArguments = 0;
		try {
			this.end = getTillSemiColon();
		}
		catch(CantGetEndNumber e)
		{
			
		}
		catch(Exception ex)
		{
			ex.getMessage();
		}
		try {
			currentToken = Global.tokens.get(start);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
			new FileHandler().log(ex);
			currentToken = null;
		}
	}
	protected String[] getVariablesFromTableau(String expr) {
		expr = expr.substring(expr.indexOf("["), expr.length());
		ArrayList<String> list = new ArrayList<String>();
		String[] n = expr.split("\\[");
		int[][] l = new int[2][3];
		for (String str : n) {
			if (str.length() > 1) {
				str = str.substring(0, str.length() - 1);
				list.add(str);
			}

		}
		String ss[] = new String[list.size()];
		for(int i = 0;i<list.size();i++)
		{
			ss[i] = list.get(i);
		}
		return ss;
	}
	protected boolean checkOpenSquareBracketVsClosed(String in)
	{
		int countOP = 0;
		for(int i = 0;i<in.length();i++)
		{
			char c = in.charAt(i);
			if(c == '[')
			{
				countOP++;
				continue;
			}
			else if(c == ']')
			{
				countOP--;
				continue;
			}
		}
		return countOP == 0;
	}
	protected ArrayList<Integer> getFunctionArguments(int start)
	{
		ArrayList<Integer> sb = new ArrayList<Integer>();
		ArrayList<Token> al = Global.tokens;
		int openB = 0;
		for(int i = start;i<al.size();i++)
		{
			int id = al.get(i).getId();
			if(id == Tokens.OPENBRACKET)
			{
				openB++;
				continue;
			}
			else if(id == Tokens.CLOSBRACKET && openB == 0)
			{
				break;
			}
			else if(id == Tokens.CLOSBRACKET)
			{
				openB--;
				continue;
			}
			sb.add(al.get(i).getId());
		}
		return sb;
	}
	private int getTillSemiColon() throws CantGetEndNumber
	{
		for(int i = start;i<Global.tokens.size();i++)
		{
			if(Global.tokens.get(i).getId() == Tokens.SEMICOLON || Global.tokens.get(i).getId() == Tokens.NEWLINE)
			{
				return i;
			}
		}
		throw new CantGetEndNumber();
	}
	protected ArrayList<Integer> TableGetDim(int pos)
	{
		//GET Dimentions
		ArrayList<Integer> size = new ArrayList<Integer>();
		while(true)
		{
			String var = Global.tokens_word.get(pos++).trim();
			if(var.equals(";") || var.equals(":") || var.equals(","))
				break;
			if(var.matches("^[0-9]+$"))
			{
				size.add(Integer.parseInt(var));
			}
		}	
		return size;
	}
	protected int nextLine()
	{
		for(int i = this.start;i<Global.tokens.size();i++)
		{
			if(Global.tokens.get(i).getId() == Tokens.NEWLINE)
				return i;
		}
		return Global.tokens.size()-1;
	}
	protected boolean isExpresion(int id)
	{
		switch(id)
		{
		case Tokens.ID:
		case Tokens.CHAR:
		case Tokens.WORD:
		case Tokens.INTEGER:
		case Tokens.FUNCTION:
		case Tokens.NUMBER:return true;
		}
		return false;
	}
	protected boolean conditionSymbole(int id)
	{
		return id == Tokens.EQULAS || id == Tokens.NOTEQULAS || id == Tokens.MORETHEN || 
				id == Tokens.MORETHENOREQUALS || id == Tokens.LESSTEHN || id == Tokens.LESSTEHNOREQUALS;
	}
	protected boolean isType(int id)
	{
		switch(id)
		{
		case Tokens.ENTIER:
		case Tokens.REEL:
		case Tokens.CARACTERE:
		case Tokens.CHAINE:
		case Tokens.BOOLEEN:return true;
		}
		return false;
	}
	
	protected boolean inDebutSection()
	{
		if(!Syntaxe.inDbutFunction)
		{
			ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.TokenInWrongSection);
			Syntaxe.index = this.nextLine();
			return false;
		}
		return true;
	}
	protected boolean inFunctionSection()
	{
		if(!Syntaxe.inFunctionSection)
		{
			ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.TokenInWrongSection);
			Syntaxe.index = this.nextLine();
			return false;
		}
		return true;
	}
	protected boolean inVariableSection()
	{
		if(!Syntaxe.inVariableSection)
		{
			ErrorHandler.lunch(Global.tokens.get(start),ErrorHandler.TokenInWrongSection);
			Syntaxe.index = this.nextLine();
			return false;
		}
		return true;
	}
	
	
	protected int getTypeEquivalent(int type) {

		switch (type) {
		case Tokens.ENTIER:
		case Tokens.REEL:
		case Tokens.NUMBER:
		case Tokens.INTEGER:
			return Tokens.NUMBER;
		case Tokens.CARACTERE:
		case Tokens.CHAINE:
		case Tokens.WORD:
		case Tokens.CHAR:
			return Tokens.WORD;
		case Tokens.BOOLEEN:
			return Tokens.BOOLEEN;
		}
		return -1;
	}
	protected int toTypeToken(int id,String word)
	{
		if(id == Tokens.ID)
		{
			Variable var = Global.declaredVariables.get(word.trim());
			if(var != null)
				id = var.getType();
		}
		switch(id)
		{
			case Tokens.ENTIER:
			case Tokens.INTEGER:return Tokens.ENTIER;
			case Tokens.REEL:
			case Tokens.NUMBER:return Tokens.REEL;
			case Tokens.WORD:
			case Tokens.CHAINE:return Tokens.CHAINE;
			case Tokens.CHAR:
			case Tokens.CARACTERE:return Tokens.CARACTERE;
			case Tokens.BOOLEEN:return id;
		}
		return -1;
	}
	protected Token getBeforeBracket(int st) {
		Token to = Global.tokens.get(st);
		int id = to.getId();
		while (id == Tokens.CLOSBRACKET || id == Tokens.OPENBRACKET) {
			--st;
			if(st<0)
				return new Token(-1, "","");
			to = Global.tokens.get(st);
			id = to.getId();
		}
		return to;
	}

	protected Token getAfterBracket(int st) {
		Token to = Global.tokens.get(st);
		int id = to.getId();
		
		while (id == Tokens.CLOSBRACKET || id == Tokens.OPENBRACKET) {
			++st;
			if(st >= Global.tokens.size())
				return new Token(-1, "","");
			to = Global.tokens.get(st);
			id = to.getId();
		}
		return to;
	}
	
	//FIXME This is connected to the IDE
	public String getOpenedTabTitle()
	{
		String title = "";
		
		
		
		return "";
	}
	 
}
