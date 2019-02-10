package compiler.syntaxe;

public class Ecrire extends Keyword {
	public Ecrire(int id)
	{	
		super(id);
		this.inDebutSection();
		this.functionArguments = 100;
		new FunctionArgSyntaxe(start+1,functionArguments);
	}
}
