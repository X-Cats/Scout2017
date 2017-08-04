package application;
/**
 * Handles creating and writing CSV files.
 * 
 * @author Aidan Sciortino
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVFileHandler {
	private String lastSave = null;
	private File outFile = null;
	FileWriter fw = null;
	/**
	 * Creates a new CSVFileHandler object with no previously created <code>FileWriter</code> or <code>File</code> objects
	 */
	public CSVFileHandler() {
		
	}
	/**
	 * Creates a new CSVFileHandler object and initializes a new <code>FileWriter</code> and <code>File</code> object.
	 * 
	 * @param file The file to create a new <code>FileWriter</code> from
	 */
	public CSVFileHandler(File file) {
		createWriter(file);
	}
	/**
	 * Creates a new <code>FileWriter</code> object based on an input file and sets the output file for the CSVFileHandler to the input file
	 * @param tempFile The input file upon which the new <code>FileWriter</code> is created.
	 */
	public void createWriter(File tempFile) {
		if (tempFile != null || tempFile != outFile) {
			outFile = tempFile;
			try {
				try{
					fw = new FileWriter(outFile);
				}
				catch(NullPointerException e){
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * Saves a string of content into the output file
	 * @param content The content to be saved into the output file
	 */
	public void saveFile(String content) {
		if (lastSave != null && lastSave.equals(content));
		else {
			try {
				fw.write(content);
				lastSave = content;
			} catch (IOException ex) {
				
			}
		}

	}
	/**
	 * Gets the output file
	 * @return The current output file for the CSVFileHandler object
	 */
	public File getOutFile() {
		return outFile;
	}
	/**
	 * Closes the current file safely to ensure data is not lost.
	 */
	public void closeFiles() {
		try {
			try{
			fw.close();
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
