package compiler.global;

import java.util.ArrayList;

public class Array extends Variable {

	private int arrayDem = 0;
	private int arraySize[];
	public Array(int type,int arrDem,ArrayList<Integer> size) {
		super(type);
		this.arrayDem = arrDem;
		
		arraySize = new int[size.size()];
		
		for (int i = 0; i < size.size(); i++) {
			arraySize[i] = size.get(i);
		}
	}
	

}
