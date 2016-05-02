package kosodrzewina.implementation;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	
	
	
}
