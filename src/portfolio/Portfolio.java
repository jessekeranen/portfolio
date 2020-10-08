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
 * double[] array = new double[12];
 * for(int i = 0; i < 12; i++) {
 *      array[i] = 0.01;
 * }
 *  
 *      addData(filename, "Nokia", 1, workbook, sheet, sheet2, sheet3, sheet4);
 *      Nokia = new Company(sheet, sheet3, sheet2, sheet4, array, 0);
 *      addData(filename, "Nordea", 2, workbook, sheet, sheet2, sheet3, sheet4);
 *      Nordea = new Company(sheet, sheet3, sheet2, sheet4, array, 0);
 *      addData(filename, "Telia", 1.5, workbook, sheet, sheet2, sheet3, sheet4);
 *      Telia = new Company(sheet, sheet3, sheet2, sheet4, array, 0);
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
 *      portfolio.portfolioBeMe(0);
 *      portfolio.calculateAverages();
 * }
 */
public class Portfolio extends Asset {
    
    /** Array that includes all the companies that portfolio consist of */
    public ArrayList<Company> companies = new ArrayList<Company>();
    private String averagePortfolioReturnString;
    private String averagePortfolioMarketValueString;
    private double averagePortfolioBeMe;
    private String averagePortfolioBeMeString;

    
    /**
     * default constuctor
     * @param size indicates the size of the companies in the portfolio
     * @param value indicates the value of the companies in the portfolio
     */
    public Portfolio(int size, int value) {
        this.name = "Portfolio " + size + value;
        this.returns = new double[12];
        this.marketValues = new double[12];
        this.beMeRatios = new double[12];
    }
    
    /**
     * Constructor for whole period portfolios
     * @param size Indicates the size of the companies in the portfolio
     * @param value Indicates the value of the companies in the portfolio
     * @param length Indicates how much information whole period holds
     */
    public Portfolio(int size, int value, int length) {
        this.name = "Portfolio " + size + value;
        this.returns = new double[length];
        this.marketValues = new double[length];
        this.beMeRatios = new double[length];
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
     * double[] array = portfolio.getArray(0);
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
                returns[i] += companies.get(j).getDouble(0, month)*(companies.get(j).getDouble(1, month)/marketValues[i]); 
            }
        }
    }
     
    /**
     * Calculates the average portfolio return
     */
    public void calculateAverages() {
        this.averageReturn =  super.average(returns, true);
        this.averagePortfolioReturnString = super.averageString(averageReturn);
        this.averageMarketValue = super.average(marketValues, false);
        this.averagePortfolioMarketValueString = averageString(averageMarketValue);
        this.averagePortfolioBeMe = super.average(beMeRatios, false);
        this.averagePortfolioBeMeString =super. averageString(averagePortfolioBeMe);
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
     * double[] array = portfolio.getArray(1);
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
            marketValues[i] += companies.get(j).getDouble(1, month);
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
     * double[] array = portfolio.getArray(2);
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
            beMeRatios[i] += companies.get(j).getDouble(2, month);
            }
            beMeRatios[i] = beMeRatios[i]/companies.size();
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
        for(int i = 0; i < returns.length; i++) {
            if(returns[i] < min) min = returns[i];
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
        for(int i = 0; i < returns.length; i++) {
            if(returns[i] > max) max = returns[i];
        }
        return max;
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < companies.size(); i++) {
            s.append(companies.get(i).getName());
        }
        return s.toString();
    }
    
    /**
     * Calculates the treynor ratio of the porrtfolio
     * @param marketReturns An array of the market returns
     * @param aveMarketReturn Average market return
     * @return Portfolios treynor ratio
     */
    public double treynorRatio(double[] marketReturns, double aveMarketReturn) {
        return super.treynorRatio(marketReturns, aveMarketReturn, returns, marketValues, averageReturn, false);
    }
    
    /**
     * @param number Which string is requeted
     * @return Certain string value of the wanted variable
     * @example
     * <pre name="test">
     * exampleCompanies();
     * portfolio.getString(0) === "0,17198";
     * portfolio.getString(1) === "19337,5";
     * portfolio.getString(2) === "0,9158";
     * </pre>
     */
    public String getString(int number) {
        switch(number) {
        case 0: return averagePortfolioReturnString; 
        case 1: return averagePortfolioMarketValueString; 
        case 2: return averagePortfolioBeMeString; 
        default: return "";
        }
    }
    
    /**
     * @param args not used
     */
    public static void main(String[] args) {
        //
    }
}
