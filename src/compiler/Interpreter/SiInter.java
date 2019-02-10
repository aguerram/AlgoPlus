package compiler.Interpreter;

import compiler.global.Global;
import compiler.lexical.Tokens;

public class SiInter extends MainInter {

	public SiInter(int start) {
		super(start);
		
		String exp = this.getExpressionCalled(start+1,false);
		addInstr("if");
		addInstr(exp+"");
	}
}
