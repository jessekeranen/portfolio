package portfolio;

/**
 * @author jessekeranen
 * @version 12.9.2020
 *
 */
public class Factor {
    
    private String name;
    public double[] premiums;
    private Portfolio[] portfolios; 
    
    /**
     * Constructor for factors
     * @param name Factors name
     * @param bemeBreakPoints Number of Be/Me breakpoints
     * @param portfolios Portfolios from which premiums are calculated
     */
    public Factor(String name, int bemeBreakPoints, Portfolio[] portfolios) {
        this.name = name;
        this.portfolios = portfolios;
        this.premiums = new double[portfolios[0].portfolioReturns.length];
        premiums(bemeBreakPoints); 
    }
    
    /**
     * @param bemeBreakPoints Number of Be/Me breakpoints
     */
    public void premiums(int bemeBreakPoints){
        for(int i = 0; i < portfolios[0].portfolioReturns.length; i++) {
            double sumSmall = 0;
            double sumLarge = 0;
            for(int j = 0; j < bemeBreakPoints; j++) {
                sumSmall += portfolios[j].portfolioReturns[i];
            }
            for(int k = bemeBreakPoints; k < portfolios.length; k++) {
                sumLarge += portfolios[k].portfolioReturns[i];
            }
            premiums[i] = ((1/(Double.valueOf(bemeBreakPoints)+1))*sumSmall)/((1/(Double.valueOf(bemeBreakPoints)+1))*sumLarge);
        }
    }
    
}
