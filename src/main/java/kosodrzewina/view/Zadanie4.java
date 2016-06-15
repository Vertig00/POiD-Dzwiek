package kosodrzewina.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import kosodrzewina.implementation.FileLoader;
import kosodrzewina.implementation.MethodsFFT;
import kosodrzewina.implementation.Task4Implementation;
import kosodrzewina.model.Sound;

public class Zadanie4 extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private int chartNumber = 0;
	/**
	 * Vars
	 */
	List<double[][]> chartData;
	
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
	String[] optionTab = {"prostok�tna","von Hanna","Hamminga"};
	String[] filtrTab = {"czas","cz�stotliwo�c"};
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
    private JButton previous;
    private JButton next;

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
		frame.requestFocusInWindow();
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
		doIt.addActionListener(this);
		
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
		
		previous = new JButton("Poprzedni");
		previous.addActionListener(this);
		previous.setBounds(10, 179, 89, 23);
		frame.getContentPane().add(previous);
		
		next = new JButton("Nast\u0119pny");
		next.addActionListener(this);
		next.setBounds(109, 179, 89, 23);
		frame.getContentPane().add(next);
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

//			int window = windowBox.getSelectedIndex();	//1 -von, 2 -hamm, 0 - kwadr
//			int zeroFeed = zeroPlug.getSelectedIndex();	//wype�nianie zerami
//			double M = Double.parseDouble(windowLenghtInput.getText());
//			double R = Double.parseDouble(przesuniecie.getText());
//			double L = Double.parseDouble(filtrLenghtField.getText());
//			double frequencyCut = Double.parseDouble(frequencyCutField.getText());
//
//			ImpulseFilter filter = new ImpulseFilter(sound, M, L, R, frequencyCut, zeroFeed, window);

			chartData = Task4Implementation.run(sound, 2048, 1024);
			placeChart();

		}else outerloop: if(z == previous){
			if(chartNumber <= 0)
				break outerloop;
			chartNumber--;
			refreshChart();
			
		}else outerloop2: if(z == next){
			if(chartData.get(0).length -1 <= chartNumber)
				break outerloop2;
			chartNumber++;
			refreshChart();
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
	public JFreeChart chartShortNP(int chartNumber, long sampleRate, int divider) {
		XYSeriesCollection data = new XYSeriesCollection();
		
		XYSeries series;
		double factor;
		double[] dataArray;
		for(int i = 0; i < chartData.size(); i++) {
			series = new XYSeries( titles[i] );
			dataArray = chartData.get(i)[chartNumber];
			factor =(double) dataArray.length / sampleRate;
			
			for(int j = 0; j <dataArray.length/divider; j++)
				series.add(j/factor, dataArray[j]);
			
			data.addSeries(series);
		}
		
		
		JFreeChart chart = ChartFactory.createXYLineChart(
				"Data Chart" + " id: " + chartNumber + "/" + (chartData.get(0).length), // chart title
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
		chartPanel.setChart( chartShortNP(chartNumber, sound.getSampleRate(), 2) );
	}

	String[] titles = {
			"FFT Magnitude", 
			"FFTE Magnitude"
			};
}























