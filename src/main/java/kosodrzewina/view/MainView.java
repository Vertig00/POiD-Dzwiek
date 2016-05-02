package kosodrzewina.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import kosodrzewina.implementation.FileLoader;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;

public class MainView implements ActionListener{

	private JFrame frame;
	JMenuItem loadFile;
	JMenuItem closeApp;
	private JButton btnPlay;
	
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    Clip clip;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView window = new MainView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnOpcje = new JMenu("Opcje");
		menuBar.add(mnOpcje);
		
		loadFile = new JMenuItem("Otw\u00F3rz");
		loadFile.addActionListener(this);
		mnOpcje.add(loadFile);
		
		JSeparator separator = new JSeparator();
		mnOpcje.add(separator);
		
		closeApp = new JMenuItem("Zamknij");
		closeApp.addActionListener(this);
		mnOpcje.add(closeApp);
		frame.getContentPane().setLayout(null);
		
		btnPlay = new JButton("Play");
		btnPlay.addActionListener(this);
		btnPlay.setBounds(135, 69, 174, 96);
		frame.getContentPane().add(btnPlay);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		if(z == loadFile){
			FileLoader fl = new FileLoader();
			File file = fl.fileOpener();

//
			try {
				stream = AudioSystem.getAudioInputStream(file);
			} catch (UnsupportedAudioFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		    format = stream.getFormat();
		    info = new DataLine.Info(Clip.class, format);
		    try {
				clip = (Clip) AudioSystem.getLine(info);
			} catch (LineUnavailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(z == closeApp){
			System.exit(0);
		}else if(z == btnPlay){
		    try
		    {
		        clip.open(stream);
		        clip.start();
		    }
		    catch (Exception exc)
		    {
		        exc.printStackTrace(System.out);
		    }

		}
		
	}

}
