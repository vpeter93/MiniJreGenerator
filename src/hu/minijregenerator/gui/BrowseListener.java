/**
 * 
 */
package hu.minijregenerator.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author Peti
 *
 */
public class BrowseListener implements ActionListener
{
	private JTextField textField;
	private String[] extensions;
	private String description;
	private String initialFolder;
	private JFrame frame;
	private boolean isDirectorySelect;
	
	private static final String USER_DIR = "user.dir";
	
	public BrowseListener(JTextField textField,JFrame frame, boolean isDirectorySelect)
	{
		this.textField = textField;
		this.frame = frame;
		this.isDirectorySelect = isDirectorySelect;
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(isDirectorySelect)
		{
			browseDirectory();
		}
		else
		{
			browseFile();
		}
	}
	
	private void browseFile()
	{
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(description,extensions);
		chooser.setFileFilter(filter);
		Action details = chooser.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		File workingDirectory = new File(initialFolder);
		if(!workingDirectory.exists())
		{
			workingDirectory = new File(System.getProperty(USER_DIR));
		}

		chooser.setCurrentDirectory(workingDirectory);
		int returnVal = chooser.showOpenDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
		    String path = chooser.getSelectedFile().getAbsolutePath();
		    if(path.contains(System.getProperty(USER_DIR)))
		    {
		    	path = path.replace(System.getProperty(USER_DIR), "");
		    	path = path.substring(1);
		    	if(path.contains("\\"))
		    	{
		    		path = path.replace("\\", "/");
		    	}
		    	textField.setText(path);
		    }
		    else
		    {
		    	textField.setText(path);
		    }
		 }
	}
	
	private void browseDirectory()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		Action details = chooser.getActionMap().get("viewTypeDetails");
		details.actionPerformed(null);
		int returnVal = chooser.showOpenDialog(frame);
		if(returnVal == JFileChooser.APPROVE_OPTION) 
		{
		    String path = chooser.getSelectedFile().getAbsolutePath();
		    if(path.contains(System.getProperty(USER_DIR)))
		    {
		    	path = path.replace(System.getProperty(USER_DIR), "");
		    	path = path.substring(1);
		    	if(path.contains("\\"))
		    	{
		    		path = path.replace("\\", "/");
		    	}
		    	textField.setText(path);
		    }
		    else
		    {
		    	textField.setText(path);
		    }
		 }
	}
	
	/**
	 * @return the extensions
	 */
	public String[] getExtensions()
	{
		return extensions;
	}
	/**
	 * @param extensions the extensions to set
	 */
	public void setExtensions(String[] extensions)
	{
		this.extensions = extensions;
	}
	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	/**
	 * @return the initialFolder
	 */
	public String getInitialFolder()
	{
		return initialFolder;
	}
	/**
	 * @param initialFolder the initialFolder to set
	 */
	public void setInitialFolder(String initialFolder)
	{
		this.initialFolder = initialFolder;
	}

}
