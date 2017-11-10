package mainWindow;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import sun.plugin.dom.exception.InvalidStateException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/*Singleton pattern */
enum LEVEL {
    ERROR, INFO;
}

/**
 * Use Example
 *         ConsoleLogger.INSTANCE.LOG(LEVEL.ERROR, "Hej");
 ConsoleLogger.INSTANCE.LOG(LEVEL.INFO,"Heeej");
 */
public enum ConsoleLogger {
    INSTANCE;

    private TextFlow textFlow;

    public void setTextFlow(TextFlow textFlow) {
        this.textFlow = textFlow;
    }

    public void LOG(LEVEL level, String msg) {
        if(textFlow == null)
            throw new InvalidStateException("TextFlow is null");

        Text text = new Text();
        Text textTime = new Text();
        if (level == LEVEL.ERROR) {
            textTime.setStyle("-fx-fill: red");
            text.setStyle("-fx-fill: red");
        } else if (level == level.INFO) {
            textTime.setStyle("-fx-fill: slategray");
            text.setStyle("-fx-fill: slategray");
        }
        // add current time to log
        textTime.setText(DateTimeFormatter.ofPattern("HH:mm:ss ").format(LocalDateTime.now()));
        text.setText(msg+"\n");

        textFlow.getChildren().addAll(textTime, text);
    }
}
