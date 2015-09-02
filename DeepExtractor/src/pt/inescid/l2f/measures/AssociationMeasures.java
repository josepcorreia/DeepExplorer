package pt.inescid.l2f.measures;

import java.lang.Math;

public class AssociationMeasures {

	public  static double PMI(long ntotalWords, long depfreq, long word1freq, long word2freq ){
		double num = ntotalWords * depfreq;
		double den = word1freq * word2freq;
		return Math.log10(num/den);
		
	}
	public  static double Dice(long depfreq, long word1freq, long word2freq ){
		double num = 2 * depfreq;
		double den = word1freq + word2freq;
		return  (num/den);
	}
	
	public static double LogDice(long depfreq, long word1freq, long word2freq ){
		return  (14+log(Dice(depfreq,word1freq,word2freq),2));
	}

	public static double chiSquarePearson(long ntotalCoocorrences,long coocorrenceFreq, long word1CooFreq, long word2CooFreq){
        //ocorrence of word1 && word2
        long O11 = coocorrenceFreq;

        //ocorrence of not_word1 && word2
        long O12 = word2CooFreq - coocorrenceFreq;

        //ocorrence of word1 && not_word2
        long O21 = word1CooFreq - coocorrenceFreq;

        //ocorrence of not_word1 && not_word2
        long O22 = ntotalCoocorrences - O11 - O12 - O21 ;

        double num = ntotalCoocorrences * Math.pow((O11 * O22) - (O12 * O21), 2);
        double den = (O11+O12)*(O11+O21)*(O12+O22)*(O21+O22);
		Double result =  num/den;

        //System.out.println(ntotalCoocorrences +" "+ coocorrenceFreq+" "+word1CooFreq+" "+word2CooFreq);

        if(result.isNaN())
            return 0;

        return result;
	}

    public static double logLikelihood(long ntotalWords, long depfreq, long word1freq, long word2freq){
        //System.out.println(ntotalWords + " "+ depfreq + " " + word1freq+" "+ word2freq );

        double p = (double)word2freq / (double)ntotalWords;
        double p1 = (double)depfreq / (double)word1freq;
        double p2 = ((double)(word2freq - depfreq))/((double)(ntotalWords - word1freq));

        Double result = -2 *( Math.log10(L(depfreq,word1freq,p)) +
                        Math.log10(L(word2freq-depfreq,ntotalWords- word1freq,p)) -
                        Math.log10(L(depfreq,word1freq,p1)) -
                        Math.log10(L(word2freq - depfreq,ntotalWords - word1freq,p2)));

//        System.out.println(result);

        if(result.isNaN())
            return -1;
        if(result.isInfinite())
            return -10;
        return result;
    }
    private static double L(double k,double n, double z){
        return Math.pow(z,k)*Math.pow((1 - z),(n-z));

    }

    public static double significance(long word1freq, long word2freq, long depfreq, long totalSentences) {
        double z = (double)(word1freq * word2freq)/ (double)totalSentences;

            //log(n!) = n*log(n) -n+1 log de base e
        double logfactorial = depfreq * Math.log(depfreq) - depfreq +1;
        Double result =  z - (depfreq * Math.log(z)) + logfactorial;

        //System.out.println(word1freq + " " + word2freq + " " + depfreq + " " + totalSentences);

        return result;

    }


    //util
	private static double log(double x, int base){
		double num = Math.log(x);
		double den = Math.log(base);
		return (num/den);
	}

}