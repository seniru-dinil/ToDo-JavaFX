package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import dbc.DataBaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddToDoItemController implements Initializable {
    public VBox todoContainer;
    public JFXTextField txtTodo;

    public void btnAddTodoOnAction(ActionEvent actionEvent) {
        addTodoItem(txtTodo.getText());
        txtTodo.clear();
    }

    private void addTodoItem(String todoText) {
        HBox todoItem = new HBox(10);
        Label todoLabel = new Label(todoText);
        JFXCheckBox checkBox = new JFXCheckBox();


        todoLabel.setStyle("-fx-font-size: 14px;");
        todoItem.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-margin-bottom: 5px;" +
                        "-fx-border-radius: 4px;"
        );


        todoItem.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(todoLabel, Priority.ALWAYS);
        HBox.setMargin(checkBox, new Insets(0, 0, 0, 0));
        todoItem.setPadding(new Insets(5, 10, 5, 10));
        VBox.setMargin(todoItem, new Insets(0, 0, 5, 0));

        try {
            Connection dbc = DataBaseConnection.getInstance().getConnection();
            String query = "INSERT INTO todo_items (todo) VALUES (?)";
            PreparedStatement stmt = dbc.prepareStatement(query);
            stmt.setString(1, todoText);
            boolean isAdded = stmt.executeUpdate() > 0;
        } catch (SQLException ex) {

        }

        checkBox.setOnAction(e -> {
            if (checkBox.isSelected()) {
                try {
                    Connection dbc = DataBaseConnection.getInstance().getConnection();
                    String query = "DELETE FROM todo_items WHERE todo=?";
                    PreparedStatement stmt = dbc.prepareStatement(query);
                    stmt.setString(1, todoText);
                    boolean isDeleted = stmt.executeUpdate() > 0;
                    String q = "INSERT INTO completed_todo_items (todo) VALUES (?)";
                    PreparedStatement statement = dbc.prepareStatement(q);
                    statement.setString(1, todoText);
                    boolean isAdded = statement.executeUpdate() > 0;
                } catch (SQLException ex) {

                }
                checkBox.setDisable(true);
            } else {

            }
        });
        todoItem.getChildren().addAll(checkBox, todoLabel);
        todoContainer.getChildren().add(todoItem);
    }

    public void txtAddTodoOnAction(ActionEvent actionEvent) {
        addTodoItem(txtTodo.getText());
        txtTodo.clear();
    }

    public void loadData() {
        try {
            Connection dbc = DataBaseConnection.getInstance().getConnection();
            String query = "SELECT * FROM todo_items";
            PreparedStatement stmt = dbc.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String todoText = rs.getString(2);
                HBox todoItem = new HBox(10);
                Label todoLabel = new Label(todoText);
                JFXCheckBox checkBox = new JFXCheckBox();

                todoLabel.setStyle("-fx-font-size: 14px;");
                todoItem.setStyle(
                        "-fx-background-color: #f5f5f5;" +
                                "-fx-margin-bottom: 5px;" +
                                "-fx-border-radius: 4px;"
                );


                todoItem.setAlignment(Pos.CENTER_LEFT);
                HBox.setHgrow(todoLabel, Priority.ALWAYS);
                HBox.setMargin(checkBox, new Insets(0, 0, 0, 0));
                todoItem.setPadding(new Insets(5, 10, 5, 10));
                VBox.setMargin(todoItem, new Insets(0, 0, 5, 0));

                checkBox.setOnAction(e -> {
                    if (checkBox.isSelected()) {
                        try {
                            String s = "DELETE FROM todo_items WHERE todo=?";
                            PreparedStatement preparedStatement = dbc.prepareStatement(s);
                            preparedStatement.setString(1, todoText);
                            boolean isDeleted = preparedStatement.executeUpdate() > 0;
                            String q = "INSERT INTO completed_todo_items (todo) VALUES (?)";
                            PreparedStatement statement = dbc.prepareStatement(q);
                            statement.setString(1, todoText);
                            boolean isAdded = statement.executeUpdate() > 0;
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        checkBox.setDisable(true);
                    } else {

                    }
                });

                todoItem.getChildren().addAll(checkBox, todoLabel);

                todoContainer.getChildren().add(todoItem);
            }

        } catch (SQLException ex) {

        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }
}
