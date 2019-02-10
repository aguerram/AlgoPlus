package compiler.Interpreter;

import java.util.HashMap;

import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class Interpreter {
	public static HashMap<Integer,String> Instructions;
	
	//function attributes
	public static HashMap<String, Integer> functionsPositions = new HashMap<String, Integer>();
	public static HashMap<String, Integer> endfunctionsPositions = new HashMap<String, Integer>();
	public static String argsList[] = null;
	//-----------
	public static boolean inVariablesBlock = false;
	public static int index = 0;
	public static boolean inDebutBlock = false;
	public static boolean conditionBlock = false;
	
	public static String returnedValue = null;
	public static int lastIndex = 0;
	//Variables to make some modifications on variables inside a function
	public static boolean insideFunction = false;
	public static String funcName = "";
	
	
	static {
		Instructions = new HashMap<Integer,String>();
	}
	public Interpreter()
	{
		
	}
	public void start()
	{
		System.out.println("Running ... \n#########################\n");
		for(index = 0;index<Global.tokens.size();index++)
		{
			int i = index;
			Token t = Global.tokens.get(i);
			switch(t.getId())
			{
				case Tokens.ALGORITHME:
					Instructions.put(size(), "start");
					Instructions.put(size(), "write");
					String ssn = "Program : "+Global.tokens_word.get(i+1);
					StringBuffer sb = new StringBuffer();
					for(int j= 0;j<ssn.length();j++)
						sb.append("-");
					
					Instructions.put(size(), ssn+"\n"+ new String(sb) +"\n");
					Instructions.put(size(),"'\n'");
					Instructions.put(size(), "endWrite");
					i++;
					break;
				case Tokens.VARIABLES:
					inVariablesBlock = true;
					inDebutBlock = false;
					break;
				case Tokens.FIN:
					inVariablesBlock = false;
					inDebutBlock = false;
					break;
				case Tokens.DEBUT:
					inVariablesBlock = false;
					inDebutBlock = true;
					break;
				case Tokens.ECRIRE:
					new EcrireInter(i);
					break;
				case Tokens.LIRE:
					new LireInter(i);
					break;
				case Tokens.REFECTOR:
					new RefectorInter(i);
					break;
				case Tokens.EQULAS:
					new EqualsInter(i);
					break;
				case Tokens.ID:
					if(inVariablesBlock == true)
					{
						new IdVariables(i);
					}
					break;
				case Tokens.SI:
					new SiInter(i);
					break;
				case Tokens.TANTQUE:
					new TantQueInter(i);
					break;
				case Tokens.FINTANTQUE:
					Instructions.put(Instructions.size(), "goto");
					Instructions.put(Instructions.size(), gotoIndex()+"");
					Instructions.put(Instructions.size(), "endLoop");
					break;
				case Tokens.FINSI:
					Instructions.put(Instructions.size(), "endIf");
					break;
				case Tokens.SINON:
					Instructions.put(Instructions.size(), "else");
					break;
				case Tokens.POUR:
					new PourInter(i);
					break;
				case Tokens.FINPOUR:
					Instructions.put(Instructions.size(), "goto");
					Instructions.put(Instructions.size(), gotoIndex()+"");
					Instructions.put(Instructions.size(), "endLoop");
					break;
				case Tokens.REPETER:
					new RepeterInter(i);
					break;
				case Tokens.JUSQUA:
					MainInter m = new MainInter(i);
					Instructions.put(Instructions.size(), "goto");
					Instructions.put(Instructions.size(), gotoIndex()+"");
					String exp = m.getExpressionCalled(i+1,false);
					Instructions.put(Instructions.size(), exp);
					Instructions.put(Instructions.size(), "endLoop");
					break;
				
				//Functions and procedure
				case Tokens.FUNCTION:
					String fname = Global.tokens.get(i).getWord();
					Instructions.put(size(),"function "+fname+" "+(functionsPositions.get(fname)-1)+"!"+i);
					break;
				case Tokens.FONCTION:
					insideFunction = true;
					funcName = Global.tokens.get(i+1).getWord();
					functionsPositions.put(funcName, size());
					index++;
					break;
				case Tokens.FINFONCTION:
					Instructions.put(Instructions.size(),"endFunction");
					insideFunction = false;
					endfunctionsPositions.put(funcName, size());
					funcName = "";
					
					break;
				case Tokens.RETOURNE:
					Instructions.put(Instructions.size(),"return "+new MainInter(0).getExpression(i+2));
					break;
				case Tokens.PROCEDURE:
					insideFunction = true;
					funcName = Global.tokens.get(i+1).getWord();
					functionsPositions.put(funcName, size());
					index++;
					break;
				case Tokens.FINPROCEDURE:
					Instructions.put(Instructions.size(),"endFunction");
					insideFunction = false;
					endfunctionsPositions.put(funcName, size());
					funcName = "";
			}
			
		}
		conditionBlock = false;
		returnedValue = null;
		lastIndex = 0;
		
	}
	private int gotoIndex()
	{
		int foundLoop = -1;
		for(int i = Instructions.size()-1;i>0;i--)
		{
			String v = Instructions.get(i);
			if(v.equals("loop"))
			{
				foundLoop++;
			}
			else if (v.equals("endLoop"))
			{
				foundLoop--;
			}
			else if(foundLoop == 0)
				return i;
		}
		return 0;
	}
	private int size()
	{
		return Instructions.size();
	}
	
}
