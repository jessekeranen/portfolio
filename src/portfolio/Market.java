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
        double[] hinnat = {10,15,20,10};
        double[] arvot = {100, 200, 1000, 300};
        double[] book = {10, 200,1000, 30};
        double[] hinnat1 = {20, 30, 40, 20};
        double[] arvot1 = {200, 400, 2000, 600};
        double[] book1 = {20, 400,2000, 60};
        double[] hinnat2 = {30,45,60,30};
        double[] arvot2 = {300, 600, 3000, 900};
        double[] book2 = {30, 600,3000, 90};
        double[] hinnat3 = {40,60,80,40};
        double[] arvot3 = {400, 800, 4000, 1200};
        double[] book3 = {40, 800,4000, 120};
        double[] hinnat4 = {50,75,100,50};
        double[] arvot4 = {500, 1000, 5000, 1500};
        double[] book4 = {50, 1000,5000, 150};
        Company nokia = new Company("Nokia", hinnat, arvot, book);
        Company elisa = new Company("Elisa", hinnat, arvot, book);
        Company nokia1 = new Company("Nokia1", hinnat1, arvot1, book1);
        Company elisa2 = new Company("Elisa1", hinnat1, arvot1, book1);
        Company nokia3 = new Company("Nokia2", hinnat2, arvot2, book2);
        Company elisa4 = new Company("Elisa2", hinnat2, arvot2, book2);
        Company nokia5 = new Company("Nokia3", hinnat3, arvot3, book3);
        Company elisa6 = new Company("Elisa3", hinnat3, arvot3, book3);
        Company nokia7 = new Company("Nokia4", hinnat4, arvot4, book4);
        Company elisa8 = new Company("Elisa4", hinnat4, arvot4, book4);
        Market one = new Market();
        one.addCompany(nokia);
        one.addCompany(elisa);
        one.addCompany(nokia1);
        one.addCompany(elisa2);
        one.addCompany(nokia3);
        one.addCompany(elisa4);
        one.addCompany(nokia5);
        one.addCompany(elisa6);
        one.addCompany(nokia7);
        one.addCompany(elisa8);
        one.beMeBreakPoints(1);
        one.sizeBreakPoints(1);
        System.out.println(Arrays.toString(one.beMeBreakPoints));
        System.out.println(Arrays.toString(one.sizeBreakPoints));
        Portfolio port = new Portfolio();
        one.addPortfolio(port);
        one.constructPortfolio(port, 1, 1, 1);
        System.out.println(port);
        System.out.println(one);
    }
}
