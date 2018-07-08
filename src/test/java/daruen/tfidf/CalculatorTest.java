package daruen.tfidf;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import daruen.tfidf.calculators.IDFCalculator;
import daruen.tfidf.calculators.TFCalculator;
import daruen.tfidf.calculators.TFIDFCalculator;

/**
 * Unit test for KComplementary.
 */
public class CalculatorTest {
	
	TFCalculator tfCalculator=new TFCalculator();
	IDFCalculator idfCalculator=new IDFCalculator();
	TFIDFCalculator tfidfCalculator=new TFIDFCalculator();

	@Test
	public void shouldCalculateTFforSample() {

		File document = new File(getClass().getClassLoader().getResource("examples/Sample.txt").getFile());
		Double tf = tfCalculator.process(document, "this");

		Assert.assertEquals(new Double(1d/5), tf);

	}
	
	@Test
	public void shouldCalculateIDFforThis() {

		File document = new File(getClass().getClassLoader().getResource("examples/Sample.txt").getFile());
		Double tf = idfCalculator.process(document, "this");

		Assert.assertEquals(new Double(0), tf);

	}
	
	@Test
	public void shouldCalculateTFforExample() {

		File document = new File(getClass().getClassLoader().getResource("examples/Example.txt").getFile());
		Double tf = tfCalculator.process(document, "example");

		Assert.assertEquals(new Double(3d/7), tf);

	}
	
	@Test
	public void shouldCalculateTFIDFforExampleinSamplefile() {

		File document = new File(getClass().getClassLoader().getResource("examples/Sample.txt").getFile());
		Double tfidf = tfidfCalculator.process(document, "example");

		Assert.assertEquals(new Double((0d/5)*Math.log10(2/1)), tfidf);

	}
	

	
}
