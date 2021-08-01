package Controller;

import Model.ItemWithinList;
import Model.ShoppingList;
import Model.ShoppingTrip;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class TripsTabController {
    @FXML
    public TableView tripTable;
    @FXML
    public TableColumn IDTableC;
    @FXML
    public TableColumn nameTableC;
    @FXML
    public TableColumn dateTableC;
    @FXML
    public TableColumn priceTableC;
    @FXML
    public TableView listTable;
    @FXML
    public TableColumn listIDTableC;
    @FXML
    public TableColumn listNameTableC;
    @FXML
    public TableColumn listPriceTableC;
    @FXML
    public PieChart pieChart;
    @FXML
    public Button newTripBtn;
    @FXML
    public Button editTripBtn;
    @FXML
    public Label dateLbl;
    @FXML
    public Button deleteTripBtn;
    @FXML
    public DatePicker datePicker;
    @FXML
    public Label newNameLbl;
    @FXML
    public TextField newNameTF;
    @FXML
    public Button saveChangesBtn;
    @FXML
    public Label tripSelectedLbl;
    @FXML
    public Label timeRemainingLbl;
    @FXML
    public Label timeLbl;
    @FXML
    public Label reminderLbl;
    @FXML
    public Label totalPriceLbl;
    @FXML
    public TextField timeTF;
    @FXML
    public Button addListBtn;
    @FXML
    public Button deleteListBtn;
    @FXML
    public Label detailsLbl;
    private boolean newEdit;
    private static ObservableList<ShoppingTrip> tripList;
    private static ShoppingTrip selected;
    private static ObservableList<ShoppingList> listsUsed;

    public static ObservableList<ShoppingTrip> getTripList() {
        return tripList;
    }

    public static ShoppingTrip getSelected() {
        return selected;
    }

    public static void setSelected(ShoppingTrip selected) {
        TripsTabController.selected = selected;
    }

    @FXML
    public void newTripBtnAction(ActionEvent e) {
            newEdit = true;
            saveChangesBtn.setVisible(true);
            newNameLbl.setVisible(true);
            newNameTF.setVisible(true);
            dateLbl.setVisible(true);
            datePicker.setVisible(true);
            timeLbl.setVisible(true);
            timeTF.setVisible(true);
    }



    @FXML
    public void editTripBtnAction(ActionEvent e){
        newEdit = false;
        saveChangesBtn.setVisible(true);
        newNameLbl.setVisible(true);
        newNameTF.setVisible(true);
        dateLbl.setVisible(true);
        datePicker.setVisible(true);
        timeLbl.setVisible(true);
        timeTF.setVisible(true);
    }

    @FXML
    public void saveChangesBtnAction(ActionEvent e){
        if (newEdit){
            if (datePicker.getValue() == null){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Choose a date for the trip!");
                a.setContentText("Check and try again");
                a.showAndWait();
            }
            else if (newNameTF.getText().isBlank()){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Choose a name for the trip!");
                a.setContentText("Check and try again");
                a.showAndWait();

            }
            else try {
                    if (timeTF.getText().isBlank()){
                        Scanner IDC = new Scanner(new File(System.getProperty("user.dir") + "/Data/trips.csv"));
                        int IDCounter = IDC.nextInt();
                        int ID = IDCounter++;
                        LocalDate d = datePicker.getValue();
                        Instant instant = Instant.from(d.atStartOfDay(ZoneId.systemDefault()));
                        Date date = Date.from(instant);
                        ShoppingTrip t = new ShoppingTrip(newNameTF.getText(), date, new ArrayList<ShoppingList>(), ID, MainController.getSelectedUser());
                        tripList.add(t);
                        PrintWriter pw = new PrintWriter(System.getProperty("user.dir") + "/Data/trips.csv");
                        pw.println(IDCounter);
                        pw.close();
                        datePicker.setValue(null);
                        newNameTF.clear();
                    }
                    else {
                        String st = timeTF.getText();
                        String[] items = st.split("[:]");
                        try {
                            if (items.length != 2 || Integer.parseInt(items[0]) > 23 || Integer.parseInt(items[0]) < 0 || Integer.parseInt(items[1]) < 0 || Integer.parseInt(items[1]) > 59) {
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setTitle("Invalid time format");
                                a.setHeaderText("Please input time in the format of HH:MM");
                                a.setContentText("Note time is in 24h clock");
                                a.showAndWait();
                            }
                            else if (newNameTF.getText().contains(",")){
                                Alert a = new Alert(Alert.AlertType.ERROR);
                                a.setHeaderText("Name invalid");
                                a.setContentText("Name cannot contain a , ");
                                a.showAndWait();

                            }
                            else {
                                Scanner IDC = new Scanner(new File( System.getProperty("user.dir") + "/Data/trips.csv"));
                                int IDCounter = IDC.nextInt();
                                int ID = IDCounter++;
                                LocalDate d = datePicker.getValue();
                                Instant instant = Instant.from(d.atStartOfDay(ZoneId.systemDefault()));
                                Date date = Date.from(instant);
                                date.setTime(date.getTime() + Integer.parseInt(items[0]) * 3600000 + Integer.parseInt(items[1]) * 60000);
                                ShoppingTrip t = new ShoppingTrip(newNameTF.getText(), date, new ArrayList<ShoppingList>(), ID, MainController.getSelectedUser());
                                tripList.add(t);
                                PrintWriter pw = new PrintWriter(System.getProperty("user.dir") + "/Data/trips.csv");
                                pw.println(IDCounter);
                                pw.close();
                                datePicker.setValue(null);
                                timeTF.clear();
                                newNameTF.clear();
                            }
                        }
                        catch (NumberFormatException ex){
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setTitle("Invalid time format");
                            a.setHeaderText("Please input time in the format of HH:MM");
                            a.setContentText("Note time is in 24h clock");
                            a.showAndWait();
                        }
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                    writeTrips();
            }
            catch (IOException ex){
            }
        }
        else{
            if (tripTable.getSelectionModel().getSelectedItem() == null){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Select a trip before making any edits");
                a.setContentText("Select a trip in the table and try again");
                a.showAndWait();
            }
            else if (newNameTF.getText().isBlank() && datePicker.getValue() == null && timeTF.getText().isBlank()){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("No changes detected");
                a.setContentText("Fill up a field and try again");
                a.showAndWait();
            }
            else if (newNameTF.getText().contains(",")){
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Name invalid");
                a.setContentText("Name cannot contain a , ");
                a.showAndWait();

            }
            else{
                if (!newNameTF.getText().isBlank()){
                    selected.setName(newNameTF.getText());
                    tripTable.refresh();
                }
                if (datePicker.getValue() != null && timeTF.getText().isBlank()){
                    LocalDate d = datePicker.getValue();
                    Instant instant = Instant.from(d.atStartOfDay(ZoneId.systemDefault()));
                    Date date = Date.from(instant);
                    selected.setDate(date);
                    tripTable.refresh();
                }
                if (datePicker.getValue() != null && !timeTF.getText().isBlank()){
                    String st = timeTF.getText();
                    String[] items = st.split("[:]");
                    if (items.length != 2 || Integer.parseInt(items[0]) > 23 || Integer.parseInt(items[0]) < 0 || Integer.parseInt(items[1]) < 0 || Integer.parseInt(items[1]) > 59) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Invalid time format");
                        a.setHeaderText("Please input time in the format of HH:MM");
                        a.setContentText("Note time is in 24h clock");
                        a.showAndWait();
                    }
                    else {
                        try {
                            LocalDate d = datePicker.getValue();
                            Instant instant = Instant.from(d.atStartOfDay(ZoneId.systemDefault()));
                            Date date = Date.from(instant);
                            date.setTime(date.getTime() + Integer.parseInt(items[0]) * 3600000 + Integer.parseInt(items[1]) * 60000);
                            selected.setDate(date);
                            tripTable.refresh();
                        }
                        catch (NumberFormatException nfe){
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setTitle("Invalid time format");
                            a.setHeaderText("Please input time in the format of HH:MM");
                            a.setContentText("Note time is in 24h clock");
                            a.showAndWait();
                        }
                    }
                }
                if (datePicker.getValue() == null && !timeTF.getText().isBlank()){
                    String st = timeTF.getText();
                    String[] items = st.split("[:]");
                    if (items.length != 2 || Integer.parseInt(items[0]) > 23 || Integer.parseInt(items[0]) < 0 || Integer.parseInt(items[1]) < 0 || Integer.parseInt(items[1]) > 59) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("Invalid time format");
                        a.setHeaderText("Please input time in the format of HH:MM");
                        a.setContentText("Note: time is in 24h clock");
                        a.showAndWait();
                    }
                    else {
                        try {
                            Date d = selected
                                    .getDate();
                            long days = d.getTime()/ 86400000;
                            d.setTime(days * 86400000 +  Integer.parseInt(items[0]) * 3600000 + Integer.parseInt(items[1]) * 60000 - 28800000);
                            selected.setDate(d);
                            tripTable.refresh();
                        }
                        catch (NumberFormatException nfe){
                            Alert a = new Alert(Alert.AlertType.ERROR);
                            a.setTitle("Invalid time format");
                            a.setHeaderText("Please input time in the format of HH:MM");
                            a.setContentText("Note: time is in 24h clock");
                            a.showAndWait();
                        }
                    }
                }
                writeTrips();

            }
        }
        saveChangesBtn.setVisible(false);
        newNameLbl.setVisible(false);
        newNameTF.setVisible(false);
        dateLbl.setVisible(false);
        datePicker.setVisible(false);
        timeLbl.setVisible(false);
        timeTF.setVisible(false);

    }

    public static void writeTrips() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            Scanner IDC = new Scanner(new File(System.getProperty("user.dir") + "/Data/trips.csv"));
            int ID = IDC.nextInt();
            PrintWriter pw = new PrintWriter(System.getProperty("user.dir") + "/Data/trips.csv");
            pw.println(ID);
            for (ShoppingTrip t : tripList) {
                String s = "";
                for (ShoppingList l : t.getListsUsed()) {
                    s += "," + l.getID();
                }
                pw.println(t.getName() + "," + sdf.format(t.getDate() )+ "," + t.getID() + "," + t.getUserBelongTo().getUserName() + s);
            }
            pw.close();
            IDC.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void deleteListBtnAction(){
        if (listTable.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("No list selected");
            a.setContentText("Choose a list within the trip and try again.");
            a.showAndWait();
        }
        else if (tripTable.getSelectionModel().getSelectedItem() == null){
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("No trip selected");
            a.setContentText("Select a trip in table and try again");
            a.showAndWait();
        }
        else{
            selected.getListsUsed().remove(listTable.getSelectionModel().getSelectedItem());
            listTable.getItems().remove(listTable.getSelectionModel().getSelectedItem());
            ObservableList<ShoppingList> ll = FXCollections.observableArrayList();
            ll.addAll(selected.getListsUsed());
            writeTrips();
            listTable.refresh();
            totalPriceLbl.setText("Total price: " + selected.getPrice());
            double totalPrice = 0;
            for (ShoppingList sl : selected.getListsUsed()) {
                totalPrice += sl.getNetPrice();
            }
            if (totalPrice == 0){
                reminderLbl.setVisible(true);
                pieChart.setVisible(false);
            }
            else{
                reminderLbl.setVisible(false);
                pieChart.setVisible(true);
                pieChart.getData().clear();
                ArrayList<ItemWithinList> ill = new ArrayList<ItemWithinList>();
                for (ShoppingList sl : selected.getListsUsed()) {
                    for (ItemWithinList il: sl.getItemList()){
                        boolean in = false;
                        for (ItemWithinList i: ill){
                            if (i.getItem().getName().equals(il.getItem().getName())){
                                i.setQuantity(i.getQuantity() + il.getQuantity());
                                in = true;
                            }
                        }
                        if (!in){
                            ill.add(il);
                        }
                    }
                }
                for (ItemWithinList iwl: ill) {
                    pieChart.getData().add(new PieChart.Data(iwl.getName(), iwl.getTotalPrice()));
                }

                TripsTabController.writeTrips();
                TripsTabController.loadTrips();
                TripsTabController.setSelected(selected);
            }
        }


        for(final PieChart.Data data : pieChart.getData()){

            data.getNode().addEventHandler(
                    javafx.scene.input.MouseEvent.MOUSE_ENTERED,
                    new EventHandler<javafx.scene.input.MouseEvent>() {

                        @Override
                        public void handle(javafx.scene.input.MouseEvent mouseEvent) {

                            String name = data.getName();
                            double value = data.getPieValue();
                            detailsLbl.setText(name + " : $" + String.format("%.2f" , value));
                        }
                    });

            data.getNode().addEventHandler(
                    javafx.scene.input.MouseEvent.MOUSE_EXITED,
                    new EventHandler<MouseEvent>() {

                        @Override
                        public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                            detailsLbl.setText("");
                        }
                    });
        }


        }


    @FXML
    public void addListBtnAction() {
        try {
            if (tripTable.getSelectionModel().getSelectedItem() == null) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("No trip selected");
                a.setContentText("Select a trip and try again");
                a.showAndWait();
            } else {
                Stage createItemStage = new Stage();
                createItemStage.initModality(Modality.APPLICATION_MODAL);
                Parent root = FXMLLoader.load(getClass().getResource("/View/AddTripLists.fxml"));
                createItemStage.setScene(new Scene(root, 500, 300));
                createItemStage.getIcons().add(new Image("file:" + System.getProperty("user.dir") + "/resources/GroSureMini.png"));
                createItemStage.setResizable(false);
                createItemStage.setTitle("Add a list to the trip");
                createItemStage.showAndWait();


                listTable.setItems(null);
                listTable.setItems(listsUsed);
                totalPriceLbl.setText("Total price: " + selected.getPrice());
                writeTrips();
                listTable.refresh();
                double totalPrice = 0;
                for (ShoppingList sl : selected.getListsUsed()) {
                    totalPrice += sl.getNetPrice();
                }
                if (totalPrice == 0) {
                    reminderLbl.setVisible(true);
                    pieChart.setVisible(false);
                } else {
                    reminderLbl.setVisible(false);
                    pieChart.setVisible(true);
                    pieChart.getData().clear();
                    ArrayList<ItemWithinList> ill = new ArrayList<ItemWithinList>();
                    for (ShoppingList sl : selected.getListsUsed()) {
                        for (ItemWithinList il : sl.getItemList()) {
                            boolean in = false;
                            for (ItemWithinList i : ill) {
                                if (i.getItem().getName().equals(il.getItem().getName())) {
                                    i.setQuantity(i.getQuantity() + il.getQuantity());
                                    in = true;
                                }
                            }
                            if (!in) {
                                ill.add(il);
                            }
                        }
                    }
                    for (ItemWithinList iwl : ill) {
                        pieChart.getData().add(new PieChart.Data(iwl.getName(), iwl.getTotalPrice()));
                    }
                }


                for (final PieChart.Data data : pieChart.getData()) {

                    data.getNode().addEventHandler(
                            javafx.scene.input.MouseEvent.MOUSE_ENTERED,
                            new EventHandler<javafx.scene.input.MouseEvent>() {

                                @Override
                                public void handle(javafx.scene.input.MouseEvent mouseEvent) {

                                    String name = data.getName();
                                    double value = data.getPieValue();
                                    detailsLbl.setText(name + " : $" + String.format("%.2f", value));
                                }
                            });

                    data.getNode().addEventHandler(
                            javafx.scene.input.MouseEvent.MOUSE_EXITED,
                            new EventHandler<MouseEvent>() {

                                @Override
                                public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                                    detailsLbl.setText("");
                                }
                            });
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteTripBtnAction(){
        try{
            tripList.remove(tripTable.getSelectionModel().getSelectedItem());
            writeTrips();
            loadTrips();
        }
        catch (NullPointerException e){
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("No trip selected");
            a.setContentText("Select a trip and try again");
            a.showAndWait();
        }
    }

    public static void loadTrips(){
        tripList.clear();
        if (listsUsed != null) {
            listsUsed.clear();
        }
        try{
            Scanner scn = new Scanner(new File(System.getProperty("user.dir") + "/Data/trips.csv"));
            scn.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            while (scn.hasNextLine()){
                String items[] = scn.nextLine().split("[,]");
                if (items[3].equals(EnterController.getSelectedUser().getUserName())){
                    ArrayList<ShoppingList> listL = new ArrayList<ShoppingList>();
                    if (items.length > 3) {
                        for (int t = 4; t < items.length; t++) {
                            for (ShoppingList sl : ListTabController.getListList()) {
                                if (sl.getID() == Integer.parseInt(items[t])) {
                                    listL.add(sl);
                                }
                            }
                        }
                    }
                    ShoppingTrip newTrip = new ShoppingTrip(items[0], sdf.parse(items[1]), listL, Integer.parseInt(items[2]),EnterController.getSelectedUser());
                    tripList.add(newTrip);
                }
            }
        }
        catch (IOException | ParseException e){
            e.printStackTrace();
        }
    }

    public void initialize(){
        tripList = FXCollections.observableArrayList();
        addListBtn.setDisable(true);
        deleteListBtn.setDisable(true);

        try{
            Scanner scn = new Scanner(new File(System.getProperty("user.dir") + "/Data/trips.csv"));
            scn.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            while (scn.hasNextLine()){
                String items[] = scn.nextLine().split("[,]");
                if (items[3].equals(EnterController.getSelectedUser().getUserName())){
                    ArrayList<ShoppingList> listL = new ArrayList<ShoppingList>();
                    if (items.length > 3) {
                        for (int t = 4; t < items.length; t++) {
                            for (ShoppingList sl : ListTabController.getListList()) {
                                if (sl.getID() == Integer.parseInt(items[t])) {
                                    listL.add(sl);
                                }
                            }
                        }
                    }
                    ShoppingTrip newTrip = new ShoppingTrip(items[0], sdf.parse(items[1]), listL, Integer.parseInt(items[2]),EnterController.getSelectedUser());
                    tripList.add(newTrip);
                }
            }
        }
        catch (IOException | ParseException e){
            e.printStackTrace();
        }
        listsUsed = FXCollections.observableArrayList();
        IDTableC.setCellValueFactory(new PropertyValueFactory<ShoppingTrip, Integer>("ID"));
        nameTableC.setCellValueFactory(new PropertyValueFactory<ShoppingTrip, String>("name"));
        dateTableC.setCellValueFactory(new PropertyValueFactory<ShoppingTrip, String>("stringDate"));
        priceTableC.setCellValueFactory(new PropertyValueFactory<ShoppingTrip, String>("stringPrice"));
        tripTable.setItems(tripList);

        reminderLbl.setVisible(false);



        listIDTableC.setCellValueFactory(new PropertyValueFactory<ShoppingList, Integer>("ID"));
        listNameTableC.setCellValueFactory(new PropertyValueFactory<ShoppingList, String>("name"));
        listPriceTableC.setCellValueFactory(new PropertyValueFactory<ShoppingList, String>("stringNetPrice"));

        listTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {

                tripTable.refresh();
                listTable.refresh();
            }
        });


        tripTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                addListBtn.setDisable(false);
                deleteListBtn.setDisable(false);

                ShoppingTrip t = (ShoppingTrip) t1;
                if (t != null) {
                    selected = t;
                }
                listTable.setItems(null);
                listsUsed.clear();

                tripTable.refresh();
                listTable.refresh();
                totalPriceLbl.setText("Total price: ");
                tripSelectedLbl.setText("Current trip selected: none");
                if (t != null) {
                    listsUsed.addAll(selected.getListsUsed());
                    listTable.setItems(listsUsed);
                    totalPriceLbl.setText("Total price: " + selected.getStringPrice());
                    tripSelectedLbl.setText("Current trip selected: " + selected.getID());
                }
                if (selected != null) {
                    long mili = selected.getDate().getTime() - new Date().getTime();
                    if (mili < 0) {
                        timeRemainingLbl.setText("Trip already occurred");
                    } else {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    long mil = selected.getDate().getTime() - new Date().getTime();
                                                    long seconds = mil / 1000;
                                                    long min = seconds / 60;
                                                    long hours = min / 60;
                                                    long days = hours / 24;
                                                    timeRemainingLbl.setText("Time Remaining: ");
                                                    if (days != 0) {
                                                        timeRemainingLbl.setText(timeRemainingLbl.getText() + days + "d ");
                                                    }
                                                    if (hours != 0) {
                                                        timeRemainingLbl.setText(timeRemainingLbl.getText() + hours % 24 + "h ");
                                                    }
                                                    if (min != 0) {
                                                        timeRemainingLbl.setText(timeRemainingLbl.getText() + min % 60 + "m ");
                                                    }
                                                    if (seconds != 0) {
                                                        timeRemainingLbl.setText(timeRemainingLbl.getText() + seconds % 60 + "s");
                                                    }
                                                } catch (NullPointerException ee) {
                                                }
                                            }


                                        });
                                        Thread.sleep(1000);
                                    }
                                } catch (InterruptedException e) {
                                } catch (NullPointerException ne) {
                                }
                            }
                        }).start();
                    }
                }
                double totalPrice = 0;
                for (ShoppingList sl : selected.getListsUsed()) {
                    totalPrice += sl.getNetPrice();
                }
                if (totalPrice == 0) {
                    reminderLbl.setVisible(true);
                    pieChart.setVisible(false);
                } else {
                    reminderLbl.setVisible(false);
                    pieChart.setVisible(true);
                    pieChart.getData().clear();
                    ArrayList<ItemWithinList> ill = new ArrayList<ItemWithinList>();
                    for (ShoppingList sl : t.getListsUsed()) {
                        for (ItemWithinList il : sl.getItemList()) {
                            boolean in = false;
                            for (ItemWithinList i : ill) {
                                if (i.getItem().getName().equals(il.getItem().getName())) {
                                    i.setQuantity(i.getQuantity() + il.getQuantity());
                                    in = true;
                                }
                            }
                            if (!in) {
                                ill.add(new ItemWithinList(il.getItem(), il.getQuantity()));
                            }
                        }
                    }
                    for (ItemWithinList iwl : ill) {
                        pieChart.getData().add(new PieChart.Data(iwl.getName(), iwl.getTotalPrice()));
                    }
                }


                for (final PieChart.Data data : pieChart.getData()) {
                    data.getNode().addEventHandler(
                            javafx.scene.input.MouseEvent.MOUSE_ENTERED,
                            new EventHandler<javafx.scene.input.MouseEvent>() {

                                @Override
                                public void handle(javafx.scene.input.MouseEvent mouseEvent) {

                                    String name = data.getName();
                                    double value = data.getPieValue();
                                    detailsLbl.setText(name + " : $" + String.format("%.2f", value));
                                }
                            });

                    data.getNode().addEventHandler(
                            javafx.scene.input.MouseEvent.MOUSE_EXITED,
                            new EventHandler<MouseEvent>() {

                                @Override
                                public void handle(javafx.scene.input.MouseEvent mouseEvent) {
                                    detailsLbl.setText("");
                                }
                            });
                }
            }

        });

    }

}
