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
public class XLSX {
    public static void main(String[] args)   
    {  
    try  
    {  
    File file = new File("/Users/jessekeranen/projects/Työkirja7.xlsx");   //creating a new file instance  
    FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
    //creating Workbook instance that refers to .xlsx file  
    XSSFWorkbook wb = new XSSFWorkbook(fis);   
    XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
    XSSFRow row;
    XSSFCell cell;
    
    int rows; // No of rows
    rows = sheet.getPhysicalNumberOfRows();

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
    String name = "";
    double[] prices = new double[sheet.getPhysicalNumberOfRows()-1];
    int columnIndex = 0;
        for (int rowIndex = 0; rowIndex<sheet.getPhysicalNumberOfRows(); rowIndex++){
            cell = sheet.getRow(rowIndex).getCell(columnIndex);
            if(cell.getCellTypeEnum() == CellType.NUMERIC) {
                prices[rowIndex-1] = cell.getNumericCellValue();
            }
            else if(cell.getCellTypeEnum() == CellType.STRING) {
                name = cell.toString();
            }
            
        }
        System.out.println(name);
        System.out.println(Arrays.toString(prices));
    
    }  
    catch(Exception e)  {  
        e.printStackTrace();  
    }  
    }  
}
