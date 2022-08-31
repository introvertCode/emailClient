package com.barosanu.controller;
import com.barosanu.EmailManager;
import com.barosanu.controller.services.MessageRendererService;
import com.barosanu.model.EmailMessage;
import com.barosanu.model.EmailTreeItem;
import com.barosanu.model.SizeInteger;
import com.barosanu.view.ViewFactory;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.web.WebView;
import javafx.util.Callback;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;


public class MainWindowController extends BaseController implements Initializable {

    private MenuItem markUnreadMenuItem = new MenuItem("mark as unread");
    private MenuItem deleteMessageMenuItem = new MenuItem("delete message");
    private MenuItem showMessageDetailsMenuItem = new MenuItem("view details");

    @FXML
    private WebView emailWebView;

    @FXML
    private TableView<EmailMessage> emailsTableView;

    @FXML
    private TreeView<String> emailsTreeView;

    @FXML
    private TableColumn<EmailMessage, Date> dateCol;

    @FXML
    private TableColumn<EmailMessage, String> recipientCol;

    @FXML
    private TableColumn<EmailMessage, String> senderCol;

    @FXML
    private TableColumn<EmailMessage, SizeInteger> sizeCol;

    @FXML
    private TableColumn<EmailMessage, String> subjectCol;

    @FXML
    void composeMessageAction() {
        viewFactory.showComposeMessageWindow();
    }

    private MessageRendererService messageRendererService;

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
    public void initialize(URL url, ResourceBundle resourceBundle) { //czynności, które mają być wykonane od razu na początku
        setUpEmailsTreeView();
        setUpEmailsTableView();
        setUpFolderSelection();
        setUpBoldRows();
        setUpMessageRendererService();
        setUpMessageSelection();//metoda będzie uruchamiana, pokaże pierwszą wiadomość i też za każdym razem gdy będziemy klikać w wiadomość
        setUpContextMenus();
            

    }

    private void setUpContextMenus() {
        markUnreadMenuItem.setOnAction(e->{
            emailManager.setUnRead();
        });
        deleteMessageMenuItem.setOnAction(e->{
            emailManager.deleteSelectedMessage();
            emailWebView.getEngine().loadContent("");
        });
        showMessageDetailsMenuItem.setOnAction(e->{
            viewFactory.showEmailDetailsWindow();
        });
    }

    private void setUpMessageSelection() {

        emailsTableView.setOnMouseClicked(e->{
            EmailMessage emailMessage = emailsTableView.getSelectionModel().getSelectedItem();
            if(emailMessage != null) {
                emailManager.setSelectedMessage(emailMessage);
                if(!emailMessage.isRead()){
                    emailManager.setRead();
                }
//                emailManager.setSelectedMessage(emailMessage);
                messageRendererService.setEmailMessage(emailMessage);
                messageRendererService.restart(); //restar, bo start można użyć tylko raz,     zaczyna Taska- ładuje wiadomość
            }
        });
    }

    private void setUpMessageRendererService() {
        messageRendererService = new MessageRendererService(emailWebView.getEngine());

    }

    private void setUpBoldRows() {
        emailsTableView.setRowFactory(new Callback<TableView<EmailMessage>, TableRow<EmailMessage>>() {
            @Override
            public TableRow<EmailMessage> call(TableView<EmailMessage> param) {
                return new TableRow<EmailMessage>(){
                        @Override
                        protected void updateItem(EmailMessage item, boolean empty) {
                    super.updateItem(item, empty);
                        if(item != null){
                            if (item.isRead()){
                                setStyle("");
                            } else {
                                setStyle("-fx-font-weight: bold");
                            }
                        }
                    }

                };
            }
        });
    }

    private void setUpFolderSelection() {
        emailsTreeView.setOnMouseClicked(e->{
            EmailTreeItem<String> item = (EmailTreeItem<String>)emailsTreeView.getSelectionModel().getSelectedItem();
            if (item != null) {
                emailManager.setSelectedFolder(item);
                emailsTableView.setItems(item.getEmailMessages());
            }
        });
    }

    private void setUpEmailsTableView() {
        senderCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("sender")));
        subjectCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("subject")));
        recipientCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, String>("recipient")));
        sizeCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, SizeInteger>("size")));
        dateCol.setCellValueFactory((new PropertyValueFactory<EmailMessage, Date>("date")));

        emailsTableView.setContextMenu(new ContextMenu(markUnreadMenuItem, deleteMessageMenuItem, showMessageDetailsMenuItem));
    }

    private void setUpEmailsTreeView() {
        emailsTreeView.setRoot(emailManager.getFoldersRoot());
        emailsTreeView.setShowRoot(false);

    }
}
