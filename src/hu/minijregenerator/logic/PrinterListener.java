/**
 * 
 */
package hu.minijregenerator.logic;

/**
 * @author Peti
 *
 */
public class PrinterListener implements MiniJreGeneratorListener
{
	@Override
	public void request(String message)
	{
		System.out.println(message);
	}

}
