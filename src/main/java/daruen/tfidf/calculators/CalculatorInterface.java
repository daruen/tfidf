package daruen.tfidf.calculators;

import java.io.File;

public interface CalculatorInterface {

	public Double process(File document, String term);
}
