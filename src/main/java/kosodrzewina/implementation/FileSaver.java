package kosodrzewina.implementation;

import java.io.File;
import java.io.IOException;
import WavFile.WavFile;
import WavFile.WavFileException;
import kosodrzewina.model.Sound;

public class FileSaver {
	
	private static String name = "EditSound.wav";
	private static String destination = "sounds/";

	public static void saveWav(Sound sound) {
		File file = new File(destination+name);
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
