package compiler.exceptions;

public class CantGetEndNumber extends Exception{
	private static final long serialVersionUID = -9058685293392256661L;

	public CantGetEndNumber()
	{
		super(Exceptions.CANTGETEND);
	}
}
