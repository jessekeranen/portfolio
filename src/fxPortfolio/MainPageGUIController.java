package fxPortfolio;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.ListChooser;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import portfolio.Company;
import portfolio.Market;
import portfolio.Portfolio;

/**
 * @author jessekeranen
 * @version 23.7.2020
 *
 */
public class MainPageGUIController {
    
    @FXML ListChooser<Portfolio> chooserPortfolios;  
    @FXML ListChooser<Company> chooserCompanies;  
    @FXML private Text portfolioName;
    @FXML private Text portfolioAveBeMe;
    @FXML private Text portfolioAveReturn;
    @FXML private Text portfolioAveMarketValue;
    @FXML private Text portfolioSharpe;
    @FXML private Text portfolioTreynor;
    @FXML private Text portfolioBeta;
    @FXML private Text return1;
    @FXML private Text return2;
    @FXML private Text return3;
    @FXML private Text marketValue1;
    @FXML private Text marketValue3;
    @FXML private Text marketValue2;
    @FXML private Text bookValue1;
    @FXML private Text bookValue2;
    @FXML private Text bookValue3;
    @FXML private Pane pane;
    @FXML private NumberAxis axis;
    @FXML private NumberAxis ayis;
    @FXML private LineChart<Number, Number> chart; 
    @FXML private ComboBoxChooser<String> comboBox;
    @FXML private MenuItem print;
    @FXML private Button exit;
    @FXML private Button Print;
    
    @FXML private void whichYear() {
        showPortfolio(market); 
        chooserPortfolios.setSelectedIndex(0);
    }
    
    @FXML private void exit() {
        Platform.exit();
        System.exit(0);
    }

    
    @FXML
    private void showCompany() {
        currentCompany = chooserCompanies.getSelectedObject();
        CompanyGUIController.askCompany(null, currentCompany);
    }
    
    @FXML
    private void printPotfolioReturns() { 
        try(XSSFWorkbook workbook = new XSSFWorkbook();) 
        {
            XSSFCellStyle cellStyle = workbook.createCellStyle();
            XSSFCellStyle cellStyle2 = workbook.createCellStyle();
            
            XSSFSheet sheet = workbook.createSheet("Portfolio returns");
            XSSFSheet sheet2 = workbook.createSheet("Portfolio names");
            XSSFSheet sheet3 = workbook.createSheet("Factors");
            
            market.constructFactors();
        
            putData(sheet, market.periodPortfolioReturns, false);
            putNames(sheet2, cellStyle, cellStyle2);
            putData(sheet3, market.periodFactorPortfolioRetruns, true);
            //Write the workbook in file system
            @SuppressWarnings("resource")
            FileOutputStream out = new FileOutputStream(new File("/Users/jessekeranen/Projects/Työkirja10.xlsx"));
            workbook.write(out);
            out.close();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void constructFarctors() {
        market.constructFactors();
    }
    
    

    private Market market;
    private Portfolio currentPortfolio;
    private Company currentCompany;
    private XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
    private int comboBoxCount = 1;
    
    /**
    * @param market market that contains all the companies of the data
    * @param mvCount number of market value breakpoints
    * @param bmCount number of Be/Me breakpoints
    */
    public void setMarket(Market market, int mvCount, int bmCount) {
        this.market = market;
        //chooserPortfolios.addSelectionListener(e-> showPortfolio(market));
        market.totalMarketValues();
        market.marketReturns();
        market.averageReturn();
        
        
        /** Treynor ratios are calculated at this point because to be able to calculate treynor ratio we need to know 
         * market returns which we don´t know before we have created all the companies */
        for(int j = 0; j < market.companies.size(); j++) {
            market.companies.get(j).treynorRatio(market.marketReturns, market.averageReturn);
        }
        
        for(int i = 0; i < Company.years; i++) {
            market.constructPortfolios(i, mvCount, bmCount, 0);
        }
        market.period(mvCount, bmCount);
        
        comboBox.clear();
        for(int i = 0; i < market.years.length; i++) {
            comboBox.add("Year " + (i+1)); 
            comboBoxCount += 1;
        }
        comboBox.add("whole period");
        comboBox.getSelectionModel().select(0);
        showPortfolio(market);   
        chooserCompanies.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) showCompany(); } );
        chooserPortfolios.setSelectedIndex(0);
    }
    
    @FXML
    private void show() {           
        int number = comboBox.getSelectedIndex();      
        currentPortfolio = chooserPortfolios.getSelectedObject();
        if(currentPortfolio == null) chooserPortfolios.setSelectedIndex(0);
        
        if(number != comboBoxCount-1) {
            currentPortfolio = chooserPortfolios.getSelectedObject();
            loadData(currentPortfolio.getArray(0));
            showCompanies(currentPortfolio);  
        }
        
        else {
            chooserCompanies.clear();
            addTextfields(market.wholePeriodPortfolios[chooserPortfolios.getSelectedIndex()]);
            loadData(market.wholePeriodPortfolios[chooserPortfolios.getSelectedIndex()].getArray(0));
        }      
    }
    
    private void addTextfields(Portfolio portfolio) {
        portfolioName.setText(portfolio.getName()); 
        portfolioAveBeMe.setText(portfolio.getString(2));
        portfolioAveMarketValue.setText(portfolio.getString(1));
        changeColor(portfolioAveReturn, portfolio, portfolio.getDouble(0));
        portfolioSharpe.setText(portfolio.averageString(portfolio.sharpeRatio(portfolio.getArray(0), market.rf)));
        portfolioTreynor.setText(portfolio.averageString(portfolio.treynorRatio(market.marketReturns, market.averageReturn)));
        portfolioBeta.setText(portfolio.averageString(portfolio.beta));
        changeColor(return1,portfolio, portfolio.getReturn(12));
        changeColor(return2, portfolio, portfolio.getReturn(6));
        changeColor(return3, portfolio, portfolio.getReturn(1));
        marketValue1.setText(portfolio.averageString(portfolio.getDouble(1, portfolio.getArray(1).length-12)));
        marketValue2.setText(portfolio.averageString(portfolio.getDouble(1, portfolio.getArray(1).length-7)));
        marketValue3.setText(portfolio.averageString(portfolio.getDouble(1, portfolio.getArray(1).length-1)));
        bookValue1.setText(portfolio.averageString(portfolio.getDouble(2, portfolio.getArray(2).length-12)));
        bookValue2.setText(portfolio.averageString(portfolio.getDouble(2, portfolio.getArray(2).length-7)));
        bookValue3.setText(portfolio.averageString(portfolio.getDouble(2, portfolio.getArray(2).length-1)));
    }
    
    private void changeColor(Text text, Portfolio portfolio, double value) {
        text.setText(portfolio.averageString(value));
        if(value >= 0) text.setFill(Color.GREEN);
        else text.setFill(Color.web("A6341B"));
    }
    
    private void showPortfolio(Market mrkt) {
        chooserPortfolios.clear();
        chooserPortfolios.setSelectedIndex(0);
        int number = comboBox.getSelectedIndex();
        if(mrkt == null) return; 
        
        if (number <=  0 || number >= mrkt.years.length) {
            number = 0;
        }

            for(int i = 0; i < mrkt.portfolioMaxCount; i++) {
                chooserPortfolios.add(mrkt.years[number][i].getName(), mrkt.years[number][i]);
            }
            show();     
    }
    
    
    private void showCompanies(Portfolio portfolio) {
        chooserCompanies.clear();
        if(portfolio == null) return;
        for(int i = 0; i < portfolio.companies.size(); i++) {
            chooserCompanies.add(portfolio.companies.get(i).getName(), portfolio.companies.get(i));
        }
        addTextfields(portfolio);
    }
    
    private void loadData(double[] array) {
        
        pane.getChildren().clear();
        
        ayis = CompanyGUIController.setAxis("month",1, market.months);
        axis = CompanyGUIController.setAxis("return",-10, 10);
         
        series = CompanyGUIController.loadData(array, chart, series);
        chart.getData().add(series);
        pane.getChildren().addAll(chart);
    }   
    
    private void putData(XSSFSheet sheet, double[][] array, boolean factor) {
        if(factor == false) {
            String[] names = new String[market.years[0].length];
            for(int i = 0; i < names.length; i++) {
                names[i] = market.years[0][i].getString(0);
            }
            putData(sheet, array, names);
        }
        else {
            String[] names = new String[market.factors.length];
            for(int i = 0; i < names.length; i++) {
                names[i] = market.factors[i].name;
            }
            putData(sheet, array, names);
        }
    }
    
    private void putData(XSSFSheet sheet, double[][] array, String[] names) {
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        
        putData(data, array, names);
        
        Set<String> keyset = data.keySet();

        int rownum = 0;
        for (String key : keyset) 
        {
            //create a row of excelsheet
            Row row = sheet.createRow(rownum++);

            //get object array of prerticuler key
            Object[] objArr = data.get(key);

            int cellnum = 0;

            for (Object obj : objArr) 
            {
                Cell cell = row.createCell(cellnum++);
                if (obj instanceof String) 
                {
                    cell.setCellValue((String) obj);
                }
                else if (obj instanceof Integer) 
                {
                    cell.setCellValue((Integer) obj);
                }
            }
        }
    }
    
    private void putData(Map<String, Object[]> data, double[][] array, String[] names) {
        Object[] names2 = new Object[array.length];
        for(int i = 0; i < array.length; i++) {
            names2[i] = names[i];
        }
        data.put("1", names2);
        
        market.periodPortfolioReturns(false);
        market.periodPortfolioReturns(true);
        
        for(int j = 0; j < array[0].length; j++) {
            Object[] returns = new Object[array.length];
            for(int k = 0; k < array.length; k++) {
                returns[k] = Double.toString(array[k][j]);
            }
        data.put(Integer.toString(j+2), returns);
        }
    }
    
    private void putNames(XSSFSheet sheet, XSSFCellStyle cellStyle, XSSFCellStyle cellStyle2) {
        
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle2.setBorderBottom(BorderStyle.THIN);
        cellStyle2.setBorderRight(BorderStyle.THIN);
        
        Row[] rows = new Row[market.companies.size()];
        for(int i = 0; i < rows.length; i++) {
            rows[i] = sheet.createRow(i);
        }       
        
        for(int i = 0; i < market.years.length; i++) {
            int cellnum = i;
            int rownum = 1;
            
            Cell cell = rows[rownum-1].createCell(cellnum);
            cell.setCellValue("Year " + i);
            cell.setCellStyle(cellStyle2);
            
            for(int j = 0; j < market.years[0].length; j++) {  
                cell = rows[rownum++].createCell(cellnum);
                cell.setCellValue("");  
                cell.setCellStyle(cellStyle);
                cell = rows[rownum++].createCell(cellnum);
                cell.setCellValue(market.years[0][j].getString(0));
                cell.setCellStyle(cellStyle2);
                for(int k = 0; k < market.years[i][j].companies.size(); k++) {  
                    cell = rows[rownum].createCell(cellnum);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(market.years[i][j].companies.get(k).getName());
                    rownum++;
                }
            }
        }
    }
}