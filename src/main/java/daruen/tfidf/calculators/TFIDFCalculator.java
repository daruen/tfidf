package daruen.tfidf.calculators;

import java.io.File;

public class TFIDFCalculator implements CalculatorInterface {

	TFCalculator tfCalculator = new TFCalculator();
	IDFCalculator idfCalculator = new IDFCalculator();

	@Override
	public Double process(File document, String term) {
		return tfCalculator.process(document, term) * idfCalculator.process(document, term);
	}

}
