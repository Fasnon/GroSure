package Controller;

import Model.User;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.naming.StringRefAddr;
import java.io.IOException;
import java.util.Date;

public class MainController {
    @FXML
    public TabPane tabPane;
    @FXML
    public Button logoutBtn;
    @FXML
    public Label usernameLbl;
    @FXML
    public ImageView imageView;

    private static User selectedUser;
    private static ObservableList<User> userList;

    public static ObservableList<User> getUserList() {
        return userList;
    }

    public static User getSelectedUser() {
        return selectedUser;
    }

    public void logoutBtnAction(ActionEvent e){
        try {
            selectedUser.setLastLogin(new Date());
            EnterController.setSelectedUser(null);
            EnterController.rewriteUsers();
            Stage mainStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/View/Enter.fxml"));
            mainStage.setTitle("Select an user");
            mainStage.setScene(new Scene(root, 335, 600));
            mainStage.setResizable(false);
            mainStage.getIcons().add(new Image("file:"+ System.getProperty("user.dir") + "/resources/GroSureMini.png"));
            mainStage.show();
            Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            appStage.close();
        }
        catch (Exception a){
            a.printStackTrace();
        }

    }


    public void initialize() throws IOException {
        selectedUser = EnterController.getSelectedUser();
        userList = EnterController.getUserList();
        usernameLbl.setText("Hello, " + selectedUser.getUserName() + "!");
        usernameLbl.textProperty().bind(selectedUser.ssp);
        imageView.setImage(new Image("file:" + System.getProperty("user.dir") + "/resources/logout.png"));


        tabPane.getTabs().add(new Tab("Manage Items", (Parent) FXMLLoader.load(MainController.class.getResource("/View/ItemsTab.fxml"))));
        tabPane.getTabs().add(new Tab("Shopping Lists", (Parent) FXMLLoader.load(MainController.class.getResource("/View/ListTab.fxml"))));
        tabPane.getTabs().add(new Tab("Shopping Trips", (Parent) FXMLLoader.load(MainController.class.getResource("/View/TripsTab.fxml"))));
        tabPane.getTabs().add(new Tab("Store Locator", (Parent) FXMLLoader.load(MainController.class.getResource("/View/LocatorTab.fxml"))));
        tabPane.getTabs().add(new Tab("User Details", (Parent) FXMLLoader.load(MainController.class.getResource("/View/UserTab.fxml"))));
        tabPane.getTabs().add(new Tab("About", (Parent) FXMLLoader.load(MainController.class.getResource("/View/AboutTab.fxml"))));
    }

}
