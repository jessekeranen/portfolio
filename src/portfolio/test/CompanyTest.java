package portfolio.test;
// Generated by ComTest BEGIN
import java.util.Arrays;
import java.io.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import portfolio.Company;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import static org.junit.Assert.*;
import org.junit.*;
import portfolio.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2020.10.08 13:16:43 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class CompanyTest {


  // Generated by ComTest BEGIN  // Company: 13

  private Company Nokia; 


  public void exampleCompany() {
  String filename = "/Users/jessekeranen/CompanyTest.xlsx" ; 
  XSSFWorkbook workbook = new XSSFWorkbook(); 
  XSSFSheet sheet = workbook.createSheet("Prices"); 
  XSSFSheet sheet2 = workbook.createSheet("MarketValues"); 
  XSSFSheet sheet3 = workbook.createSheet("BookValues"); 
  XSSFSheet sheet4 = workbook.createSheet("Dividends"); 

  XSSFRow row = sheet.createRow(0); 
  row.createCell(0).setCellValue("Nokia"); 
  for(int i = 1; i < 13; i++){
       row = sheet.createRow(i); 
       row.createCell(0).setCellValue(i*11.05+i-i*i); 
  }

  XSSFRow row2 = sheet2.createRow(0); 
  row2.createCell(0).setCellValue("Nokia"); 
  for(int i = 1; i < 13; i++){
       row2 = sheet2.createRow(i); 
       row2.createCell(0).setCellValue(i*1100.05+i-i*i); 
  }

  XSSFRow row3 = sheet3.createRow(0); 
  row3.createCell(0).setCellValue("Nokia"); 
  for(int i = 1; i < 13; i++){
       row3 = sheet3.createRow(i); 
       row3.createCell(0).setCellValue(i*700.05+1000-i*i); 
  }

  XSSFRow row4 = sheet4.createRow(0); 
  row4.createCell(0).setCellValue("Nokia"); 
  for(int i = 1; i < 13; i++){
       row4 = sheet4.createRow(i); 
       row4.createCell(0).setCellValue(1*11.05-i*i); 
  }

   try{
   FileOutputStream fileOut = new FileOutputStream(filename); 
   workbook.write(fileOut); 
   fileOut.close(); 
   workbook.close(); 
   }catch( Exception e) {
        System.err.println(e.getMessage()); 
   }

  double[] array = new double[12]; 
  for(int i = 0; i < 12; i++) {
       array[i] = 0.01; 
  }

  Nokia = new Company(sheet, sheet3, sheet2, sheet4, array, 0); 
  }
  // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testReturns154 
   * @throws Exception when error
   */
  @Test
  public void testReturns154() throws Exception {    // Company: 154
    exampleCompany(); 
    assertEquals("From: Company line: 161", "Nokia", Nokia.getName()); 
    assertEquals("From: Company line: 162", 12, Nokia.getArray(0).length); 
    double[] array = Nokia.getArray(0); 
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US); 
    DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols); 
    for (int i = 0; i < array.length; i++) {
    array[i] = Double.valueOf(decimalFormat.format(array[i])); 
    }
    assertEquals("From: Company line: 169", "[0.0, 0.82, 0.35, 0.19, 0.09, 0.03, -0.03, -0.08, -0.15, -0.25, -0.44, -0.95]", Arrays.toString(array)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testBeMeRatio184 
   * @throws Exception when error
   */
  @Test
  public void testBeMeRatio184() throws Exception {    // Company: 184
    exampleCompany(); 
    assertEquals("From: Company line: 191", "Nokia", Nokia.getName()); 
    assertEquals("From: Company line: 192", 12, Nokia.getArray(0).length); 
    double[] array = Nokia.getArray(2); 
    DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US); 
    DecimalFormat decimalFormat = new DecimalFormat("0.00", symbols); 
    for (int i = 0; i < array.length; i++) {
    array[i] = Double.valueOf(decimalFormat.format(array[i])); 
    }
    assertEquals("From: Company line: 199", "[1.54, 1.09, 0.94, 0.86, 0.82, 0.79, 0.76, 0.75, 0.73, 0.72, 0.72, 0.71]", Arrays.toString(array)); 
  } // Generated by ComTest END
}