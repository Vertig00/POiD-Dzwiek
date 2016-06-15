package kosodrzewina.implementation;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;
import kosodrzewina.model.Sound;

public class Task4Implementation {

	/**
	 * RUN
	 */
	public static void run(Sound originalSound, int partLength) {

		// #1. Clone original sound
		Sound modifiedSound = new Sound(originalSound);
		modifiedSound.setNumChannels(1);
		
		// #2. Divide original sound
		List<double[]> soundParts = divideSound(originalSound, partLength);
		
		// TODO ...
		
		
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
	/**
	 * Tests
	 */
	public static double[] testPhaseFFT(Sound originalSound, int partLength) {
		// #1. Clone original sound
		Sound modifiedSound = new Sound(originalSound);
		modifiedSound.setNumChannels(1);
		
		// #2. Divide original sound
		List<double[]> soundParts = divideSound(originalSound, partLength);
		
		// 3#. Calculate fft (for 10th element)
		double[] fftFrames = calcFFT( soundParts.get(10), originalSound.getSampleRate() );
		
		// 4#. Calculate phase
		double[] phase = calcSpectrumPhase(fftFrames);
		
		return phase;	
	}
	
	
}
