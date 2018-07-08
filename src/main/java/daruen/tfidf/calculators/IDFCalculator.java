package daruen.tfidf.calculators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class IDFCalculator implements CalculatorInterface {

	@Override
	public Double process(File document, String term) {
		File folder = new File(document.getParentFile().getAbsolutePath());
		File[] files = folder.listFiles();
		Long totalFiles = (long) files.length;
		Long presentInFiles = 0l;

		for (File file : files) {
			BufferedReader fileReader=null;
			try {

				fileReader = new BufferedReader(new FileReader(file));
				String line;
				while ((line = fileReader.readLine()) != null) {
					if (line.contains(term)) {
						presentInFiles++;
						break;
					}
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				if(fileReader!=null)
					try {
						fileReader.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}

		return Math.log10(totalFiles / presentInFiles.doubleValue());

	}
}
