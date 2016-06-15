package kosodrzewina.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import kosodrzewina.implementation.FileLoader;
import kosodrzewina.implementation.ImpulseFilter;
import kosodrzewina.implementation.MethodsFFT;
import kosodrzewina.implementation.Task4Implementation;
import kosodrzewina.model.Sound;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class Zadanie4 implements ActionListener{

	/**
	 * Vars
	 */
	double[] phase;
	
	private JFrame frame;
	JMenuItem loadFile;
	JMenuItem closeApp;
	private JButton btnPlay;
	JLabel fileName;
	JLabel filePath;
	InputStream in;
	Sound sound;
	JFreeChart loadImageChart;
	JComboBox<String> windowBox;
	
	byte[] soundTab = null;
	String[] optionTab = {"prostok¹tna","von Hanna","Hamminga"};
	String[] filtrTab = {"czas","czêstotliwoœc"};
	String[] zeroPlugTab = {"?","?"};
	Double[] samplingTab = {(double) 512,(double) 1024,(double) 2048,(double) 4096,(double) 8192};
	
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    Clip clip;
    private JLabel lblOkno;
    private JLabel lblPrbkowanie;
    private JComboBox<Double> framesBox;
    private JLabel lblFiltracja;
    private JComboBox<String> filtrBox;
    private JLabel lblDugocOkna;
    private JTextField windowLenghtInput;
    private JLabel lblPrzesunicieHopsize;
    private JTextField przesuniecie;
    private JLabel lblUzupelnienieZerami;
    private JComboBox<String> zeroPlug;
    private JButton doIt;
    private JLabel lblDugoFiltraL;
    private JTextField filtrLenghtField;
    private JLabel lblCzestotliwoscOdciecia;
    private JTextField frequencyCutField;
    private ChartPanel chartPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Zadanie4 window = new Zadanie4();
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
	public Zadanie4() {
		initialize();
	}

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
		btnPlay.setBounds(10, 36, 200, 92);
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
		
		lblOkno = new JLabel("Okno:");
		lblOkno.setBounds(220, 36, 96, 14);
		frame.getContentPane().add(lblOkno);
		
		windowBox = new JComboBox<String>();
		for (String string : optionTab) {
			windowBox.addItem(string);
		}
		windowBox.setBounds(326, 33, 112, 20);
		frame.getContentPane().add(windowBox);
		
		lblPrbkowanie = new JLabel("Pr\u00F3bkowanie:");
		lblPrbkowanie.setBounds(220, 61, 97, 14);
		frame.getContentPane().add(lblPrbkowanie);
		
		framesBox = new JComboBox<Double>();
		for (Double string : samplingTab) {
			framesBox.addItem(string);
		}
		framesBox.setEnabled(false);
		framesBox.setBounds(326, 58, 112, 20);
		frame.getContentPane().add(framesBox);
		
		lblFiltracja = new JLabel("Filtracja:");
		lblFiltracja.setBounds(219, 86, 97, 14);
		frame.getContentPane().add(lblFiltracja);
		
		filtrBox = new JComboBox<String>();
		for (String string : filtrTab) {
			filtrBox.addItem(string);
		}
		filtrBox.setBounds(326, 83, 112, 20);
		frame.getContentPane().add(filtrBox);
		
		lblDugocOkna = new JLabel("D\u0142ugo\u015Bc okna M:");
		lblDugocOkna.setBounds(448, 86, 103, 14);
		frame.getContentPane().add(lblDugocOkna);
		
		windowLenghtInput = new JTextField();
		windowLenghtInput.setBounds(561, 83, 112, 20);
		frame.getContentPane().add(windowLenghtInput);
		windowLenghtInput.setColumns(10);
		
		lblPrzesunicieHopsize = new JLabel("Przesuni\u0119cie R:");
		lblPrzesunicieHopsize.setBounds(448, 111, 103, 14);
		frame.getContentPane().add(lblPrzesunicieHopsize);
		
		przesuniecie = new JTextField();
		przesuniecie.setColumns(10);
		przesuniecie.setBounds(561, 108, 112, 20);
		frame.getContentPane().add(przesuniecie);
		
		lblUzupelnienieZerami = new JLabel("Uzupelnienie zerami?");
		lblUzupelnienieZerami.setBounds(220, 111, 96, 14);
		frame.getContentPane().add(lblUzupelnienieZerami);
		
		zeroPlug = new JComboBox<String>();
		for (String string : zeroPlugTab) {
			zeroPlug.addItem(string);
		}
		zeroPlug.setBounds(326, 108, 112, 20);
		frame.getContentPane().add(zeroPlug);
		
		doIt = new JButton("Wykonaj");
		doIt.setBounds(326, 139, 112, 23);
		frame.getContentPane().add(doIt);
		
		lblDugoFiltraL = new JLabel("D\u0142ugo\u015B filtra L");
		lblDugoFiltraL.setBounds(448, 36, 103, 14);
		frame.getContentPane().add(lblDugoFiltraL);
		
		filtrLenghtField = new JTextField();
		filtrLenghtField.setBounds(561, 33, 112, 20);
		frame.getContentPane().add(filtrLenghtField);
		filtrLenghtField.setColumns(10);
		
		lblCzestotliwoscOdciecia = new JLabel("Czestotliwosc odciecia ");
		lblCzestotliwoscOdciecia.setBounds(448, 61, 103, 14);
		frame.getContentPane().add(lblCzestotliwoscOdciecia);
		
		frequencyCutField = new JTextField();
		frequencyCutField.setColumns(10);
		frequencyCutField.setBounds(561, 58, 112, 20);
		frame.getContentPane().add(frequencyCutField);
		
		chartPanel = new ChartPanel((JFreeChart) null);
		chartPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		chartPanel.setBounds(10, 213, 697, 280);
		frame.getContentPane().add(chartPanel);

	}

	public void actionPerformed(ActionEvent e) {
		Object z = e.getSource();
		if(z == loadFile){
			FileLoader fl = new FileLoader();
			File file = fl.fileOpener();
			fileName.setText(file.getName());
			filePath.setText(FileLoader.getPath(file.getAbsolutePath()));
			try{
				stream = AudioSystem.getAudioInputStream(file);
			    format = stream.getFormat();
			    info = new DataLine.Info(Clip.class, format);
			    clip = (Clip) AudioSystem.getLine(info);
			    clip.open(stream);
			    btnPlay.setEnabled(true);
				
			    sound = FileLoader.readWav(file);
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
		}else if(z == doIt){
			System.out.println("elo1");
			int window = windowBox.getSelectedIndex();	//1 -von, 2 -hamm, 0 - kwadr
			int zeroFeed = zeroPlug.getSelectedIndex();	//wype³nianie zerami
			double M = Double.parseDouble(windowLenghtInput.getText());
			double R = Double.parseDouble(przesuniecie.getText());
			double L = Double.parseDouble(filtrLenghtField.getText());
			double frequencyCut = Double.parseDouble(frequencyCutField.getText());
			System.out.println("elo2");
			ImpulseFilter filter = new ImpulseFilter(sound, M, L, R, frequencyCut, zeroFeed, window);
			
			System.out.println("elo3");
			phase = Task4Implementation.testPhaseFFT(sound, 2048);
			System.out.println(phase);
			placeChart();
		}
	}
	
	//read sound in byte - to AudioInputStream
	public byte[] readSound(AudioInputStream stream){
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

	/**
	 * Charts Methods
	 */
	public static JFreeChart chartShortNP(double[] phase, long sampleRate, int divider) {
		XYSeries series = new XYSeries("Phase Series");
		
		double factor =(double) phase.length / sampleRate;
		for(int i = 0; i <phase.length/divider; i++)
			series.add(i/factor, phase[i]);
		XYSeriesCollection data = new XYSeriesCollection(series);
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Phase Title",          // chart title
				"X",
				"Y",
				data,                // data
				PlotOrientation.VERTICAL,
	            true,                // include legend
	            true,
	            false);

		return chart;
	}
	private void refreshChart() {
		placeChart();
		frame.getContentPane().remove(chartPanel);
		frame.getContentPane().add(chartPanel);
	}
	private void placeChart() {
		chartPanel.setChart( chartShortNP(phase, sound.getSampleRate(), 2) );
	}
	
}
























