package compiler.global;

import java.util.ArrayList;
import java.util.HashMap;

import compiler.Interpreter.Interpreter;
import compiler.Interpreter.RunTheProgram;
import compiler.error.ErrorHandler;
import compiler.lexical.Token;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class Global {
	//loop's speed on AlgoPlus 0 default 
	public static final int loopSpeed = 20;
	public static HashMap<String,Integer> reservedWords;
	public static HashMap<String,Variable> declaredVariables;
	public static HashMap<String,Variable> FunctiondeclaredVariables;
	public static HashMap<String,Variable> declaredConstantes;
	public static HashMap<String,Function> declaredFunctions;
	public static HashMap<String,Array> declaredTables;
	public static String ProgramTitle = "";
	public static ArrayList<Token> tokens;
	public static ArrayList<String> tokens_word;
	public static ArrayList<String> word;
	
	public static double startedTime = 0;
	public static double endedTime = 0;
	public static char separators[] = {
			',','/','*','-','+',';','\n','\t',' ','%','<','[',']','=','>','&','(',')','{','}','"','\'',':','^','!'
	};
	public static char separatorsSpecial[] = {
			',','/','*','-','+',';','\n','\t',' ','%','<','=','>','&','(',')','{','}','"','\'',':','^','!' //Dosen't Contains '[' and ']'
	};
	public static final String[] KEYWORDS = new String[] {TokensWords.ALGORITHME, TokensWords.DEBUT, TokensWords.CONSTANTES
			,TokensWords.VARIABLES, TokensWords.FIN,TokensWords.TABLEAU,TokensWords.FONCTION,TokensWords.PROCEDURE,TokensWords.FINFONCTION,
			TokensWords.FINPROCEDURE,TokensWords.RETOURNE};
		
	public static final String[] SECONDARYKEYWORDS = new String[] {
			TokensWords.ECRIRE,TokensWords.LIRE,TokensWords.A,TokensWords.ALLANT,TokensWords.ALORS,
			TokensWords.BOOLEEN,TokensWords.CARACTERE,TokensWords.CHAINE,TokensWords.DE,TokensWords.ENTIER,TokensWords.ET,TokensWords.FAIRE
			,TokensWords.FAUX,TokensWords.FINPOUR,TokensWords.FINSI,TokensWords.FINTANTQUE,TokensWords.JUSQUA,TokensWords.OU,
			TokensWords.POUR,TokensWords.RACINE,TokensWords.REEL,TokensWords.REPETER,TokensWords.SI,TokensWords.SINON,
			TokensWords.TANTQUE,TokensWords.VARIABLES,TokensWords.VRAIS,TokensWords.PAS
	};
	public static final String IdRegExp = "^[a-zA-Z_][a-zA-Z0-9_]*[\\[\\d\\w*\\]]*$";
	static {
		startStatic();
	}
	public static void startStatic()
	{
		ErrorHandler.ErrorData.clear();
		ErrorHandler.error.clear();
		reservedWords = new HashMap<String,Integer>();
		declaredTables = new HashMap<String,Array>();
		declaredVariables = new HashMap<String,Variable>();
		FunctiondeclaredVariables = new HashMap<String,Variable>();
		declaredConstantes = new HashMap<String,Variable>();
		declaredFunctions = new HashMap<String,Function>();
		tokens = new ArrayList<Token>();
		tokens_word = new ArrayList<String>();
		RunTheProgram.waitingUserInput=false;
		ProgramTitle = "";
		
		Interpreter.endfunctionsPositions = new HashMap<String, Integer>();
		Interpreter.functionsPositions = new HashMap<String, Integer>();
		Interpreter.funcName = "";
		Interpreter.insideFunction = false;
		Interpreter.argsList = null;
		Interpreter.inVariablesBlock = false;
		Interpreter.inDebutBlock = false;
		Interpreter.conditionBlock = false;
		Interpreter.index = 0;
		Interpreter.lastIndex = 0;
		Interpreter.returnedValue = null;
		
		//Boolean add
		declaredVariables.put(TokensWords.FAUX, new Variable(Tokens.BOOLEEN,"false"));
		declaredVariables.put(TokensWords.VRAIS, new Variable(Tokens.BOOLEEN,"true"));
		reservedWords.put(TokensWords.FAUX, Tokens.BOOLEEN);
		reservedWords.put(TokensWords.VRAIS, Tokens.BOOLEEN);
		
		
		inisializeRacineFunction();
	}
	
	//global functions
	
	public static boolean inArray(char find,char[] array)
	{
		for(int i = 0;i<array.length;i++)
		{
			if(find == array[i])
				return true;
		}
		return false;
	}
	public static boolean inArray(int find,int[] array)
	{
		for(int i = 0;i<array.length;i++)
		{
			if(find == array[i])
				return true;
		}
		return false;
	}
	public static int count(String find,String str)
	{
		int found = 0;
		for(int i = 0;i<str.length();i++)
		{
			try {
				if(find.equals(str.substring(i,i+ find.length())))
				{
					found++;
				}
			}
			catch(Exception ex) {}
		}
		return found;
	}
	private static void inisializeRacineFunction()
	{
		ArrayList<Integer> argList = new ArrayList<Integer>();
		argList.add(Tokens.REEL);
		Function fnc = new Function(argList, Tokens.REEL,null);
		declaredFunctions.put(TokensWords.RACINE, fnc);
		reservedWords.put(TokensWords.RACINE, Tokens.FUNCTION);
	}
	//This method is to get an unique id inside a function
	public static String getUniqueId(String varname,String function)
	{
		//__func_3927_function_varname
		int size = function.length();
		String id = size+""+(size*size)+""+(size*size*2);
		
		return "__func_"+id+"_"+function+"_"+varname;
	}
}
