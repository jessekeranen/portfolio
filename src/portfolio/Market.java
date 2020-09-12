package portfolio;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class Market {
    
    Portfolios portfolios = new Portfolios();

    /** Array of companies */
    public ArrayList<Company> companies = new ArrayList<Company>();
    private double[] beMeBreakPoints;
    private double[] sizeBreakPoints;
    public int portfolioMaxCount;
    /** Two dimensional array of the portfolio for each year */
    public Portfolio[][] years;
    /** From how many months does the data consist */
    public int months;
    /** Two dimensional array of the portfolio retruns for whole period for each portfolio */
    public double[][] periodPortfolioRetruns;      
    private Portfolio[] factorPortfolios = new Portfolio[6];
    private int factorPortfolioMaxCount = 6;
    /** Array of factors */
    public Factor[] factors = new Factor[2]; 
    
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
    }
    
    /**
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
        years[year]= port.clone();
    }
    
    /**
     * calculates breakpoints from market data
     * @param year whitch months break points
     * @param count number of breakpoints 
     * @param number indicates which variable is handled
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
     */
    public void periodPortfolioReturns() { 
        for(int j = 0; j < portfolioMaxCount; j++) {
            int beginning = 0; 
            for(int i = 0; i < months/12; i++) {
                System.arraycopy(years[i][j].portfolioReturns, 0, periodPortfolioRetruns[j], beginning, years[i][j].portfolioReturns.length);
                beginning += years[i][j].portfolioReturns.length;
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
    
    /**
     * Construct factors
     */
    public void constructFactors() {
        for(int i = 0; i < Company.years; i++) {
            constructPortfolios(i,3,2,1);
        }
        for(int j = 0; j < factorPortfolios.length; j++) {
            factorPortfolios[j].portfolioMarketValue();
            factorPortfolios[j].portfolioReturn();
        }
        factors[0] = new Factor("SMB", 2, factorPortfolios);
        
        for(int i = 0; i < Company.years; i++) {
            constructPortfolios(i,2,1,1);
        }
        for(int j = 0; j < factorPortfolios.length; j++) {
            factorPortfolios[j].portfolioReturn();
        }
        factors[1] = new Factor("HML", 1, factorPortfolios);
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
