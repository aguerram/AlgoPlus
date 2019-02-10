package compiler.Interpreter;

public class RepeterInter extends MainInter {

	public RepeterInter(int start) {
		super(start);
		addInstr("loop");
		
		addInstr("true");
	}

}
