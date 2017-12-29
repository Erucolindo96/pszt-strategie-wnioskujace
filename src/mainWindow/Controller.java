package mainWindow;

import Inference.InferenceMachine;
import fileManager.FileLoader;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import Inference.strategy.*;

import java.util.Observable;
import java.util.Observer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, Observer {

    private InferenceMachine inference;
    private Strategy selected_strategy;


    @FXML
    MenuBar menuBar;
    @FXML
    TextFlow textFlow;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        ConsoleLogger.INSTANCE.setTextFlow(textFlow);
        ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Hej");
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Heeej");


    }


    public void doLoadFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileChooserTxtExtension = new FileChooser.ExtensionFilter("TXT Files(*.txt)", "*.txt");

        fileChooser.getExtensionFilters().add(fileChooserTxtExtension);
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if(file != null){
            FileLoader fileLoader = new FileLoader(file);
            fileLoader.parseFile();
        }

    }
    public void doStrategyA(){
        selected_strategy = new LinearStrategy();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono strategie A");
    }
    public void doStrategyB(){
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono strategie B");
        //TODO dodaj uzycie strategii
    }
    public void doStrategyC(){
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Uruchomiono strategie C");
        //TODO dodaj uzycie strategii
    }
    public void doExit(){
        Platform.exit();
    }

    @Override
    public void update(Observable observable, Object o)
    {
        System.out.println("Jestem obserwatorem");// TODO jak Tomasz zrobi GUI to tu bedzie obsluga tego co wywnioskowalo InferenceMachine
    }


}
