package portfolio;

import java.util.ArrayList;

/**
 * @author jessekeranen
 * @version 12.9.2020
 * Class that contains information about different kind of groups of portfolios
 */
public class Portfolios {
    
    /** Number of portfolios */
    public int portfolioCount = 0;
    /** Maximum number of the portfolios */
    public int portfolioMaxCount;
    /** Array of portfolios on ceratin year */
    public Portfolio[] portfolios;
    private Portfolio[] factorPortfolios = new Portfolio[6];
    private int factorPortfolioCount;
    private int factorPortfolioMaxCount = 6;
    
    /**
     * Default constructor
     */
    public Portfolios() {
        //
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
     * @param portfolio that is constructed from stocks on market
     */
    public void addFactorPortfolio(Portfolio portfolio) {
        factorPortfolios[factorPortfolioCount] = portfolio;
        factorPortfolioCount++;
        if(factorPortfolioCount == factorPortfolioMaxCount) factorPortfolioCount = 0;
    }
    
    /**
     * Constructs amount of portfolios depending on user input in portfolio constructor 
     * @param mvCount Number of market value breakpoints
     * @param bmCount Number of Be/Me breakpoints
     * @param number Number that indicates are we constructing portfolios (0) or factors (1)
     */
    public void adjustPortfolios(int mvCount, int bmCount, int number) {
        
        portfolios = new Portfolio[(mvCount)*(bmCount)];
        portfolioMaxCount = mvCount*bmCount;
        
        for(int size = 1; size < mvCount+1; size++) {
            for(int value = 1; value < bmCount+1; value++) {
                Portfolio portfolio = new Portfolio(size, value);
                if(number == 0) {
                    addPortfolio(portfolio);
                }
                else if (number == 1) {
                    addFactorPortfolio(portfolio);
                }
            }
        }
    }
    /**
     * Adds companies to the portfolio depending on their market and book values
     * @param period which year
     * @param mvCount Number of market value breakpoints
     * @param bmCount Number of Be/Me breakpoints
     * @param number Number that indicates are we constructing portfolios (0) or factors (1)
     * @param companies Array that contains all the companies
     * @param sizeBreakPoints Array of the market value breakpoints
     * @param beMeBreakPoints Array of the value breakpoints
     * @return Returns an array of portfolios
     */
    public Portfolio[] constructPortfolios(int period, int mvCount, int bmCount, int number, ArrayList<Company> companies, double[] sizeBreakPoints, double[] beMeBreakPoints) {
        int year = period*12+1;
        adjustPortfolios(mvCount, bmCount,  number);     
        
        for(int i = 0; i < companies.size(); i++) {
            int size = size(companies.get(i).getDouble(1, year), sizeBreakPoints);
            int beme = beMe(companies.get(i).getDouble(2, year), beMeBreakPoints);
            if(companies.get(i).getDouble(1, year) != 0 && number == 0) {
                portfolios[size*bmCount+beme].addCompany(companies.get(i));
            }    
            else if(companies.get(i).getDouble(1, year) != 0 && number == 1) {
                factorPortfolios[size*bmCount+beme].addCompany(companies.get(i));
            }
        }
        if(number == 0) {
            for( int i = 0; i < portfolios.length; i++) {
                portfolios[i].portfolioMarketValue(period);
                portfolios[i].portfolioReturn(period);
                portfolios[i].portfolioBeMe(period); 
                portfolios[i].calculateAverages();
                portfolios[i].cumulativeReturn();
            }
            return portfolios;
        }
        for(int j = 0; j < factorPortfolios.length; j++) {
            factorPortfolios[j].portfolioMarketValue(period);
            factorPortfolios[j].portfolioReturn(period);
        }
        return factorPortfolios;   
    }
    
    /**
     * @param size companys market value
     * @param sizeBreakPoints Array of the market value breakpoints
     * @return number that indicates in which size group company belongs
     */
    public int size(double size, double[] sizeBreakPoints) {
        for(int k = 0; k < sizeBreakPoints.length; k++) {
            if(size < sizeBreakPoints[k]) return k;  
        }
        return sizeBreakPoints.length;
    }
    
    /**
     * @param beme companys be/me-ratio
     * @param beMeBreakPoints Array of the value breakpoints
     * @return number that indicates in which beme group company belongs
     */
    public int beMe(double beme, double[] beMeBreakPoints) {
        for(int j = 0; j < beMeBreakPoints.length; j++) {
            if(beme < beMeBreakPoints[j]) return j;
        }
        return beMeBreakPoints.length;
    }
}
