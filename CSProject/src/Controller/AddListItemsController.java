package Controller;

import Model.ItemWithinList;
import Model.ShoppingItem;
import Model.ShoppingList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddListItemsController {
    @FXML
    public ComboBox<ShoppingItem> itemsComboBox;
    @FXML
    public TextField quantityTF;
    @FXML
    public Button addItemBtn;
    @FXML
    public Label discountsLbl;
    @FXML
    public Label priceLbl;
    private ShoppingList l;

    @FXML
    public void addItemBtnAction(){
        boolean cont = true;
        if (itemsComboBox.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Select an item in the Combo box");
            a.setContentText("Choose an item and try again");
            a.showAndWait();
        }
        else{
            for (ItemWithinList il : l.getItemList()) {
                if (il.getItem().getName().equals(itemsComboBox.getSelectionModel().getSelectedItem().getName())) {
                    cont = false;
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText("Item already inside list");
                    a.setContentText("Use the edit button instead");
                    a.showAndWait();
                }
            }
            if (cont){
                try{
                    l.getItemList().add(new ItemWithinList(itemsComboBox.getSelectionModel().getSelectedItem(), Integer.parseInt(quantityTF.getText())));
                    Stage s = (Stage) quantityTF.getScene().getWindow();
                    ItemsTabController.writeItems();
                    ItemsTabController.loadItems();
                    ListTabController.writeLists();
                    ListTabController.loadLists();
                    TripsTabController.writeTrips();
                    TripsTabController.loadTrips();
                    s.close();
                }
                catch (NumberFormatException ne){
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setHeaderText("Quantity can only be an integer");
                    a.setContentText("Check and try again");
                    a.showAndWait();

                }
            }
        }
    }


    public void initialize(){
        l = ListTabController.getSelectedList();
        itemsComboBox.setItems(ItemsTabController.getItemList());
        itemsComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ShoppingItem>() {
            @Override
            public void changed(ObservableValue<? extends ShoppingItem> observableValue, ShoppingItem shoppingItem, ShoppingItem t1) {
                if (t1!= null) {
                    priceLbl.setText(t1.getPriceString());
                    discountsLbl.setText(t1.getPriceEffectsString());
                }
            }
        });
        }
    }
