package com.barosanu;

import com.barosanu.controller.services.FetchFoldersService;
import com.barosanu.model.EmailAccount;
import com.barosanu.model.EmailTreeItem;
import javafx.scene.control.TreeItem;

public class EmailManager {
    //Folder handling
    private EmailTreeItem<String> foldersRoot = new EmailTreeItem<String>("");

    public EmailTreeItem<String> getFoldersRoot() {
        return foldersRoot;
    }

    public void addEmailAccount(EmailAccount emailAccount){
        EmailTreeItem<String> treeItem = new EmailTreeItem<String>(emailAccount.getAddress());
        //treeItem.setExpanded(true); //Sets the expanded state of this TreeItem.  On a TreeItem with children however, the result of toggling this property is that visually the children will either become visible or hidden, based on whether expanded is set to true or false.

        FetchFoldersService fetchFoldersService = new FetchFoldersService(emailAccount.getStore(), treeItem);
        fetchFoldersService.start();


        foldersRoot.getChildren().add(treeItem);
    }
}
