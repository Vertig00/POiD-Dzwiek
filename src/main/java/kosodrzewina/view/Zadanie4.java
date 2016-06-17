package kosodrzewina.view;

import java.awt.Color;
import java.awt.Dimension;
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
import java.net.CacheRequest;
import java.util.ArrayList;
import java.util.HashMap;
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
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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
import javax.swing.JTabbedPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;

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
    private ChartPanel chartPanel2;
    private JButton previous;
    private JButton next;
    JSlider slider1;
    
    ArrayList<JSlider> sliderArray = new ArrayList<JSlider>();
    ArrayList<JSpinner> spinnerArray = new ArrayList<JSpinner>();

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
		frame.setBounds(100, 100, 900, 672);
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
//		for (String string : optionTab) {
//			windowBox.addItem(string);
//		}
//		for (Double string : samplingTab) {
//			framesBox.addItem(string);
//		}
//		for (String string : filtrTab) {
//			filtrBox.addItem(string);
//		}
//		for (String string : zeroPlugTab) {
//			zeroPlug.addItem(string);
//		}
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 93, 864, 504);
		frame.getContentPane().add(tabbedPane);
		
		
		//-----------------------------------   KOREKCJA
		Korekcja = new JPanel();
		tabbedPane.addTab("Korekcja Graficzna", null, Korekcja, null);
		Korekcja.setLayout(null);
		
		int w = 0;
		for(int i = 0; i < 30;i++){
			final int count = i;
			int x = 57;
			int y = 230;
			SpinnerNumberModel spinnerModel = new SpinnerNumberModel(50.0, 0.0, 100.0, 1.0);
			sliderArray.add(new JSlider());
			spinnerArray.add(new JSpinner(spinnerModel));
			if(i<15){
				sliderArray.get(i).setBounds(10+x*i, 10, 40, 180);
				spinnerArray.get(i).setBounds(10+x*i, 190, 40, 20);
			}
			else{
				sliderArray.get(i).setBounds(10+x*w, 10+y, 40, 180);
				spinnerArray.get(i).setBounds(10+x*w, 190+y, 40, 20);
				w++;
			}
			sliderArray.get(i).setOrientation(SwingConstants.VERTICAL);
			spinnerArray.get(i).setValue(50);
			spinnerArray.get(i).setMaximumSize(new Dimension(100, 0));
			Korekcja.add(sliderArray.get(i));
			Korekcja.add(spinnerArray.get(i));
			
			sliderArray.get(i).addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					Integer q = sliderArray.get(count).getValue();
					spinnerArray.get(count).setValue(q);			
				}
			});
			
			spinnerArray.get(i).addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					double val = checkBounds(Double.parseDouble(spinnerArray.get(count).getValue().toString()));
					sliderArray.get(count).setValue((int) val);
				}
			});
			
		}

		
		//-----------------------------------  WAH-WAH
		
		WahWah = new JPanel();
		tabbedPane.addTab("Wha-Wah", null, WahWah, null);
		WahWah.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Zmienna 1");
		lblNewLabel.setBounds(84, 47, 88, 20);
		WahWah.add(lblNewLabel);
		
		variable1 = new JTextField();
		variable1.setBounds(170, 47, 86, 20);
		WahWah.add(variable1);
		variable1.setColumns(10);
		
		JLabel lblZmienna = new JLabel("Zmienna 2");
		lblZmienna.setBounds(84, 73, 88, 20);
		WahWah.add(lblZmienna);
		
		variable2 = new JTextField();
		variable2.setColumns(10);
		variable2.setBounds(170, 73, 86, 20);
		WahWah.add(variable2);
		
		lblZmienna_1 = new JLabel("Zmienna 3");
		lblZmienna_1.setBounds(84, 96, 88, 20);
		WahWah.add(lblZmienna_1);
		
		variable3 = new JTextField();
		variable3.setColumns(10);
		variable3.setBounds(170, 96, 86, 20);
		WahWah.add(variable3);


		//----------------------------------
		
		Filtr = new JPanel();
		tabbedPane.addTab("Filtr \"impulsowy\"", null, Filtr, null);
		Filtr.setLayout(null);
		
		chartPanel2 = new ChartPanel((JFreeChart) null);
		chartPanel2.setBounds(0, 183, 421, 293);
		Filtr.add(chartPanel2);
		chartPanel2.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		filtrLenghtField = new JTextField();
		filtrLenghtField.setBounds(212, 97, 101, 20);
		Filtr.add(filtrLenghtField);
		filtrLenghtField.setColumns(10);
		
		lblCzestotliwoscOdciecia = new JLabel("Czestotliwosc odciecia ");
		lblCzestotliwoscOdciecia.setBounds(49, 72, 153, 14);
		Filtr.add(lblCzestotliwoscOdciecia);
		
		frequencyCutField = new JTextField();
		frequencyCutField.setBounds(212, 72, 101, 20);
		Filtr.add(frequencyCutField);
		frequencyCutField.setColumns(10);
		
		chartPanel = new ChartPanel((JFreeChart) null);
		chartPanel.setBounds(443, 183, 416, 293);
		Filtr.add(chartPanel);
		chartPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		chartPanel.setLayout(null);
		
		next = new JButton("Nast\u0119pny");
		next.setBounds(99, 157, 79, 23);
		Filtr.add(next);
		
		doIt = new JButton("Wykonaj");
		doIt.setBounds(679, 146, 99, 23);
		Filtr.add(doIt);
		
		lblOkno = new JLabel("Okno:");
		lblOkno.setBounds(514, 87, 155, 14);
		Filtr.add(lblOkno);
		
		previous = new JButton("Poprzedni");
		previous.setBounds(10, 157, 79, 23);
		Filtr.add(previous);
		
		lblUzupelnienieZerami = new JLabel("Uzupelnienie zerami?");
		lblUzupelnienieZerami.setBounds(514, 25, 155, 14);
		Filtr.add(lblUzupelnienieZerami);
		
		lblDugoFiltraL = new JLabel("D\u0142ugo\u015B filtra L");
		lblDugoFiltraL.setBounds(49, 97, 153, 14);
		Filtr.add(lblDugoFiltraL);
		
		przesuniecie = new JTextField();
		przesuniecie.setBounds(212, 47, 101, 20);
		Filtr.add(przesuniecie);
		przesuniecie.setColumns(10);
		
		lblPrzesunicieHopsize = new JLabel("Przesuni\u0119cie R:");
		lblPrzesunicieHopsize.setBounds(49, 47, 153, 14);
		Filtr.add(lblPrzesunicieHopsize);
		
		zeroPlug = new JComboBox<String>();
		zeroPlug.setBounds(679, 22, 99, 20);
		Filtr.add(zeroPlug);
		
		lblDugocOkna = new JLabel("D\u0142ugo\u015Bc okna M:");
		lblDugocOkna.setBounds(49, 22, 153, 14);
		Filtr.add(lblDugocOkna);
		
		windowBox = new JComboBox<String>();
		windowBox.setBounds(679, 84, 99, 20);
		Filtr.add(windowBox);
		
		lblPrbkowanie = new JLabel("Pr\u00F3bkowanie:");
		lblPrbkowanie.setBounds(514, 56, 155, 14);
		Filtr.add(lblPrbkowanie);
		
		framesBox = new JComboBox<Double>();
		framesBox.setBounds(679, 53, 99, 20);
		Filtr.add(framesBox);
		
		filtrBox = new JComboBox<String>();
		filtrBox.setBounds(679, 115, 99, 20);
		Filtr.add(filtrBox);
		
		windowLenghtInput = new JTextField();
		windowLenghtInput.setBounds(212, 22, 101, 20);
		Filtr.add(windowLenghtInput);
		windowLenghtInput.setColumns(10);
		
		lblFiltracja = new JLabel("Filtracja:");
		lblFiltracja.setBounds(514, 118, 155, 14);
		Filtr.add(lblFiltracja);
		previous.addActionListener(this);
		doIt.addActionListener(this);
		next.addActionListener(this);
		
		btnPlay = new JButton("Play");
		btnPlay.setBounds(10, 47, 101, 35);
		frame.getContentPane().add(btnPlay);
		btnPlay.setEnabled(false);
		
		JLabel lblWczytano = new JLabel("Wczytano: ");
		lblWczytano.setBounds(10, 15, 91, 20);
		frame.getContentPane().add(lblWczytano);
		
		fileName = new JLabel("");
		fileName.setBounds(111, 15, 140, 20);
		frame.getContentPane().add(fileName);
		
		filePath = new JLabel("");
		filePath.setBounds(261, 15, 369, 20);
		frame.getContentPane().add(filePath);
		btnPlay.addActionListener(this);
		
//		tabbedPane.addTab("Filtr", component);
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
//			int zeroFeed = zeroPlug.getSelectedIndex();	//wype³nianie zerami
//			double M = Double.parseDouble(windowLenghtInput.getText());
//			double R = Double.parseDouble(przesuniecie.getText());
//			double L = Double.parseDouble(filtrLenghtField.getText());
//			double frequencyCut = Double.parseDouble(frequencyCutField.getText());
//
//			ImpulseFilter filter = new ImpulseFilter(sound, M, L, R, frequencyCut, zeroFeed, window);

			chartData = Task4Implementation.run(sound, 2048, 1024);
			placeAllCharts();

		}else outerloop: if(z == previous){
			if(chartNumber <= 0)
				break outerloop;
			chartNumber--;
			refreshAllCharts();
			
		}else outerloop2: if(z == next){
			if(chartData.get(0).length -1 <= chartNumber)
				break outerloop2;
			chartNumber++;
			refreshAllCharts();
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
	public JFreeChart chartShortNP(int chartNumber, long sampleRate, int divider, int[] indexes) {
		XYSeriesCollection data = new XYSeriesCollection();
		
		XYSeries series;
		double factor;
		double[] dataArray;
		for(int i : indexes) {
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
	private void refreshChart(ChartPanel chartPanel, int[] args) {
		placeChart(chartPanel, args);
		frame.getContentPane().remove(chartPanel);
		frame.getContentPane().add(chartPanel);
	}
	private void placeChart(ChartPanel chartPanel, int[] args) {
		chartPanel.setChart( chartShortNP(chartNumber, sound.getSampleRate(), 2, args ) );
	}
	private void refreshAllCharts() {
		placeAllCharts();
		refreshChart( chartPanel, new int[]{0,1} );
		refreshChart( chartPanel2, new int[]{2,3} );
	}
	private void placeAllCharts() {
		placeChart( chartPanel, new int[]{0,1} );
		placeChart( chartPanel2, new int[]{2,3} );
	}
	
	private double checkBounds(double n){
		if(n > 100)		return 100;
		else if(n < 0)	return 0;
		else 			return n;
	}
	
	String[] titles = {
			"FFT Magnitude", 
			"FFTE Magnitude",
			"FFT Phase", 
			"FFTE Phase"
			};
	private JPanel Filtr;
	private JPanel Korekcja;
	private JPanel WahWah;
	private JTextField variable1;
	private JTextField variable2;
	private JLabel lblZmienna_1;
	private JTextField variable3;
}
























