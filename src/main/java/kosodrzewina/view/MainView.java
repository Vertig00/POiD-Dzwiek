package kosodrzewina.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import kosodrzewina.implementation.FileLoader;
import kosodrzewina.implementation.FileSaver;
import kosodrzewina.model.Sound;
import sun.audio.AudioStream;
import javax.sound.sampled.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import WavFile.WavFile;

import javax.swing.JSpinner;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class MainView implements ActionListener, ChangeListener{

	private JFrame frame;
	JMenuItem loadFile;
	JMenuItem closeApp;
	private JButton btnPlay;
	JButton saveSound;
	JLabel fileName;
	JLabel filePath;
	JSlider volume;
	JSpinner volumeValue;
	InputStream in;
	AudioStream audios = null;
	Sound sound;
	JLabel soundChart;
	
	byte[] soundTab = null;
	
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
		frame.setBounds(100, 100, 733, 564);
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
		btnPlay.setEnabled(false);
		btnPlay.addActionListener(this);
		btnPlay.setBounds(10, 52, 200, 92);
		frame.getContentPane().add(btnPlay);
		
		JLabel lblWczytano = new JLabel("Wczytano: ");
		lblWczytano.setBounds(10, 11, 68, 14);
		frame.getContentPane().add(lblWczytano);
		
		fileName = new JLabel("");
		fileName.setBounds(77, 11, 112, 14);
		frame.getContentPane().add(fileName);
		
		filePath = new JLabel("");
		filePath.setBounds(199, 11, 467, 14);
		frame.getContentPane().add(filePath);
		
		volume = new JSlider();
		volume.setBounds(10, 155, 200, 26);
		volume.addChangeListener(this);
		frame.getContentPane().add(volume);
		
		volumeValue = new JSpinner();
		volumeValue.setValue(volume.getValue());
		volumeValue.addChangeListener(this);
		volumeValue.setBounds(220, 155, 40, 26);
		frame.getContentPane().add(volumeValue);
		
		saveSound = new JButton("Zapisz");
		saveSound.addActionListener(this);
		saveSound.setBounds(220, 52, 89, 23);
		frame.getContentPane().add(saveSound);
		
		soundChart = new JLabel("");
		soundChart.setBorder(new LineBorder(new Color(0, 0, 0)));
		soundChart.setBounds(10, 213, 697, 280);
		frame.getContentPane().add(soundChart);
	}

	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		if(z == loadFile){
			FileLoader fl = new FileLoader();
			File file = fl.fileOpener();
			fileName.setText(file.getName());
			try {
				filePath.setText(file.getCanonicalPath().toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//			try {
//				stream = AudioSystem.getAudioInputStream(file);
//				AudioInputStream stream2 = AudioSystem.getAudioInputStream(file);
//			    format = stream.getFormat();
//			    System.out.println(format);
//			    info = new DataLine.Info(Clip.class, format);
//			    clip = (Clip) AudioSystem.getLine(info);
//			    clip.open(stream);
////				soundTab = readSound(stream2);
//				sound = new Sound(readSound(stream2), format.isBigEndian(), format.getChannels(), 
//								  format.getEncoding(), format.getFrameRate(), format.getSampleSizeInBits());
//			    btnPlay.setEnabled(true);
//			} catch (Exception e1) {
//				JOptionPane.showMessageDialog(null, e1);
//			}
			try{
				stream = AudioSystem.getAudioInputStream(file);
			    format = stream.getFormat();
			    info = new DataLine.Info(Clip.class, format);
			    clip = (Clip) AudioSystem.getLine(info);
			    clip.open(stream);
			    btnPlay.setEnabled(true);
				
				 // Open the wav file specified as the first argument
		         WavFile wavFile = WavFile.openWavFile(file);

		         // Display information about the wav file
//		         wavFile.display();
		         // Get the number of audio channels in the wav file
		         int numChannels = wavFile.getNumChannels();

		         // Create a buffer of 100 frames
		         double[] buffer = new double[100 * numChannels];
		         System.out.println(buffer.length);
		         int framesRead;
		         double min = Double.MAX_VALUE;
		         double max = Double.MIN_VALUE;

		         do
		         {
		            // Read frames into buffer
		            framesRead = wavFile.readFrames(buffer, 100);
		            // Loop through frames and look for minimum and maximum value
		            for (int s=0 ; s<framesRead * numChannels ; s++)
		            {
		               if (buffer[s] > max) max = buffer[s];
		               if (buffer[s] < min) min = buffer[s];
		            }
		         }while (framesRead != 0);

		         // Close the wavFile
		         wavFile.close();
		         sound = new Sound(buffer, wavFile.getNumChannels()
		        		 		  , wavFile.getNumFrames(), wavFile.getValidBits()
		        		 		  , min, max);
		         System.out.println(sound.toString());
//		         System.out.printf("Min: %f, Max: %f\n", min, max);
		         double[][] cos = new double[2][2];
		         cos[0][0] = 2;
		         cos[0][1] = 3;
		         cos[1][0] = -2;
		         cos[1][1] = 5;
		         makeChart(frame, "Wykres", cos);
		      }
		      catch (Exception exc)
		      {
		         System.err.println(exc);
		      }

		}else if(z == closeApp){
			clip.close();
			System.exit(0);
		}else if(z == btnPlay){
			clip.stop();
			clip.setMicrosecondPosition(0);
			clip.start();
		}else if(z == saveSound){
			FileSaver.saveSoundFile(sound);
		}
	}

	public void stateChanged(ChangeEvent e) {
		Object z = e.getSource();
		
		if(z == volume){
			volumeValue.setValue(volume.getValue());
		}else if(z == volumeValue){
			Integer val = (Integer) volumeValue.getValue();
			if(val > 100)		volumeValue.setValue(100);
			else if(val < 0) 	volumeValue.setValue(0);
			volume.setValue( (Integer) volumeValue.getValue());
		}
	}
	
	//read sound in byte - to AudioInputStream
	public byte[] readSound( AudioInputStream stream){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedInputStream in = new BufferedInputStream(stream);

		int read;
		byte[] tab = null;
		byte[] buff = new byte[1024];
		try {
			while ((read = in.read(buff)) > 0)
			{
			    out.write(buff, 0, read);
			}
			out.flush();
			tab = out.toByteArray();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e);
			e.printStackTrace();
		}
		return tab;
	}
	
	private void makeChart(JFrame frame, String name, double[][] tab) {

		DefaultCategoryDataset data = new DefaultCategoryDataset();
		
		data.setValue(tab[0][0], "a", "b");
		data.setValue(tab[0][1], "a", "b");
		data.setValue(tab[1][0], "a", "b");
		data.setValue(tab[1][1], "a", "b");
		CategoryDataset dataset = data;
		//trzeba jakos wype³ni ten dataset
		JFreeChart loadImageChart = ChartFactory.createBarChart(name, "cat x", "val y",
				dataset, PlotOrientation.VERTICAL, true, true, true);
		
		ChartPanel loadChartPanel = new ChartPanel(loadImageChart);
		loadChartPanel.setBounds(soundChart.getX(), soundChart.getY(), soundChart.getWidth(), soundChart.getHeight());
		frame.getContentPane().add(loadChartPanel, soundChart);
		
//		if(loadChartPanelLeft!=null)
//			frame.getContentPane().remove(loadChartPanelLeft);
//		loadChartPanelLeft = loadChartPanel;
//		frame.getContentPane().add(loadChartPanelLeft);
		
	}
}
