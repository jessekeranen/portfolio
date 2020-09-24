package portfolio;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;  

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
 * 
 * private Company Nokia; 
 * 
 * 
 * public void exampleCompany() {
 * String filename = "/Users/jessekeranen/CompanyTest.xlsx" ;
 * XSSFWorkbook workbook = new XSSFWorkbook();
 * XSSFSheet sheet = workbook.createSheet("Prices");
 * XSSFSheet sheet2 = workbook.createSheet("MarketValues");
 * XSSFSheet sheet3 = workbook.createSheet("BookValues");
 * XSSFSheet sheet4 = workbook.createSheet("Dividends"); 
 * 
 * XSSFRow row = sheet.createRow(0);
 * row.createCell(0).setCellValue("Nokia");
 * for(int i = 1; i < 13; i++){
 *      row = sheet.createRow(i);
 *      row.createCell(0).setCellValue(i*11.05+i-i*i);
 * }
 *      
 * XSSFRow row2 = sheet2.createRow(0);
 * row2.createCell(0).setCellValue("Nokia");
 * for(int i = 1; i < 13; i++){
 *      row2 = sheet2.createRow(i);
 *      row2.createCell(0).setCellValue(i*1100.05+i-i*i);
 * }
 * 
 * XSSFRow row3 = sheet3.createRow(0);
 * row3.createCell(0).setCellValue("Nokia");
 * for(int i = 1; i < 13; i++){
 *      row3 = sheet3.createRow(i);
 *      row3.createCell(0).setCellValue(i*700.05+1000-i*i);
 * }
 * 
 * XSSFRow row4 = sheet4.createRow(0);
 * row4.createCell(0).setCellValue("Nokia");
 * for(int i = 1; i < 13; i++){
 *      row4 = sheet4.createRow(i);
 *      row4.createCell(0).setCellValue(1*11.05-i*i);
 * }
 *  
 *  try{
 *  FileOutputStream fileOut = new FileOutputStream(filename);
 *  workbook.write(fileOut);
 *  fileOut.close();
 *  workbook.close(); 
 *  }catch( Exception e) {
 *       System.err.println(e.getMessage());
 *  }
 *  
 * Nokia = new Company(sheet, sheet3, sheet2, sheet4, 0);
 * }
 * </pre>
 */
public class Company {
    
    /** Number of years in data */
    public static int years;
    /** Name of the company */
    public String name;
    private double[] prices;
    /** Array of the market values of the company */
    public double[] marketValues;
    /** An array of the book values of the company */
    public double[] bookValues;
    /** Array of the monthly returns of the company */
    public double[] returns;
    /** Array of the Be/Mr-ratios of the company */
    public double[] beMeRatios;
    /** Number of the rows in data sheet */
    public static int rows;
    @SuppressWarnings("unused")
    private double[] dividends;
    
    /**
     * Reads the input sheet and adds the information from the sheet to certain company
     * @param sheet where the price information is read
     * @param sheet2 where the book value information is read
     * @param sheet3 where the market value information is read
     * @param sheet4 where the dividend information is read
     * @param number indicates different companies
     */
    public Company(XSSFSheet sheet, XSSFSheet sheet2, XSSFSheet sheet3, XSSFSheet sheet4, int number) {
        try  
        {  
        XSSFRow row;
        XSSFCell cell;        
       
        rows = sheet.getPhysicalNumberOfRows();
        years = rows/12;
        
        prices = new double[rows-1];

        int cols = 0; // No of columns
        int tmp = 0;

        // This trick ensures that we get the data properly even if it doesn't start from first few rows
        for(int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if(row != null) {
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if(tmp > cols) cols = tmp;
            }
        }
        int columnIndex = number;
            for (int rowIndex = 0; rowIndex<sheet.getPhysicalNumberOfRows(); rowIndex++){
                cell = sheet.getRow(rowIndex).getCell(columnIndex);
                if(cell.getCellTypeEnum() == CellType.NUMERIC) {
                    prices[rowIndex-1] = cell.getNumericCellValue();
                }
                if(cell.getCellTypeEnum() == CellType.STRING && cell.toString().equals("NA")) {
                    prices[rowIndex-1] = -Double.MAX_VALUE;
                }
                else if(cell.getCellTypeEnum() == CellType.STRING) {
                    name = cell.toString();
                }
                
            } 
            returns = returns();
            dividends = dividends(sheet4, number);
            marketValues = marketValues(sheet3, number);
            bookValues = bookValues(sheet2, number);  
            beMeRatios = beMeRatio();
        }  
        catch(Exception e)  {  
            e.printStackTrace();  
        }  
        
    }
    
    /**
     * calculates monthly returns from prices of the stock
     * @return array of the monthly returns
     * @example
     * <pre name="test">
     * #THROWS Exception
     * #import java.text.DecimalFormat;
     * #import java.text.DecimalFormatSymbols;
     * #import java.util.Locale;
     * 
     * exampleCompany();
     * Nokia.name === "Nokia";
     * Nokia.returns.length === 12;
     * double[] array = Nokia.returns;
     * DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
     * DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
     * for (int i = 0; i < array.length; i++) {
     *      array[i] = Double.valueOf(decimalFormat.format(array[i]));
     * } 
     * Arrays.toString(array) === "[0.0, 0.82, 0.35, 0.19, 0.09, 0.03, -0.03, -0.08, -0.15, -0.25, -0.44, -0.95]";
     * </pre>
     */
    public double[] returns() {
        double[] returns1 = new double[prices.length];
        for(int i = 1; i < prices.length; i++) {
            returns1[i] = (prices[i]-prices[i-1])/prices[i-1]; 
        }
        return returns1;
    }
    
    /**
     * calculates monthly be/me-ratios of the company
     * @return monthly be/me-ratios of the company
     * @example
     * <pre name="test">
     * #THROWS Exception
     * #import java.text.DecimalFormat;
     * #import java.text.DecimalFormatSymbols;
     * #import java.util.Locale;
     * 
     * exampleCompany();
     * Nokia.name === "Nokia";
     * Nokia.returns.length === 12;
     * double[] array = Nokia.beMeRatios;
     * DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
     * DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols);
     * for (int i = 0; i < array.length; i++) {
     *      array[i] = Double.valueOf(decimalFormat.format(array[i]));
     * } 
     * Arrays.toString(array) === "[1.54, 1.09, 0.94, 0.86, 0.82, 0.79, 0.76, 0.75, 0.73, 0.72, 0.72, 0.71]";
     * </pre>
     */
    public double[] beMeRatio() {
        double[] bemeRatios = new double[marketValues.length];
        for (int i = 0; i < marketValues.length; i++) {
            if(marketValues[i] != 0) {
            bemeRatios[i] =  bookValues[i]/marketValues[i];
            }
            else bemeRatios[i] = 0;
        }
        return bemeRatios;
    }
    
    /**     
     * subroutine to calculate the book values of the company. DataStream shows same dividend 
     * for each month. Because of that subroutine checks if the dividend is different than last 
     * month and only after that adds it to the share price of the company. In case company pays same dividend
     * in succesive years subroutine adds dividend to the share price of the if the dividend is added last time 
     * 12 months ago
     * @param sheet sheet that contains the information
     * @param number indicates different companies
     * @return array of the companys dividends
     */
    public double[] dividends(XSSFSheet sheet, int number) {
        double[] array = reader(sheet, number);
        int lastChange = 0;
        for(int i = 1; i < prices.length; i++) {
            if(array[i] != array[i-1]) {
                //double dreturn = array[i]/prices[i];
                //returns[i] += dreturn; 
                lastChange = 0;
            }
            else if(lastChange == 12){
                //double dreturn = array[i]/prices[i];
                //returns[i] += dreturn;
                lastChange = 0;
            }
            else {
                lastChange += 1;
            }
        }
        return reader(sheet, number);
    }
    
    /**
     * subroutine to calculate the book values of the company
     * @param sheet sheet that contains the information
     * @param number indicates different companies
     * @return book values of the company
     */
    public double[] bookValues(XSSFSheet sheet, int number) {
        return reader(sheet, number);
    }
    
    /**
     * subroutine to calculate the market values of the company
     * @param sheet sheet that contains the information
     * @param number indicates different companies
     * @return market values of the company
     */
    public double[] marketValues(XSSFSheet sheet, int number) {
        return reader(sheet, number);
    }
    
    /**
     * Reads the input file and adds the information depending on the input file to the company.
     * @param sheet where the infomation is read
     * @param number indicates the company
     * @return Array of the market vakues or book values depending on the input file.
     */
    public double[] reader(XSSFSheet sheet, int number) {
        try  
        {
        XSSFCell cell;
        
        int rowIndex = 0;
        double[] array = new double[rows-1];
        int columnIndex = number;
        for (rowIndex = 0; rowIndex<rows; rowIndex++){
            cell = sheet.getRow(rowIndex).getCell(columnIndex);
            if(cell.getCellTypeEnum() == CellType.NUMERIC) {
                array[rowIndex-1] = cell.getNumericCellValue();
                }
            else if(cell.toString().equals("NA")) {
                array[rowIndex-1] = 0;
                    if(marketValues != null) {
                        marketValues[rowIndex-1] = 0;
                    }   
                }
            }
        return array;
        }
        catch(Exception e)  {  
            e.printStackTrace();  
        }
        return null;
    } 
    
    /**
     * Calculates the mean of the given array return
     * @param array An array holding all the information
     * @return average value
     * @example
     * <pre name="test">
     * #TOLERANCE=0.001
     * exampleCompany();
     * Nokia.average(Nokia.returns) ~~~ 0.012;
     * double[] array = new double[]{1.5, 0, -1.5, -1.6};
     * double[] array2 = new double[]{};
     * double[] array3 = new double[]{1.6, 5.9, 99, 6.45, 1.88};
     * Nokia.average(array) ~~~ -0.4;
     * Nokia.average(array2) ~~~ 0.0;
     * Nokia.average(array3) ~~~ 22.966;
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
     * @param args not used
     */
    public static void main(String[] args) {
        //Company nokia = new Company("Työkirja7.xlsx", 1);
        //System.out.println(nokia);
    }
}
