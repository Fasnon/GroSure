package Controller;

import Model.ItemWithinList;
import Model.ShoppingItem;
import Model.ShoppingList;
import Model.ShoppingTrip;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.naming.LimitExceededException;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

public class ListTabController {
    @FXML
    public Label netPriceLbl;
    @FXML
    public TableView listItemsTable;
    @FXML
    public TableColumn itemQuantityTableC;
    @FXML
    public ImageView copyPasteImage;
    @FXML
    public TableColumn itemNameTableC;
    @FXML
    public TableColumn itemPriceTableC;
    @FXML
    public TableColumn itemTotalPriceTableC;
    @FXML
    public Button addListItemsBtn;
    @FXML
    public Button editListItemBtn;
    @FXML
    public Label listItemsLbl;
    @FXML
    public Button deleteListBtn;
    @FXML
    public Button createBtn;
    @FXML
    public TableView listTable;
    @FXML
    public TableColumn listIDTableC;
    @FXML
    public TableColumn listNameTableC;
    @FXML
    public TableColumn listPriceTableC;
    @FXML
    public Label listSelectedLbl;
    @FXML
    public TextField listNameTF;
    @FXML
    public TextArea copyTA;
    @FXML
    public Button copyToClipboardBtn;

    private static ObservableList<ShoppingList> listList;
    private static ShoppingList selectedList;

    @FXML
    public void newListBtnAction(){
        createBtn.setVisible(true);
        listNameTF.setVisible(true);
    }

    public static ObservableList<ShoppingList> getListList() {
        return listList;
    }

    @FXML
    public void createBtnAction(){
        try{
            if (listNameTF.getText().contains(",")){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Name invalid");
                a.setContentText("Name cannot contain a , ");
                a.showAndWait();

            }
            else {
                Scanner IDC = new Scanner(new File(System.getProperty("user.dir") + "/Data/lists.csv"));
                int IDCounter = IDC.nextInt();
                int ID = IDCounter++;
                ShoppingList l = new ShoppingList(listNameTF.getText(), new ArrayList<ItemWithinList>(), ID);

                listList.add(l);
                PrintWriter pw = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/Data/lists.csv")); //Prints to venues.csv details in required format
                pw.println(IDCounter);

                for (ShoppingList ll : listList) {
                    String s = "";
                    for (ItemWithinList i : ll.getItemList()) {
                        s = s + "," + i.getName() + "|" + i.getQuantity();
                    }
                    pw.println(ll.getID() + "," + ll.getName() + s);
                }
                pw.close();
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        createBtn.setVisible(false);
        listNameTF.setVisible(false);

    }

    @FXML
    public void deleteListBtnAction(){
        try{
            for (ShoppingTrip t: TripsTabController.getTripList()){
                ShoppingList slist = (ShoppingList) listTable.getSelectionModel().getSelectedItem();
                for (ShoppingList sl : t.getListsUsed()){
                    if (sl.getID() == slist.getID()){
                        t.getListsUsed().remove(sl);

                    }
                }
            }
            listList.remove(listTable.getSelectionModel().getSelectedItem());

            writeLists();
        }
        catch(NullPointerException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Choose a list to delete!");
            a.setContentText("Choose a list in the table to delete");
            a.showAndWait();
        }
    }

    @FXML
    public void addListItemsBtnAction() {
        try {
            if (listTable.getSelectionModel().getSelectedItem() == null){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("No list selected");
                a.setContentText("Select a list and try again");
                a.showAndWait();
            }
            else {
                Stage createItemStage = new Stage();
                createItemStage.initModality(Modality.APPLICATION_MODAL);
                Parent root = FXMLLoader.load(getClass().getResource("/View/AddListItems.fxml"));
                createItemStage.setScene(new Scene(root, 600, 400));
                createItemStage.getIcons().add(new Image("file:" + System.getProperty("user.dir") + "/resources/GroSureMini.png"));
                createItemStage.setResizable(false);
                createItemStage.setTitle("Add an item to the list");
                createItemStage.showAndWait();
                refresh();
                netPriceLbl.setText("Net Price: " + selectedList.getStringNetPrice());
                writeLists();
            }
        }
        catch(Exception e){
        }
    }


    @FXML
    public void editListItemBtnAction() {
        try {
            if (listTable.getSelectionModel().getSelectedItem() == null){
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("No list selected");
                a.setContentText("Select a list and try again");
                a.showAndWait();
            }
            else {
                Stage createItemStage = new Stage();
                createItemStage.initModality(Modality.APPLICATION_MODAL);
                Parent root = FXMLLoader.load(getClass().getResource("/View/EditListItem.fxml"));
                createItemStage.setScene(new Scene(root, 600, 400));
                createItemStage.getIcons().add(new Image("file:" + System.getProperty("user.dir") + "/resources/GroSureMini.png"));
                createItemStage.setResizable(false);
                createItemStage.setTitle("Edit an Item in the list");
                createItemStage.showAndWait();
                netPriceLbl.setText("Net Price: " + selectedList.getStringNetPrice());
                refresh();
                writeLists();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void refresh(){
        ObservableList<ItemWithinList> il = FXCollections.observableArrayList();
        listItemsTable.getItems().clear();
        il.addAll(selectedList.getItemList());
        listItemsTable.setItems(il);
        listItemsTable.refresh();
    }

    public static void writeLists(){
        try{
            Scanner IDC = new Scanner(new File(System.getProperty("user.dir") + "/Data/lists.csv"));
            int IDCounter = IDC.nextInt();

            PrintWriter pw = new PrintWriter(new FileOutputStream(System.getProperty("user.dir") + "/Data/lists.csv")); //Prints to venues.csv details in required format
            pw.println(IDCounter);

            for (ShoppingList ll: listList) {
                String s = "";
                for (ItemWithinList i : ll.getItemList()){
                    s = s + ","+ i.getName()  + "|" + i.getQuantity();
                }
                pw.println(ll.getID() + "," + ll.getName() + s);
            }
            pw.close();
        }
        catch (IOException i){
            i.printStackTrace();
        }
    }

    public static ShoppingList getSelectedList() {
        return selectedList;
    }

    public static void loadLists(){
        listList.clear();
        try {
            Scanner scn = new Scanner(new File(System.getProperty("user.dir") + "/Data/lists.csv"));
            scn.nextLine();
            while (scn.hasNextLine()) {
                String item[] = scn.nextLine().split(",");
                int t = item.length;
                if (t == 2) {
                    listList.add(new ShoppingList(item[1], new ArrayList<ItemWithinList>(), Integer.parseInt(item[0])));
                } else {
                    ArrayList<ItemWithinList> a = new ArrayList<ItemWithinList>();
                    for (int x = 2; x < t; x++) {
                        String items[] = item[x].split("[|]");
                        for (ShoppingItem si : ItemsTabController.getItemList()) {
                            if (si.getName().equals(items[0])) {
                                a.add(new ItemWithinList(si, Integer.parseInt(items[1])));
                            }
                        }
                    }
                    ShoppingList sli = new ShoppingList(item[1], a, Integer.parseInt(item[0]));
                    listList.add(sli);
                }
            }
        }
        catch (IOException  e){
            e.printStackTrace();
        }
    }

    public void copytoClipboardBtnAction(){
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(copyTA.getText()), new StringSelection(copyTA.getText()));
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Success");
        a.setContentText("List Contents saved to clipboard");
        a.showAndWait();
    }

    public void initialize(){
        editListItemBtn.setDisable(true);
        addListItemsBtn.setDisable(true);
        listList = FXCollections.observableArrayList();
        copyPasteImage.setImage(new Image("file:" + System.getProperty("user.dir") + "/resources/copyPaste.png"));
        try {
            Scanner scn = new Scanner(new File(System.getProperty("user.dir") + "/Data/lists.csv"));
            scn.nextLine();

            while (scn.hasNextLine()) {
                String item[] = scn.nextLine().split(",");
                int t = item.length;
                if (t == 2) {
                    listList.add(new ShoppingList(item[1], new ArrayList<ItemWithinList>(), Integer.parseInt(item[0])));
                } else {
                    ArrayList<ItemWithinList> a = new ArrayList<ItemWithinList>();
                    for (int x = 2; x < t; x++) {
                        String items[] = item[x].split("[|]");
                        for (ShoppingItem si : ItemsTabController.getItemList()) {
                            if (si.getName().equals(items[0])) {
                                a.add(new ItemWithinList(si, Integer.parseInt(items[1])));
                            }
                        }
                    }
                    ShoppingList sli = new ShoppingList(item[1], a, Integer.parseInt(item[0]));
                    listList.add(sli);
                }
            }
        }
        catch (IOException  e){
            e.printStackTrace();
        }
        listItemsTable.refresh();


        listNameTableC.setCellValueFactory(new PropertyValueFactory<ShoppingList, String>("name"));
        listIDTableC.setCellValueFactory(new PropertyValueFactory<ShoppingList, Integer>( "ID"));
        listPriceTableC.setCellValueFactory(new PropertyValueFactory<ShoppingList, String>("stringNetPrice"));
        listTable.setItems(listList);


        itemQuantityTableC.setCellValueFactory(new PropertyValueFactory<ItemWithinList, Integer>("quantity"));
        itemNameTableC.setCellValueFactory(new PropertyValueFactory<ItemWithinList, String>("name"));
        itemPriceTableC.setCellValueFactory(new PropertyValueFactory<ItemWithinList, Double>("price"));
        itemTotalPriceTableC.setCellValueFactory(new PropertyValueFactory<ItemWithinList, Double>("stringTotalPrice"));

        listTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                copyTA.clear();
                if (t1!= null){
                    editListItemBtn.setDisable(false);
                    addListItemsBtn.setDisable(false);
                    ShoppingList l = (ShoppingList) t1;
                    selectedList = l;
                    netPriceLbl.setText("Net Price: " + l.getStringNetPrice());
                    listSelectedLbl.setText("Current List selected: ID is " + l.getID());
                    ObservableList<ItemWithinList> il = FXCollections.observableArrayList();
                    il.addAll(l.getItemList());
                    listItemsTable.setItems(il);
                    listItemsTable.refresh();
                    copyTA.appendText("List: " +  selectedList.getName() +"\n");
                    for (ItemWithinList a: selectedList.getItemList()){
                        copyTA.appendText(a.getName()  + ", " + a.getItem().getQuantityName() + ": $" + String.format("%.2f",a.getPrice()) + " x " + a.getQuantity() + " = " + a.getStringTotalPrice() + "\n");

                    }
                    copyTA.appendText("\n");
                    copyTA.appendText("Total price: " + selectedList.getStringNetPrice());
                    copyTA.appendText("\nMade by GroSure.");


                }
            }
        });


    }
}
