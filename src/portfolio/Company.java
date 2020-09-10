package portfolio;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;  

/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class Company {
    
    /** Number of years in data */
    public static int years;
    /** Name of the company */
    public String name;
    private double[] prices;
    /** Array of the market values of the company */
    protected double[] marketValues;
    private double[] bookValues;
    /** Array of the monthly returns of the company */
    protected double[] returns;
    /** Array of the Be/Mr-ratios of the company */
    protected double[] beMeRatios;
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
     */
    public double[] returns() {
        double[] returns1 = new double[prices.length-1];
        for(int i = 0; i < prices.length-1; i++) {
            returns1[i] = (prices[i+1]-prices[i])/prices[i]; 
        }
        return returns1;
    }
    
    /**
     * calculates monthly be/me-ratios of the company
     * @return monthly be/me-ratios of the company
     */
    public double[] beMeRatio() {
        double[] bemeRatios = new double[marketValues.length];
        for (int i = 0; i < marketValues.length-1; i++) {
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
        double[] array = new double[sheet.getPhysicalNumberOfRows()-1];
        int columnIndex = number;
        for (rowIndex = 0; rowIndex<sheet.getPhysicalNumberOfRows()-1; rowIndex++){
            cell = sheet.getRow(rowIndex).getCell(columnIndex);
            if(cell.getCellTypeEnum() == CellType.NUMERIC) {
                array[rowIndex] = cell.getNumericCellValue();
                }
            else if(cell.toString().equals("NA")) {
                array[rowIndex] = 0;
                    if(marketValues != null) {
                        marketValues[rowIndex] = 0;
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
     * @param args not used
     */
    public static void main(String[] args) {
        //Company nokia = new Company("Työkirja7.xlsx", 1);
        //System.out.println(nokia);
    }
}
