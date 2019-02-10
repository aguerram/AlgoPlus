package compiler.Interpreter;

import java.util.HashMap;

import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class PourInter extends MainInter {
	private HashMap<String,Variable> declaredVariables;
	public PourInter(int start) {
		super(start);
		declaredVariables = Global.declaredVariables;
		String varname = Global.tokens_word.get(start + 1);
		if(Interpreter.insideFunction)
		{
			varname = Global.getUniqueId(varname, Interpreter.funcName);
		}
		//Expresion to start
		String startExpr = this.getExpression(start + 4);
		//Expresion to stop
		int nextPos = getNextPos(start + 4, declaredVariables)+1;
		String stopExpr = this.getExpression(nextPos);
		
		
		String PAS = "1";
		//check if Pas keyword is exist
		nextPos = getNextPos(nextPos+1, declaredVariables);
		if(Global.tokens.get(nextPos).getId() == Tokens.PAS)
		{
			PAS = this.getExpression(nextPos+1);
		}
		
		
		//Check first if Pas is positive or negative
		//Positive
		addInstr("if");
		addInstr(PAS+">0");
		addInstr("set");
		addInstr(varname+" "+startExpr+"-1");
		addInstr("endSet");
		addInstr("else");
		addInstr("set");
		addInstr(varname+" "+startExpr+"+1");
		addInstr("endSet");
		addInstr("endIf");
		
		
		addInstr("loop");
		//i+1 < 10
		addInstr("("+PAS+">0 && "+varname+" + "+PAS+" <= "+stopExpr+") || ("+PAS+"< 0 && "+varname+" + "+PAS+" >= "+stopExpr+")");
		//System.out.println(startExpr+" < "+stopExpr);
		addInstr("set");
		addInstr(varname + " " + varname + " + "+PAS);
		addInstr("endSet");
	}

}
