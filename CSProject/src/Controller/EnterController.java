package Controller;

import Model.ShoppingList;
import Model.ShoppingTrip;
import Model.User;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.naming.LimitExceededException;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class EnterController {
    @FXML
    public Button selectBtn;
    @FXML
    public Button addUserBtn;
    @FXML
    public TableColumn userNameColumn;
    @FXML
    public TableColumn lastModColumn;
    @FXML
    public TableView<User> tableView;


    @FXML
    private static User selectedUser;

    private static ObservableList<User> userList;

    public static User getSelectedUser() {
        return selectedUser;
    }

    public static ObservableList<User> getUserList() {
        return userList;
    }

    public static void setSelectedUser(User selectedUser) {
        EnterController.selectedUser = selectedUser;
    }

    @FXML
    public void selectButtonAction(ActionEvent e) throws Exception {
        try {
            selectedUser = tableView.getSelectionModel().getSelectedItem();
            selectedUser.setLastLogin(new Date());
            selectedUser.ssp = new SimpleStringProperty("Hello, " +selectedUser.getUserName() + "!");
            rewriteUsers();
            Stage mainStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/View/MainWindow.fxml"));
            mainStage.setTitle("GroSure - Online Grocery Tracking");
            mainStage.setScene(new Scene(root, 900, 600));
            mainStage.setResizable(false);
            mainStage.getIcons().add(new Image("file:" + System.getProperty("user.dir") + "/resources/GroSureMini.png"));
            mainStage.show();
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            appStage.close();
        } catch (NullPointerException x) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("No user selected");
            a.setContentText("Select an user before entering.");
            a.showAndWait();
        }
    }

    @FXML
    public void addUserAction(ActionEvent e) throws Exception{
        Stage addUserStage = new Stage();
        addUserStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLLoader.load(getClass().getResource("/View/CreateUser.fxml"));
        addUserStage.setScene(new Scene(root, 410,260));
        addUserStage.getIcons().add(new Image("file:"  + System.getProperty("user.dir") + "src/resources/GroSureMini.png"));
        addUserStage.setResizable(false);
        addUserStage.setTitle("Create a new user");
        addUserStage.show();
    }

    public static void rewriteUsers(){
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream( System.getProperty("user.dir") + "/Data/users.csv"));
            pw.println(userList.size());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            for (User u : userList) {
                pw.println(u.getUserName() + "," + sdf.format(u.getLastLogin()) + "," + sdf.format(u.getCreationDate()));
            }
            pw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    public void initialize(){
        try {
            userList = FXCollections.observableArrayList();
            Scanner scn = new Scanner(new File(  System.getProperty("user.dir") + "/Data/users.csv"));
            String a = scn.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            while(scn.hasNextLine()) {
                String[] items = new String[3];
                items =scn.nextLine().split(",");
                ArrayList<ShoppingList> lists = new ArrayList<ShoppingList>();
                User s = new User(items[0], sdf.parse(items[1]), sdf.parse(items[2]));
                userList.add(s);
            }
            scn.close();
            try{
                tableView.setItems(userList);
                userNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("userName"));
                lastModColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastLoginFromNow"));
            }
            catch(Exception t){
            }

        }

        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(ParseException e){
            e.printStackTrace();
        }

    }




}


