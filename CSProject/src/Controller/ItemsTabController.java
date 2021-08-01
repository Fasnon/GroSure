package Controller;

import Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

//import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class ItemsTabController {
    @FXML
    public TableView itemTableView;
    @FXML
    public TableColumn nameTableC;
    @FXML
    public TableColumn priceTableC;
    @FXML
    public TableColumn discountsTableC;
    @FXML
    public TableColumn quantityTableC;
    @FXML
    public Button createItemBtn;
    @FXML
    public Button loadBtn;
    @FXML
    public Button editItemBtn;
    @FXML
    public ComboBox<ItemCategory> categoryComboBox;
    @FXML
    public Label mostExpensiveLbl;
    @FXML
    public Label leastExpensiveLbl;
    @FXML
    public Label lastModLbl;
    @FXML
    public Label totalLbl;

    private static ObservableList<ShoppingItem> itemList = FXCollections.observableArrayList();
    private static ArrayList<ItemCategory> categoryList;

    public void createItemBtnAction(ActionEvent e) throws IOException{
        Stage createItemStage = new Stage();
        createItemStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLLoader.load(getClass().getResource("/View/CreateItem.fxml"));
        createItemStage.setScene(new Scene(root, 600,400));
        createItemStage.getIcons().add(new Image("file:" + System.getProperty("user.dir") + "/resources/GroSureMini.png"));
        createItemStage.setResizable(false);
        createItemStage.setTitle("Create a new item");
        createItemStage.showAndWait();

        itemTableView.setItems(null);
        itemTableView.setItems(itemList);
    }

    public void editItemBtnAction(ActionEvent e) throws IOException{
        Stage createItemStage = new Stage();
        createItemStage.initModality(Modality.APPLICATION_MODAL);
        Parent root = FXMLLoader.load(getClass().getResource("/View/EditItem.fxml"));
        createItemStage.setScene(new Scene(root, 600,400));
        createItemStage.getIcons().add(new Image("file:" + System.getProperty("user.dir") + "/resources/GroSureMini.png"));
        createItemStage.setResizable(false);
        createItemStage.setTitle("Edit an item");
        createItemStage.showAndWait();


        itemTableView.setItems(null);
        itemTableView.setItems(itemList);

    }

    public static ObservableList<ShoppingItem> getItemList() {
        return itemList;
    }

    public static ArrayList<ItemCategory> getCategoryList() {
        return categoryList;
    }

    @FXML
    public void hyperAction(){

    }

    public static void loadItems(){
        itemList.clear();
        try {

            Scanner scn = new Scanner(new File(System.getProperty("user.dir") + "/Data/FruitsVeg.csv"));
            while(scn.hasNextLine()){
                String[] items = scn.nextLine().split("[,]");
                ShoppingItem newItem = new ShoppingItem(items[0], Double.parseDouble(items[1]), items[3]);
                if (items[4].matches("(Buy).*")){
                    boolean u = false;
                    boolean s = false;
                    int start = 0, end = 0;
                    for (int x = 0; x < items[4].length(); x++) {
                        if (s && u) {
                            break;
                        }
                        if (Character.isDigit(items[4].charAt(x))) {
                            u = true;
                            start = x;
                        } else {
                            if (u)
                                s = true;
                            end = x;
                        }
                    }
                    int require = Integer.parseInt(items[4].substring(start, end));
                    String[] items2 = items[4].split("[$]");
                    int end2 = 0;
                    for (int x = 0; x < items2[1].length(); x++) {
                        if (items2[1].charAt(x) == " ".charAt(0)) {
                            end2 = x - 1;
                            break;
                        }
                    }
                    double discountPrice = Double.parseDouble(items2[1].substring(0, end2));
                    PriceEffects pe = new BuyXPriceEffects( require, discountPrice);
                    newItem.setPriceEffects(pe);
                }
                else if (items[4].matches("(UP: ).*")){
                    int index = items[4].indexOf("$");
                    String originalPrice = items[4].substring(index);
                    PriceEffects discount = new DiscountPriceEffects(Double.parseDouble(originalPrice.substring(1)));
                    newItem.setPriceEffects(discount);
                }
                newItem.setCategory(new ItemCategory(items[2], "FruitsVeg"));
                itemList.add(newItem);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public void loadBtnAction(ActionEvent e)throws IOException {
        if (categoryComboBox.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Select a Category");
            a.showAndWait();
        }
        else {
            Scanner scn = new Scanner(new File(System.getProperty("user.dir") + "/Data/FairpriceWebsiteLoad.txt"));
            boolean cont = false;
            int counter = 0;


            String regex = "((Buy)|(Any))[A-Za-z0-9 $,.]+";
            PriceEffects offer = null;
            while (scn.hasNextLine()) {
                String t = scn.nextLine();
                if (t.equals("Help")) {
                    cont = false;
                }
                if (t.equals("Offer")) {
                    cont = true;
                    continue;
                }
                if (t.equals("Out of stock") || t.equals("OfferNew") || t.equals("New")) {
                    continue;
                }
                if (t.equals("Must Buy")) {
                    continue;
                }
                if (t.matches(regex)) {
                    boolean u = false;
                    boolean s = false;
                    int start = 0, end = 0;
                    for (int x = 0; x < t.length(); x++) {
                        if (s && u) {
                            break;
                        }
                        if (Character.isDigit(t.charAt(x))) {
                            u = true;
                            start = x;
                        } else {
                            if (u)
                                s = true;
                            end = x;
                        }
                    }
                    int require = Integer.parseInt(t.substring(start, end));
                    String[] items2 = t.split("[$]");
                    int end2 = 0;
                    for (int x = 0; x < items2[1].length(); x++) {
                        if (items2[1].charAt(x) == " ".charAt(0)) {
                            end2 = x - 1;
                            break;
                        }
                    }
                    double discountPrice = Double.parseDouble(items2[1].substring(0, end2));
                    offer = new BuyXPriceEffects(require, discountPrice);
                    itemList.get(itemList.size() - 1).setPriceEffects(offer);
                } else if (cont) {
                    double price = 0;
                    String itemName = t;
                        String priceLine = scn.nextLine();
                        String[] items = priceLine.split("[$]");
                        PriceEffects discount = null;
                        if (items.length == 2) {
                            price = Double.parseDouble(items[1]);
                        } else if (items.length == 3) {
                            price = Double.parseDouble(items[1]);
                            discount = new DiscountPriceEffects(Double.parseDouble(items[2]));
                        }
                        scn.nextLine();

                        String quantity = scn.nextLine();
                        boolean conti = true;
                        for (ShoppingItem i : itemList) {
                            if (i.getName().equals(itemName) && i.getQuantityName().equals(quantity)) {
                                ++counter;
                                conti = false;
                            }
                        }
                        if (conti) {
                            ShoppingItem newItem = new ShoppingItem(itemName, price, quantity);
                            if (discount != null) {
                                newItem.setPriceEffects(discount);
                            }
                            newItem.setCategory(categoryComboBox.getSelectionModel().getSelectedItem());

                            if (!(itemName.contains(",")||itemName.isEmpty())) {
                                itemList.add(newItem);
                            }
                            if (itemName.contains(",")){
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setHeaderText( "Item contains illegal characters");
                                a.setContentText(itemName + " cannot be added to GroSure");
                                a.showAndWait();
                            }
                        }
                    }
            }

            if (counter!= 0) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("Item already in the List!");
                a.setContentText(counter + " items were already inside the list, and could not be loaded");
                a.showAndWait();
            }
            writeItems();
        }
    }

    public static void writeItems(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH/mm");
        for (ItemCategory c: categoryList){
            String path = System.getProperty("user.dir") + "/Data/" + c.getPathName() + ".csv";
            try{
                PrintWriter pw = new PrintWriter(path);
                for (ShoppingItem i : itemList){
                    if (i.getCategory().getName().equals(c.getName())) {
                            pw.println(i.getName() + "," + i.getPrice() + "," + i.getCategory().getName()+ "," + i.getQuantityName() + "," + i.getPriceEffectsString()+ " ,");
                        }
                    }
                pw.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }


    public void initialize(){
        try {
            itemList = FXCollections.observableArrayList();
            itemList.addListener(new ListChangeListener<ShoppingItem>() {
                @Override
                public void onChanged(Change<? extends ShoppingItem> change) {
                    totalLbl.setText("Total " + itemList.size() + " items in database.");
                    ShoppingItem spi = new ShoppingItem("", 0, "");
                    ShoppingItem shopi = new ShoppingItem("", Double.MAX_VALUE, "");
                    for (ShoppingItem si: itemList){
                        if (si.getPrice() > spi.getPrice()){
                            spi = si;
                        }
                        if (si.getPrice()<shopi.getPrice()){
                            shopi = si;
                        }
                    }
                    mostExpensiveLbl.setText("The most expensive item in the list is the " +spi.getName() + " which costs " + spi.getPriceString() + " per " + spi.getQuantityName());
                    leastExpensiveLbl.setText("The least expensive item in the list is the " +shopi.getName() + " which costs " + shopi.getPriceString() + " per " + shopi.getQuantityName());

                    if (spi.getName().equals("") && spi.getQuantityName().equals("")) {
                        mostExpensiveLbl.setText("");
                    }

                    if (shopi.getName().equals("") && shopi.getQuantityName().equals("")) {
                        leastExpensiveLbl.setText("");
                    }

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                    lastModLbl.setText("Last modified on: " + sdf.format(new Date()));


                }
            });

            Scanner scn = new Scanner(new File(System.getProperty("user.dir") + "/Data/FruitsVeg.csv"));
            while(scn.hasNextLine()){
                String[] items = scn.nextLine().split("[,]");
                ShoppingItem newItem = new ShoppingItem(items[0], Double.parseDouble(items[1]), items[3]);
                if (items[4].matches("(Buy).*")){
                    boolean u = false;
                    boolean s = false;
                    int start = 0, end = 0;
                    for (int x = 0; x < items[4].length(); x++) {
                        if (s && u) {
                            break;
                        }
                        if (Character.isDigit(items[4].charAt(x))) {
                            u = true;
                            start = x;
                        } else {
                            if (u)
                                s = true;
                            end = x;
                        }
                    }
                    int require = Integer.parseInt(items[4].substring(start, end));
                    String[] items2 = items[4].split("[$]");
                    int end2 = 0;
                    for (int x = 0; x < items2[1].length(); x++) {
                        if (items2[1].charAt(x) == " ".charAt(0)) {
                            end2 = x - 1;
                            break;
                        }
                    }
                    double discountPrice = Double.parseDouble(items2[1].substring(0, end2));
                    PriceEffects pe = new BuyXPriceEffects( require, discountPrice);
                    newItem.setPriceEffects(pe);
                }
                else if (items[4].matches("(UP: ).*")){
                    int index = items[4].indexOf("$");
                    String originalPrice = items[4].substring(index);
                    PriceEffects discount = new DiscountPriceEffects(Double.parseDouble(originalPrice.substring(1)));
                    newItem.setPriceEffects(discount);
                }
                newItem.setCategory(new ItemCategory(items[2], "FruitsVeg"));
                itemList.add(newItem);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        categoryList = new ArrayList<ItemCategory>();
        categoryList.add(new ItemCategory("Fruits & Vegetables","FruitsVeg"));

        categoryComboBox.getItems().addAll(categoryList);
        categoryComboBox.valueProperty().addListener(new ChangeListener<ItemCategory>() {
            @Override
            public void changed(ObservableValue<? extends ItemCategory> observableValue, ItemCategory itemCategory, ItemCategory t1) {
                itemTableView.setItems(itemList);
            }
        });



        nameTableC.setCellValueFactory(new PropertyValueFactory<ShoppingItem, String>("name"));
        discountsTableC.setCellValueFactory(new PropertyValueFactory<ShoppingItem, String>("priceEffectsString"));
        priceTableC.setCellValueFactory(new PropertyValueFactory<ShoppingItem, String>("priceString"));
        quantityTableC.setCellValueFactory(new PropertyValueFactory<ShoppingItem, String>("quantityName"));
    }
}