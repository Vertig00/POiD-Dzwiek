package kosodrzewina.implementation;

import java.io.File;
import java.io.IOException;

import WavFile.WavFile;
import WavFile.WavFileException;
import kosodrzewina.model.Sound;

public class Run {

	public static void main(String[] args) {
		String path1 = "src/main/resources/TestWav/i.wav";
		String path2 = "sounds/seq/DWK_violin.wav";
		File file = new File(path2);

		Sound sound = readWav(file);
		
		sound.info();
		
		String pathOut2 = "sounds/seq/Violinno.wav";
		File fileOut = new File(pathOut2);
		saveWav(sound, fileOut);
		
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
			wf.display();
			System.out.println("");
			
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
