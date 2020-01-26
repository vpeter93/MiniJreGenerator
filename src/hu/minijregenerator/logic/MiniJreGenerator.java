/**
 * 
 */
package hu.minijregenerator.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 * @author Varga Péter
 *
 */
public class MiniJreGenerator
{
	private String jdkPath = "E:\\java\\jdk-11.0.2";
	private List<MiniJreGeneratorListener> listeners;
	boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

	public MiniJreGenerator()
	{
		listeners = new ArrayList<MiniJreGeneratorListener>();
	}
	
	
	public String runJdeps(String classPath, String jarPath)
	{
		String command = "\""+jdkPath+File.separator+"bin"+File.separator+"jdeps\" --print-module-deps -cp \""+classPath+"\" \""+jarPath+"\"";
		if(classPath == null || classPath.equals(""))
			command = "\""+jdkPath+File.separator+"bin"+File.separator+"jdeps\" --print-module-deps \""+jarPath+"\"";

		return runCommand(command);
	}
	public String generateMiniJre(String modules, String minijrePath)
	{
		String command = "\""+jdkPath+File.separator+"bin"+File.separator+"jlink\" --module-path \""+ jdkPath+File.separator+"jmods\" --no-header-files --no-man-pages --compress=2 --strip-debug --add-modules "+modules+" --output \""+minijrePath+"\"";
		if(modules.equals(""))
			command = "\""+jdkPath+File.separator+"bin"+File.separator+"jlink\" --module-path \""+ jdkPath+File.separator+"jmods\" --no-header-files --no-man-pages --compress=2 --strip-debug --add-modules java.base --output \""+minijrePath+"\"";

		return runCommand(command);
	}
	public void generate(MiniJreConfig config)
	{
		setJdkPath(config.getJdkPath());
		String modules = runJdeps(config.getClassPath(),config.getJarPath());
		modules = normalizeOutput(modules);
		generateMiniJre(modules,config.getOutputPath());
	}
	public void generate(String classPath,String jarPath,String minijrePath)
	{
		String modules = runJdeps(classPath,jarPath);
		modules = normalizeOutput(modules);
		generateMiniJre(modules,minijrePath);
	}
	
	public String runCommandUnix(String command)
	{
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("/bin/bash", "-c", command);
		
		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				return output.toString();

			} else {
				return output.toString();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String runCommand(String command)
	{
		if(!isWindows)
		{
			return runCommandUnix(command);
			//command = "/bin/bash -c "+command.replace("\"", "");
		}
			
		
		StringBuilder output = new StringBuilder();
		try
		{
			logToListeners(command);
			Process process = Runtime.getRuntime().exec(command);
			BufferedReader lineReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
            while ((line = lineReader.readLine()) != null) 
            {
            	logToListeners(line);
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
	public void createMJGFile(MiniJreConfig config, String fileName)
	{
		createFile(fileName, config.toString());
	}
	public void createFile(String filepath, String fileContent)
	{
		File file = new File(filepath);
		try
		{
			FileOutputStream out = new FileOutputStream(file);
			OutputStreamWriter streamWriter = new OutputStreamWriter(out);
			BufferedWriter writer = new BufferedWriter(streamWriter);
			writer.write(fileContent);
			
			if(writer !=  null)
			{
				writer.close();
			}
		}
		catch (IOException e1)
		{
			logToListeners(e1.getMessage().toString()+"");
		}
	}
	public void welcome() 
	{
		Properties properties = loadProperties("data/version.properties");
		logToListeners("--------------------------------------------------------------------------------");
		logToListeners("                    MiniJreGenerator");
		logToListeners("                      v"+properties.getProperty("version"));
		logToListeners("                      "+properties.getProperty("build"));
		logToListeners("--------------------------------------------------------------------------------");
	}
	public Properties loadPropertiesFromJar(String path)
	{
		try
		{
			Properties properties = new Properties();
			InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
			properties.load(stream);
			stream.close();
			return properties;
		}
		catch (IOException e)
		{
			logToListeners(e.getMessage());
			return null;
		}
	}
	public Properties loadProperties(String path)
	{
		try
		{
			Properties properties = new Properties();
			InputStream stream = new FileInputStream(path);
			properties.load(stream);
			stream.close();
			return properties;
		}
		catch (IOException e)
		{
			logToListeners(e.getMessage());
			return null;
		}
	}
	public MiniJreConfig readConfigFile(String configFilePath)
	{
		Properties configFile = loadProperties(configFilePath);
		MiniJreConfig config = new MiniJreConfig();
		
		String jdkPath = (String)configFile.get("jdkPath");
		String jarPath = (String)configFile.get("jarPath");
		String outputPath = (String)configFile.get("outputPath");
		String classPath = (String)configFile.get("classPath");
		
		if(jdkPath == null)
		{
			jdkPath = System.getenv("JAVA_HOME");
		}
		
		config.setJdkPath(jdkPath);
		config.setJarPath(jarPath);
		config.setOutputPath(outputPath);
		config.setClassPath(classPath);
		
		return config;
	}
}
