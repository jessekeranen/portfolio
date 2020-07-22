package portfolio;

import java.io.File;  
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Iterator;  
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;  

/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class Company {
    
    protected String name;
    private double[] prices;
    protected double[] marketValues;
    private double[] bookValues;
    protected double[] returns;
    protected double[] beMeRatios;
    
    /**
     * @param name name of the company
     * @param prices monthly prices of the company
     * @param marketValues monthly market value of the company
     * @param bookValues monthly book value of the company
     */
    public Company(String name, double[] prices, double [] marketValues, double [] bookValues) {
        this.name = name;
        this.prices = prices;
        this.marketValues = marketValues;
        this.bookValues = bookValues;
        this.returns = returns();
        this.beMeRatios = beMeRatio();
    }
    
    public Company(String filename, int number) {
        try  
        {  
        File file = new File(filename);   //creating a new file instance  
        FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
        //creating Workbook instance that refers to .xlsx file  
        XSSFWorkbook wb = new XSSFWorkbook(fis);   
        XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
        XSSFRow row;
        XSSFCell cell;
        
        int rows; // No of rows
        rows = sheet.getPhysicalNumberOfRows();
        
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
                else if(cell.getCellTypeEnum() == CellType.STRING) {
                    name = cell.toString();
                }
                
            }    
        }  
        catch(Exception e)  {  
            e.printStackTrace();  
        }  
    }
    
    /**
     * calculates monthly return from prices of the stock
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
        for (int i = 0; i < marketValues.length; i++) {
            bemeRatios[i] =  bookValues[i]/marketValues[i];
        }
        return bemeRatios;
    }
    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        double[] hinnat = {10,15,20,10};
        double[] arvot = {100, 200, 1000, 300};
        double[] book = {10, 200,1000, 30};
        Company nokia = new Company("Nokia", hinnat, arvot, book);
        System.out.println(nokia);
    }
}
