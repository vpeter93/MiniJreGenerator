/**
 * 
 */
package hu.minijregenerator.gui;

import hu.minijregenerator.logic.MiniJreGenerator;

/**
 * @author Varga Péter
 *
 */
public class Main
{
	public static void main(String[] args)
	{
		if(args.length == 0)
		{
			MiniJreWindow window = new MiniJreWindow();
		}
		if(args.length == 1)
		{
			if(args[0].equals("--help") || args[1].equals("-h"))
				printUsage();
		}
		if(args.length == 8)
			console(args);
	}
	public static void printUsage()
	{
		System.out.println("Usage : java -jar MiniJreGenerator.jar --class-path <path> --jar-path <path> --output-path <path> --jdk-path <path>" );
		System.out.println("        java -jar MiniJreGenerator.jar -cp <path> -jp <path> -op <path> -jdkp <path>" );
	}
	public static void console(String[] args)
	{
		MiniJreGenerator generator = new MiniJreGenerator();
		String classPath = null;
		String jarPath = null;
		String outputJrePath = null;
		String jdkPath = null;
		
		for(int i = 0; i < args.length; i++)
		{
			if(args[i].equals("--class-path") || args[i].equals("-cp"))
			{
				classPath = args[i+1];
			}
			if(args[i].equals("--jar-path") || args[i].equals("-jp"))
			{
				jarPath = args[i+1];
			}
			if(args[i].equals("--output-path") || args[i].equals("-op"))
			{
				outputJrePath = args[i+1];
			}
			if(args[i].equals("--jdk-path") || args[i].equals("-jdkp"))
			{
				jdkPath = args[i+1];
			}
		}
		
		if(classPath == null || jarPath == null || outputJrePath == null || jdkPath == null)
		{
			printUsage();
			System.exit(-1);
		}
		
		generator.setJdkPath(jdkPath);
		generator.generate(classPath, jarPath, outputJrePath);
	}

}
