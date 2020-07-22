package portfolio;

import java.util.Arrays;

/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class Market {
    
    private int companyCount;
    private int companyMaxCount = 1;
    private Company[] companies = new Company[companyMaxCount];
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
        portfolios[portfolioCount] = portfolio;
        portfolioCount++;
    }
    
    /**
     * @param port portfolio in which certain companies are added
     * @param bemebreakpoint break point for be/me-ratio
     * @param sizebreakpoint break point for market value
     * @param year which year
     */
    public void constructPortfolio(Portfolio port, int bemebreakpoint, int sizebreakpoint, int year) {
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
        Company yritys = new Company("/Users/jessekeranen/projects/Työkirja7.xlsx",0);
        Company yritys1 = new Company("/Users/jessekeranen/projects/Työkirja7.xlsx",1);
        Company yritys2 = new Company("/Users/jessekeranen/projects/Työkirja7.xlsx",2);
        Company yritys3 = new Company("/Users/jessekeranen/projects/Työkirja7.xlsx",3);
        one.addCompany(yritys);
        one.addCompany(yritys1);
        one.addCompany(yritys2);
        one.addCompany(yritys3);
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
