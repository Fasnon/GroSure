package Controller;

import Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class EditItemController {
    @FXML
    public TextField itemNameTF;
    @FXML
    public TextField quantityTF;
    @FXML
    public TextField priceTF;
    @FXML
    public ComboBox discountsOffersComboB;
    @FXML
    public ComboBox<ShoppingItem> itemsComboBox;
    @FXML
    public Label originalPriceLbl;
    @FXML
    public TextField originalPriceTF;
    @FXML
    public Label buyXForYLbl;
    @FXML
    public TextField requirementTF;
    @FXML
    public TextField newPriceTF;
    @FXML
    public Button editItemBtn;
    @FXML
    public Button deleteItemBtn;
    @FXML
    public ComboBox itemCategoryComboB;

    private ShoppingItem itemSelected;
    private static ArrayList<ItemCategory> categoryList;

    @FXML
    public void editItemBtnAction(ActionEvent e) {
        if (itemSelected == null) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Select an item");
            a.setContentText("Select item in ComboBox to edit");
            a.showAndWait();
        } else {
            if (itemNameTF.getText().isEmpty() && quantityTF.getText().isEmpty() && priceTF.getText().isEmpty() && itemCategoryComboB.getSelectionModel().getSelectedItem() == null && discountsOffersComboB.getSelectionModel().getSelectedItem() == null) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("All fields are blank.");
                a.setContentText("Check fields and do again");
                a.showAndWait();
            } else {
                if (!itemNameTF.getText().isEmpty()) {
                   if (itemNameTF.getText().contains(",")){
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setHeaderText("Name invalid");
                        a.setContentText("Name cannot contain a , ");
                        a.showAndWait();

                    }
                    else {
                        itemSelected.setName(itemNameTF.getText());
                    }
                }

                if (!quantityTF.getText().isEmpty()) {
                    itemSelected.setQuantityName(quantityTF.getText());
                }
                if (!priceTF.getText().isEmpty()) {
                    itemSelected.setPrice(Double.parseDouble(priceTF.getText()));
                }
                if (itemCategoryComboB.getSelectionModel().getSelectedItem() != null) {
                    itemSelected.setCategory(new ItemCategory((String) itemCategoryComboB.getSelectionModel().getSelectedItem(), "FruitsVeg"));
                }

                if (discountsOffersComboB.getSelectionModel().getSelectedItem() == null || discountsOffersComboB.getSelectionModel().getSelectedItem().equals("None")) {
                } else if (discountsOffersComboB.getSelectionModel().getSelectedItem().equals("Promotion")) {
                    try {
                    if (Double.parseDouble(originalPriceTF.getText()) < Double.parseDouble(priceTF.getText())) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Discounted price cannot be higher than original price");
                        a.setHeaderText("Error with discount");
                        a.showAndWait();
                    } else {
                            PriceEffects discount = new DiscountPriceEffects(Double.parseDouble(originalPriceTF.getText()));
                            itemSelected.setPriceEffects(discount);
                        }
                    }
                    catch (NumberFormatException ex){
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setHeaderText("Error with the Format of the number");
                        a.setContentText("Check again and retry");
                        a.showAndWait();
                    }
                } else if (discountsOffersComboB.getSelectionModel().getSelectedItem().equals("Buy X for Y")) {
                    if (Double.parseDouble(requirementTF.getText()) <= 1) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Discount requirement can only be 2 or greater");
                        a.setHeaderText("Error with discount");
                        a.showAndWait();

                    } else {
                        PriceEffects discount = new BuyXPriceEffects( Double.parseDouble(requirementTF.getText()), Double.parseDouble(newPriceTF.getText()));
                        itemSelected.setPriceEffects(discount);
                    }
                }

            }
            ItemsTabController.writeItems();
            ItemsTabController.loadItems();
            ListTabController.writeLists();
            ListTabController.loadLists();
            TripsTabController.writeTrips();
            TripsTabController.loadTrips();
            Stage s = (Stage) itemNameTF.getScene().getWindow();
            s.close();

        }
    }

    @FXML
    public void deleteItemBtnAction(ActionEvent e){
        if (itemSelected == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Select an item");
            a.setContentText("Select item in ComboBox to delete");
            a.showAndWait();
        }
        else{
            ItemsTabController.getItemList().remove(itemSelected);
            ItemsTabController.writeItems();
            for (ShoppingList sl: ListTabController.getListList()){
                for (ItemWithinList si: sl.getItemList()){
                    if (si.getItem().getQuantityName().equals(itemSelected.getQuantityName()) && itemSelected.getName().equals(si.getName())){
                        sl.getItemList().remove(si);

                        ItemsTabController.writeItems();
                        ListTabController.writeLists();
                        ListTabController.loadLists();
                        ItemsTabController.loadItems();
                        TripsTabController.writeTrips();
                        TripsTabController.loadTrips();
                    }
                }
            }
            ItemsTabController.writeItems();
            ListTabController.writeLists();
            ListTabController.loadLists();
            ItemsTabController.loadItems();
            TripsTabController.writeTrips();
            TripsTabController.loadTrips();
            Stage s = (Stage) itemNameTF.getScene().getWindow();
            s.close();
        }

    }

    public void initialize(){
        categoryList = ItemsTabController.getCategoryList();
        itemCategoryComboB.getItems().addAll("Fruits & Vegetables");
        discountsOffersComboB.getItems().addAll("None", "Promotion", "Buy X for Y");
        itemSelected = null;
        itemsComboBox.getItems().addAll(ItemsTabController.getItemList());
        itemsComboBox.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                itemSelected = (ShoppingItem) t1;
            }
        });

        discountsOffersComboB.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.equals("Promotion")){
                    buyXForYLbl.setVisible(false);
                    newPriceTF.setVisible(false);
                    requirementTF.setVisible(false);
                    originalPriceLbl.setVisible(true);
                    originalPriceTF.setVisible(true);
                }
                if (t1.equals("Buy X for Y")){
                    buyXForYLbl.setVisible(true);
                    newPriceTF.setVisible(true);
                    requirementTF.setVisible(true);
                    originalPriceLbl.setVisible(false);
                    originalPriceTF.setVisible(false);

                }
                if (t1.equals("None")){
                    buyXForYLbl.setVisible(false);
                    newPriceTF.setVisible(false);
                    requirementTF.setVisible(false);
                    originalPriceLbl.setVisible(false);
                    originalPriceTF.setVisible(false);
                }
            }
        });
    }
}
