package kosodrzewina.implementation;

import java.io.File;
import java.io.IOException;
import WavFile.WavFile;
import WavFile.WavFileException;
import kosodrzewina.model.Sound;

public class FileSaver {
	
	private static String name = "EditSound.wav";
	private static String destination = "C://Users/kluch_000/Documents/GitHub/POiD-Dzwiek/sounds/";

//	public static void SaveSoundFile(Sound s){
//        try {
//        	InputStream b_in = new ByteArrayInputStream(s.getSoundTab());
////	        AudioFormat format = new AudioFormat(8000f, 16, 1, true, false);
//        	AudioFormat format = new AudioFormat(s.getFrameRate(),s.getSampleSizeInBits(),
//        										 s.getChannels(),s.getEncoding(),s.isBigEndian());
//	        AudioInputStream stream = new AudioInputStream(b_in, format,
//	                s.getSoundTab().length);
//	        File file = new File(destination + name);
//			AudioSystem.write(stream, Type.WAVE, file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
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
