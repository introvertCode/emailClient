package com.barosanu.controller;
import com.barosanu.EmailManager;
import com.barosanu.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;


public class MainWindowController extends BaseController implements Initializable {

        @FXML
        private WebView emailWebView;

        @FXML
        private TableView<?> emailsTableView;

        @FXML
        private TreeView<String> emailsTreeView;

        public MainWindowController(EmailManager emailManager, ViewFactory viewFactory, String fxmlName) {
                super(emailManager, viewFactory, fxmlName);
        }

        @FXML
        void addAccountAction() {
                viewFactory.showLoginWindow();
        }

        @FXML
        void optionsAction() {
                viewFactory.showOptionsWindow();
        }

        @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {
             setUpEmailsTreeView();
        }

        private void setUpEmailsTreeView() {
                emailsTreeView.setRoot(emailManager.getFoldersRoot());
                emailsTreeView.setShowRoot(false);

        }
}
