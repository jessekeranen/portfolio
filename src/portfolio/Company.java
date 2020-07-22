package portfolio;

/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class Company {
    
    protected String name;
    private double[] prices;
    protected double[] marketValues;
    private double[] bookValues;
    protected double[] returns;
    protected double[] beMeRatios;
    
    /**
     * @param name name of the company
     * @param prices monthly prices of the company
     * @param marketValues monthly market value of the company
     * @param bookValues monthly book value of the company
     */
    public Company(String name, double[] prices, double [] marketValues, double [] bookValues) {
        this.name = name;
        this.prices = prices;
        this.marketValues = marketValues;
        this.bookValues = bookValues;
        this.returns = returns();
        this.beMeRatios = beMeRatio();
    }
    
    /**
     * calculates monthly return from prices of the stock
     * @return array of the monthly returns
     */
    public double[] returns() {
        double[] returns1 = new double[prices.length-1];
        for(int i = 0; i < prices.length-1; i++) {
            returns1[i] = (prices[i+1]-prices[i])/prices[i]; 
        }
        return returns1;
    }
    
    /**
     * calculates monthly be/me-ratios of the company
     * @return monthly be/me-ratios of the company
     */
    public double[] beMeRatio() {
        double[] bemeRatios = new double[marketValues.length];
        for (int i = 0; i < marketValues.length; i++) {
            bemeRatios[i] =  bookValues[i]/marketValues[i];
        }
        return bemeRatios;
    }
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        double[] hinnat = {10,15,20,10};
        double[] arvot = {100, 200, 1000, 300};
        double[] book = {10, 200,1000, 30};
        Company nokia = new Company("Nokia", hinnat, arvot, book);
        System.out.println(nokia);
    }
}
