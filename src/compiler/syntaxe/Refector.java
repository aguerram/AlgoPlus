package compiler.syntaxe;

import compiler.Interpreter.Interpreter;
import compiler.Interpreter.MainInter;
import compiler.Interpreter.RunTheProgram;
import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class Refector extends MainInter {

	public Refector(int start) {
		super(start);
		if (this.inDebutSection()) {
			String variableName = Global.tokens_word.get(start - 1).trim();
			//Check if we're inside a fuction, to change the variables name to an unique name
			if(Syntaxe.inFunctionSection && ! Syntaxe.MainFunctionFounded)
			{
				
				variableName = Global.getUniqueId(variableName, Syntaxe.currentFunctionName);
			}
			int typeOfID = -1;
			int typeOfExp = -1;
			try {
				variableName = removeVariablesFromTable(variableName,null);
				if(Global.declaredVariables.get(variableName) != null)
				{
					typeOfID = Global.declaredVariables.get(variableName).getType();
				}
				
				switch (typeOfID) {
				case Tokens.ENTIER:
				case Tokens.REEL:
					typeOfID = Tokens.NUMBER;
					break;
				case Tokens.CHAINE:
					typeOfID = Tokens.WORD;
					break;
				case Tokens.CARACTERE:
					typeOfID = Tokens.CHAR;
					break;
				case Tokens.BOOLEEN:
					typeOfID = Tokens.BOOLEEN;
					break;
				default:
					typeOfID = -1;
				}
			} catch (Exception ex) {
				typeOfID = -1;
			}
			try {
				Token after = getAfterBracket(start + 1);
				if (after.getId() == Tokens.ID || after.getId() == Tokens.FUNCTION) {
					String vna = after.getWord();
					if(Syntaxe.inFunctionSection && ! Syntaxe.MainFunctionFounded)
					{
						vna = Global.getUniqueId(vna, Syntaxe.currentFunctionName);
					}
					vna = removeVariablesFromTable(vna,null);
					if(Global.declaredVariables.get(vna) != null)
					{
						typeOfExp = Global.declaredVariables.get(vna).getType();
					}
					else {
						if(Global.declaredFunctions.get(vna) != null)
						{
							typeOfExp = Global.declaredFunctions.get(vna).getReturnType();
						}
					}
					switch (typeOfExp) {
					case Tokens.ENTIER:
					case Tokens.REEL:
						typeOfExp = Tokens.NUMBER;
						break;
					case Tokens.CHAINE:
						typeOfExp = Tokens.WORD;
						break;
					case Tokens.CARACTERE:
						typeOfExp = Tokens.CHAR;
						break;
					case Tokens.BOOLEEN:
						typeOfExp = Tokens.BOOLEEN;
						break;
					default:
						typeOfExp = -1;
					}
				} else {
					typeOfExp = Global.tokens.get(pos).getId();
					if (typeOfExp == 54)
						typeOfExp = 53;
				}
			} catch (Exception ex) {
				typeOfExp = -1;
			}
			String expr = new String(this.Expression(this.getExpression(start + 1)));
			if ((expr.contains(">") || expr.contains("<") || expr.contains("=") || expr.contains("||")
					|| expr.contains("&&") || expr.contains("false") || expr.contains("true"))
					&& !(expr.contains("\"") || expr.contains("'"))) {
				typeOfExp = Tokens.BOOLEEN;
			}

			if (typeOfID!=-1 && typeOfExp != typeOfID) {
				ErrorHandler.Expected(Global.tokens.get(start), false, ErrorHandler.ExpectedTheSameTypeAsBefor);
			}
			int id = Global.tokens.get(start - 1).getId();
			if (id != Tokens.ID) {
				ErrorHandler.Expected(Global.tokens.get(start), true, ErrorHandler.Identifier);
			} else {
				// check if the id is constantes
				String idd = Global.tokens_word.get(start - 1);
				if (Global.declaredConstantes.get(idd) != null) {
					ErrorHandler.lunch(Global.tokens.get(start - 1), ErrorHandler.ConstantesCantBeChanged);
				}
			}
			// after
			id = Global.tokens.get(start + 1).getId();
			if (id != Tokens.OPENBRACKET && !Global.tokens_word.get(start + 1).equals("-")
					&& !this.isExpresion(Global.tokens.get(start + 1).getId())) {
				ErrorHandler.Expected(Global.tokens.get(start), false, ErrorHandler.Expression);
			}
		}
	}

	protected Token getAfterBracket(int st) {
		Token to = Global.tokens.get(st);
		int id = to.getId();

		while (id == Tokens.CLOSBRACKET || id == Tokens.OPENBRACKET || Global.tokens_word.get(st).equals("-")) {
			++st;
			if (st >= Global.tokens.size())
				return new Token(-1, "", "");
			to = Global.tokens.get(st);
			id = to.getId();
		}
		pos = st;
		return to;
	}

	private int pos = 0;
}
