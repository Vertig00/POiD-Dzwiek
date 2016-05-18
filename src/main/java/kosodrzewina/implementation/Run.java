package kosodrzewina.implementation;

import java.io.File;
import java.io.IOException;

import WavFile.WavFile;
import WavFile.WavFileException;

public class Run {

	public static void main(String[] args) {
		String path1 = "src/main/resources/TestWav/i.wav";
		String path2 = "sounds/seq/DWK_violin.wav";
		File file = new File(path2);
		
		
		try {
			// Open .wav
			WavFile wf = WavFile.openWavFile(file);
			wf.display();
			
			// Read frames
			int size = 1024;
			int channelsNum = wf.getNumChannels();
			double a[] = new double[size*channelsNum];
			wf.readFrames(a, size);
			outDoubles(a);
			
			wf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WavFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private static void outDoubles(double tab[]) {
		System.out.println("Tab");
		for(int i = 0; i < tab.length; i++) {
			System.out.println("  " + tab[i]);
		}
		System.out.println("Tab:end");
		System.out.println("");
	}

}
