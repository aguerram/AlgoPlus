package compiler.syntaxe;

import java.util.HashMap;

import compiler.Interpreter.MainInter;
import compiler.error.ErrorHandler;
import compiler.global.Global;
import compiler.global.Variable;
import compiler.lexical.Tokens;
import compiler.lexical.TokensWords;

public class Pour extends MainInter {
	private HashMap<String,Variable> declaredVariables;
	private HashMap<String,Variable> declaredConstantes;
	public Pour(int start) {
		super(start);
		declaredVariables = Global.declaredVariables;
		declaredConstantes = Global.declaredConstantes;
		if(this.inDebutSection())
		{
			// Pour i Allant de N a 20 Pas 1 Faire
			int added = 0;
			int id = Global.tokens.get(start+1).getId(); 
			if( id == Tokens.ID)
			{
				String varname = Global.tokens_word.get(start+1);
				
				Variable var = declaredVariables.get(varname);
				
				if(var != null)
				{
					
					if(var.getType() == Tokens.ENTIER)
					{
						if(this.declaredConstantes.get(varname) != null)
						{
							ErrorHandler.lunch(Global.tokens.get(start+1), ErrorHandler.ConstantesCantBeChanged);
						}
						
						id = Global.tokens.get(start+2).getId();
						if(id == Tokens.ALLANT)
						{
							if(Global.tokens.get(start+3).getId() == Tokens.DE)
							{	
								id = Global.tokens.get(start+4).getId();
								int nextPos = this.getNextPos(start+4,declaredVariables);
								
								if(start+4 != nextPos)
								{
									id = Global.tokens.get(nextPos).getId();
									if(id == Tokens.A)
									{
										int lastPos = nextPos+1;
										nextPos = this.getNextPos(lastPos,declaredVariables);
										if(lastPos != nextPos)
										{
											//Pas is optional so it will be there a condition to check if Pas is exist
											//if it this it has it's one traiting 
											lastPos = nextPos+1;
											id = Global.tokens.get(nextPos).getId();
											if(id == Tokens.PAS)
											{
												nextPos = this.getNextPos(lastPos,declaredVariables);
												if(nextPos != lastPos)
												{
													lastPos = nextPos+1;
													id = Global.tokens.get(nextPos).getId();
													if(id != Tokens.FAIRE)
													{
														ErrorHandler.Expected(Global.tokens.get(nextPos-1), false, TokensWords.FAIRE);
													}
													
												}
												else {
													ErrorHandler.Expected(Global.tokens.get(nextPos-1), false, ErrorHandler.Expression);
												}
											}
											else {
												//Pas isn't found 
												if(id == Tokens.FAIRE)
												{
													
												}
												else {
													ErrorHandler.Expected(Global.tokens.get(lastPos), false, TokensWords.FAIRE);
												}
											}
										}
										else {
											ErrorHandler.Expected(Global.tokens.get(lastPos), false, ErrorHandler.Expression);
										}
										
									}
									else {
										ErrorHandler.Expected(Global.tokens.get(nextPos-1), false,TokensWords.A);
									}
								}
								else {
									ErrorHandler.Expected(Global.tokens.get(start+3), false, ErrorHandler.Expression);
								}
							}
							else {
								ErrorHandler.Expected(Global.tokens.get(start+3), false, TokensWords.DE);
							}
						}
						else {
							ErrorHandler.Expected(Global.tokens.get(start+2), false, TokensWords.ALLANT);
						}
					}
					else {
						ErrorHandler.lunch(Global.tokens.get(start+1), ErrorHandler.VariableTypeMustBeInteger+" '"+varname+"'");
					}
				}
			}
			else {
				ErrorHandler.Expected(currentToken, false, ErrorHandler.Identifier);
			}
		}
	}
	
}
