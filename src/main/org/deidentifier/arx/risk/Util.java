package org.deidentifier.arx.risk;

public class Util {

    private static double GAMMA = 0.577215664901532860606512090082;
    private static double GAMMA_MINX = 1.e-12;
    private static double DIGAMMA_MINNEGX = -1250;
    private static double C_LIMIT = 49;
    private static double S_LIMIT = 1e-5;
    
    public static double digamma(double x) {

        double value = 0;

        while (true){

            if (x >= 0 && x < GAMMA_MINX) {
                x = GAMMA_MINX;
            }
            if (x < DIGAMMA_MINNEGX) {
                x = DIGAMMA_MINNEGX + GAMMA_MINX;
                continue;
            }
            if (x > 0 && x <= S_LIMIT) {
                return value + -GAMMA - 1 / x;
            }

            if (x >= C_LIMIT) {
                double inv = 1 / (x * x);
                return value + Math.log(x) - 0.5 / x - inv
                        * ((1.0 / 12) + inv * (1.0 / 120 - inv / 252));
            }

            value -= 1 / x;
            x = x + 1;
        }

    }
    
    /**
     * Returns the value of the trigamma function 
     * <SPAN CLASS="MATH"><I>d&#968;</I>(<I>x</I>)/<I>dx</I></SPAN>, the derivative of
     *    the digamma function, evaluated at <SPAN CLASS="MATH"><I>x</I></SPAN>.
     * 
     */
    public static double trigamma (double x) {
       double y, sum;

       if (x < 0.5) {
          y = (1.0 - x) - Math.floor (1.0 - x);
          sum = Math.PI / Math.sin (Math.PI * y);
          return  sum * sum - trigamma (1.0 - x);
       }

       if (x >= 40.0) {
          // Asymptotic series
          y = 1.0/(x*x);
          sum = 1.0 + y*(1.0/6.0 - y*(1.0/30.0 - y*(1.0/42.0 - 1.0/30.0*y)));
          sum += 0.5/x;
          return sum/x;
       }

       int i;
       int p = (int) x;
       y = x - p;
       sum = 0.0;

       if (p > 3) {
          for (i = 3; i < p; i++)
             sum -= 1.0/((y + i)*(y + i));

       } else if (p < 3) {
          for (i = 2; i >= p; i--)
             sum += 1.0/((y + i)*(y + i));
       }

       /* Chebyshev coefficients for trigamma (x + 3), 0 <= x <= 1. In Yudell
          Luke: The special functions and their approximations, Vol. II,
          Academic Press, p. 301, 1969. */
       final int N = 15;
       final double A[] = { 2.0*0.33483869791094938576, -0.05518748204873009463,
          0.00451019073601150186, -0.00036570588830372083,
          2.943462746822336e-5, -2.35277681515061e-6, 1.8685317663281e-7,
          -1.475072018379e-8, 1.15799333714e-9, -9.043917904e-11,
          7.029627e-12, -5.4398873e-13, 0.4192525e-13, -3.21903e-15, 0.2463e-15,
         -1.878e-17, 0., 0. };

       return sum + evalChebyStar (A, N, y);
    }

    /**
     * Evaluates a series of shifted Chebyshev polynomials <SPAN CLASS="MATH"><I>T</I><SUB>j</SUB><SUP>*</SUP></SPAN>
     *    at <SPAN CLASS="MATH"><I>x</I></SPAN> over the basic interval <SPAN CLASS="MATH">[0, &nbsp;1]</SPAN>. It uses
     *    the method of Clenshaw, i.e., computes and  returns
     *   
     * <P></P>
     * <DIV ALIGN="CENTER" CLASS="mathdisplay">
     * <I>y</I> = [tex2html_wrap_indisplay783] + &sum;<SUB>j=1</SUB><SUP>n</SUP><I>a</I><SUB>j</SUB><I>T</I><SUB>j</SUB><SUP>*</SUP>(<I>x</I>).
     * </DIV><P></P>
     * @param a coefficients of the polynomials
     * 
     *        @param n largest degree of polynomials
     * 
     *        @param x the parameter of the <SPAN CLASS="MATH"><I>T</I><SUB>j</SUB><SUP>*</SUP></SPAN> functions
     * 
     *        @return  the value of a series of Chebyshev polynomials <SPAN CLASS="MATH"><I>T</I><SUB>j</SUB><SUP>*</SUP></SPAN>.
     * 
     */
    public static double evalChebyStar (double a[], int n, double x)  {
       if (x > 1.0 || x < 0.0)
          System.err.println ("Shifted Chebychev polynomial evaluated " +
                              "at x outside [0, 1]");
       final double xx = 2.0*(2.0*x - 1.0);
       double b0 = 0.0;
       double b1 = 0.0;
       double b2 = 0.0;
       for (int j = n; j >= 0; j--) {
          b2 = b1;
          b1 = b0;
          b0 = xx*b1 - b2 + a[j];
       }
       return (b0 - b2)/2.0;
    }
}
