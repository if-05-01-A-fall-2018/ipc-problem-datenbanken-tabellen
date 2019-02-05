package sample;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Table {
    private String name;
    private boolean locked;
    private Label label;
    private Label lock;
    private User user = null;

    public Table(String name, boolean locked, Label label, Label lock) {
        this.name = name;
        this.locked = locked;
        this.label = label;
        this.lock = lock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User isLocked() {
        if (user == null) return null;
        return user;
    }

    public void setLocked(boolean locked, String color, User user) {
        this.locked = locked;
        if (locked) {
            lock.setTextFill(Color.web("#8B0000"));
            label.setTextFill(Color.web(color));
            this.user = user;
            return;
        }
        this.user = null;
        lock.setTextFill(Color.web("#FFFFFF"));
        label.setTextFill(Color.web("#000000"));
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
