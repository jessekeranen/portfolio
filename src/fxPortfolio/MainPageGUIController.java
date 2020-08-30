package fxPortfolio;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.ListChooser;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import portfolio.Company;
import portfolio.Market;
import portfolio.Portfolio;

/**
 * @author jessekeranen
 * @version 23.7.2020
 *
 */
public class MainPageGUIController {
    
    @FXML
    ListChooser<Portfolio> chooserPortfolios;
    
    @FXML
    ListChooser<Company> chooserCompanies;
    
    @FXML private TextField portfolioName;
    @FXML private TextField portfolioAveBeMe;
    @FXML private TextField portfolioAveReturn;
    @FXML private TextField portfolioAveMarketValue;
    @FXML private BorderPane pane;
    @FXML private NumberAxis axis;
    @FXML private NumberAxis ayis;
    @FXML private LineChart<Number, Number> chart; 
    @FXML private ComboBoxChooser<String> comboBox;
    @FXML private MenuItem print;
    
    @FXML private void whichYear() {
        showPortfolio(market); 
    }
    
    @FXML
    private void show() {           
        int number = comboBox.getSelectedIndex();      
        currentPortfolio = chooserPortfolios.getSelectedObject();
        
        if(chooserPortfolios.getSelectedObject() == null) { 
            chooserPortfolios.getSelectionModel().select(0);
        }
        
        if(number != comboBoxCount-1) {
            currentPortfolio = chooserPortfolios.getSelectedObject();
            loadData(currentPortfolio);
            showCompanies(currentPortfolio);  
        }
        else {
            //showPortfolio(market);
            if(chooserPortfolios.getSelectedIndex() != -1) {
                chooserCompanies.clear();
                portfolioName.setText(market.years[0][chooserPortfolios.getSelectedIndex()].name); 
                portfolioAveBeMe.setText("");
                portfolioAveMarketValue.setText(market.periodPortfolioMV(chooserPortfolios.getSelectedIndex()));
                portfolioAveReturn.setText(market.periodAverageReturn(chooserPortfolios.getSelectedIndex()));
                loadData(market.periodPortfolioRetruns[chooserPortfolios.getSelectedIndex()]);
                }
            else {
                chooserCompanies.clear();
                portfolioName.setText(market.years[0][0].name); 
                portfolioAveBeMe.setText("");
                portfolioAveMarketValue.setText(market.periodPortfolioMV(0));
                portfolioAveReturn.setText(market.periodAverageReturn(0));
                loadData(market.periodPortfolioRetruns[0]);
            }
        }      
    }
    
    @FXML
    private void printPotfolioReturns() {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Portfolio returns");
        
        putData(sheet);        
        
        try 
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("/Users/jessekeranen/Projects/Työkirja10.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    

    private Market market;
    private Portfolio currentPortfolio;
    private XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
    private int comboBoxCount = 1;
    
    /**
     * @param market market that contains all the companies of the data
     */
    public void setMarket(Market market) {
        this.market = market;
        //chooserPortfolios.addSelectionListener(e-> showPortfolio(market));
        
        for(int i = 0; i < Company.years; i++) {
            market.constructPortfolios( i);
        }
        
        comboBox.clear();
        for(int i = 0; i < market.years.length; i++) {
            comboBox.add("Year " + (i+1)); 
            comboBoxCount += 1;
        }
        comboBox.add("whole period");
        comboBox.getSelectionModel().select(0);
        showPortfolio(market);     
    }
    
    private void addTextfields(Portfolio portfolio) {
        portfolioName.setText(portfolio.name); 
        portfolioAveBeMe.setText("");
        portfolioAveMarketValue.setText(portfolio.averagePortfolioMarketValueString);
        portfolioAveReturn.setText(portfolio.averagePortfolioReturnString);
    }
    
    private void showPortfolio(Market mrkt) {
        chooserPortfolios.clear();
        int number = comboBox.getSelectedIndex();
        if(mrkt == null) return; 
        
        if (number ==  -1) {
            number = 0;
        }
        
        if(number != comboBoxCount-1) {
            for(int i = 0; i < mrkt.portfolioCount; i++) {
                chooserPortfolios.add(mrkt.years[number][i].name, mrkt.years[number][i]);
            }
            showCompanies(mrkt.years[number][0]);  
            loadData(mrkt.years[number][0]);
        }
        else {
            for(int i = 0; i < mrkt.portfolioCount; i++) {
                chooserPortfolios.add(mrkt.years[0][i].name, mrkt.years[0][i]);
            }
            show();
        }
    }
    
    
    private void showCompanies(Portfolio portfolio) {
        chooserCompanies.clear();
        if(portfolio == null) return;
        for(int i = 0; i < portfolio.companies.length; i++) {
            chooserCompanies.add(portfolio.companies[i].name, portfolio.companies[i]);
        }
        addTextfields(portfolio);
    }
    
    private void loadData(Portfolio port) {
       loadData(port.portfolioReturns);
    }   
    
    private void loadData(double[] array) {
        pane.getChildren().clear();
        chart.getData().clear();
        series.getData().clear();
        
        
        NumberAxis xAxis = new NumberAxis(1,market.periodPortfolioRetruns[0].length , 0.5);
        xAxis.setLabel("month");
        NumberAxis yAxis = new NumberAxis(-10,10,1);
        yAxis.setLabel("return");
        ayis = yAxis;
        axis = xAxis;
         
        for(int i = 0; i < array.length; i++) {
            series.getData().add(new XYChart.Data<>(i, array[i]));
        }
        
        chart.getData().add(series);
        pane.getChildren().addAll(chart);
    }
    
    private void putData(XSSFSheet sheet) {
        Map<String, Object[]> data = new TreeMap<String, Object[]>();
        
        putData(data);
        
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
    
    private void putData(Map<String, Object[]> data) {
        Object[] names = new Object[market.periodPortfolioRetruns.length];
        for(int i = 0; i < market.periodPortfolioRetruns.length; i++) {
            names[i] = market.portfolios[i].name;
        }
        data.put("1", names);
        
        market.periodPortfolioReturns();
        
        for(int j = 0; j < market.periodPortfolioRetruns[0].length; j++) {
            Object[] returns = new Object[market.periodPortfolioRetruns.length];
            for(int k = 0; k < market.periodPortfolioRetruns.length; k++) {
                returns[k] = Double.toString(market.periodPortfolioRetruns[k][j]);
            }
        data.put(Integer.toString(j+2), returns);
        }
    }
}