package fxPortfolio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import fi.jyu.mit.fxgui.ListChooser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import portfolio.Company;
import portfolio.Market;

/**
 * @author jessekeranen
 * @version 23.7.2020
 *
 */
public class PortfolioConstructorGUIController {

    @FXML
    private TextField textBox;

    @FXML
    private Button okButton;
    

    @FXML
    void createCompanies(ActionEvent event) throws IOException {
          
        final FXMLLoader ldr = new FXMLLoader(getClass().getResource("MainPageGUIView.fxml"));
        Pane root = (Pane)ldr.load();
        final MainPageGUIController marketCtrl = (MainPageGUIController)ldr.getController();
        Scene scene = new Scene(root,800,400);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
        
        Market market = new Market();
        
        try  
        {  
        File file = new File("/Users/jessekeranen/projects/Työkirja7.xlsx");
        //File file = new File(textBox.getText());   //creating a new file instance  
        FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file  
        //creating Workbook instance that refers to .xlsx file  
        XSSFWorkbook wb = new XSSFWorkbook(fis);   
        XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
        
        
        for(int i = 0; i < sheet.getRow(1).getPhysicalNumberOfCells(); i++) {
            Company company = new Company(file, i);
            market.addCompany(company);
            }
        }
        catch(Exception e)  {  
            e.printStackTrace();  
        }
        marketCtrl.setMarket(market);
    }
}

