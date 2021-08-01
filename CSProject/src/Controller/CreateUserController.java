package Controller;

import Model.User;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.naming.LimitExceededException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class CreateUserController {
    @FXML
    public Button createUserBtn;
    @FXML
    public TextField newNameTF;
    private static ObservableList<User> userList;


    @FXML
    public void createUserAction(ActionEvent e) {
        try {
            Scanner scn = new Scanner(new File(System.getProperty("user.dir")+"/Data/users.csv"));
            int IDCounter = scn.nextInt();
            if (IDCounter > 4) {
                throw new LimitExceededException("More than 5 users");
            }
            String newUserName = newNameTF.getText();
            String regex = "[a-zA-Z0-9]{1,16}";
            if (newUserName.matches(regex)) {
                for (User m : userList){
                    if (m.getUserName().equals(newUserName)){
                        throw new Exception("User already exists");
                    }
                }
                User t = new User(newUserName);
                userList.add(t);
                for (User m: userList){
                    System.out.println(m.getUserName());
                }
                Stage a = (Stage) newNameTF.getScene().getWindow();
                a.close();
                EnterController.rewriteUsers();
                Alert b = new Alert(Alert.AlertType.INFORMATION);
                b.setHeaderText("User Successfully created");
                b.setContentText("User " + t.getUserName() + " successfully created.");
            }
            else{
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Invalid username");
                a.setContentText("Username can only be 1-16 alphanumeric characters.");
                a.showAndWait();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (LimitExceededException limitExceededException) {
            Alert a  = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Error");
            a.setContentText("Sorry, only a maximum of 5 users can be created at this time.");
            a.showAndWait();
        } catch (Exception x){
            if (x.getMessage().equals("User already exists")){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Error");
                a.setContentText("User with name " + newNameTF.getText() + " already exists. ");
                a.showAndWait();

            }
        }
    }

    public void initialize(){
        userList = EnterController.getUserList();
    }

}
