package compiler.syntaxe;

import compiler.Interpreter.MainInter;
import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class ConditionSymbole extends MainInter{

	public ConditionSymbole(int j) {
		
		super(j);
		if(this.inDebutSection())
		{
			int idf = Global.tokens.get(start-1).getId();
			if(idf != Tokens.ID && idf != Tokens.NUMBER && idf != Tokens.WORD && 
					idf != Tokens.CHAR && idf != Tokens.INTEGER && idf != Tokens.FALSE && idf != Tokens.TRUE)
			{
				ErrorHandler.Expected(currentToken, true, ErrorHandler.Identifier);
			}
			
			idf = Global.tokens.get(start+1).getId();
			
			if(idf != Tokens.ID && idf != Tokens.NUMBER && idf != Tokens.WORD && 
					idf != Tokens.CHAR && idf != Tokens.INTEGER && idf != Tokens.FALSE && idf != Tokens.TRUE)
			{
				ErrorHandler.Expected(currentToken, true, ErrorHandler.Identifier);
			}
			
			// Check if they have same type
			
			
			Token startT = getBeforeBracket(start - 1);
			Token endT = getAfterBracket(start + 1);
			if (isExpresion(startT.getId()) && isExpresion(endT.getId())) {
				int sss = startT.getId();
				int eee = endT.getId();
				if (sss == Tokens.ID) {
					try {
						String vss = startT.getWord();
						if(Syntaxe.inFunctionSection && ! Syntaxe.MainFunctionFounded)
						{
							vss = Global.getUniqueId(vss, Syntaxe.currentFunctionName);
						}
						vss = removeVariablesFromTable(vss,null);
						sss = Global.declaredVariables.get(vss).getType();
					} catch (Exception x) {
					}
				}
				else if(sss == Tokens.FUNCTION)
				{
					try {
						String funcName = startT.getWord().trim();
						sss = Global.declaredFunctions.get(funcName).getReturnType();
					}
					catch(Exception ex)
					{
						sss = -1;
					}
				}
				if (eee == Tokens.ID) {
					try {
						String ess = endT.getWord();
						if(Syntaxe.inFunctionSection && ! Syntaxe.MainFunctionFounded)
						{
							ess = Global.getUniqueId(ess, Syntaxe.currentFunctionName);
						}
						ess = removeVariablesFromTable(ess,null);
						eee = Global.declaredVariables.get(ess).getType();
					} catch (Exception x) {
					}
				}
				else if(eee == Tokens.FUNCTION)
				{
					try {
						String funcName = endT.getWord().trim();
						eee = Global.declaredFunctions.get(funcName).getReturnType();
					}
					catch(Exception ex)
					{
						eee = -1;
					}
				}

				if (getTypeEquivalent(sss) !=  getTypeEquivalent(eee)) {
					ErrorHandler.Expected(Global.tokens.get(start), true, ErrorHandler.ExpectedTheSameTypeAsBefor);
				} else {
					 if (getTypeEquivalent(sss) == Tokens.WORD) {
						if (!currentToken.getWord().trim().equals("==") && !currentToken.getWord().trim().equals("!=")) {
							ErrorHandler.Expected(Global.tokens.get(start + 1), true, "==");
						}
					}	
				}
			}
			else {
				if(!isExpresion(startT.getId()))
					ErrorHandler.Expected(currentToken, true, ErrorHandler.Expression);
				if(!isExpresion(endT.getId()))
					ErrorHandler.Expected(currentToken, false, ErrorHandler.Expression);
				
			}
		}
	}
	
}
