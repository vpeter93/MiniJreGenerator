/**
 * 
 */
package hu.minijregenerator.logic;

/**
 * @author Peti
 *
 */
public class MiniJreConfig
{
	private String jdkPath;
	private String classPath;
	private String outputPath;
	private String jarPath;
	
	/**
	 * @param jdkPath
	 * @param classPath
	 * @param outputPath
	 * @param jarPath
	 */
	public MiniJreConfig(String jdkPath, String classPath, String outputPath, String jarPath)
	{

		this.jdkPath = jdkPath;
		this.classPath = classPath;
		this.outputPath = outputPath;
		this.jarPath = jarPath;
	}
	public MiniJreConfig()
	{
	}
	
	public boolean validate(MiniJreGeneratorListener listener)
	{
		boolean valid = true;
		if(jdkPath == null || "".equals(jdkPath))
		{
			listener.request("Please add jdk path");
			valid = false;
		}
		if(outputPath == null || "".equals(outputPath))
		{
			listener.request("Please add output path");
			valid = false;
		}
		if(jarPath == null || "".equals(jarPath))
		{
			listener.request("Please add jar path");
			valid = false;
		}
		if(classPath == null || "".equals(classPath))
		{
			listener.request("No class path");
		}
		return valid;
	}
	
	@Override
	public String toString()
	{
		return "jdkPath = "+jdkPath+"\n"+
				"classPath = "+classPath+"\n"+
				"outputPath = "+outputPath+"\n"+
				"jarPath = "+jarPath+"\n";
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

	/**
	 * @return the classPath
	 */
	public String getClassPath()
	{
		return classPath;
	}

	/**
	 * @param classPath the classPath to set
	 */
	public void setClassPath(String classPath)
	{
		this.classPath = classPath;
	}

	/**
	 * @return the outputPath
	 */
	public String getOutputPath()
	{
		return outputPath;
	}

	/**
	 * @param outputPath the outputPath to set
	 */
	public void setOutputPath(String outputPath)
	{
		this.outputPath = outputPath;
	}

	/**
	 * @return the jarPath
	 */
	public String getJarPath()
	{
		return jarPath;
	}

	/**
	 * @param jarPath the jarPath to set
	 */
	public void setJarPath(String jarPath)
	{
		this.jarPath = jarPath;
	}
	
	
}
