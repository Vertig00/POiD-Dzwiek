package kosodrzewina.implementation;

import java.io.File;
import java.io.IOException;

import WavFile.WavFile;
import WavFile.WavFileException;
import kosodrzewina.model.Sound;

public class Run {

	public static void mainRun(Sound args) {
		//do GUI, jak chcesz wrócic do swojego odkomenduj powy¿sze i zmieñ main na String[]
		Sound sound = args;
		
		// FFT
//		Sound modifiedSound = MethodsFFT.fourierSpectrum(sound);
		// TODO: ZMIENIC METODE; WYCHODZI LISTA WIEC TRZEBA W GUI WYPISAC LISTE HZ
		System.out.println("");

		// Save
		String pathOut2 = "sounds/generatedTone.wav";
		File fileOut = new File(pathOut2);
//		saveWav(modifiedSound, fileOut);
		
		System.out.println("run:end");
	}
	
	public static File fftSound(Sound sound, int partLength) {
		// Method
		Sound modifiedSound = MethodsFFT.generateSoundFourier(sound, partLength);
		
		// sysout Hzts
		for(int i = 0; i < MethodsFFT.statPartsHz.length; i++) 
        	System.out.println( (i) + ": " + MethodsFFT.statPartsHz[i] + " Hz");
		
		// Save
		String pathOut2 = "sounds/generatedTones.wav";
		File fileOut = new File(pathOut2);
		saveWav(modifiedSound, fileOut);
		
		return fileOut;
	}
	
	public static void main(String[] args) {
	    File file = new File(args[0]);
	    int partLength = Integer.parseInt(args[1]);
	    
          // Read
          Sound sound = readWav(file);
          //sound.info();
	    
//		Sound modifiedSound = MethodsFFT.fourierSpectrum(sound);
        Sound modifiedSound = MethodsFFT.generateSoundFourier(sound, partLength);
		
        for(int i = 0; i < MethodsFFT.statPartsHz.length; i++) 
        	System.out.println( (i) + ": " + MethodsFFT.statPartsHz[i] + " Hz");
        
		// Save
		String pathOut2 = "sounds/generatedTones.wav";
		File fileOut = new File(pathOut2);
		saveWav(modifiedSound, fileOut);
	}
	
	private static void outDoubles(double tab[]) {
		System.out.println("Tab");
		for(int i = 0; i < tab.length; i++) {
			System.out.println("  " + tab[i]);
		}
		System.out.println("Tab:end");
		System.out.println("");
	}
	
	private static Sound readWav(File file) {
		Sound sound = new Sound();
		try {
			
			// Open .wav
			WavFile wf = WavFile.openWavFile(file);
//			wf.display();
//			System.out.println("");
			
			// Save parameters
			sound.setNumChannels( wf.getNumChannels() );
			sound.setValidBits( wf.getValidBits() );
			sound.setSampleRate( wf.getSampleRate() );
			
			// Save sound/all frames
			double[][] frames = new double[sound.getNumChannels()][(int) wf.getNumFrames()];
			wf.readFrames(frames, (int) wf.getNumFrames());
			sound.setFrames(frames);
			
			// Close WavFile
			wf.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WavFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return sound;
	}
	
	private static void saveWav(Sound sound, File file) {
		try {
			// Create new .wav
			WavFile wf = WavFile.newWavFile(file, 
					sound.getNumChannels(), 
					sound.getNumFrames(), 
					sound.getValidBits(), 
					sound.getSampleRate()
					);
			
			// Write Frames
			wf.writeFrames(sound.getFrames(), 
					sound.getFrames()[0].length
					);
			
			// Close WavFile
			wf.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WavFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
