package portfolio;

import java.util.Arrays;

/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class Portfolio {
    
    private int count = 0;
    private int maxCount = 1;
    private Company[] companies = new Company[maxCount];
    private double[] portfolioReturns = new double[8];
    private double[] portfolioMarketValue = new double[8];
    private int period = 4;
    
    /**
     * default constuctor
     */
    public Portfolio() {
        //
    }
    
    /**
     * @param company company that is added to the portfolio
     */
    public void addCompany(Company company) {
        if(count < maxCount) {
        companies[count] = company;
        count++;
        }
        else {
            Company[] companies2 = new Company[count+1];
            for(int i = 0; i < companies.length; i++) {
                    companies2[i] = companies[i];
            }
            companies2[count] = company;
            companies = companies2;
        }
    }
    
    /**
     * calculates monthly weighted returns of the portfolio
     */
    public void portfolioReturn() {
        for(int i = 0; i < period-1; i++) {
            for(int j = 0; j < companies.length; j++) {
                portfolioReturns[i] += companies[j].returns[i]*(companies[j].marketValues[i]/portfolioMarketValue[i]); 
            }
        }
    }
    
    /**
     * calculates total market value of the portfolio
     */
    public void portfolioMarketValue() {
        for(int i = 0; i < period; i++) {
            for(int j = 0; j < companies.length; j++) {
            portfolioMarketValue[i] += companies[j].marketValues[i];
            }
        }
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < companies.length; i++) {
            s.append(companies[i].name);
        }
        return s.toString();
    }
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        double[] hinnat = {10,15,20,10};
        double[] arvot = {100, 200, 1000, 300};
        double[] book = {10, 200,1000, 30};
        Company nokia = new Company("Nokia", hinnat, arvot, book);
        Company elisa = new Company("Elisa", hinnat, arvot, book);
        Portfolio one = new Portfolio();
        one.addCompany(nokia);
        one.addCompany(elisa);
        one.portfolioMarketValue();
        one.portfolioReturn();
        System.out.println(Arrays.toString(one.portfolioMarketValue));
        System.out.println(Arrays.toString(one.portfolioReturns));
    }
}
