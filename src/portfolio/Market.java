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
    public int portfolioCount;
    private int portfolioMaxCount = 1;
    public Company[] companies = new Company[companyMaxCount];
    public Portfolio[] portfolios = new Portfolio[portfolioMaxCount];
    private double[] beMeBreakPoints;
    private double[] sizeBreakPoints;
    public Portfolio[][] years;
    public double[][] periodPortfolioRetruns;
    private int MarketValueCounts;
    private int BeMeCounts;        
    
    /**
     * default constructor
     * @param MarketValueCounts number of market value break points
     * @param BeMeCounts number of Be/Me break points
     * @param months number of months
     */
    public Market(int MarketValueCounts, int BeMeCounts, int months) {
        this.MarketValueCounts = MarketValueCounts;
        this.BeMeCounts = BeMeCounts;
        portfolioCount = MarketValueCounts*BeMeCounts;
        int yearCount =  months/12;
        this.years = new Portfolio[yearCount][portfolioCount];
        this.periodPortfolioRetruns = new double[portfolioCount][months];
        this.beMeBreakPoints = new double[BeMeCounts-1];
        this.sizeBreakPoints = new double[MarketValueCounts-1];
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
        if(portfolioCount < portfolioMaxCount && portfolioCount < 4) {
            portfolios[portfolioCount] = portfolio;
            portfolioCount++;
        }
        else if (portfolioCount < 4){
            Portfolio[] portfolios2 = new Portfolio[portfolioCount+1];
            for(int i = 0; i < portfolioCount; i++) {
                portfolios2[i] = portfolios[i];
            }
            portfolios2[portfolioCount] = portfolio;
            portfolios = portfolios2;
            portfolioCount++;
        }
        else {
            portfolios[0] = portfolio;
            portfolioCount = 1;
        }
        
    }
    
    public void adjustPortfolios() {
        for(int size = 1; size < MarketValueCounts+1; size++) {
            for(int value = 1; value < BeMeCounts+1; value++) {
                Portfolio portfolio = new Portfolio(size, value);
                addPortfolio(portfolio);
            }
        }
    }
    
    /**
     * @param port portfolio in which certain companies are added
     * @param period which year
     */
    public void constructPortfolios(Portfolio[] port, int period) {
        int year = period+1;
        adjustPortfolios();
        beMeBreakPoints(year);
        sizeBreakPoints(year);
        for(int i = 0; i < companies.length; i++) {
            if(companies[i].beMeRatios[year] <= beMeBreakPoints[0] && companies[i].marketValues[year] <= sizeBreakPoints[0]) portfolios[0].addCompany(companies[i]);
            else if(companies[i].beMeRatios[year] >= beMeBreakPoints[0] && companies[i].marketValues[year] <= sizeBreakPoints[0]) portfolios[1].addCompany(companies[i]);
            else if(companies[i].beMeRatios[year] <= beMeBreakPoints[0] && companies[i].marketValues[year] >= sizeBreakPoints[0]) portfolios[2].addCompany(companies[i]);
            else portfolios[3].addCompany(companies[i]);
        }
        for( int i = 0; i < portfolios.length; i++) {
            portfolios[i].portfolioMarketValue(year);
            portfolios[i].portfolioReturn(year);
            portfolios[i].averagePortfolioReturn();
            portfolios[i].averagePortfolioMarketValue();
        }
        addPortfolios(portfolios, period);
    }
    
    public void addPortfolios(Portfolio[] port, int year) {
        years[year] = port;
    }
    
    /**
     * calculates be/me-break points from market data
     * @param year whitch months break points
     */
    public void beMeBreakPoints(int year) {
        int month = year*12;
        double[] bemeRatios = new double[companies.length];
        for (int i = 0; i < companies.length; i++) {
            bemeRatios[i] = companies[i].beMeRatios[month];
        }
        Arrays.sort(bemeRatios);
        double[] breakPoints = new double[1];
        int amount = bemeRatios.length/2;
        for(int j = 0; j < breakPoints.length; j++) {
            beMeBreakPoints[j] = bemeRatios[amount];
            amount += amount;
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
        double[] breakPoints = new double[1];
        int amount = sizeRatios.length/2;
        for(int j = 0; j < breakPoints.length; j++) {
            sizeBreakPoints[j] = sizeRatios[amount];
            amount += amount;
        }
    }
    
    public void periodPortfolioReturns() { 
        for(int j = 0; j < 4; j++) {
            int beginning = 0; 
            for(int i = 0; i < 3; i++) {
                System.arraycopy(years[i][j].portfolioReturns, 0, periodPortfolioRetruns[j], beginning, years[i][j].portfolioReturns.length);
                beginning += years[i][j].portfolioReturns.length-1;
            }
        }
    }
    
    public String periodPortfolioMV(int number) {
        double average = 0;
        for(int i = 0; i < years.length; i++) {
            average += years[i][number].averagePortfolioMarketValue;
        }
        average = average/years.length;
        return String.format("%.2f", average);
    }
    
    public String periodAverageReturn(int number) {
        periodPortfolioReturns();
        double average = 0;
        for(int i = 0; i < periodPortfolioRetruns[number].length; i++) {
            average += periodPortfolioRetruns[number][i];
        }
        average = average/periodPortfolioRetruns[number].length;
        return String.format("%.5f", average);
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
        //Market one = new Market();
        
        //one.beMeBreakPoints(1);
        //one.sizeBreakPoints(1);
        //System.out.println(Arrays.toString(one.beMeBreakPoints));
        //System.out.println(Arrays.toString(one.sizeBreakPoints));
        //Portfolio port = new Portfolio();
        //one.addPortfolio(port);
        //one.constructPortfolio(port, 1, 1, 1);
        //System.out.println(port);
        //System.out.println(one);
        
        
    }
}
