package portfolio;

import java.util.Arrays;

/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class Market {
    
    public int companyCount;
    private int companyMaxCount = 1;
    public Company[] companies = new Company[companyMaxCount];
    private int portfolioCount;
    private int portfolioMaxCount = 1;
    private Portfolio[] portfolios = new Portfolio[portfolioMaxCount];
    private double[] beMeBreakPoints = new double[3];
    private double[] sizeBreakPoints = new double[3];
    
    /**
     * default constructor
     */
    public Market() {
        //
    }
    
    /**
     * @param company company that is added to the market portfolio
     */
    public void addCompany(Company company) {
        if(companyCount < companyMaxCount) {
            companies[companyCount] = company;
            companyCount++;
            }
            else {
                Company[] companies2 = new Company[companyCount+1];
                for(int i = 0; i < companies.length; i++) {
                        companies2[i] = companies[i];
                }
                companies2[companyCount] = company;
                companies = companies2;
                companyCount++;
            }
        
    }
    
    /**
     * @param portfolio that is constructed from stocks on market
     */
    public void addPortfolio(Portfolio portfolio) {
        if(portfolioCount < portfolioMaxCount) {
            portfolios[portfolioCount] = portfolio;
            portfolioCount++;
        }
        else {
            Portfolio[] portfolios2 = new Portfolio[portfolioCount+1];
            for(int i = 0; i < portfolios.length; i++) {
                portfolios2[i] = portfolios[i];
            }
            portfolios2[portfolioCount] = portfolio;
            portfolios = portfolios2;
            portfolioCount++;
        }
        
    }
    
    /**
     * @param port portfolio in which certain companies are added
     * @param bemebreakpoint break point for be/me-ratio
     * @param sizebreakpoint break point for market value
     * @param year which year
     */
    public void constructPortfolio(Portfolio port, int bemebreakpoint, int sizebreakpoint, int year) {
        beMeBreakPoints(year);
        sizeBreakPoints(year);
        for(int i = 0; i < companies.length; i++) {
            if((companies[i].beMeRatios[year] <= beMeBreakPoints[bemebreakpoint]) &&(companies[i].marketValues[year] <= sizeBreakPoints[sizebreakpoint])) port.addCompany(companies[i]);
        }
    }
    
    
    /**
     * calculates be/me-break points from market data
     * @param month whitch months break points
     */
    public void beMeBreakPoints(int month) {
        double[] bemeRatios = new double[companies.length];
        for (int i = 0; i < companies.length; i++) {
            bemeRatios[i] = companies[i].beMeRatios[month];
        }
        Arrays.sort(bemeRatios);
        double[] breakPoints = new double[3];
        int amount = bemeRatios.length/4;
        for(int j = 0; j < breakPoints.length; j++) {
            beMeBreakPoints[j] = bemeRatios[amount];
            amount += bemeRatios.length/4;
        }
    }
    
    /**
     * calculates be/me-break points from market data
     * @param month whitch months break points
     */
    public void sizeBreakPoints(int month) {
        double[] sizeRatios = new double[companies.length];
        for (int i = 0; i < companies.length; i++) {
            sizeRatios[i] = companies[i].marketValues[month];
        }
        Arrays.sort(sizeRatios);
        double[] breakPoints = new double[3];
        int amount = sizeRatios.length/4;
        for(int j = 0; j < breakPoints.length; j++) {
            sizeBreakPoints[j] = sizeRatios[amount];
            amount += sizeRatios.length/4;
        }
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < companies.length; i++) {
            s.append(companies[i].name + " ");
        }
        return s.toString();
    }
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Market one = new Market();
        
        //one.beMeBreakPoints(1);
        //one.sizeBreakPoints(1);
        //System.out.println(Arrays.toString(one.beMeBreakPoints));
        //System.out.println(Arrays.toString(one.sizeBreakPoints));
        //Portfolio port = new Portfolio();
        //one.addPortfolio(port);
        //one.constructPortfolio(port, 1, 1, 1);
        //System.out.println(port);
        System.out.println(one);
        
        
    }
}
