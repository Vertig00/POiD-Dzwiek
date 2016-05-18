package kosodrzewina.implementation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import WavFile.WavFile;

import javax.sound.sampled.AudioFileFormat.Type;

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
	
	public static void saveSoundFile(Sound s){
		File file = new File(destination + name);
	      try
	      {	         // Create a wav file with the name specified as the first argument
	         WavFile wavFile = WavFile.newWavFile(file, 
	        		 							  s.getChannels(),
	        		 							  (long) s.getFrameRate(),
	        		 							  s.getSampleSizeInBits(),
	        		 							  (long) s.getFrameRate());
	         // Create a buffer of 100 frames
	         double[] buffer = new double[100];

	         // Initialise a local frame counter
	         long frameCounter = 0;

	         // Loop until all frames written
	         while (frameCounter < s.getFrameRate())
	         {
	            // Determine how many frames to write, up to a maximum of the buffer size
	            long remaining = wavFile.getFramesRemaining();
	            int toWrite = (remaining > 100) ? 100 : (int) remaining;


	            // Write the buffer
	            wavFile.writeFrames(s.getSoundTab(), toWrite);
	         }
	         wavFile.close();
	      }
	      catch (Exception e)
	      {
	         System.err.println(e);
	      }
	}
		
}
