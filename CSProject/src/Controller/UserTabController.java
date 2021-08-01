package Controller;

import Model.User;
import javafx.application.Preloader;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class UserTabController {
    @FXML
    public Label usernameLbl;
    @FXML
    public Button editNameBtn;
    @FXML
    public Button deleteUserBtn;
    @FXML
    public TextField newUserTF;
    @FXML
    public Button confirmEditBtn;
    @FXML
    public Label joinDateLbl;
    @FXML
    public ImageView imageView;

    public static User selectedUser;

    public void editNameBtnAction(ActionEvent e){
        newUserTF.setVisible(true);
        confirmEditBtn.setVisible(true);
    }

    public void confirmEditBtnAction(ActionEvent e){
        String str = newUserTF.getText();
        if (str.equals(selectedUser.getUserName())){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("The new name is the same as the current name.");
            a.showAndWait();
        }
        else if (!str.matches("[a-zA-Z0-9]{1,16}")){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Invalid username");
            a.setContentText("Username can only be 1-16 alphanumeric characters.");
            a.showAndWait();
        }
        else{
            boolean cont = true;
            for (User u: MainController.getUserList()){
                if (str.equals(u.getUserName())){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText("User already exists");
                    a.setContentText("Check name and try again");
                    a.showAndWait();
                    cont = false;
                }
            }
            if (cont){
                selectedUser.setUserName(str);
                usernameLbl.setText(str);
                selectedUser.ssp.set("Hello, " + selectedUser.getUserName() +  "!");
                selectedUser.setLastLogin(new Date());
                TripsTabController.writeTrips();
                TripsTabController.loadTrips();
                EnterController.rewriteUsers();
                newUserTF.setVisible(false);
                confirmEditBtn.setVisible(false);
            }
        }

    }


    public void deleteUserBtnAction(ActionEvent e){
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setHeaderText("Are you sure?");
        a.setContentText("You will lose all your saved lists and trips.");
        Optional<ButtonType> choice = a.showAndWait();
        if (choice.get() == ButtonType.OK){
            MainController.getUserList().remove(selectedUser);
            try {
                selectedUser.setLastLogin(new Date());
                EnterController.setSelectedUser(null);
                EnterController.rewriteUsers();
                Stage mainStage = new Stage();
                Thread.sleep(30);
                Parent root = FXMLLoader.load(getClass().getResource("/View/Enter.fxml"));
                mainStage.setTitle("Select an user");
                mainStage.setScene(new Scene(root, 335, 600));
                mainStage.setResizable(false);
                mainStage.getIcons().add(new Image("file:" + System.getProperty("user.dir") + "/resources/GroSureMini.png"));
                mainStage.show();
                Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                appStage.close();
            }
            catch (Exception mm){
                mm.printStackTrace();
            }
        }
    }

    public void initialize(){
        selectedUser = MainController.getSelectedUser();
        imageView.setImage(new Image("file:" + System.getProperty("user.dir") + "/resources/edit.png"));
        usernameLbl.setText(selectedUser.getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        joinDateLbl.setText(sdf.format(selectedUser.getCreationDate()));
    }
}
