package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    public Button pauseButton;
    int pauseCount = 0;

    private Timeline failTimer = null;
    private Timeline timer = null;

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

        pauseButton.setDisable(true);

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
        pauseButton.setDisable(false);

        for (int i = 0; i < allUsers.size(); i++) {
            allUsers.get(i).setPriority(-1);
        }
        timer = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> onTimerTick()));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.playFromStart();
    }

    public void onStartCorrectButtonPressed(ActionEvent actionEvent) {
        lockAllTables();
        stopAnimation();
        pauseButton.setDisable(false);

        for (int i = 0; i < allUsers.size(); i++) {
            allUsers.get(i).setPriority(0);
        }
        timer = new Timeline(new KeyFrame(
                Duration.millis(3000),
                ae -> onTimerTickCorrect()));
        timer.setCycleCount(Animation.INDEFINITE);
        timer.playFromStart();
    }

    public void startPerfectAction(ActionEvent actionEvent) {
        onStartCorrectButtonPressed(new ActionEvent());

        for (int i = 0; i < allUsers.size(); i++) {
            if (i % 2 == 0) allUsers.get(i).setPriority(40);
            else allUsers.get(i).setPriority(50);
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
        if (timer != null && timer.getStatus() == Animation.Status.RUNNING) timer.pause();
        else if (failTimer != null && failTimer.getStatus() == Animation.Status.RUNNING) failTimer.pause();
        else if (timer != null && timer.getStatus() == Animation.Status.PAUSED) timer.playFromStart();
        else if (failTimer != null && failTimer.getStatus() == Animation.Status.PAUSED) failTimer.playFromStart();

        if (pauseCount == 1) {
            System.out.println("Continued.");
            pauseCount--;
            return;
        }
        System.out.println("Paused.");
        pauseCount++;
    }

    public void stopAnimation(){
        if (timer != null && timer.getStatus() == Animation.Status.RUNNING) timer.stop();
    }

    public void startSimulation(String fail){

        for (int i = 0; i < allUsers.size(); i++) {
            allUsers.get(i).lockTable(0, fail);
            System.out.println(String.format("%-" + 35 + "s", "Priority " + allUsers.get(i).getName() + ": ") + allUsers.get(i).getPriority()+"   times worked: " + allUsers.get(i).getTimesWorked() );
        }
        System.out.println();
        for (int i = 0; i < allUsers.size(); i++) {
            allUsers.get(i).checkTablesForWork();
        }
    }
}
