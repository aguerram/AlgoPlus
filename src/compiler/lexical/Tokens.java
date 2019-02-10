package compiler.lexical;

public final class Tokens {
	//Declaration types
	public final static int ENTIER = 1;
	public final static int REEL = 2;
	public final static int CARACTERE = 3; 
	public final static int CHAINE = 4; 
	public final static int BOOLEEN = 5; 
	public final static int TABLEAU = 6; 
	
	//Main words
	public final static int ALGORITHME = 10;
	public final static int VARIABLES = 11;
	public final static int CONSTANTES = 12;
	public final static int DEBUT = 13;
	public final static int FIN = 14;
	
	//Reserved functions
	public final static int ECRIRE = 20;
	public final static int LIRE = 21;
	
	//Loops
	public final static int POUR = 30;
	public final static int TANTQUE = 31;
	public final static int REPETER = 32;
	public static final int FAIRE = 33;
	public static final int FINTANTQUE = 34;
	public static final int A = 35;
	public static final int ALLANT = 36;
	public static final int FINPOUR = 37;
	public static final int DE = 38;
	public static final int JUSQUA = 39;
	public static final int PAS = 200;
	
	//CONDITON
	public final static int SI = 40;
	public final static int SINON = 41;
	public final static int FINSI = 42;
	public final static int ALORS = 43;
	
	//OTHER
	public final static int CHAR = 50;
	public final static int WORD = 51;
	public final static int ID = 52;
	public final static int IDArray = 153;
	public final static int FUNCTION = 154;
	public final static int NUMBER = 53;
	public final static int INTEGER = 54;
	public final static int EXPR = 55;
	public final static int NEWLINE = 56;
	public final static int NULL = 58;
	public final static int SPACE = 59;
	
	
	//SYMBOLES
	public final static int ARITHMETIC = 60;
	public final static int REFECTOR = 61;
	public final static int SEMICOLON = 62;
	public final static int COMMA = 63;
	public final static int OPENBRACKET = 64;
	public final static int CLOSBRACKET = 65;
	public final static int OPENSQUAREBRACKET = 166;
	public final static int CLOSESQUAREBRACKET = 167;
	public final static int TOWPOINTS = 66;
	public final static int INCREMENT = 67;
	public final static int DECREASE = 68;
	
	
	//boolean
	
	public final static int TRUE = 70;
	public final static int FALSE = 71;
	
	public final static int EQULAS = 72;
	public final static int NOTEQULAS = 73;
	public final static int MORETHEN = 74;
	public final static int MORETHENOREQUALS = 75;
	public final static int LESSTEHN = 76;
	public final static int LESSTEHNOREQUALS = 77;
	
	public final static int AND = 78;
	public final static int OR = 79;
	public final static int XOR = 80;
	
	//errors 
	public final static int ExpectedQuote = 100;
	public final static int ExpectedDoubleQuote = 101;
	
	//funcion
	public final static int FONCTION = 110;
	public final static int RETOURNE = 111;
	public final static int PROCEDURE = 112;
	public final static int FINPROCEDURE = 113;
	public final static int FINFONCTION = 114;
	
	
}
