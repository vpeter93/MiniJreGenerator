/**
 * 
 */
package hu.minijregenerator.gui;

import hu.minijregenerator.logic.MiniJreConfig;
import hu.minijregenerator.logic.MiniJreGenerator;
import hu.minijregenerator.logic.PrinterListener;

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
			window.show();
		}
		else if(args.length == 1)
		{
			if(args[0].equals("--help") || args[0].equals("-h"))
				printUsage();
		}
		else if(args.length == 2)
		{
			if(args[0].equals("--config-file") || args[0].equals("-cf"))
				console(args);
		}
		else if(args.length == 8)
			console(args);
	}
	public static void printUsage()
	{
		System.out.println("Usage : java -jar MiniJreGenerator.jar --class-path <path> --jar-path <path> --output-path <path> --jdk-path <path>" );
		System.out.println("        java -jar MiniJreGenerator.jar -cp <path> -jp <path> -op <path> -jdkp <path>" );
		System.out.println("        java -jar MiniJreGenerator.jar --config-file example.mjg" );

	}
	public static void console(String[] args)
	{
		MiniJreGenerator generator = new MiniJreGenerator();
		generator.addMiniJreGeneratorListener(new PrinterListener());
		
		MiniJreConfig config = new MiniJreConfig();
		
		for(int i = 0; i < args.length; i++)
		{
			loadGeneratorConfigFromCommandLineArguments(args, i, config);
			if(args[i].equals("--config-file") || args[i].equals("-cf"))
			{
				config = generator.readConfigFile(args[i+1]);
				config.validate(new PrinterListener());
			}
		}
		generator.welcome();
		generator.generate(config);
	}

	private static void loadGeneratorConfigFromCommandLineArguments(String[] args, int i, MiniJreConfig config)
	{
		if(args[i].equals("--class-path") || args[i].equals("-cp"))
		{
			config.setClassPath(args[i +1]);
		}
		if(args[i].equals("--jar-path") || args[i].equals("-jp"))
		{
			config.setJarPath(args[i +1]);
		}
		if(args[i].equals("--output-path") || args[i].equals("-op"))
		{
			config.setOutputPath(args[i +1]);
		}
		if(args[i].equals("--jdk-path") || args[i].equals("-jdkp"))
		{
			config.setJdkPath(args[i +1]);
		}
	}

}
