package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public Label tableTop;
    public Label tableLeft;
    public Label tableRight;
    public Label tableBottom;
    public Label lockLeft;
    public Label lockTop;
    public Label lockRght;
    public Label lockBottom;
    public Label userBottomLeftWorking;
    public Label userTopLeftWorking;
    public Label userTopRightWorking;
    public Label userBottomRightWorking;
    int pauseCount = 0;

    private Timeline failTimer = null;
    private Timeline correctTimer = null;
    private Timeline perfectTimer = null;

    List<User> allUsers = new LinkedList<>();
    List<Table> allTables = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Table tableOnTop = new Table("Table Top", false, tableTop, lockTop);
        Table tableOnLeft = new Table("Table Left", false, tableLeft, lockLeft);
        Table tableOnRight = new Table("Table Right", false, tableRight, lockRght);
        Table tableOnBottom = new Table("Table Bottom", false, tableBottom, lockBottom);

        User userTopLeft = new User("User Top Left", userTopLeftWorking, "#0000FF", tableOnLeft, tableOnTop);
        User userTopRight = new User("User Top Right", userTopRightWorking, "#FFA500", tableOnTop, tableOnRight);
        User userBottomRight = new User("User Bottom Right", userBottomRightWorking, "#BD61CE", tableOnRight, tableOnBottom);
        User userBottomLeft = new User("User Bottom Left", userBottomLeftWorking, "#008000", tableOnBottom, tableOnLeft);

        allUsers.add(userTopLeft);
        allUsers.add(userTopRight);
        allUsers.add(userBottomRight);
        allUsers.add(userBottomLeft);

        allTables.add(tableOnLeft);
        allTables.add(tableOnTop);
        allTables.add(tableOnRight);
        allTables.add(tableOnBottom);
    }

    public void onStartFailButtonPressed(ActionEvent actionEvent) {
        lockAllTables();
        stopAnimation();
        for (int i = 0; i < allUsers.size(); i++) {
            allUsers.get(i).setPriority(-1);
        }
        failTimer = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> onTimerTick()));
        failTimer.setCycleCount(Animation.INDEFINITE);
        failTimer.playFromStart();
    }

    public void onStartCorrectButtonPressed(ActionEvent actionEvent) {
        lockAllTables();
        stopAnimation();

        for (int i = 0; i < allUsers.size(); i++) {
            allUsers.get(i).setPriority(0);
        }
        correctTimer = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> onTimerTickCorrect()));
        correctTimer.setCycleCount(Animation.INDEFINITE);
        correctTimer.playFromStart();
    }

    public void startPerfectAction(ActionEvent actionEvent) {
        onStartCorrectButtonPressed(new ActionEvent());

        for (int i = 0; i < allUsers.size(); i++) {
            if (i % 2 == 0) allUsers.get(i).setPriority(90);
            else allUsers.get(i).setPriority(100);
        }
    }

    public void onTimerTick(){
        startSimulation("User Bottom Left");
    }

    public void onTimerTickCorrect(){

        startSimulation("");

    }

    public void lockAllTables(){

        for (int i = 0; i < allTables.size(); i++) {
            allTables.get(i).setLocked(false,"", null);
        }
    }

    public void pauseBtnAction(ActionEvent actionEvent) {
        if (correctTimer != null && correctTimer.getStatus() == Animation.Status.RUNNING) correctTimer.pause();
        else if (failTimer != null && failTimer.getStatus() == Animation.Status.RUNNING) failTimer.pause();
        else if (correctTimer != null && correctTimer.getStatus() == Animation.Status.PAUSED) correctTimer.playFromStart();
        else if (failTimer != null && failTimer.getStatus() == Animation.Status.PAUSED) failTimer.playFromStart();
        else return;

        if (pauseCount == 1) {
            System.out.println("Continued.");
            pauseCount--;
            return;
        }
        System.out.println("Paused.");
        pauseCount++;
    }

    public void stopAnimation(){
        if (correctTimer != null && correctTimer.getStatus() == Animation.Status.RUNNING) correctTimer.stop();
        else if (failTimer != null && failTimer.getStatus() == Animation.Status.RUNNING) failTimer.stop();
    }

    public void startSimulation(String fail){
        for (int i = 0; i < allUsers.size(); i++) {
            allUsers.get(i).lockTable(0, fail);
            System.out.println("Priority " + allUsers.get(i).getName() + ": " + allUsers.get(i).getPriority());
        }
        for (int i = 0; i < allUsers.size(); i++) {
            allUsers.get(i).checkTablesForWork();
        }
    }
}
