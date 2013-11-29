package de.codebucket.bungeesigns.utils;

public class CustomVariable 
{
	public enum VariableType
	{
		SPLIT,
		SUBSTRING,
		REPLACE;
	}
	
	private VariableType type;
	private String variable;
	private String arguments;
	
	public CustomVariable(String type, String variable, String arguments) throws IllegalArgumentException
	{
		for(VariableType vartype : VariableType.values())
		{
			if(vartype.equals(VariableType.valueOf(type)))
			{
				this.type = vartype;
				break;
			}
		}
		
		if(this.type == null)
		{
			throw new IllegalArgumentException("Syntax error: Unknown VariableType!");
		}
		
		this.variable = variable;
		this.arguments = arguments;
	}
	
	public VariableType getType()
	{
		return this.type;
	}
	
	public String getVariable()
	{
		return this.variable;
	}
	
	public String getArguments()
	{
		return this.arguments;
	}
	
	public String parseVariable(String input)
	{
		if(type == VariableType.SPLIT)
		{
			String[] args = arguments.split(",");
			
			if(args.length == 2 && isInteger(args[1]))
			{
				String[] splitted = input.split(args[0]);
				return splitted[(Integer.parseInt(args[1]) -1)];
			}
		}
		
		if(type == VariableType.SUBSTRING)
		{
			String[] args = arguments.split(",");
			
			if(args.length == 2 && isInteger(args[0]) && isInteger(args[1]))
			{
				return input.substring(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
			}
		}
		
		if(type == VariableType.REPLACE)
		{
			String[] args = arguments.split(",");
			
			if(args.length == 2)
			{
				input = input.replaceAll(args[0], args[1]);
				return input;
			}
		}
		
		return input;
	}
	
	private boolean isInteger(String integer)
	{
		try
		{
			Integer.parseInt(integer);
			return true;
		}
		catch(NumberFormatException e)
		{
			return false;
		}
	}
}
