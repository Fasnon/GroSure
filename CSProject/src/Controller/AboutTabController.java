package Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.util.Locale;
import java.util.ResourceBundle;

public class AboutTabController {
    @FXML
    public Label aboutLbl;
    @FXML
    public Label createLbl;
    @FXML
    public Label currentVerLbl;
    @FXML
    public ComboBox<String> languageComboB;

    public void initialize(){
        String[] items = {"English", "简体中文", "繁體中文", "French"};
        ObservableList<String> ll = FXCollections.observableArrayList();
        ll.addAll(items);
        languageComboB.setItems(ll);

        languageComboB.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                Locale userLocale = null;
                if (t1.equals("English")){
                    userLocale = new Locale("en", "UK");
                }
                if (t1.equals("French")){
                    userLocale = new Locale("fr", "FR");
                }
                if (t1.equals("简体中文")) {
                    userLocale = new Locale("zh", "ZH");
                }
                if (t1.equals("繁體中文")) {
                    userLocale = new Locale("zh", "TW");
                }
                ResourceBundle messages = ResourceBundle.getBundle("AboutTab", userLocale);
                aboutLbl.setText(messages.getString("about"));
                createLbl.setText(messages.getString("created"));
                currentVerLbl.setText(messages.getString("currentVersion"));





            }
        });


    }
}
