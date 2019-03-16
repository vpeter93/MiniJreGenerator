/**
 * 
 */
package hu.minijregenerator.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import hu.minijregenerator.logic.MiniJreGenerator;

/**
 * @author Peti
 *
 */
@SuppressWarnings("serial")
public class MiniJreWindow	extends JFrame
{
	private JLabel jdkPathLabel;
	private JTextField jdkPathField;
	private JButton browseJdkPath;
	
	private JLabel classPathLabel;
	private JTextField classPathField;
	private JButton browseClassPath;
	
	private JLabel outputPathLabel;
	private JTextField outputPathField;
	private JButton browseOutputPath;
	
	private JLabel jarPathLabel;
	private JTextField jarPathField;
	private JButton browseJarPath;
	
	private JButton generateButton;
	private JTextArea logArea;
	
	public MiniJreWindow()
	{
		this.setTitle("MiniJreGenerator");
		this.setSize(450, 420);
		this.setResizable(true);
		this.setLayout(new GridLayout(2, 1));
		
		jdkPathLabel = new JLabel("JDK path (Java 11+)");
		jdkPathField = new JTextField(20);
		browseJdkPath = new JButton("Browse");
		
		jarPathLabel = new JLabel("Jar file");
		jarPathField = new JTextField(20);
		browseJarPath = new JButton("Browse");
		
		classPathLabel = new JLabel("Classpath");
		classPathField = new JTextField(20);
		browseClassPath = new JButton("Browse");
		
		outputPathLabel = new JLabel("Output directory");
		outputPathField = new JTextField(20);
		browseOutputPath = new JButton("Browse");
		
		generateButton = new JButton("Generate");
		generateButton.addActionListener(new GenerationButtonListener());
		
		JPanel jdkPanel = new JPanel();
		jdkPanel.setLayout(new FlowLayout());
		
		jdkPanel.add(jdkPathLabel);
		jdkPanel.add(jdkPathField);
		jdkPanel.add(browseJdkPath);
		
		JPanel jarPanel = new JPanel();
		jarPanel.setLayout(new FlowLayout());
		
		jarPanel.add(jarPathLabel);
		jarPanel.add(jarPathField);
		jarPanel.add(browseJarPath);
		
		JPanel classPathPanel = new JPanel();
		classPathPanel.setLayout(new FlowLayout());
		
		classPathPanel.add(classPathLabel);
		classPathPanel.add(classPathField);
		classPathPanel.add(browseClassPath);
		
		JPanel outputPathPanel = new JPanel();
		outputPathPanel.setLayout(new FlowLayout());
		
		outputPathPanel.add(outputPathLabel);
		outputPathPanel.add(outputPathField);
		outputPathPanel.add(browseOutputPath);
		
		JPanel generatePanel = new JPanel();
		generatePanel.setLayout(new FlowLayout());
		generatePanel.add(generateButton);
		
		JPanel logPanel = new JPanel();
		logPanel.setLayout(new FlowLayout());
		logArea = new JTextArea(11,38);
		logArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(logArea);
		logPanel.add(scroll);
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(6, 1));
		
		browseJdkPath.addActionListener(new BrowseListener(jdkPathField, this,true));
		BrowseListener browseJar = new BrowseListener(jarPathField, this,false);
		browseJar.setExtensions(new String[] {"jar"});
		browseJar.setDescription("jar file");
		browseJar.setInitialFolder(System.getProperty("user.dir"));
		browseJarPath.addActionListener(browseJar);
		
		browseClassPath.addActionListener(new BrowseListener(classPathField, this,true));
		browseOutputPath.addActionListener(new BrowseListener(outputPathField, this,true));

		formPanel.add(jdkPanel);
		formPanel.add(jarPanel);
		formPanel.add(classPathPanel);
		formPanel.add(outputPathPanel);
		formPanel.add(generatePanel);
		
		add(formPanel);
		add(logPanel);
		
		this.setVisible(true);
	}
	
	public class GenerationButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			MiniJreGenerator generator = new MiniJreGenerator();
			generator.addMiniJreGeneratorListener(new TextAreaLoggerListener(logArea));
			generator.setJdkPath(jdkPathField.getText());
			generator.generate(classPathField.getText(), jarPathField.getText(), outputPathField.getText());
			
		}
		
	}
}
