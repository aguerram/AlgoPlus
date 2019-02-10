package compiler.Interpreter;

import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class RefectorInter extends MainInter {

	public RefectorInter(int start) {
		super(start);
		String varname = "";
		String cmd = "", expression = "", expressionCal = "";
		try {
			varname = Global.tokens_word.get(start - 1);
			if(Interpreter.insideFunction)
			{
				varname = Global.getUniqueId(varname, Interpreter.funcName);
			}
			expression = getExpression(start + 1);
			
			cmd = varname + " " + expression;
			//expressionCal = new String(Expression(expression));
			expressionCal = expression;
			if (expressionCal.contains("\"") || expressionCal.contains("'")) {
				try {
					
					cmd = varname + " " + expressionCal;
				} catch (Exception xx) {
					xx.printStackTrace();
				}
			} else {
				try {
					
					cmd = varname + " " + expressionCal;
					
				} catch (Exception ex) {

				}
			}

			addInstr("set");
			addInstr(cmd);
			addInstr("endSet");
		} catch (Exception ex) {

		}
	}
}
