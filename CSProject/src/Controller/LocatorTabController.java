package Controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class LocatorTabController {
    @FXML
    public WebView webView;
    @FXML
    public RadioButton fairpriceRB;
    @FXML
    public RadioButton coldstorageRB;

    public void initialize(){
        fairpriceRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    coldstorageRB.setSelected(false);
                    WebEngine webEngine = webView.getEngine();
                    webEngine.load("https://www.fairprice.com.sg/store-locator");
                    }
                }
            });
        coldstorageRB.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    fairpriceRB.setSelected(false);
                    WebEngine webEngine = webView.getEngine();
                    webEngine.load("https://coldstorage.com.sg/store-locator");
                }
            }
        });

    }
}
