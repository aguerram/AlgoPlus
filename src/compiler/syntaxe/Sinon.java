package compiler.syntaxe;

import java.util.ArrayList;

import compiler.error.ErrorHandler;
import compiler.lexical.TokensWords;

public class Sinon extends Keyword {

	public Sinon(int j,ArrayList<Integer> SiBlock,ArrayList<Integer> Sinon) {
		super(j);
		//block[0] = SiBlock
		//block[1] = SiNon Word
		if(SiBlock.size()<1) 
		{
			ErrorHandler.lunch(currentToken, ErrorHandler.UnExpectedToken+" '"+TokensWords.SINON+"'");
		}
		else {
			if(Sinon.indexOf(SiBlock.get(SiBlock.size()-1)) != -1)
			{
				ErrorHandler.lunch(currentToken, ErrorHandler.SinonTokenWithoutPreviousSiToken);
			}
			else {
				Sinon.add(SiBlock.get(SiBlock.size()-1));
			}
		}
	}

}
