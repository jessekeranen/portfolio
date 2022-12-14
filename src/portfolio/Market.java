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
    public double[][] periodPortfolioReturns;      
    /** An array of factors */
    public Factor[] factors = new Factor[2]; 
    /** An array of factor portfolios for each year */
    public Portfolio[][] factorYears;
    /** Two dimensional array of the factor portfolio retruns for whole period for each factor portfolio */
    public double[][] periodFactorPortfolioRetruns;
    /** An array of weighted average monthly returns of the whole market */
    public double[] marketReturns;
    private double[] marketTotalMarketValues;
    /** Average return */
    public double averageReturn;
    /** An array of the risk free returns */
    public double rf[];
    /** An array of portfolios that hold information from whole perdiod */
    public Portfolio[] wholePeriodPortfolios;
    
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
        this.periodPortfolioReturns = new double[portfolioMaxCount][months];
        this.beMeBreakPoints = new double[BeMeCounts-1];
        this.sizeBreakPoints = new double[MarketValueCounts-1];
        this.factorYears = new Portfolio[months/12][6];
        this.periodFactorPortfolioRetruns = new double[2][months];
        this.wholePeriodPortfolios = new Portfolio[portfolioMaxCount];
        rf();
    }
    
    
    /**
     * Adds a company to the companies array
     * @param company company that is added to the market portfolio
     */
    public void addCompany(Company company) {
        companies.add(company);
    }  
    
    /**
     * Mean of the monthly weighted average returns
     */
    public void averageReturn() {
        for(int i = 0; i < months; i++) {
            averageReturn += marketReturns[i];
        }
        averageReturn = averageReturn/months;
    }
    
    /**
     * Calculates total market value of the market for each month
     */
    public void totalMarketValues() {
        double[] array = new double[months];
        for(int i = 0; i < months; i++) {
            for(int j = 0; j < companies.size(); j++) {
                array[i] += companies.get(j).getDouble(1, i);
            }
        }
        marketTotalMarketValues = array;
    }
    
    /**
     * Calculates monthly weighted returns of the whole market
     */
    public void marketReturns() {
        double[] array = new double[months];
        for(int i = 0; i < months; i++) {
            for(int j = 0; j < companies.size(); j++) {
                array[i] += companies.get(j).getDouble(0, i)*(companies.get(j).getDouble(1, i)/marketTotalMarketValues[i]);
            }
        }
        marketReturns = array;
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
            if(companies.get(i).getDouble(2, month) != 0) {
               ratios.add(companies.get(i).getDouble(2, month));
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
            if(companies.get(i).getDouble(1, month) != 0) {
            sizeRatios.add(companies.get(i).getDouble(1, month));
            }
        }
        return sizeRatios;
    }
    
    /**
     * Constructs portfolios holding information from whole period
     * @param mvCount How many size portfolios there is?
     * @param bmCount How many value portfolios there is?
     */
    public void period(int mvCount, int bmCount) {
        for(int j =0; j < mvCount; j++) {
            for(int i = 0; i < bmCount; i++) {
                int index = j*bmCount + i;
                wholePeriodPortfolios[index] = new Portfolio(j+1,i+1, months/12*12);
                wholePeriodPortfolios[index].returns = periodPortfolios(index, 0, months/12*12, false);
                wholePeriodPortfolios[index].marketValues = periodPortfolios(index, 1, months/12*12, false);
                wholePeriodPortfolios[index].beMeRatios = periodPortfolios(index, 2, months/12*12, false);
                wholePeriodPortfolios[index].calculateAverages();
                wholePeriodPortfolios[index].cumulativeReturn();
            }
        }
    }
    /**
     * Calculates portfolio returns for each portfoio for whole period. 
     * @param port which portfolio
     * @param number What information is added
     * @param length Length of the array returnes
     * @param factor Are we dealing with factor portfolios or regular portfolios
     * @return An array that holds information from whole period 
     */
    private double[] periodPortfolios(int port, int number, int length, boolean factor) { 
        double[] array = new double[length];
        if(factor == false) {
                int beginning = 0; 
                for(int i = 0; i < months/12; i++) {
                    System.arraycopy(years[i][port].getArray(number), 0, array, beginning, years[i][port].getArray(number).length);
                    beginning += years[i][port].getArray(number).length;
                }
        }
        return array;
    }
    
    /**
     * This method is used when factor or portfolio returns are printed
     * @param factor Are we printing factors or portfolios
     */
    public void periodPortfolioReturns(boolean factor) { 
        if(factor == false) {
            for(int j = 0; j < portfolioMaxCount; j++) {
                int beginning = 0; 
                for(int i = 0; i < months/12; i++) {
                    System.arraycopy(years[i][j].getArray(0), 0, periodPortfolioReturns[j], beginning, years[i][j].getArray(0).length);
                    beginning += years[i][j].getArray(0).length;
                }
            }
        }
        else {
            for(int j = 0; j < factors.length; j++) {
                    System.arraycopy(factors[j].getPremiums(), 0, periodFactorPortfolioRetruns[j], 0, factors[j].getPremiums().length);
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
            average += years[i][number].getDouble(1, i);
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
        //periodPortfolios(false);
        double average = 0;
        for(int i = 0; i < periodPortfolioReturns[number].length; i++) {
            average += periodPortfolioReturns[number][i];
        }
        average = average/periodPortfolioReturns[number].length;
        return String.format("%.5f", average);
    }
    
    /**
     * Constructs portfolios in the spirit of Fama & French. Then calculates premium between small and large 
     * portfolios as well as between high value and low value portfolios. Then adds these constructed factors
     * to the factors array
     */
    public void constructFactors() {
        for(int i = 0; i < years.length; i++) {
            constructPortfolios(i,3,2,1);
        }
        for(int i = 0; i < factorYears.length; i++) {
            for(int j = 0; j < factorYears[0].length; j++) {
                factorYears[i][j].portfolioMarketValue(i);
                factorYears[i][j].portfolioReturn(i);
            }
        }
        factors[0] = new Factor("SMB", 2, factorYears);
        
        for(int i = 0; i < years.length; i++) {
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
            s.append(companies.get(i).getName() + " ");
        }
        return s.toString();
    }
    
    private void rf() {
        rf = new double[months];
        for(int i = 0; i < months; i++) {
            rf[i] = 0.01;
        }
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
