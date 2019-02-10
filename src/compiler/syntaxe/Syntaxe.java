package compiler.syntaxe;

import java.util.ArrayList;

import compiler.error.ErrorHandler;
import compiler.global.Array;
import compiler.global.Global;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class Syntaxe extends Keyword {
	//Save current function name
	public static compiler.global.Function currentFunction = null;
	public static String currentFunctionName = null;
	
	public static boolean inFunctionSection = false; // check if it's inside a function
	public static boolean retourneFounded = false; // check if retourne key work is founded
	
	public static boolean inVariableSection = false;
	public static boolean inDbutFunction = false;
	public static boolean inConstantesFunction = false;
	public static boolean MainFunctionFounded = false;
	public static boolean inTableauBlock = false;
	public static boolean procedure = false;
	
	public static boolean inFunctionArg = false; //check if Fonction keyword is declared
	
	public static int index = 0;

	
	public static int AlgorithmeCount = 0;
	public static int DeclareCount = 0;
	public static int DebutCount = 0;

	public static ArrayList<Integer> SiBlock = new ArrayList<Integer>();
	public static ArrayList<Integer> SiNonWord = new ArrayList<Integer>();
	public static ArrayList<Integer> TantQueBlock = new ArrayList<Integer>();
	public static ArrayList<Integer> PourBlock = new ArrayList<Integer>();
	public static ArrayList<Integer> RepeterBlock = new ArrayList<Integer>();


	public Syntaxe(String OpenedtabTitle) {
		super(0);
		try {
			int Bracket = 0;
			int lastBracketPos = 0;
			int lastBracketClosedPos = 0;

			for (index = 0; index < Global.tokens.size(); index++) {
				int j = index;
				switch (Global.tokens.get(j).getId()) {
			
				case Tokens.ECRIRE:
					new Ecrire(j);
					break;
				case Tokens.LIRE:
					new Lire(j);
					break;
				case Tokens.OPENBRACKET:
					Bracket++;
					lastBracketPos = j;
					new OpenBracket(j);
					;
					break;
				case Tokens.CLOSBRACKET:
					Bracket--;
					lastBracketClosedPos = j;
					new CloseBracket(j);
					;
					break;
				case Tokens.OPENSQUAREBRACKET:
					new OpenSquareBracket(j);
					break;
				case Tokens.CLOSESQUAREBRACKET:
					new CloseSquareBracket(j);
					break;
				case Tokens.COMMA:
					new Comma(j);
					try {
						if (Global.tokens.get(j + 1).getId() == Tokens.COMMA)
							j++;
					} catch (Exception ex) {
					}
					break;
				case Tokens.CHAR:
					new Char(j);
					break;
				case Tokens.TOWPOINTS:
					new TowPoints(j);
					break;
				case Tokens.ALGORITHME:
					new Algoithme(j,OpenedtabTitle);
					break;
				case Tokens.CONSTANTES:
					new Constantes(j);
					break;
				case Tokens.DEBUT:
					new Debut(j);
					break;
				case Tokens.WORD:
					new Word(j);
					break;
				case Tokens.VARIABLES:
					new Variables(j);
					break;
				case Tokens.FIN:
					new Fin(j);
					break;
				case Tokens.TABLEAU:
					new Tableau(j);
					break;
				case Tokens.ID:
					new Id(j);
					break;
				//Function variable
				case Tokens.FUNCTION:
					new Function(j);
					break;
				case Tokens.ARITHMETIC:
					new Arithmetic(j);
					break;
				case Tokens.NUMBER:
				case Tokens.INTEGER:
					if (inTableauBlock) {
						new ReelInTableau(j);
					} else
						new Real(j);
					break;
				case Tokens.INCREMENT:
				case Tokens.DECREASE:
					new IncrementDecrement(j);
					break;
				case Tokens.REFECTOR:
					
					new Refector(j);
					break;
				case Tokens.ENTIER:
				case Tokens.REEL:
				case Tokens.CHAINE:
				case Tokens.CARACTERE:
				case Tokens.BOOLEEN:
					new TypeKeyWord(j);
					break;
				case Tokens.EQULAS:
					if(this.inConstantesFunction)
						new Equals(j);
					else
						new ConditionSymbole(j);
					break;
				case Tokens.SI:
					SiBlock.add(j);
					new Si(j);
					break;
				case Tokens.SINON:
					new Sinon(j,SiBlock,SiNonWord);
					break;
				case Tokens.FINSI:
					if (inDbutFunction) {
						if (SiBlock.size() > 0)
							SiBlock.remove(SiBlock.size() - 1);
						else
							ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.UnExpectedToken+" '"+TokensWords.FINSI+"'");
					} else {
						ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.TokenInWrongSection);
					}
					break;
				case Tokens.TANTQUE:
					TantQueBlock.add(j);
					new TantQue(j);
					break;
				case Tokens.FINTANTQUE:
					if (inDbutFunction) {
						if (TantQueBlock.size() > 0)
							TantQueBlock.remove(TantQueBlock.size() - 1);
						else
							ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.UnExpectedToken+" '"+TokensWords.FINTANTQUE+"'");
					} else {
						ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.TokenInWrongSection);
					}
					break;
				case Tokens.REPETER:
					RepeterBlock.add(j);
					new Repeter(j);
					break;
				case Tokens.JUSQUA:
					new Jusqua(j);
					if (inDbutFunction) {
						if (RepeterBlock.size() > 0)
							RepeterBlock.remove(RepeterBlock.size() - 1);
						else
							ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.UnExpectedToken+" '"+TokensWords.FINTANTQUE+"'");
					} else {
						ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.TokenInWrongSection);
					}
					break;
				case Tokens.POUR:
					PourBlock.add(j);
					new Pour(j);
					break;
				case Tokens.RETOURNE:
					new Retourne(j);
					break;
				case Tokens.FINPOUR:
					if (inDbutFunction) {
						if (PourBlock.size() > 0)
							PourBlock.remove(PourBlock.size() - 1);
						else
							ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.UnExpectedToken+" '"+TokensWords.FINTANTQUE+"'");
					} else {
						ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.TokenInWrongSection);
					}
					break;
				//Fonction declaration
				case Tokens.FONCTION:
					new Fonction(j);
					break;
				case Tokens.FINFONCTION:
					new FinFonction(j);
					break;
				case Tokens.PROCEDURE:
					new Procedure(j);
					break;
				case Tokens.FINPROCEDURE:
					new FinProcedure(j);
					break;
				case Tokens.ExpectedQuote:
					ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.QuoteOpened);
					break;
				case Tokens.ExpectedDoubleQuote:
					ErrorHandler.lunch(Global.tokens.get(j), ErrorHandler.QuoteOpened);
					break;
				
				case -1:
					ErrorHandler.lunch(Global.tokens.get(j),
							ErrorHandler.UnExpectedToken + " '" + Global.tokens_word.get(j) + "'");
					break;
				default:
					
					if (this.conditionSymbole(Global.tokens.get(j).getId())) {
						new ConditionSymbole(j);
					}
				}
			}
			// Check other errors
			if (Bracket > 0) {
				ErrorHandler.lunch(Global.tokens.get(lastBracketPos), ErrorHandler.OpenBracketButNotClosed);
			} else if (Bracket < 0) {
				ErrorHandler.lunch(Global.tokens.get(lastBracketClosedPos), ErrorHandler.CloseBracketButNotClosed);
			}

			if (SiBlock.size() > 0) {
				SiBlock.forEach(e -> {
					ErrorHandler.lunch(Global.tokens.get(e), ErrorHandler.OpenedSiBlockButNotClosed);
				});
			}
			if (TantQueBlock.size() > 0) {
				TantQueBlock.forEach(e -> {
					ErrorHandler.lunch(Global.tokens.get(e), ErrorHandler.OpenedTantQueBlockButNotClosed);
				});
			}
			if (RepeterBlock.size() > 0) {
				RepeterBlock.forEach(e -> {
					ErrorHandler.lunch(Global.tokens.get(e), ErrorHandler.OpenedRepeteBlockButNotClosed);
				});
			}
			if (PourBlock.size() > 0) {
				PourBlock.forEach(e -> {
					ErrorHandler.lunch(Global.tokens.get(e), ErrorHandler.OpenedPourBlockButNotClosed);
				});
			}

			// check if Main function is founded
			if (!MainFunctionFounded) {
				ErrorHandler.lunch(ErrorHandler.MainFunctionNotFound);
			}
			// check if program closed
			if (this.inFunctionSection) {
				ErrorHandler.lunch(ErrorHandler.ExpectedFinIntTheEndOfProgram);
			}
			Syntaxe.inFunctionSection = false; // check if it's inside a function
			Syntaxe.inVariableSection = false;
			Syntaxe.inDbutFunction = false;
			Syntaxe.inConstantesFunction = false;
			Syntaxe.MainFunctionFounded = false;
			Syntaxe.inFunctionArg = false;
			Syntaxe.inTableauBlock = false;
			Syntaxe.retourneFounded = false;
			Syntaxe.procedure = false;
			Syntaxe.AlgorithmeCount = 0;
			Syntaxe.DeclareCount = 0;
			Syntaxe.DebutCount = 0;
			Syntaxe.index = 0;
			Syntaxe.SiBlock = new ArrayList<Integer>();
			Syntaxe.SiNonWord = new ArrayList<Integer>();
			Syntaxe.TantQueBlock = new ArrayList<Integer>();
			Syntaxe.PourBlock = new ArrayList<Integer>();
			Syntaxe.RepeterBlock = new ArrayList<Integer>();
			//
			Syntaxe.currentFunction = null;
			Syntaxe.currentFunctionName = null;
			
			Global.endedTime = System.currentTimeMillis();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
