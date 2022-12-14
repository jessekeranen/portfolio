package portfolio;

/**
 * @author jessekeranen
 * @version 12.9.2020
 *
 */
public class Factor {
    
    
    private String name;
    /** An array of premiums. For example in the case of SMB facctor this is returns of the small market value portfolios substracted with returns of the large market value portfolios */
    private double[] premiums;
    private Portfolio[][] portfolios; 
    
    /**
     * Constructor for factors
     * @param name Factors name
     * @param bemeBreakPoints Number of Be/Me breakpoints
     * @param portfolios Portfolios from which premiums are calculated
     */
    public Factor(String name, int bemeBreakPoints, Portfolio[][] portfolios) {
        this.name = name;
        this.portfolios = portfolios;
        this.premiums = new double[Company.rows-1];
        premiums(bemeBreakPoints); 
    }
    
    /**
     * Calculates the premiums small and large market value portfolios and high and low value portfolios
     * @param bemeBreakPoints Number of Be/Me breakpoints
     */
    public void premiums(int bemeBreakPoints){
        for(int i = 0; i < Company.rows/12; i ++) {
            for(int j = 0; j < portfolios[0][0].getArray(0).length; j++) {
                double sumSmall = 0;
                double sumLarge = 0;
                for(int k = 0; k < bemeBreakPoints+1; k++) {
                    sumSmall += portfolios[i][k].getDouble(0, j);
                }
                for(int k = bemeBreakPoints+1; k < portfolios[0].length; k++) {
                    sumLarge += portfolios[i][k].getDouble(0, j);
                }
                premiums[i*12+j] = ((1/(Double.valueOf(bemeBreakPoints)+1))*sumSmall)/((1/(Double.valueOf(bemeBreakPoints)+1))*sumLarge);
            }
        }
    }
    
    /**
     * Retruns premium array of the factor
     * @return Premiums array
     */
    public double[] getPremiums() {
        return premiums;
    }
    
    /**
     * Returns name of the factor
     * @return Name of the portfolio
     */
    public String getName() {
        return name;
    }
}
