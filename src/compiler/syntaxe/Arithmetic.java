package compiler.syntaxe;

import compiler.Interpreter.Interpreter;
import compiler.Interpreter.MainInter;
import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.lexical.Token;
import compiler.lexical.Tokens;

public class Arithmetic extends MainInter {

	public Arithmetic(int start) {
		super(start);

		this.inDebutSection();
		// check before
		String arith = currentToken.getWord();
		try {
			
			if(!arith.equals("-"))
			{
				Token to = Global.tokens.get(start - 1);
				if (to.getId() != Tokens.ID && !isExpresion(to.getId()) && to.getId() != Tokens.OPENBRACKET
						&& to.getId() != Tokens.CLOSBRACKET && to.getId() != Tokens.INCREMENT
						&& to.getId() != Tokens.DECREASE) {
					ErrorHandler.Expected(Global.tokens.get(start), true, ErrorHandler.Expression);
				}
			}
			
		} catch (Exception ex) {
			ErrorHandler.Expected(Global.tokens.get(start), true, ErrorHandler.Expression);
			ex.printStackTrace();
		}
		// check after
		try {
			Token to = Global.tokens.get(start + 1);
			if (to.getId() != Tokens.ID && !isExpresion(to.getId()) && to.getId() != Tokens.CLOSBRACKET
					&& to.getId() != Tokens.OPENBRACKET && to.getId() != Tokens.INCREMENT
					&& to.getId() != Tokens.DECREASE && to.getId() != Tokens.FUNCTION) {
				ErrorHandler.Expected(Global.tokens.get(start), false, ErrorHandler.Expression);
			}

		} catch (Exception ex) {
			ErrorHandler.Expected(Global.tokens.get(start), true, ErrorHandler.Expression);
			ex.printStackTrace();
		}

		// Check if they have same type
		Token startT = getBeforeBracket(start - 1);
		Token endT = getAfterBracket(start + 1);
		
		if(!arith.equals("-") || true)
		{
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

				if (!valideExpression(getTypeEquivalent(sss), getTypeEquivalent(eee))) {
					ErrorHandler.Expected(Global.tokens.get(start), true, ErrorHandler.ExpectedTheSameTypeAsBefor);
				} else {
					if(sss == Tokens.BOOLEEN || eee == Tokens.BOOLEEN)
						ErrorHandler.lunch(currentToken,ErrorHandler.CantDoArithmeticOperationsBooleen);
					else if (getTypeEquivalent(sss) == Tokens.WORD) {
						if (!currentToken.getWord().trim().equals("+")) {
							ErrorHandler.Expected(Global.tokens.get(start + 1), true, "+");
						}
					}
					
				}
			}
		}
		else {
			if(endT.getId() != Tokens.NUMBER && endT.getId() != Tokens.INTEGER && endT.getId() != Tokens.ID)
			{
				ErrorHandler.Expected(Global.tokens.get(start), false, ErrorHandler.Number);
			}
		}
	}

	private boolean valideExpression(int exp1, int exp2) {
		return exp1 == exp2;
	}

	
}
