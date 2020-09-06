package portfolio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class Market {
    
    /**
     * Number of companies */
    public int companyCount;
    private int companyMaxCount = 1;
    /**
    *  Number of portfolios */
    public int portfolioCount = 0;
    public int portfolioMaxCount;
    /**
     * Array o companies */
    public Company[] companies = new Company[companyMaxCount];
    /**
     * Array of portfolios */
    public Portfolio[] portfolios;
    private double[] beMeBreakPoints;
    private double[] sizeBreakPoints;
    /**
     * Two dimensional array of the portfolio for each year */
    public Portfolio[][] years;
    public int months;
    /**
     * Two dimensional array of the portfolio retruns for whole period for each portfolio */
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
        this.portfolioMaxCount = (MarketValueCounts*BeMeCounts);
        this.portfolios = new Portfolio[portfolioMaxCount];
        int yearCount =  months/12;
        this.months = months;
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
        portfolios[portfolioCount] = portfolio;
        portfolioCount++;
        if(portfolioCount == portfolioMaxCount) portfolioCount = 0;
    }
    
    /**
     * Constructs amount of portfolios depending on user input in portfolio constructor 
     */
    public void adjustPortfolios() {
        for(int size = 1; size < MarketValueCounts+1; size++) {
            for(int value = 1; value < BeMeCounts+1; value++) {
                Portfolio portfolio = new Portfolio(size, value);
                addPortfolio(portfolio);
            }
        }
    }
    
    /**
     * Adds companies to the portfolio depending on their market and book values
     * @param period which year
     */
    public void constructPortfolios(int period) {
        int year = period*12+1;
        adjustPortfolios();
        beMeBreakPoints(period);
        sizeBreakPoints(period);      
        
        for(int i = 0; i < companies.length; i++) {
            int size = size(companies[i].marketValues[year]);
            int beme = beMe(companies[i].beMeRatios[year]);
            if(companies[i].marketValues[year] != 0) {
                portfolios[size*MarketValueCounts+beme].addCompany(companies[i]);
            }    
        }
        for( int i = 0; i < portfolios.length; i++) {
            portfolios[i].portfolioMarketValue();
            portfolios[i].portfolioReturn();
            portfolios[i].averagePortfolioReturn();
            portfolios[i].averagePortfolioMarketValue();
        }
        addPortfolios(portfolios, period);
    }
    
    /**
     * @param size companys market value
     * @return number that indicates in which size group company belongs
     */
    public int size(double size) {
        for(int k = 0; k < sizeBreakPoints.length; k++) {
            if(size < sizeBreakPoints[k]) return k;  
        }
        return sizeBreakPoints.length;
    }
    
    /**
     * @param beme companys be/me-ratio
     * @return number that indicates in which beme group company belongs
     */
    public int beMe(double beme) {
        for(int j = 0; j < beMeBreakPoints.length; j++) {
            if(beme < beMeBreakPoints[j]) return j;
        }
        return beMeBreakPoints.length;
    }
    
    /**
     * Adds portfolios from certain year to an array
     * @param port portfolio array that is added to the array
     * @param year which years portfolios  are added to the array
     */
    public void addPortfolios(Portfolio[] port, int year) {
        //years[year] = port;
        years[year]= port.clone();
    }
    
    /**
     * calculates be/me-break points from market data
     * @param year whitch months break points
     */
    public void beMeBreakPoints(int year) {
        int month = year*12+1;
        ArrayList<Double> bemeRatios = new ArrayList<Double>(1);
        for (int i = 0; i < companies.length; i++) {
            if(companies[i].beMeRatios[month] != 0) {
               bemeRatios.add(companies[i].beMeRatios[month]);
            }
        }
        Collections.sort(bemeRatios);
        int amount = bemeRatios.size()/BeMeCounts;
        for(int j = 0; j < BeMeCounts-1; j++) {
            beMeBreakPoints[j] = bemeRatios.get(amount);
            amount += bemeRatios.size()/BeMeCounts;
        }
    }
    
    /**
     * calculates be/me-break points from market data
     * @param year whitch months break points
     */
    public void sizeBreakPoints(int year) {
        int month = year*12+1;
        ArrayList<Double> sizeRatios = new ArrayList<Double>();
        for (int i = 0; i < companies.length; i++) {
            if(companies[i].marketValues[month] != 0) {
            sizeRatios.add(companies[i].marketValues[month]);
            }
        }
        Collections.sort(sizeRatios);
        int amount = sizeRatios.size()/MarketValueCounts;
        for(int j = 0; j < MarketValueCounts-1; j++) {
            sizeBreakPoints[j] = sizeRatios.get(amount);
            amount += sizeRatios.size()/MarketValueCounts;
        }
    }
    
    /**
     * Calculates portfolio returns for each portfoio for whole period. 
     */
    public void periodPortfolioReturns() { 
        for(int j = 0; j < 4; j++) {
            int beginning = 0; 
            for(int i = 0; i < 3; i++) {
                System.arraycopy(years[i][j].portfolioReturns, 0, periodPortfolioRetruns[j], beginning, years[i][j].portfolioReturns.length);
                beginning += years[i][j].portfolioReturns.length-1;
            }
        }
    }
    
    /**
     * Calculates portfolios average market value for whole period
     * @param number which portfiolios average market value is calculated
     * @return average market value
     */
    public String periodPortfolioMV(int number) {
        double average = 0;
        for(int i = 0; i < years.length; i++) {
            average += years[i][number].averagePortfolioMarketValue;
        }
        average = average/years.length;
        return String.format("%.2f", average);
    }
    
    /**
     * Calculates portfolios average book value for whole period
     * @param number which portfiolios average book value is calculated
     * @return average book value
     */
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
