package compiler.global;

import java.util.ArrayList;
import java.util.HashMap;

public class Function {
	private ArrayList<Integer> argList;
	private ArrayList<String> argListName;
	private int returnType;
	private int argSize;
	private HashMap<Integer,String> Instructions;
	public Function(ArrayList<Integer> argList,int returnType,ArrayList<String> argListName)
	{
		this.argList = argList;
		this.returnType = returnType;
		this.argListName = argListName;
		if(argList != null)
		{
			this.argSize = argList.size();
		}
			
	}
	public ArrayList<Integer> getArgList() {
		return argList;
	}
	public int getReturnType() {
		return returnType;
	}
	public int getArgSize() {
		return argSize;
	}
	public void addInst(String inst)
	{
		Instructions.put(this.Instructions.size(), inst);
	}
	public HashMap<Integer,String> getInstructions()
	{
		return this.Instructions;
	}
	public void setArgList(ArrayList<Integer> list)
	{
		this.argList = list;
		this.argSize = argList.size();
	}
	public void setArgNameList(ArrayList<String> list)
	{
		this.argListName = list;
	}
	public ArrayList<String> getArgNameList()
	{
		return this.argListName;
	}
	public void setReturnType(int type)
	{
		this.returnType = type;
	}
	
	@Override
	public String toString()
	{
		return "type = "+returnType+" -- args = "+argList+" -- argsize = "+argSize;
	}
}
