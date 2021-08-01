package Controller;

import Model.ItemWithinList;
import Model.ShoppingItem;
import Model.ShoppingList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class EditListItemController {
    @FXML
    public ComboBox<ItemWithinList> itemsComboBox;
    @FXML
    public TextField quantityTF;
    @FXML
    public Button editItemBtn;
    @FXML
    public Button deleteItemBtn;
    @FXML
    public Label discountsLbl;
    @FXML
    public Label priceLbl;
    private ShoppingList l;

    @FXML
    public void editItemBtnAction(){
        if (itemsComboBox.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Select an item in the Combo box");
            a.setContentText("Choose an item and try again");
            a.showAndWait();
        }
        else {
            try {
                itemsComboBox.getSelectionModel().getSelectedItem().setQuantity(Integer.parseInt(quantityTF.getText()));
                Stage s = (Stage) itemsComboBox.getScene().getWindow();
                ItemsTabController.writeItems();
                ItemsTabController.loadItems();
                ListTabController.writeLists();
                ListTabController.loadLists();
                TripsTabController.writeTrips();
                TripsTabController.loadTrips();
                s.close();
            } catch (NumberFormatException ne) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Quantity can only be an integer");
                a.setContentText("Check and try again");
                a.showAndWait();
            }
        }

    }

    @FXML
    public void deleteItemBtnAction(){
        if (itemsComboBox.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Select an item in the Combo box");
            a.setContentText("Choose an item and try again");
            a.showAndWait();
        }
        else{
            l.getItemList().remove(itemsComboBox.getSelectionModel().getSelectedItem());
            for (ShoppingList sl: ListTabController.getListList()){
                for (ItemWithinList si: sl.getItemList()){
                    if (si.getName().equals(itemsComboBox.getSelectionModel().getSelectedItem().getName())){
                        sl.getItemList().remove(si);
                        ListTabController.writeLists();
                    }
                }
            }
            Stage s = (Stage) itemsComboBox.getScene().getWindow();
            s.close();
            ItemsTabController.writeItems();
            ItemsTabController.loadItems();
            ListTabController.writeLists();
            ListTabController.loadLists();
            TripsTabController.writeTrips();
            TripsTabController.loadTrips();
        }

    }

    public void initialize(){
        l = ListTabController.getSelectedList();
        boolean go = true;
        if (l.getItemList() == null || l.getItemList().size() == 0){
            go = false;

                Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("No items in the list");
            a.setContentText("Add some items and try again");
            a.show();
            try {
                Stage s = (Stage) deleteItemBtn.getScene().getWindow(); //
                s.close();
            }
            catch ( Exception ex){
            }

        }
        ObservableList<ItemWithinList> il = FXCollections.observableArrayList();
        il.addAll(l.getItemList());
        itemsComboBox.setItems(il);
        itemsComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemWithinList>() {
            @Override
            public void changed(ObservableValue<? extends ItemWithinList> observableValue, ItemWithinList itemWithinList, ItemWithinList t1) {
                        priceLbl.setText(t1.getItem().getPriceString());
                        discountsLbl.setText(t1.getItem().getPriceEffectsString());
                    }
                });
            }
}
