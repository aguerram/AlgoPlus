package compiler.Interpreter;

public class TantQueInter extends MainInter {

	public TantQueInter(int start) {
		super(start);
		addInstr("loop");
		String exp = this.getExpressionCalled(start+1,false);
		addInstr(exp);
		
	}

}
