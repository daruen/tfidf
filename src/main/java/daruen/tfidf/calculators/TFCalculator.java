package daruen.tfidf.calculators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

public class TFCalculator implements CalculatorInterface {

	@Override
	public Double process(File document, String term) {
		Long timesTermInDoc = 0L;
		Long wordsInDoc = 0L;
		BufferedReader fileReader=null;
		try {

			 fileReader = new BufferedReader(new FileReader(document));
			String line;
			while ((line = fileReader.readLine()) != null) {
				int idx = 0;
				while ((idx = line.indexOf(term, idx)) != -1) {
					timesTermInDoc++;
					idx += term.length();
				}
			}

			wordsInDoc = Files.lines(document.toPath()).flatMap(str -> Stream.of(str.split("[ ,.!?\r\n]")))
					.filter(s -> s.length() > 0).count();

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

		return timesTermInDoc.doubleValue() / wordsInDoc;
	}

}
