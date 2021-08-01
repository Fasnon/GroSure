package Controller;

import Model.ShoppingList;
import Model.ShoppingTrip;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class AddTripListsController {
    @FXML
    public ComboBox<ShoppingList> listsComboBox;
    @FXML
    public Button addBtn;
    private static ShoppingTrip selected;

    @FXML
    public void addBtnAction(){
        if (listsComboBox.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Choose a list in the combo box");
            a.setContentText("Check and try again");
            a.showAndWait();
        }
        else{
            boolean in = false;
            for (ShoppingList l: selected.getListsUsed()){
                if (l.getID() == listsComboBox.getSelectionModel().getSelectedItem().getID()){
                    in = true;
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText("List is already incorporated in the trip");
                    a.setContentText("Check and try again");
                    a.showAndWait();
                }
            }
            if (!in){
                selected.getListsUsed().add(listsComboBox.getSelectionModel().getSelectedItem());
                Stage s = (Stage) addBtn.getScene().getWindow();
                TripsTabController.writeTrips();
                TripsTabController.loadTrips();
                TripsTabController.setSelected(selected);
                s.close();
            }
        }
    }

    public void initialize(){
        selected = TripsTabController.getSelected();
        listsComboBox.setItems(ListTabController.getListList());
    }
}
