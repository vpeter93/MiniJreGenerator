/**
 * 
 */
package hu.minijregenerator.logic;

/**
 * @author Varga Péter
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
