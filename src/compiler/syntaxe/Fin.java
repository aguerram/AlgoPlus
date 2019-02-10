package compiler.syntaxe;

public class Fin extends Keyword {

	public Fin(int start) {
		super(start);
		if(this.inFunctionSection() && this.inDebutSection())
		{
			Syntaxe.inFunctionSection = false;
			Syntaxe.inDbutFunction = false;
			Syntaxe.inVariableSection = false;
		}
	}
	
}
