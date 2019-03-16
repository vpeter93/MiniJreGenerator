/**
 * 
 */
package hu.minijregenerator.gui;

import javax.swing.JTextArea;

import hu.minijregenerator.logic.MiniJreGeneratorListener;

/**
 * @author Peti
 *
 */
public class TextAreaLoggerListener implements MiniJreGeneratorListener
{
	private JTextArea area;
	
	public TextAreaLoggerListener(JTextArea area)
	{
		this.area = area;
	}

	@Override
	public void request(String message)
	{
		area.append(message);
	}

}
