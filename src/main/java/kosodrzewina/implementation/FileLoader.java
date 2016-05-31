package kosodrzewina.implementation;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import WavFile.WavFile;
import WavFile.WavFileException;
import kosodrzewina.model.Sound;

public class FileLoader {
	
	private String destinationFolder = ".\\sounds";
	private String chooserTitle = "Wybierz dŸwiêk";
	
	public FileLoader(){
		
	}

	private void createFilter(JFileChooser jf){
		FileFilter wavFilter = new FileNameExtensionFilter(".wav","wav");
		jf.addChoosableFileFilter(wavFilter);
		jf.setFileFilter(wavFilter);
	}
	
	private void selectDestinationFolder(JFileChooser fc){
		fc.setCurrentDirectory(new java.io.File(destinationFolder));
		fc.setDialogTitle(chooserTitle);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	}
	
	public File fileOpener(){
		JFileChooser jf = new JFileChooser();
		selectDestinationFolder(jf);
		createFilter(jf);
		File file = null;
		if(jf.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
			file = jf.getSelectedFile();
		}
		return file;
	}
	
	public static Sound readWav(File file) {
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
	
	public static String getPath(String path){
		String endPath = "";
		StringTokenizer token = new StringTokenizer(path, "\\");
		String[] tab = new String[token.countTokens()];
		int p = 0;
		while (token.hasMoreTokens()) {
			tab[p] = token.nextToken();
			p++;
		}

		for(int i = 0; i < tab.length; i++){
			if(!tab[i].equals("sounds")){
				tab[i] = "";
			}else{
				break;
			}
		}
		for (String string : tab) {
			if(!string.equals("")){
				endPath += string + "\\";
			}
		}
		
		return endPath.substring(0, endPath.length()-1);
	}
	
}
