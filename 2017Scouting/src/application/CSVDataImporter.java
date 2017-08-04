package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class CSVDataImporter {
	private File csvFile;
	public CSVDataImporter(File csvFile){
		this.csvFile = csvFile;
	}
	public String[] importCSVData(){
		String[] out = null;
		FileReader fileIn;
		try {
			fileIn = new FileReader(csvFile);
			Scanner fileScan = new Scanner(fileIn);
			while(fileScan.hasNextLine()){
				String dataIn = fileScan.nextLine();
				String[] dataArray = dataIn.split(",");
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out;
	}
	public int parseInt(String str){
		if(str.equals(null) || str.equals("") || str.equals(" ")){
			return 0;
		}
		else{
			try{
				Integer.parseInt(str);
			}
			catch(NumberFormatException e){
				e.printStackTrace();
				return 0;
			}
		}
		return 0;
	}
	
}
