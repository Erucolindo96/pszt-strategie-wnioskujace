package mainWindow;

import Inference.InferenceMachine;
import Inference.InferenceProduct;
import Inference.Predicate.Clause;
import Inference.Predicate.KnowledgeBase;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import Inference.strategy.*;
import javafx.scene.layout.RowConstraints;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class Controller implements Initializable, Observer {

    private InferenceMachine machine;
    private Strategy selected_strategy;
    private KnowledgeBase knowledge;

    @FXML
    private MenuBar menuBar;
    @FXML
    private TextFlow textFlow;
    @FXML
    TreeView ClausulesTree;
    TreeItem<String> root;

    private final Node rootIcon = new ImageView(new Image("/info.png", 20, 20, false, false));
    private ArrayList<Clause> newestClauses;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ConsoleLogger.INSTANCE.setTextFlow(textFlow);
    }

    public void doLoadFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter fileChooserTxtExtension = new FileChooser.ExtensionFilter("TXT Files(*.txt)", "*.txt");

        fileChooser.getExtensionFilters().add(fileChooserTxtExtension);
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
            knowledge = new KnowledgeBase();
            try {
                knowledge.loadFromFile(file.getPath());
            } catch (IOException e) {
                ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Nie udało się wczytać pliku. Spróbuj jeszcze raz");
                ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, e.getMessage());
                knowledge = null;
                return;
            }
            clearClausulesAndConsole();
            ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wczytano plik z klauzulami");
            ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Oto klauzule pobrane z pliku:");
            for (int i = 0; i < knowledge.getClauseCount(); ++i) {
                ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, knowledge.getClause(i).toString());
            }
            ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Oto klauzule, będące antytezą, wczytane z pliku");
            for (int i = 0; i < knowledge.getAntithesis().size(); ++i) {
                ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, knowledge.getAntithesis().get(i).toString());
            }
        }
    }

    private void initializeInference() {
        machine.addObserver(this);
    }

    private InferenceProduct runInference() {
        return machine.inference();
    }

    private void resetInferenceMachine() {
        machine.deleteObservers(); //aby nie powiadamiac usunietej juz maszyny
        machine = null;
        selected_strategy.resetStep();
        knowledge = null;
    }

    /**
     * Jak wszystkie elementy są dane to uruchamia wnioskowanie i wypisuje wynik
     */
    public void startResolution() {
        if (selected_strategy == null) {
            ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Wybierz najpierw strategie");
            return;
        }
        if (knowledge == null) {
            ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Wczytaj najpierw plik z klauzulami");
            return;
        }

        machine = new InferenceMachine(knowledge, selected_strategy);
        initializeInference();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Uruchomiono");
        InferenceProduct product = runInference();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wynik wnioskowania:" + product);
        if(product == InferenceProduct.TRUE)
           makeTree();
        resetInferenceMachine();

    }

    private void makeTree() {
        TreeItem it = addClausesHeader("Klauzule sprzeczne");
        Stream.iterate(0, (x) -> x + 1).limit(newestClauses.size()).forEach((x) -> addNode(it, newestClauses.get(x)));
        //clearClausulesAndConsole();
    }

    public void doStrategyJustificationSet() {
        selected_strategy = new JustificationSetStrategy();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wybrano strategie zbioru uzasadnień");
    }

    public void doStrategyLinear() {
        selected_strategy = new LinearStrategy();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wybrano strategie liniową");
    }

    public void doStrategyShortClauses() {
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wybrano strategie krótkich klauzul");
        selected_strategy = new ShortClausuleStrategy();
    }

    public void doExit() {
        Platform.exit();
    }


    @Override
    public void update(Observable observable, Object o) {
        InferenceMachine machine = (InferenceMachine) observable;
        ArrayList<Clause> contradict_clauses = machine.getContradictClauses();
        newestClauses = contradict_clauses;
    }

    public TreeItem<String> addClausesHeader(String... clausules) {
        root = new TreeItem<String>(String.join(" ", clausules), rootIcon);
        ClausulesTree.setRoot(root);
        ClausulesTree.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
            handleMouseClicked(event);
        });
        return root;
    }
    private void handleMouseClicked(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
            TreeItem parent = ((TreeItem)ClausulesTree.getSelectionModel().getSelectedItem());
            if(parent == null || !parent.isExpanded()){
                return;
            }
            clearAllGraphics(root);
            if(parent != root)
                parent.setGraphic(new ImageView(new Image("/quality.png")));

            for(Object child : parent.getChildren()){
                if(child instanceof TreeItem && ((TreeItem) child).getParent() == parent){
                    TreeItem item = (TreeItem) child;
                    item.setGraphic(new ImageView(new Image("/play-button.png")));
                }
            }
        }
    }
    private void clearAllGraphics(TreeItem item){
        for(Object child : item.getChildren()){
            if(child instanceof TreeItem){
                TreeItem childTree = (TreeItem) child;
                childTree.setGraphic(null);
                if(((TreeItem) child).getChildren().size() > 0){
                    clearAllGraphics(childTree);
                }
            }
        }
    }
    private void clearClausulesAndConsole() {
        clearConsole();
        clearClausules();
    }
    private void expandWholeTree(TreeItem item){
        if(item == null){
            item = root;
        }
        for(Object child : item.getChildren()){
            if(child instanceof TreeItem){
                TreeItem childTree = (TreeItem) child;
                childTree.setExpanded(true);
                if(((TreeItem) child).getChildren().size() > 0){
                    expandWholeTree(childTree);
                }
            }
        }
    }


    public TreeItem<String> addNode(TreeItem<String> node, Clause clause) { //Returns a TreeItem representation of the specified directory
        TreeItem<String> item = new TreeItem<>(String.join(" ", clause.toString()));
        if (clause.getMather() != null) {
            addNode(item, clause.getMather());
            addNode(item, clause.getFather());
        }
        node.getChildren().add(item);
        return item;
    }

    public void clearClausules() {
        ClausulesTree.setRoot(null);
    }

    private void clearConsole() {
        textFlow.getChildren().clear();
    }


}