package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import kosodrzewina.model.Sound;

public class Task4Implementation {

	/**
	 * RUN
	 */
	public static List<double[][]> run(Sound originalSound, int M, int R) {
		List<double[][]> resultData = new ArrayList<double[][]>();
		List<double[]> fftMag = new ArrayList<double[]>();
		List<double[]> fftEMag = new ArrayList<double[]>();

		// #1. Clone original sound
		Sound modifiedSound = new Sound(originalSound);
		modifiedSound.setNumChannels(1);

		// #2. Divide original sound **Windows (R)
		List<double[]> soundParts = divideSoundWindows(originalSound, M, R);

		double[] fft, fftE, spectrum;
		for (int i = 0; i < soundParts.size(); i++) {

			// 3#. Calculate fft
			fft = calcFFT(soundParts.get(i), originalSound.getSampleRate());

			// 4#. DFT * e^...
			fftE = multipyDFTwithE(fft, i, R);
			
			// 5#. Results
			// FFT Magnitude
			spectrum = calcSpectrumMagnitude(fft);
			fftMag.add(spectrum);
			// FFTe Magnitude
			spectrum = calcSpectrumMagnitude(fftE);
			fftEMag.add(spectrum);

		}
		
		resultData.add( listToDouble2d(fftMag) );
		resultData.add( listToDouble2d(fftEMag)  );
		return resultData;
	}
	/**
	 * Privs
	 */
	private static List<double[]> divideSound(Sound sound, int partLength) {
		int soundLength = sound.getNumFrames();
		int partsCount = soundLength/partLength;
		List<double[]> soundParts = new ArrayList<double[]>();

		// full length parts
		double[] part;
		for(int i = 0; i < partsCount; i++) {		
			part = new double[partLength];
			for(int j = 0; j < partLength; j++) {
				part[j] = sound.getFrames()[0][partLength*i + j];
			}
			soundParts.add( part );
		}
		// last part (not full sized)
		int lastLength = soundLength%partLength;
		if(lastLength > 0) {
			part = new double[ lastLength ];
			for(int j = 0; j < lastLength; j++) {
				part[j] = sound.getFrames()[0][partLength*partsCount + j];
			}
			soundParts.add( part );
		}
		
		return soundParts;
	}
	private static List<double[]> divideSoundWindows(Sound sound, int partLength, int distance) {
		int soundLength = sound.getNumFrames();
		int partsCount = (soundLength-partLength)/distance;
		List<double[]> soundParts = new ArrayList<double[]>();
		
		System.out.println();

		// full length parts
		double[] part;
		for(int i = 0; i < partsCount; i++) {		
			part = new double[partLength];
			for(int j = 0; j < partLength; j++) {
				part[j] = sound.getFrames()[0][distance*i + j];
			}
			soundParts.add( part );
		}
		
		return soundParts;
	}
	private static double[] calcFFT(double[] frames, long soundSampleRate) {
		// place frames to real part
		double fftFrames[] = new double[frames.length * 2];	
		for(int i = 0; i < frames.length; i++)
			fftFrames[i*2] = frames[i];
		
		// FFT
		DoubleFFT_1D fft = new DoubleFFT_1D(frames.length);
		fft.complexForward(fftFrames);
		
		return fftFrames;
	}
	private static double[] calcSpectrumPhase(double[] fft) {
		int size = fft.length/2;
		double[] phase = new double[size];
		
		for(int i = 0; i < size; i++) {
			phase[i] = Math.atan2( fft[2*i+1], fft[2*i]	);
		}
		
		return phase;
	}
	private static double[] calcSpectrumMagnitude(double[] fft) {
		int size = fft.length/2;
		double[] magnitude = new double[size];
		
		for (int i = 0; i < size; i++)
			magnitude[i] = Math.sqrt(Math.pow(fft[2 * i], 2) + Math.pow(fft[2 * i + 1], 2));

		return magnitude;
	}
	private static double[] multipyDFTwithE(double[] dft, int m, int R) {
		int N = dft.length/2;
		double[] dftE = new double[N*2];
		
		double lambda;
		for(int k = 0; k < N; k++) {
			lambda = 2*Math.PI * k*m*R/N;
			dftE[k*2] = Math.cos( lambda );
			dftE[k*2+1] = Math.sin( lambda );
			
			dftE[k*2] = dftE[k*2] * dft[k*2];
			dftE[k*2+1] = dftE[k*2+1] * dft[k*2+1];
		}
		
		return dftE;
	}
	private static double[][] listToDouble2d(List<double[]> list) {
		double[][] array = new double[list.size()][];
		
		for(int i = 0; i < list.size(); i++)
			array[i] = list.get(i);
		
		return array;
	}
	/**
	 * Tests
	 */
	public static List<double[]> testPhaseFFT(Sound originalSound, int partLength) {
		List<double[]> resultData = new ArrayList<double[]>();
		
		// #1. Clone original sound
		Sound modifiedSound = new Sound(originalSound);
		modifiedSound.setNumChannels(1);
		
		// #2. Divide original sound 	**TODO: windows
		List<double[]> soundParts = divideSound(originalSound, partLength);
		
		double[] fft, spectrum;
		for(int i = 0; i < soundParts.size(); i++) {
			
			// 3#. Calculate fft
			fft = calcFFT( soundParts.get(i), originalSound.getSampleRate() );
			
			// 4#. Calculate spectrum
			spectrum = calcSpectrumMagnitude(fft);
			resultData.add(spectrum);

		}
		
		
		return resultData;	
	}
	
	
}
