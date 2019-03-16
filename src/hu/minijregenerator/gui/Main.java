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
		String classPath = "\"E:\\java\\KÉSZ programok\\CatDragonAdventures\\catDragon_lib/*\"";
		String jarPath = "\"E:\\java\\KÉSZ programok\\CatDragonAdventures\\catDragon.jar\"";
		String generatedJrePath = "minijre";
		
		MiniJreGenerator generator = new MiniJreGenerator();
		generator.generate(classPath, jarPath, generatedJrePath);

	}

}
