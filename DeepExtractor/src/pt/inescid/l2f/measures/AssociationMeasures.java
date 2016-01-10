package pt.inescid.l2f.measures;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

        double num = (double)ntotalCoocorrences * Math.pow((O11 * O22) - (O12 * O21), 2);

        double den = (double)(plusChi(O11,O12))*(plusChi(O11,O21))*(plusChi(O12,O22))*(plusChi(O21,O22));

        return num/den;
	}
    private static long plusChi(long op1 , long op2){
        long result = op1 + op2;

        if(result == 0)
             result = 1;

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
        return result;
    }
    private static double L(double k,double n, double z){
        double l = Math.pow(z,k)*Math.pow((1 - z),(n-z));

            if(l == 0.0){
                l=1.0;
            }
        return l;

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

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}