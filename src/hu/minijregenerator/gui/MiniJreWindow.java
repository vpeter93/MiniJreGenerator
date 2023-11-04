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

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import hu.minijregenerator.logic.MiniJreConfig;
import hu.minijregenerator.logic.MiniJreGenerator;

/**
 * @author Varga Péter
 *
 */
public class MiniJreWindow
{
	public static final String BROWSE = "Browse";
	public static final String JAVA_HOME = "JAVA_HOME";
	public static final String USER_DIR = "user.dir";
	private final JTextField jdkPathField;

	private final JTextField classPathField;

	private final JTextField outputPathField;

	private final JTextField modulePathField;
	private final JTextField addModulesField;

	private final JTextField jarPathField;

	private final JCheckBox ignoreMissingDependencies;

	private final JFrame frame;
	
	private final MiniJreGenerator generator;
	private final TextAreaLoggerListener textAreaLogger;
	
	public MiniJreWindow()
	{
		frame = new JFrame();
		frame.setTitle("MiniJreGenerator");
		frame.setSize(900, 500);
		frame.setResizable(true);
		frame.setLayout(new GridLayout(2, 1));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JLabel jdkPathLabel = new JLabel("JDK path (Java 11+)");
		jdkPathField = new JTextField(20);
		if(System.getenv(JAVA_HOME) != null)
			jdkPathField.setText(System.getenv(JAVA_HOME));
		JButton browseJdkPath = new JButton(BROWSE);

		JLabel jarPathLabel = new JLabel("Jar file");
		jarPathField = new JTextField(20);
		JButton browseJarPath = new JButton(BROWSE);

		JLabel classPathLabel = new JLabel("Classpath");
		classPathField = new JTextField(20);
		JButton browseClassPath = new JButton(BROWSE);

		JLabel modulePathLabel = new JLabel("ModulePath");
		modulePathField = new JTextField(20);
		JButton browseModulePath = new JButton(BROWSE);

		JLabel addModulesLabel = new JLabel("Modules");
		addModulesField = new JTextField(20);

		JLabel outputPathLabel = new JLabel("Output directory");
		outputPathField = new JTextField(20);
		JButton browseOutputPath = new JButton(BROWSE);

		JLabel ignoreMissingDependenciesLabel = new JLabel("Ignore missing dependencies");
		ignoreMissingDependencies = new JCheckBox();
		
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

		JPanel modulePathPanel = new JPanel();
		modulePathPanel.setLayout(new FlowLayout());

		modulePathPanel.add(modulePathLabel);
		modulePathPanel.add(modulePathField);
		modulePathPanel.add(browseModulePath);

		JPanel ignoreMissingPanel = new JPanel();
		ignoreMissingPanel.setLayout(new FlowLayout());

		ignoreMissingPanel.add(ignoreMissingDependenciesLabel);
		ignoreMissingPanel.add(ignoreMissingDependencies);

		JPanel addModulesPanel = new JPanel();
		addModulesPanel.setLayout(new FlowLayout());

		addModulesPanel.add(addModulesLabel);
		addModulesPanel.add(addModulesField);
		
		JPanel outputPathPanel = new JPanel();
		outputPathPanel.setLayout(new FlowLayout());
		
		outputPathPanel.add(outputPathLabel);
		outputPathPanel.add(outputPathField);
		outputPathPanel.add(browseOutputPath);

		JPanel generatePanel = createGeneratePanel();

		JPanel logPanel = new JPanel();
		logPanel.setLayout(new FlowLayout());
		JTextArea logArea = new JTextArea(11, 80);
		logArea.setEditable(false);
		JScrollPane scroll = new JScrollPane(logArea);
		logPanel.add(scroll);
		
		JPanel formPanel = new JPanel();
		formPanel.setLayout(new GridLayout(6, 1));
		
		browseJdkPath.addActionListener(new BrowseListener(jdkPathField, frame,true));
		BrowseListener browseJar = new BrowseListener(jarPathField, frame,false);
		browseJar.setExtensions(new String[] {"jar"});
		browseJar.setDescription("jar file");
		browseJar.setInitialFolder(System.getProperty(USER_DIR));
		browseJarPath.addActionListener(browseJar);
		
		browseClassPath.addActionListener(new BrowseListener(classPathField, frame,true));
		browseOutputPath.addActionListener(new BrowseListener(outputPathField, frame,true));
		browseModulePath.addActionListener(new BrowseListener(modulePathField, frame,true));

		formPanel.add(jdkPanel);
		formPanel.add(jarPanel);
		formPanel.add(classPathPanel);
		formPanel.add(outputPathPanel);
		formPanel.add(modulePathPanel);
		formPanel.add(addModulesPanel);
		formPanel.add(ignoreMissingPanel);
		formPanel.add(generatePanel);
		frame.setJMenuBar(createMenuBar());
		
		frame.add(formPanel);
		frame.add(logPanel);
		textAreaLogger = new TextAreaLoggerListener(logArea);
		generator = new MiniJreGenerator();
		generator.addMiniJreGeneratorListener(textAreaLogger);
		generator.welcome();
	}

	private JPanel createGeneratePanel()
	{
		JButton generateButton = new JButton("Generate");
		generateButton.addActionListener(new GenerationButtonListener());
		JPanel generatePanel = new JPanel();
		generatePanel.setLayout(new FlowLayout());
		generatePanel.add(generateButton);
		return generatePanel;
	}

	private MenuBarPanel createMenuBar()
	{
		MenuBarPanel menuPanel = new MenuBarPanel();
		menuPanel.add(new JMenu("File"));
		menuPanel.addMenuItem(0, "New", KeyEvent.VK_U,  ActionEvent.CTRL_MASK, new NewButtonListener());
		menuPanel.addMenuItem(0, "Load", KeyEvent.VK_L,  ActionEvent.CTRL_MASK, new OpenButtonListener());
		menuPanel.addMenuItem(0, "Save", KeyEvent.VK_S,  ActionEvent.CTRL_MASK, new SaveButtonListener());
		return menuPanel;
	}

	public void show()
	{
		frame.setVisible(true);
	}
	public MiniJreConfig uiToConfig()
	{
		return new MiniJreConfig
		(
			jdkPathField.getText(),
			classPathField.getText(),
			modulePathField.getText(),
			addModulesField.getText(),
			outputPathField.getText(),
			jarPathField.getText(),
			ignoreMissingDependencies.isSelected()
		);
	}
	public void configToUI(MiniJreConfig config)
	{
		jdkPathField.setText(config.getJdkPath());
		classPathField.setText(config.getClassPath());
		outputPathField.setText(config.getOutputPath());
		modulePathField.setText(config.getModulePath());
		addModulesField.setText(config.getAddModules());
		jarPathField.setText(config.getJarPath());
		ignoreMissingDependencies.setSelected(config.isIgnoreMissingDependencies());
	}
	public class GenerationButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			MiniJreConfig config = uiToConfig();
			if(config.validate(textAreaLogger))
			{
				generator.generate(uiToConfig());
			}
		}
		
	}
	public class OpenButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty(USER_DIR)));
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
			addModulesField.setText("");
			modulePathField.setText("");
			jarPathField.setText("");
			ignoreMissingDependencies.setSelected(false);
		}
		
	}
	public class SaveButtonListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory(new File(System.getProperty(USER_DIR)));
			chooser.setFileFilter(new FileNameExtensionFilter("mjg files", "mjg"));
			int returnVal = chooser.showSaveDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				MiniJreConfig config = uiToConfig();
				if(!chooser.getSelectedFile().getAbsolutePath().endsWith(".mjg"))
					generator.createMJGFile(config, chooser.getSelectedFile().getAbsolutePath()+".mjg");
				else
					generator.createMJGFile(config, chooser.getSelectedFile().getAbsolutePath());
			}
		}
		
	}
}
