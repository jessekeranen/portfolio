package portfolio;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author jessekeranen
 * @version 22.7.2020
 * Class that contains information about all the companies. Contains also information about different breakpoints and two dimensional array that contains all the portfolios for all the years
 */
public class Market {
    
    Portfolios portfolios = new Portfolios();

    /** An array of companies */
    public ArrayList<Company> companies = new ArrayList<Company>();
    private double[] beMeBreakPoints;
    private double[] sizeBreakPoints;
    /** Maximum amount of portfolios for each year */
    public int portfolioMaxCount;
    /** Two dimensional array of the portfolio for each year */
    public Portfolio[][] years;
    /** From how many months does the data consist */
    public int months;
    /** Two dimensional array of the portfolio retruns for whole period for each portfolio */
    public double[][] periodPortfolioRetruns;      
    /** An array of factors */
    public Factor[] factors = new Factor[2]; 
    /** An array of factor portfolios for each year */
    public Portfolio[][] factorYears;
    /** Two dimensional array of the factor portfolio retruns for whole period for each factor portfolio */
    public double[][] periodFactorPortfolioRetruns;
    
    /**
     * Constructor
     * @param MarketValueCounts number of market value break points
     * @param BeMeCounts number of Be/Me break points
     * @param months number of months
     */
    public Market(int MarketValueCounts, int BeMeCounts, int months) {
        this.portfolioMaxCount = (MarketValueCounts*BeMeCounts);
        this.months = months;
        this.years = new Portfolio[months/12][portfolioMaxCount];
        this.periodPortfolioRetruns = new double[portfolioMaxCount][months];
        this.beMeBreakPoints = new double[BeMeCounts-1];
        this.sizeBreakPoints = new double[MarketValueCounts-1];
        this.factorYears = new Portfolio[months/12][6];
        this.periodFactorPortfolioRetruns = new double[2][months];
    }
    
    /**
     * Adds a company to the companies array
     * @param company company that is added to the market portfolio
     */
    public void addCompany(Company company) {
        companies.add(company);
    }     
    
    /**
     * Adds companies to the portfolio depending on their market and book values
     * @param period which year
     * @param mvCount Number of market value breakpoints
     * @param bmCount Number of Be/Me breakpoints
     * @param number Number that indicates are we constructing portfolios (0) or factors (1)
     */
    public void constructPortfolios(int period, int mvCount, int bmCount, int number) {
        beMeBreakPoints = breakPoints(period, bmCount, 0);
        sizeBreakPoints = breakPoints(period, mvCount, 1); 
        
       Portfolio[] portfolios2 = portfolios.constructPortfolios(period, mvCount, bmCount, number, companies, sizeBreakPoints, beMeBreakPoints);
       addPortfolios(portfolios2, period);
    }
    
    /**
     * Adds portfolios from certain year to an array
     * @param port portfolio array that is added to the array
     * @param year which years portfolios  are added to the array
     */
    public void addPortfolios(Portfolio[] port, int year) {
        if(port.equals(portfolios.portfolios)) {
                    years[year] = port.clone();
        }
        else factorYears[year] = port.clone();
    }
    
    /**
     * Calculates breakpoints from market data for certain year. Can be used to calculate size and Be/Me breakpoints
     * @param year whitch years breakpoints
     * @param count number of breakpoints 
     * @param number indicates which variable is handled. Be/Me or size.
     * @return Array of breakpoints
     */
    public double[] breakPoints(int year, int count, int number) {
        double[] array = new double[count-1];
        int month = year*12+1;
        ArrayList<Double> ratios = new ArrayList<Double>();
        if(number == 0) {
            ratios = bemeRatios(month);
        }
        else if(number == 1) {
            ratios = sizes(month);
        }
        Collections.sort(ratios);
        int amount = ratios.size()/count;
        for(int j = 0; j < count-1; j++) {
            array[j] = ratios.get(amount);
            amount += ratios.size()/count;
        }
        return array;
    }
    
    /**
     * Returns an array that contains Be/Me ratios of each company which belong to the portfolio
     * @param month Which month
     * @return Returns an array of the companies Be/Me ratios
     */
    public ArrayList<Double> bemeRatios(int month) {
        ArrayList<Double> ratios = new ArrayList<Double>(1);
        for (int i = 0; i < companies.size(); i++) {
            if(companies.get(i).beMeRatios[month] != 0) {
               ratios.add(companies.get(i).beMeRatios[month]);
            }
        }
        return ratios;
    }  
    
    /**
     * Returns an array that contains market values of each company which belong to the portfolio
     * @param month which month
     * @return Returns an array of the market values of the companies
     */
    public ArrayList<Double> sizes(int month){
        ArrayList<Double> sizeRatios = new ArrayList<Double>();
        for (int i = 0; i < companies.size(); i++) {
            if(companies.get(i).marketValues[month] != 0) {
            sizeRatios.add(companies.get(i).marketValues[month]);
            }
        }
        return sizeRatios;
    }
    
    /**
     * Calculates portfolio returns for each portfoio for whole period. 
     * @param factor Are we dealing with factor portfolios or regular portfolios
     */
    public void periodPortfolioReturns(boolean factor) { 
        if(factor == false) {
            for(int j = 0; j < portfolioMaxCount; j++) {
                int beginning = 0; 
                for(int i = 0; i < months/12; i++) {
                    System.arraycopy(years[i][j].portfolioReturns, 0, periodPortfolioRetruns[j], beginning, years[i][j].portfolioReturns.length);
                    beginning += years[i][j].portfolioReturns.length;
                }
            }
        }
        else {
            for(int j = 0; j < factors.length; j++) {
                    System.arraycopy(factors[j].premiums, 0, periodFactorPortfolioRetruns[j], 0, factors[j].premiums.length);
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
        periodPortfolioReturns(false);
        double average = 0;
        for(int i = 0; i < periodPortfolioRetruns[number].length; i++) {
            average += periodPortfolioRetruns[number][i];
        }
        average = average/periodPortfolioRetruns[number].length;
        return String.format("%.5f", average);
    }
    
    /**
     * Constructs portfolios in the spirit of Fama & French. Then calculates premium between small and large 
     * portfolios as well as between high value and low value portfolios. Then adds these constructed factors
     * to the factors array
     */
    public void constructFactors() {
        for(int i = 0; i < Company.years; i++) {
            constructPortfolios(i,3,2,1);
        }
        for(int i = 0; i < factorYears.length; i++) {
            for(int j = 0; j < factorYears[0].length; j++) {
                factorYears[i][j].portfolioMarketValue(i);
                factorYears[i][j].portfolioReturn(i);
            }
        }
        factors[0] = new Factor("SMB", 2, factorYears);
        
        for(int i = 0; i < Company.years; i++) {
            constructPortfolios(i,3,2,1);
        }
        for(int i = 0; i < factorYears.length; i++) {
            for(int j = 0; j < factorYears[0].length; j++) {
                factorYears[i][j].portfolioMarketValue(i);
                factorYears[i][j].portfolioReturn(i);
            }
        }
        factors[1] = new Factor("HML", 1, factorYears); 
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < companies.size(); i++) {
            s.append(companies.get(i).name + " ");
        }
        return s.toString();
    }
    
    /**
     * @param args not used
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
