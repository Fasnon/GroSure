package Controller;

import Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;

public class CreateItemController {
    @FXML
    public TextField itemNameTF;
    @FXML
    public TextField quantityTF;
    @FXML
    public TextField priceTF;
    @FXML
    public ComboBox<String> discountsOffersComboB;
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
    public Button createItemBtn;
    @FXML
    public ComboBox<ItemCategory> itemCategoryComboB;


    private static ObservableList<ShoppingItem> itemList = FXCollections.observableArrayList();
    private static ArrayList<ItemCategory> categoryList;


    @FXML
    public void createItemBtnAction(ActionEvent e){
        if (itemNameTF.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Name not set");
            a.setContentText("Set the name in the Text Field and try again");
            a.showAndWait();
        }
        else if (itemNameTF.getText().contains(",")){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Name invalid");
            a.setContentText("Name cannot contain a , ");
            a.showAndWait();

        }
        else if (quantityTF.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Quantity not set");
            a.setContentText("Set the quantity in the Text Field and try again");
            a.showAndWait();
        }
        else if (priceTF.getText().isEmpty()){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Price not set");
            a.setContentText("Set the price in the Text Field and try again");
            a.showAndWait();
        }
        else if (itemCategoryComboB.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Category not set");
            a.setContentText("Choose category in the combo box and try again");
            a.showAndWait();
        }
        else if (priceTF.getText().matches("[$].*")){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Error with Price Text Field");
            a.setContentText("Enter price without the dollar symbol");
            a.showAndWait();

        }
        else{
            boolean conti = true;
            for (ShoppingItem i: itemList){
                if (i.getName().equals(itemNameTF.getText()) && i.getQuantityName().equals(quantityTF.getText())){
                    conti = false;
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setHeaderText("Item already in the List!");
                    a.setContentText("item with name " + itemNameTF.getText() + ", and quantity " + i.getQuantityName()+ " already inside the list");
                    a.showAndWait();
                }
            }
            if (conti) {
                if (discountsOffersComboB.getSelectionModel().getSelectedItem() == null || discountsOffersComboB.getSelectionModel().getSelectedItem().equals("None")) {
                    ShoppingItem newItem = new ShoppingItem(itemNameTF.getText(), Double.parseDouble(priceTF.getText()), quantityTF.getText());
                    newItem.setCategory(itemCategoryComboB.getSelectionModel().getSelectedItem());
                    ItemsTabController.getItemList().add(newItem);
                    Stage s = (Stage) itemNameTF.getScene().getWindow();
                    s.close();


                } else if (discountsOffersComboB.getSelectionModel().getSelectedItem().equals("Promotion")) {
                    if (Double.parseDouble(originalPriceTF.getText()) < Double.parseDouble(priceTF.getText())){
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Discounted price cannot be higher than original price");
                        a.setHeaderText("Error with discount");
                        a.showAndWait();
                    }
                    else {
                        PriceEffects discount = new DiscountPriceEffects( Double.parseDouble(originalPriceTF.getText()));
                        ShoppingItem newItem = new ShoppingItem(itemNameTF.getText(), Double.parseDouble(priceTF.getText()), quantityTF.getText());
                        newItem.setCategory(itemCategoryComboB.getSelectionModel().getSelectedItem());
                        newItem.setPriceEffects(discount);
                        ItemsTabController.getItemList().add(newItem);
                        Stage s = (Stage) itemNameTF.getScene().getWindow();
                        s.close();
                    }
                } else if (discountsOffersComboB.getSelectionModel().getSelectedItem().equals("Buy X for Y")) {
                    if (Double.parseDouble(requirementTF.getText()) <= 1){
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Discount requirement can only be 2 or greater");
                        a.setHeaderText("Error with discount");
                        a.showAndWait();
                        Stage s = (Stage) itemNameTF.getScene().getWindow();
                        s.close();

                    }
                    else {
                        PriceEffects discount = new BuyXPriceEffects(Double.parseDouble(requirementTF.getText()), Double.parseDouble(newPriceTF.getText()));
                        ShoppingItem newItem = new ShoppingItem(itemNameTF.getText(), Double.parseDouble(priceTF.getText()), quantityTF.getText());
                        newItem.setCategory(itemCategoryComboB.getSelectionModel().getSelectedItem());
                        newItem.setPriceEffects(discount);
                        ItemsTabController.getItemList().add(newItem);
                        Stage s = (Stage) itemNameTF.getScene().getWindow();
                        s.close();
                    }
                }
            }
            ItemsTabController.writeItems();
            ListTabController.writeLists();
            ListTabController.loadLists();
            ItemsTabController.loadItems();
            TripsTabController.loadTrips();

        }

    }


    public void initialize(){
        categoryList = ItemsTabController.getCategoryList();
        itemList = ItemsTabController.getItemList();
        itemCategoryComboB.getItems().addAll(categoryList);
        discountsOffersComboB.getItems().addAll("None", "Promotion", "Buy X for Y");
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
