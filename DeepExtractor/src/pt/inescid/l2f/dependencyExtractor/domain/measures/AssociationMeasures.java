package pt.inescid.l2f.dependencyExtractor.domain.measures;

import java.lang.Math;

public class AssociationMeasures {

	public  static double PMI(long nwords, long depfreq, long word1freq, long word2freq ){
		double num = nwords * depfreq;
		double den = word1freq * word2freq;
		return Math.log10(num/den); 
	}
}
