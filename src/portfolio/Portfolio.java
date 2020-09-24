package portfolio;

import java.util.ArrayList;

/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 * @example
 * <pre name="testJAVA">
 * #import java.util.Arrays;
 * #TOLERANCE=0.01
 * #import  java.io.*;
 * #import  org.apache.poi.xssf.usermodel.XSSFSheet;
 * #import  org.apache.poi.xssf.usermodel.XSSFWorkbook;
 * #import  org.apache.poi.xssf.usermodel.XSSFRow;
 * #import portfolio.Company;
 * #import portfolio.Portfolio;
 * 
 * private Company Nokia; 
 * private Company Nordea;
 * private Company Telia;
 * 
 * private Portfolio portfolio;
 * public double[] portfolioMarketValue;
 * 
 * public void addData(String filename, String name, double multiplier, XSSFWorkbook workbook, XSSFSheet sheet, XSSFSheet sheet2, XSSFSheet sheet3, XSSFSheet sheet4) {
 * 
 * addData(name, sheet, multiplier, false, 1);
 * addData(name, sheet2, multiplier, false, 100);
 * addData(name, sheet3, multiplier, true, 1);
 * addData(name, sheet4, multiplier, false, 0.01);
 *  
 *  try{
 *  FileOutputStream fileOut = new FileOutputStream(filename);
 *  workbook.write(fileOut);
 *  fileOut.close(); 
 *  }catch( Exception e) {
 *       System.err.println(e.getMessage());
 *  }
 * }
 * 
 * public void addData(String name, XSSFSheet sheet, double multiplier, boolean bookvalue, double coefficient) {
 * XSSFRow row = sheet.createRow(0);
 * row.createCell(0).setCellValue(name);
 * for(int i = 1; i < 13; i++){
 *      row = sheet.createRow(i);
 *      if(bookvalue == false){
 *          row.createCell(0).setCellValue((i*11.5*multiplier+i-(i*i))*coefficient);
 *      }
 *      else{
 *          row.createCell(0).setCellValue(i*400.05+1000/multiplier-i*i);
 *      }
 *  }
 * }
 * 
 *  public void exampleCompanies(){
 * String filename = "/Users/jessekeranen/CompanyTest.xlsx" ;
 * XSSFWorkbook workbook = new XSSFWorkbook();
 * XSSFSheet sheet = workbook.createSheet("Prices");
 * XSSFSheet sheet2 = workbook.createSheet("MarketValues");
 * XSSFSheet sheet3 = workbook.createSheet("BookValues");
 * XSSFSheet sheet4 = workbook.createSheet("Dividends"); 
 *  
 *      addData(filename, "Nokia", 1, workbook, sheet, sheet2, sheet3, sheet4);
 *      Nokia = new Company(sheet, sheet3, sheet2, sheet4, 0);
 *      addData(filename, "Nordea", 2, workbook, sheet, sheet2, sheet3, sheet4);
 *      Nordea = new Company(sheet, sheet3, sheet2, sheet4, 0);
 *      addData(filename, "Telia", 1.5, workbook, sheet, sheet2, sheet3, sheet4);
 *      Telia = new Company(sheet, sheet3, sheet2, sheet4, 0);
 *      
 *      try{
 *          workbook.close(); 
 *      }catch( Exception e) {
 *          System.err.println(e.getMessage());
 *      }
 *      portfolio = new Portfolio(1,1);
 *      
 *      portfolio.addCompany(Nokia);
 *      portfolio.addCompany(Nordea);
 *      portfolio.addCompany(Telia);
 *      
 *      portfolio.portfolioMarketValue(0);
 *      portfolio.portfolioReturn(0);
 * }
 */
public class Portfolio {
    
    /** Name of the portfolio. Indicates the size and value of the companies in it */
    public String name;
    /** Array that includes all the companies that portfolio consist of */
    public ArrayList<Company> companies = new ArrayList<Company>();
    /** Weighted monthly average portfolio return */
    public double[] portfolioReturns = new double[12];
    /** An array of total market values of the portfolio for each month */
    public double[] portfolioMarketValue = new double[12];
    /** An array of average portfolio Be/Me ratios */
    public double[] portfolioBeMe = new double[12];
    /** Mean of the portfolio returns */
    public double averagePortfolioReturn;
    /** String value of the mean of the portfolio returns */
    public String averagePortfolioReturnString;
    /** Mean of the portfolios market values */
    public double averagePortfolioMarketValue;
    /** String value of the mean of the portfolio market values */
    public String averagePortfolioMarketValueString;
    /** Mean of the portfolios average Be/Me values */
    public double averagePortfolioBeMe;
    /** String value of the Mean of the portfolio Be/Me values */
    public String averagePortfolioBeMeString;
    
    /**
     * default constuctor
     * @param size indicates the size of the companies in the portfolio
     * @param value indicates the value of the companies in the portfolio
     */
    public Portfolio(int size, int value) {
        this.name = "portfolio " + size + value;
    }
   
    
    /**
     * @param company company that is added to the portfolio
     * @example
     * <pre name="test">
     * exampleCompanies();
     * int count = portfolio.companies.size();
     * count === 3;
     * </pre>
     */
    public void addCompany(Company company) {
        companies.add(company);
    }
    
    /**
     * calculates monthly weighted returns of the portfolio
     * @param period Which year
     * @example
     * <pre name="test">
     * #THROWS Exception
     * #import java.text.DecimalFormat;
     * #import java.text.DecimalFormatSymbols;
     * #import java.util.Locale;
     * 
     * exampleCompanies();
     * double[] array = portfolio.portfolioReturns;
     * DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
     * DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
     * for (int i = 0; i < array.length; i++) {
     *      array[i] = Double.valueOf(decimalFormat.format(array[i]));
     * }
     * Arrays.toString(array) === "[0.0, 0.88, 0.41, 0.25, 0.16, 0.11, 0.07, 0.04, 0.02, -0.0, -0.02, -0.03]";
     * </pre> 
     */
    public void portfolioReturn(int period) {
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < companies.size(); j++) {
                int month = period*12+i;
                portfolioReturns[i] += companies.get(j).returns[month]*(companies.get(j).marketValues[month]/portfolioMarketValue[i]); 
            }
        }
    }
     
    /**
     * Calculates the average portfolio return
     */
    public void calculateAverages() {
        this.averagePortfolioReturn =  average(portfolioReturns);
        this.averagePortfolioReturnString = averageString(averagePortfolioReturn);
        this.averagePortfolioMarketValue = average(portfolioMarketValue);
        this.averagePortfolioMarketValueString = averageString(averagePortfolioMarketValue);
        this.averagePortfolioBeMe = average(portfolioBeMe);
        this.averagePortfolioBeMeString = averageString(averagePortfolioBeMe);
    }

    /**
     * Calculates the mean of the given array return
     * @param array An array holding all the information
     * @return average value
     * @example
     * <pre name="test">
     * #TOLERANCE=0.001
     * exampleCompanies();
     * portfolio.average(Nokia.returns) ~~~ 0.012;
     * double[] array = new double[]{1.5, 0, -1.5, -1.6};
     * double[] array2 = new double[]{};
     * double[] array3 = new double[]{1.6, 5.9, 99, 6.45, 1.88};
     * portfolio.average(array) ~~~ -0.4;
     * portfolio.average(array2) ~~~ 0.0;
     * portfolio.average(array3) ~~~ 22.966;
     * </pre>
     */
    public double average(double[] array) {
        if(array.length != 0) {
            double average = 0;
            for(int i = 0; i < array.length; i++) {
                average += array[i];
            }
            average =  average/array.length;
            return average;
        }
        return 0;
    }
    
    /**
     * Changes the average portfolio return from double to string
     * @param average double value of the mean
     * @return String value of the average
     */
    public String averageString(double average) {
        String string = String.format("%.5f", average);
        return String.valueOf(string);
    }
    
    /**
     * calculates total market value of the portfolio for each month
     * @param period Which year
     * @example
     * <pre name="test">
     * #THROWS Exception
     * #import java.text.DecimalFormat;
     * #import java.text.DecimalFormatSymbols;
     * #import java.util.Locale;
     * 
     * exampleCompanies();
     * double[] array = portfolio.portfolioMarketValue;
     * DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
     * DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
     * for (int i = 0; i < array.length; i++) {
     *      array[i] = Double.valueOf(decimalFormat.format(array[i]));
     * }
     * 
     * Arrays.toString(array) === "[5175.0, 9750.0, 13725.0, 17100.0, 19875.0, 22050.0, 23625.0, 24600.0, 24975.0, 24750.0, 23925.0, 22500.0]";
     * </pre> 
     */
    public void portfolioMarketValue(int period) {
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < companies.size(); j++) {
                int month = period*12+i;
            portfolioMarketValue[i] += companies.get(j).marketValues[month];
            }
        }
    }
    
    /**
     * Calculates average Be/Me values for portfolio for each month
     * @param period Which year
     * @example
     * <pre name="test">
     * #TOLERANCE=0.01
     * #THROWS Exception
     * #import java.text.DecimalFormat;
     * #import java.text.DecimalFormatSymbols;
     * #import java.util.Locale;
     * 
     * exampleCompanies();
     * portfolio.portfolioBeMe(0); 
     * double[] array = portfolio.portfolioBeMe;
     * 
     * DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
     * DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
     * for (int i = 0; i < array.length; i++) {
     *      array[i] = Double.valueOf(decimalFormat.format(array[i]));
     * }
     * 
     * Arrays.toString(array) === "[0.74, 0.53, 0.48, 0.47, 0.48, 0.51, 0.56, 0.63, 0.75, 0.94, 1.38, 3.5]";
     * </pre>
     */
    public void portfolioBeMe(int period) {
        for(int i = 0; i < 12; i++) {
            for(int j = 0; j < companies.size(); j++) {
                int month = period*12+i;
            portfolioBeMe[i] += companies.get(j).beMeRatios[month];
            }
            portfolioBeMe[i] = portfolioBeMe[i]/companies.size();
        }
    }
    
    /**
     * @return returns the lowest mothly return of the portfolio on certain year
     * @example
     * <pre name="test">
     * #TOLERANCE=0.01;
     * exampleCompanies();
     * portfolio.getMinValue() ~~~ -0.03;
     * </pre>
     */
    public double getMinValue() {
        double min = Double.MAX_VALUE;
        for(int i = 0; i < portfolioReturns.length; i++) {
            if(portfolioReturns[i] < min) min = portfolioReturns[i];
        }
        return min;
    }
    
    /**
     * @return returns the highest mothly return of the portfolio on certain year
     * @example
     * <pre name="test">
     * #TOLERANCE=0.01;
     * exampleCompanies();
     * portfolio.getMaxValue() ~~~ 0.88;
     * </pre>
     */
    public double getMaxValue() {
        double max = -Double.MAX_VALUE;
        for(int i = 0; i < portfolioReturns.length; i++) {
            if(portfolioReturns[i] > max) max = portfolioReturns[i];
        }
        return max;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < companies.size(); i++) {
            s.append(companies.get(i).name);
        }
        return s.toString();
    }
    
    /**
     * @param args not used
     */
    public static void main(String[] args) {
        //
    }
}
