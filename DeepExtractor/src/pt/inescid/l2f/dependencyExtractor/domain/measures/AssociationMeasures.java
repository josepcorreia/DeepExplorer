package pt.inescid.l2f.dependencyExtractor.domain.measures;

import java.lang.Math;

public class AssociationMeasures {

	public  static double PMI(long nwords, long depfreq, long word1freq, long word2freq ){
		double num = nwords * depfreq;
		double den = word1freq * word2freq;
		return Math.log10(num/den); 
	}
	public  static double Dice(long depfreq, long word1freq, long word2freq ){
		double num = 2 * depfreq;
		double den = word1freq + word2freq;
		return (num/den);
	}
	public static double LogDice(long depfreq, long word1freq, long word2freq ){
		return (14+log(Dice(depfreq,word1freq,word2freq),2));
	}



	public static double log(double x, int base){
		double num = Math.log(x);
		double den = Math.log(base);
		return (num/den);
	}
}