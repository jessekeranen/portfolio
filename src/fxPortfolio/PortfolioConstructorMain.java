package fxPortfolio;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author jessekeranen
 * @version 22.7.2020
 *
 */
public class PortfolioConstructorMain extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader ldr = new FXMLLoader(getClass().getResource("PortfolioConstructorGUIView.fxml"));
            final Pane root = ldr.load();
            //final PortfolioConstructorGUIController portfolioconstructorCtrl = (PortfolioConstructorGUIController) ldr.getController();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("portfolioconstructor.css").toExternalForm());
            primaryStage.setTitle("PortfolioConstructor");
            @SuppressWarnings("resource")
            Image icon = new Image(getClass().getResourceAsStream("../content/icon.png"));
            primaryStage.getIcons().add(icon);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args cmd parameters
     */
    public static void main(String[] args) {
        launch(args);
    }
}