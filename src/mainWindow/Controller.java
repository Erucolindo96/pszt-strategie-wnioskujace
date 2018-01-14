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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
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

//        TreeItem it = addClausesHeader("elo");
//        Stream.iterate(0, (x) -> x + 1).limit(15).forEach((x) -> addNode(it, "a"));
//        String[] x = {"f", "f"};
//        addNode(addNode(it, "b"), x);
        //clearClausulesAndConsole();

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
            //addClauseStep("Teza:");
            //for (Clause clause: knowledge.getThesis())
            //   addClauseStep(clause.toString());
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
        clearConsole();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Uruchomiono");
        InferenceProduct product = runInference();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wynik wnioskowania:" + product);
        makeTree();
        resetInferenceMachine();

    }

    private void makeTree() {
        TreeItem it = addClausesHeader("klauzule uzyskane w ostatnim kroku");
        Stream.iterate(0, (x) -> x + 1).limit(newestClauses.size()).forEach((x) -> addNode(it, newestClauses.get(x)));
        //clearClausulesAndConsole();
    }

    public void doStrategyJustificationSet() {
        selected_strategy = new JustificationSetStrategy();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wwybrano strategie zbioru uzasadnień");
    }

    public void doStrategyLinear() {
        selected_strategy = new LinearStrategy();
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wybrano strategie liniową");
    }

    public void doStrategyShortClauses() {
        ConsoleLogger.INSTANCE.LOG(LEVEL.INFO, "Wybrano strategie krótkich klauzul(jeszcze nie działa)");
        //TODO dodaj uzycie strategii
    }

    public void doExit() {
        Platform.exit();
    }

    /**
     * Metoda odczytujaca od maszyny wnioskujacej jakie klauzule zostaly wytworzone w danym kroku.
     * Wypisuje ona klauzule na ekran za pomoca metody
     *
     * @param observable Referencja do InferenceMachine przeprowadzajaca wnioskowanie
     * @param o          Argumenty przekazywane funkcji update() (tutaj powinny byc nullem)
     */
    @Override
    public void update(Observable observable, Object o) {
        InferenceMachine machine = (InferenceMachine) observable;
        ArrayList<Clause> generated_clauses = machine.getActualNewClauses();

        int inference_step = machine.getInferenceStep();

        String[] clauses_in_string = new String[generated_clauses.size()];
        for (int i = 0; i < generated_clauses.size(); ++i) {
            clauses_in_string[i] = generated_clauses.get(i).toString();
        }
        //addClauseStep(clauses_in_string);
        newestClauses = generated_clauses;
        Thread t = new Thread(() -> printAtConsole(LEVEL.INFO, "Przechwycilem obserwacje nr " + inference_step));
        //ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Przechwycilem obserwacje nr " + inference_step );
    }

    public TreeItem<String> addClausesHeader(String... clausules) {
        root = new TreeItem<String>(String.join(" ", clausules), rootIcon);
        ClausulesTree.setRoot(root);
        return root;
    }

    private void addClauseStep(TreeItem<String> node, String... clausules) {
        //TreeItem<String> item = new TreeItem<String>(String.join(" ", clausules));
        // node.getChildren().add(item);
    }

    private void clearClausulesAndConsole() {
        clearConsole();
        clearClausules();
    }


    public TreeItem<String> addNode(TreeItem<String> node, Clause clause) { //Returns a TreeItem representation of the specified directory
        TreeItem<String> item = new TreeItem<>(String.join(" ", clause.toString()));
        if (clause.getMather() != null) {
            System.out.println("debuger jest nudny");
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

    private void printAtConsole(LEVEL l, String s) {
        ConsoleLogger.INSTANCE.LOG(l, s);
    }
}