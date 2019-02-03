package sample;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.Random;

public class User {

    private String name;
    private Label working;
    private String color;
    private Table[] tablesUsed = new Table[2];
    private int priority = -1;


    public User(String name, Label working, String color, Table first, Table second) {
        this.name = name;
        this.working = working;
        this.color = color;
        tablesUsed[0] = first;
        tablesUsed[1] = second;
    }

    public void lockTable(int x, String fail) {
        if (tablesUsed[0].isLocked() == this && tablesUsed[1].isLocked() == this){
            tablesUsed[0].setLocked(false, color, null);
            tablesUsed[1].setLocked(false, color, null);
            return;
        }
        if (tablesUsed[0].isLocked() == null || tablesUsed[0].isLocked().getPriority() > this.priority){
            tablesUsed[0].setLocked(true, color, this);
            return;
        }
        if ((tablesUsed[1].isLocked() == null || tablesUsed[1].isLocked().getPriority() > this.priority) && !name.equals(fail)){
            tablesUsed[1].setLocked(true, color, this);
            return;
        }
        if (tablesUsed[x].isLocked() == this) {
            tablesUsed[x].setLocked(false, color, null);
        }
    }

    public int getPriority(){
        if (priority == 0){
            Random random = new Random();
            this.priority = random.nextInt(100);
        }
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return this.name;
    }

    public boolean checkTablesForWork() {
        if (tablesUsed[0].isLocked() == this && tablesUsed[1].isLocked() == this) {
            working.setTextFill(Color.web("#8B0000"));
            this.priority += 50;
            return true;
        }
        else{
            working.setTextFill(Color.web("#FFFFFF"));
        }
        return false;
    }
}
