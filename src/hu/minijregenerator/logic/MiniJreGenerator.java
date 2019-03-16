/**
 * 
 */
package hu.minijregenerator.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Varga Péter
 *
 */
public class MiniJreGenerator
{
	private String jdkPath = "E:\\java\\jdk-11.0.2";
	private List<MiniJreGeneratorListener> listeners;
	
	public MiniJreGenerator()
	{
		listeners = new ArrayList<MiniJreGeneratorListener>();
	}
	
	public String runJdeps(String classPath, String jarPath)
	{
		return runCommand("\""+jdkPath+"\\bin\\jdeps\" --print-module-deps -cp \""+classPath+"\" \""+jarPath+"\"");
	}
	public String generateMiniJre(String modules, String minijrePath)
	{
		return runCommand("\""+jdkPath+"\\bin\\jlink\" --module-path \""+ jdkPath+"\\jmods\" --no-header-files --no-man-pages --compress=2 --strip-debug --add-modules "+modules+" --output \""+minijrePath+"\"");
	}
	public void generate(String classPath,String jarPath,String minijrePath)
	{
		String modules = runJdeps(classPath,jarPath);
		modules = normalizeOutput(modules);
		generateMiniJre(modules,minijrePath);
	}
	public String runCommand(String command)
	{
		StringBuilder output = new StringBuilder();
		try
		{
			logToListeners(command);
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader lineReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
            while ((line = lineReader.readLine()) != null) 
            {
                output.append(line + "\n");
            }
            logToListeners(output.toString());
		}
		catch (IOException e)
		{
			logToListeners(e.getMessage());
		}
		return output.toString();
	}
	public String normalizeOutput(String input)
	{
		return input.replace("\n", "");
	}
	/**
	 * @return the jdkPath
	 */
	public String getJdkPath()
	{
		return jdkPath;
	}
	/**
	 * @param jdkPath the jdkPath to set
	 */
	public void setJdkPath(String jdkPath)
	{
		this.jdkPath = jdkPath;
	}
	public void addMiniJreGeneratorListener(MiniJreGeneratorListener listener)
	{
		listeners.add(listener);
	}
	public void removeMiniJreGeneratorListener(MiniJreGeneratorListener listener)
	{
		listeners.remove(listener);
	}
	private void logToListeners(String message)
	{
		for(MiniJreGeneratorListener i : listeners)
		{
			i.request(message);
		}
	}
}
