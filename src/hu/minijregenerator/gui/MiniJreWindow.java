/**
 * 
 */
package hu.minijregenerator.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import hu.minijregenerator.logic.MiniJreConfig;
import hu.minijregenerator.logic.MiniJreGenerator;

/**
 * @author Varga Péter
 *
 */
public class MiniJreWindow
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
	
	private JFrame frame;
	
	private MiniJreGenerator generator;
	private TextAreaLoggerListener textAreaLogger;
	
	public MiniJreWindow()
	{
		frame = new JFrame();
		frame.setTitle("MiniJreGenerator");
		frame.setSize(450, 440);
		frame.setResizable(true);
		frame.setLayout(new GridLayout(2, 1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jdkPathLabel = new JLabel("JDK path (Java 11+)");
		jdkPathField = new JTextField(20);
		if(System.getenv("JAVA_HOME") != null)
			jdkPathField.setText(System.getenv("JAVA_HOME"));
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
		
		browseJdkPath.addActionListener(new BrowseListener(jdkPathField, frame,true));
		BrowseListener browseJar = new BrowseListener(jarPathField, frame,false);
		browseJar.setExtensions(new String[] {"jar"});
		browseJar.setDescription("jar file");
		browseJar.setInitialFolder(System.getProperty("user.dir"));
		browseJarPath.addActionListener(browseJar);
		
		browseClassPath.addActionListener(new BrowseListener(classPathField, frame,true));
		browseOutputPath.addActionListener(new BrowseListener(outputPathField, frame,true));

		formPanel.add(jdkPanel);
		formPanel.add(jarPanel);
		formPanel.add(classPathPanel);
		formPanel.add(outputPathPanel);
		formPanel.add(generatePanel);
		
		MenuBarPanel menuPanel = new MenuBarPanel();
		menuPanel.add(new JMenu("File"));
		menuPanel.addMenuItem(0, "New", KeyEvent.VK_U,  ActionEvent.CTRL_MASK, new NewButtonListener());
		menuPanel.addMenuItem(0, "Load", KeyEvent.VK_L,  ActionEvent.CTRL_MASK, new OpenButtonListener());
		menuPanel.addMenuItem(0, "Save", KeyEvent.VK_S,  ActionEvent.CTRL_MASK, new SaveButtonListener());

		frame.setJMenuBar(menuPanel);
		
		frame.add(formPanel);
		frame.add(logPanel);
		textAreaLogger = new TextAreaLoggerListener(logArea);
		generator = new MiniJreGenerator();
		generator.addMiniJreGeneratorListener(textAreaLogger);
		generator.welcome();
		
		frame.setVisible(true);
	}
	public MiniJreConfig UIToConfig()
	{
		return new MiniJreConfig
		(
			jdkPathField.getText(),
			classPathField.getText(),
			outputPathField.getText(),
			jarPathField.getText()
		);
	}
	public void configToUI(MiniJreConfig config)
	{
		jdkPathField.setText(config.getJdkPath());
		classPathField.setText(config.getClassPath());
		outputPathField.setText(config.getOutputPath());
		jarPathField.setText(config.getJarPath());
	}
	public class GenerationButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			MiniJreConfig config = UIToConfig();
			if(config.validate(textAreaLogger))
			{
				generator.generate(UIToConfig());
			}
		}
		
	}
	public class OpenButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			chooser.setFileFilter(new FileNameExtensionFilter("mjg files", "mjg"));
			int returnVal = chooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				MiniJreConfig config = generator.readConfigFile(chooser.getSelectedFile().getAbsolutePath());
				configToUI(config);
			}
		}
		
	}
	public class NewButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			jdkPathField.setText("");
			classPathField.setText("");
			outputPathField.setText("");
			jarPathField.setText("");
		}
		
	}
	public class SaveButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
			chooser.setFileFilter(new FileNameExtensionFilter("mjg files", "mjg"));
			int returnVal = chooser.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				MiniJreConfig config = UIToConfig();
				if(!chooser.getSelectedFile().getAbsolutePath().endsWith(".mjg"))
					generator.createMJGFile(config, chooser.getSelectedFile().getAbsolutePath()+".mficu");
				else
					generator.createMJGFile(config, chooser.getSelectedFile().getAbsolutePath());
			}
		}
		
	}
}
