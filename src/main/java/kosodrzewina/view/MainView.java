package kosodrzewina.view;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import kosodrzewina.implementation.FileLoader;
import kosodrzewina.implementation.FileSaver;
import kosodrzewina.implementation.Methods;
import kosodrzewina.implementation.MethodsFFT;
import kosodrzewina.implementation.Run;
import kosodrzewina.model.Sound;
import sun.audio.AudioStream;

import javax.sound.midi.MetaEventListener;
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
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class MainView implements ActionListener{

	private int chartNumber = 0;
	
	private JFrame frame;
	JMenuItem loadFile;
	JMenuItem closeApp;
	private JButton btnPlay;
	JLabel fileName;
	JLabel filePath;
	InputStream in;
	Sound sound;
	File modifiedSound;
	ChartPanel loadChartPanelLeft;
	JFreeChart loadImageChart;
	JComboBox<String> options;
	JComboBox<Double> sampling;
	JLabel frequencyLabel;
	JButton doIt;
	JButton playNewSound;
	
	byte[] soundTab = null;
	String[] optionTab = {"zero-crossing","Upelszone zero-crossing"};
	Double[] samplingTab = {(double) 512,(double) 1024,(double) 2048,(double) 4096,(double) 8192};
	
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    Clip clip;
    Clip clip2;
    private JTextField dokl;
    private JLabel lblProcent;
    private JTextField tresholdDivider;
    private JTextField minTreshold;
    private JTextField maxesDiff;
    JLabel lblDivider;
    JLabel lblMinimum_1;
    JLabel lblMinimum;
    
    JButton btnPrawo;
    JButton btnLewo;
    JButton fourierButton;
    


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

	public MainView() {
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
		
		loadChartPanelLeft = new ChartPanel(loadImageChart);
		loadChartPanelLeft.setBorder(new LineBorder(new Color(0, 0, 0)));
		loadChartPanelLeft.setBounds(10, 213, 697, 280);
		frame.getContentPane().add(loadChartPanelLeft);
		
		JLabel lblWybierzOpcje = new JLabel("Wybierz opcje");
		lblWybierzOpcje.setBounds(220, 37, 105, 14);
		frame.getContentPane().add(lblWybierzOpcje);
		
		options = new JComboBox<String>();
		for (String string : optionTab) {
			options.addItem(string);
		}
		options.setBounds(335, 36, 133, 20);
		frame.getContentPane().add(options);
		
		doIt = new JButton("Wykonaj");
		doIt.setEnabled(false);
		doIt.addActionListener(this);
		doIt.setBounds(335, 147, 112, 23);
		frame.getContentPane().add(doIt);
		
		JLabel lblWynik = new JLabel("Wynik: ");
		lblWynik.setBounds(335, 188, 105, 14);
		frame.getContentPane().add(lblWynik);
		
		frequencyLabel = new JLabel("");
		frequencyLabel.setBounds(450, 188, 112, 14);
		frame.getContentPane().add(frequencyLabel);
		
		dokl = new JTextField();
		dokl.setText("1");
		dokl.setBounds(335, 108, 86, 20);
		frame.getContentPane().add(dokl);
		dokl.setColumns(10);
		
		sampling = new JComboBox<Double>();
		for (Double string : samplingTab) {
			sampling.addItem(string);
		}
		sampling.setBounds(335, 72, 133, 20);
		frame.getContentPane().add(sampling);
		
		JLabel lblPrbkowanie = new JLabel("Pr\u00F3bkowanie");
		lblPrbkowanie.setBounds(220, 75, 105, 14);
		frame.getContentPane().add(lblPrbkowanie);
		
		lblProcent = new JLabel("Procent (%)");
		lblProcent.setBounds(220, 111, 105, 14);
		frame.getContentPane().add(lblProcent);
		
		JLabel lblFourier = new JLabel("Fourier");
		lblFourier.setBounds(590, 11, 46, 14);
		frame.getContentPane().add(lblFourier);
		
		tresholdDivider = new JTextField();
		tresholdDivider.setText("20");
		tresholdDivider.setBounds(621, 36, 86, 20);
		frame.getContentPane().add(tresholdDivider);
		tresholdDivider.setColumns(10);
		
		minTreshold = new JTextField();
		minTreshold.setText("8");
		minTreshold.setColumns(10);
		minTreshold.setBounds(621, 72, 86, 20);
		frame.getContentPane().add(minTreshold);
		
		maxesDiff = new JTextField();
		maxesDiff.setText("50");
		maxesDiff.setColumns(10);
		maxesDiff.setBounds(621, 108, 86, 20);
		frame.getContentPane().add(maxesDiff);
		
		lblDivider = new JLabel("DividerThres");
		lblDivider.setBounds(517, 39, 79, 14);
		frame.getContentPane().add(lblDivider);
		
		lblMinimum_1 = new JLabel("MinThres");
		lblMinimum_1.setBounds(517, 75, 79, 14);
		frame.getContentPane().add(lblMinimum_1);
		
		lblMinimum = new JLabel("minDiffMaxes");
		lblMinimum.setBounds(517, 114, 79, 14);
		frame.getContentPane().add(lblMinimum);
		
		fourierButton = new JButton("Wykonaj ");
		fourierButton.setBounds(618, 147, 89, 23);
		fourierButton.addActionListener(this);
		frame.getContentPane().add(fourierButton);
		
		btnLewo = new JButton("Lewo");
		btnLewo.addActionListener(this);
		btnLewo.setBounds(10, 184, 89, 23);
		frame.getContentPane().add(btnLewo);
		
		btnPrawo = new JButton("Prawo");
		btnPrawo.addActionListener(this);
		btnPrawo.setBounds(109, 184, 89, 23);
		frame.getContentPane().add(btnPrawo);
		
		playNewSound = new JButton("Odtwórz");
		playNewSound.addActionListener(this);
		playNewSound.setBounds(400, 147, 89, 23);
		frame.getContentPane().add(playNewSound);
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
			    doIt.setEnabled(true);
				
			    sound = FileLoader.readWav(file);
//			    double[][] cos = sound.getFrames();
//			    double w = Methods.zeroCrossingLine(sound);
//			    System.out.println("cos: "+w);
//			    double[][] forChart = new double[2][cos[0].length];
//			    for(int i = 0; i < cos[0].length;i++ ){
//			    	forChart[0][i] = i/cos[0].length;
//			    	forChart[1][i] = cos[0][i];
//			    }
//		        makeChart(frame, "Wykres", forChart);
//		        loadChartPanelLeft.updateUI();
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
			Methods.setNumberProbe(Double.parseDouble(sampling.getSelectedItem().toString()));
			System.out.println("Próbki: "+Methods.getNumberProbe());
			if(options.getSelectedIndex() == 0){
				Double frequency = Methods.zeroCrossingLine(sound);
				frequencyLabel.setText(frequency.toString()+"Hz");
			}else if(options.getSelectedIndex() == 1){
				Double frequency = Methods.zeroCrossingLineUpdated(sound, Double.parseDouble(dokl.getText()));
				frequencyLabel.setText(frequency.toString()+"Hz");
			}
			//fourier na starym obecnie nieu¿ywany
//				Double frequency = Methods.zeroCrossingLineUpdated(sound, Double.parseDouble(dokl.getText()));
		}else if(z == playNewSound){
		    try {
                stream = AudioSystem.getAudioInputStream(modifiedSound);
                format = stream.getFormat();
                info = new DataLine.Info(Clip.class, format);
                clip2 = (Clip) AudioSystem.getLine(info);
                clip2.open(stream);
            } catch (UnsupportedAudioFileException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (LineUnavailableException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            		    
	          clip2.stop();
	          clip2.setMicrosecondPosition(0);
	          clip2.start();
		}
		else if(z == fourierButton){
			//wykonanie fouriera
			int partLenght = (int) Double.parseDouble(sampling.getSelectedItem().toString());
			
			MethodsFFT.thresholdDivider = Double.parseDouble(tresholdDivider.getText());
			MethodsFFT.minThreshold = Double.parseDouble(minTreshold.getText());
			MethodsFFT.minMaxesDiff = Double.parseDouble(maxesDiff.getText());
			
			modifiedSound = Run.fftSound(sound, partLenght);
			System.out.println(MethodsFFT.thresholdDivider);
			placeChart();
			
			//wyœwietlic frequency w elemencie frequencyLabel
			//frequencyLabel.setText(frequency+"Hz");
			
		}else outerloop: if(z == btnLewo){
			if(chartNumber <= 0)
				break outerloop;
			chartNumber--;
			placeChart();
			
		}else outerloop2: if(z == btnPrawo){
			if(MethodsFFT.statPartsHz.length-1 <= 1)
				break outerloop2;
			chartNumber++;
			placeChart();
			
		}
	}
	private void refreshChart() {
		placeChart();
		frame.getContentPane().remove(loadChartPanelLeft);
		frame.getContentPane().add(loadChartPanelLeft);
	}
	private void placeChart() {
		loadChartPanelLeft.setChart( MethodsFFT.chartShortNP(chartNumber, 2) );
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
	
	private void makeChart(JFrame frame, String name, double[][] tab) {

		DefaultCategoryDataset data = new DefaultCategoryDataset();
		
		for(int i = 0 ; i < tab[0].length;i++){
			data.setValue(tab[0][i], "x", "y");
			data.setValue(tab[1][i], "x", "y");
		}
		CategoryDataset dataset = data;
		//trzeba jakos wype³ni ten dataset
		loadImageChart = ChartFactory.createLineChart(name, "cat x", "val y",
				dataset, PlotOrientation.VERTICAL, true, true, true);
		
		ChartPanel loadChartPanel = new ChartPanel(loadImageChart);
		loadChartPanel.setBounds(loadChartPanelLeft.getX(), loadChartPanelLeft.getY(), loadChartPanelLeft.getWidth(), loadChartPanelLeft.getHeight());
		frame.getContentPane().add(loadChartPanel, loadChartPanelLeft);
		
		if(loadChartPanelLeft!=null)
			frame.getContentPane().remove(loadChartPanelLeft);
		loadChartPanelLeft = loadChartPanel;
		frame.getContentPane().add(loadChartPanelLeft);
		
	}
}
