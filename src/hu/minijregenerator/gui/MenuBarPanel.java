/**
 * 
 */
package hu.minijregenerator.gui;

import java.awt.event.ActionListener;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * Usage:
 * MenuBarPanel menuPanel = new MenuBarPanel();<br>
 * menuPanel.add(new JMenu("Menü"));<br>
 * menuPanel.add(new JMenu("Súgó"));<br>
 * menuPanel.addMenuItem(0, "Új", KeyEvent.VK_U,  ActionEvent.CTRL_MASK, new ÚjListener());<br>
 * menuPanel.addMenuItem(0, "Megnyitás", KeyEvent.VK_L,  ActionEvent.CTRL_MASK, new BeolvasListener());<br>
 * menuPanel.addMenuItem(1, "Névjegy", KeyEvent.VK_H,  ActionEvent.CTRL_MASK, new NevjegyListener());<br>
 * JFrame frame = new JFrame();<br>
 * frame.setSize(800,600);<br>
 * frame.setJMenuBar(menuPanel);<br>
 * frame.setVisible(true);<br>
 * 
 * @author Varga Péter
 *
 */
@SuppressWarnings("serial")
public class MenuBarPanel extends JMenuBar
{
	public MenuBarPanel()
	{
		super();
		setVisible(true);
	}

	public void addMenuItem(int mainMenuIndex,String menuText, int hotKey, int keyMask, ActionListener actionListener)
	{
		JMenuItem menuItem = new JMenuItem(menuText,hotKey);
		menuItem.addActionListener(actionListener);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(hotKey, keyMask));
		this.getMenu(mainMenuIndex).add(menuItem);
		this.repaint();
	}

	public void addMenuItem(int mainMenuIndex,JMenuItem item, int hotKey, int keyMask, ActionListener actionListener)
	{
		item.setMnemonic(hotKey);
		item.addActionListener(actionListener);
		item.setAccelerator(KeyStroke.getKeyStroke(hotKey, keyMask));
		this.getMenu(mainMenuIndex).add(item);
		this.repaint();
	}	

	public void addMenuItem(int mainMenuIndex,JMenuItem item, ActionListener actionListener)
	{
		item.addActionListener(actionListener);
		this.getMenu(mainMenuIndex).add(item);
		this.repaint();
	}
}
