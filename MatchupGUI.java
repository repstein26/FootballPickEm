import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import javax.swing.text.PlainDocument;
import javax.swing.text.*;


public class MatchupGUI extends JFrame{
	private Dimension window = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
	private JTextField[] textField = new JTextField[32];
	private JLabel[] awayTeams = new JLabel[16];
	private JLabel[] homeTeams = new JLabel[16];
	private static JTextField[] awayText = new JTextField[16];
	private JTextField[] homeText = new JTextField[16];
	private static JSlider[] slider = new JSlider[32];
	//private JButton[] copyButton = new JButton[25];
	//private JButton[] pasteButton = new JButton[25];
	//private JButton[] clearSingleButton = new JButton[25];
	private JButton saveButton = new JButton("Save and Compute");
	private JButton clearButton = new JButton("Reset Defaults");
	private String borderTitle;
	private String frameTitle;  
	private int awayProbability;
	private int numPairs;

	private List<ActionListener> saveListeners = new ArrayList<ActionListener>();
	private List<ActionListener> clearAllListeners = new ArrayList<ActionListener>();

	public MatchupGUI(String frameTitle, String borderTitle, int numPairs){
		super(frameTitle);
		this.frameTitle = frameTitle;
		this.borderTitle = borderTitle;
		this.numPairs = numPairs;
		createGUI();
	}


	private void createGUI() {		
		attachListeners();
		//frame = new JFrame(frameTitle);
		ImagePanel panel = new ImagePanel("football.png", new GridBagLayout());
		panel.setPreferredSize(window);
		//panel.setBackground(Color.BLACK);
        GridBagConstraints cst = new GridBagConstraints();
        Color fieldGreen = new Color(0,102,0);

        for (int i = 0; i < numPairs; i++){
        	//slider
        	slider[i] = new JSlider(JSlider.HORIZONTAL,
                                              0, 100, 50);
        	awayText[i] = new JTextField();
        	awayText[i].setEditable(false);
        	awayText[i].setHorizontalAlignment(JTextField.CENTER);
        	homeText[i] = new JTextField();
        	homeText[i].setEditable(false);
        	homeText[i].setHorizontalAlignment(JTextField.CENTER);

        	final int index = i;
        	slider[i].addChangeListener(new ChangeListener() {
            	@Override
            	public void stateChanged(ChangeEvent e) {
            		if (slider[index].getValue() >= 50){
            			awayProbability = slider[index].getValue();
            			awayText[index].setText(String.valueOf(100-awayProbability));
            			homeText[index].setText(String.valueOf(awayProbability));
            		
            		} else {
            			awayProbability = slider[index].getValue();
            			awayText[index].setText(String.valueOf(100-awayProbability));
            			homeText[index].setText(String.valueOf(awayProbability));
            		
            		}
            	}
        	}); 

			slider[i].setMajorTickSpacing(10);
			slider[i].setMinorTickSpacing(5);
			Hashtable<Integer, JLabel> labels =
                new Hashtable<Integer, JLabel>();
        	labels.put(0, new JLabel("<html><font color='white'>100</font></html>"));
        	labels.put(10, new JLabel("<html><font color='white'>90</font></html>"));
        	labels.put(20, new JLabel("<html><font color='white'>80</font></html>"));
        	labels.put(30, new JLabel("<html><font color='white'>70</font></html>"));
        	labels.put(40, new JLabel("<html><font color='white'>60</font></html>"));
        	labels.put(50, new JLabel("<html><font color='white'>50</font></html>"));
        	labels.put(100, new JLabel("<html><font color='white'>100</font></html>"));
        	labels.put(90, new JLabel("<html><font color='white'>90</font></html>"));
        	labels.put(80, new JLabel("<html><font color='white'>80</font></html>"));
        	labels.put(70, new JLabel("<html><font color='white'>70</font></html>"));
        	labels.put(60, new JLabel("<html><font color='white'>60</font></html>"));
        	slider[i].setLabelTable(labels);
			slider[i].setPaintLabels(true);
			slider[i].setPaintTicks(true);

			slider[i].setForeground(Color.white);
			slider[i].setSnapToTicks(true);

			//slider[i].setPreferredSize(new Dimension(500, 500));
        	Font font = new Font("Serif", Font.BOLD, 15);
        	slider[i].setFont(font);

        	//left panel
        	
        	awayTeams[i] = new JLabel("", JLabel.TRAILING);
			awayTeams[i].setFont(new Font("Serif", Font.BOLD, 32));
			//awayTeams[i].setOpaque(true);
			//awayTeams[i].setBackground(Color.BLACK);
			awayTeams[i].setForeground(Color.WHITE);
			awayText[i].setFont(new Font("Serif", Font.BOLD, 20));
			awayText[i].setPreferredSize(new Dimension(40, 40));
			cst.gridx = 0;
			cst.gridy = i;
			cst.insets = new Insets(0,0,10,0); //int top, int left, int bottom, int right
			panel.add(awayTeams[i],cst);

			cst.gridx = 1;
			cst.gridy = i;
			cst.insets = new Insets(0,0,10,10); //int top, int left, int bottom, int right
			panel.add(awayText[i],cst);

			//slider 
			GridBagConstraints cst_slider = new GridBagConstraints();

			//cst_slider.fill = GridBagConstraints.HORIZONTAL;
			cst_slider.gridx = 2;
			cst_slider.gridy = i;
			cst_slider.gridwidth = 9;
			cst_slider.insets = new Insets(0,0,10,0);
			slider[i].setPreferredSize(new Dimension(500, 40));
			slider[i].setOpaque(false);
			//slider[i].setBackground(fieldGreen);
			panel.add(slider[i], cst_slider);

			//right panel
			GridBagConstraints cst_home = new GridBagConstraints();
			//JPanel homePanel = new JPanel(new BorderLayout());
			homeText[i].setFont(new Font("Serif", Font.BOLD, 20));
			homeText[i].setPreferredSize(new Dimension(40, 40));
			homeTeams[i] = new JLabel("", JLabel.TRAILING);
			homeTeams[i].setFont(new Font("Serif", Font.BOLD, 32));
			//homeTeams[i].setOpaque(true);
			//homeTeams[i].setBackground(Color.BLACK);
			homeTeams[i].setForeground(Color.WHITE);
			cst_home.gridx = 11;
			cst_home.gridy = i;
			cst_home.insets = new Insets(0,10,10,0);
        	panel.add(homeText[i],cst_home);

        	cst_home.gridx = 12;
			cst_home.gridy = i;
			cst_home.insets = new Insets(0,0,10,0);
        	panel.add(homeTeams[i],cst_home);
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       // setSize((int)window.getWidth(),(int)window.getHeight());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        getContentPane().add(panel);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(Color.black);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		bottomPanel.add(clearButton);
		bottomPanel.add(saveButton);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);

	//	frame.setPreferredSize(new Dimension((int)window.getWidth(),(int)window.getHeight()));
	//	setPreferredSize(new Dimension((int)window.getWidth(),(int)window.getHeight()));
		
	}

	public void setText(int index, String text){
		textField[index].setText(text);
	}

	public void setAwayText(int index, String text){
		awayText[index].setText(text);
	}

	public void setHomeText(int index, String text){
		homeText[index].setText(text);
		slider[index].setValue(Integer.valueOf(text));
	}

	public void setHomeLabel(int index, String text){
		homeTeams[index].setText(text);
	}

	public void setAwayLabel(int index, String text){
		awayTeams[index].setText(text);
	}

	private void attachListeners(){
		ActionListener saveListener = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
     				fireSavePressed(e);
   			}
		};

		ActionListener clearAllListener = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
     				fireClearAllPressed(e);
   			}
		};

		saveButton.addActionListener(saveListener);
		clearButton.addActionListener(clearAllListener);
	}

	public String toString(){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < homeText.length; i++){
			if (homeText[i] != null && awayText[i] != null){
					sb.append(awayTeams[i].getText() + " " + awayText[i].getText() + "|" + homeTeams[i].getText() + " " + homeText[i].getText() + "\r\n");
			}
		}
		return sb.toString();
	}

	public void clearAllTextFields(){
		for (int i = 0; i < homeText.length-1; i++){
			homeText[i].setText("50");
			awayText[i].setText("50");
			slider[i].setValue(50);
		}
		
	}

	public void addSaveButtonListener(ActionListener a){
		saveListeners.add(a);
	}

	public void addClearAllButtonListener(ActionListener a){
		clearAllListeners.add(a);
	}

	public void fireSavePressed(ActionEvent e){
		System.out.println("Save button pressed");
		for (ActionListener a : saveListeners){
			a.actionPerformed(e);
		}

	}

	public void fireClearAllPressed(ActionEvent e){
		System.out.println("Clear all button pressed");
		for (ActionListener a : clearAllListeners){
			a.actionPerformed(e);
		}
	}

	public void showGUI(){
		 javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				pack();
				setVisible(true);
			}
		});
		
	}



}

