package com.barosanu.controller.services;

import com.barosanu.model.EmailTreeItem;
import com.barosanu.view.IconResolver;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store; //An abstract class that models a message store and its access protocol, for storing and retrieving messages.
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class FetchFoldersService extends Service<Void> {

    private Store store;
    private EmailTreeItem<String> foldersRoot;
    private List<Folder> folderList;
    private IconResolver iconResolver = new IconResolver();

    public FetchFoldersService(Store store, EmailTreeItem<String> foldersRoot, List<Folder> folderList) {
        this.store = store;
        this.foldersRoot = foldersRoot;
        this.folderList = folderList;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                fetchFolders();
                return null;
            }
        };
    }

    private void fetchFolders() throws MessagingException {
        Folder[] folders = store.getDefaultFolder().list();
        handleFolders(folders, foldersRoot);
    }

    private void handleFolders(Folder[] folders, EmailTreeItem<String> foldersRoot) throws MessagingException {
        for(Folder folder: folders){
            folderList.add(folder);//potrzebne do folderUpdateService
            EmailTreeItem<String> emailTreeItem = new EmailTreeItem<String>(folder.getName());
            emailTreeItem.setGraphic(iconResolver.getIconForFolder(folder.getName()));
            foldersRoot.getChildren().add((emailTreeItem));
            foldersRoot.setExpanded(true);
//            System.out.println(folder.getName()+" type: "+folder.getType()+" holds folder "+folder.HOLDS_FOLDERS);
            fetchMessagesOnFolder(folder, emailTreeItem);
            addMessageListenerToFolder(folder, emailTreeItem);//nas??uchuje czy przychodz?? nowe wiadomo??ci

            if (folder.getType()-1 == folder.HOLDS_FOLDERS) {
//                System.out.println("s?? foldery podrz??dne");
                Folder[] subFolders = folder.list();

                handleFolders(subFolders, emailTreeItem);

            }
        }
    }

    private void addMessageListenerToFolder(Folder folder, EmailTreeItem<String> emailTreeItem) { //dodaje now?? wiadomo???? kt??ra przysz??a w trakcie dzia??ania programu
        folder.addMessageCountListener(new MessageCountListener() { //nie mo??na u??y?? lambdy bo s?? 2 metody
            @Override
            public void messagesAdded(MessageCountEvent e) {
//                System.out.println("message added event!: " + e);
                for(int i =0; i < e.getMessages().length; i++){
                    try {
                        Message message = folder.getMessage(folder.getMessageCount());//-1 w oryginale bierze,,,,, ostatni?? wiadomo???? z folderu czyli ta kt??ra w??a??nie przysz??a
                        emailTreeItem.addEmailToTop(message);//metoda addEmail dodaje j?? na d????, a najnowsza wiadomo???? ma byc u g??ry
                    } catch (MessagingException | UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void messagesRemoved(MessageCountEvent e) {
//                System.out.println("message removed event!: " + e);

            }
        });
    }

    private void fetchMessagesOnFolder(Folder folder, EmailTreeItem<String> emailTreeItem) {
        //service pozwala na wykorzystanie w??tk??w
        Service fetchMessagesService = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() throws Exception {
                        if(folder.getType() != Folder.HOLDS_FOLDERS){
                            folder.open(Folder.READ_WRITE);
                            int folderSize =folder.getMessageCount();
                            for(int i = folderSize; i>0; i--){
//                                System.out.println(folder.getMessage(i).getSubject());
                                emailTreeItem.addEmail(folder.getMessage(i));

                            }
                        }
                        return null;
                    }
                };
            }
        };
        fetchMessagesService.start();
    }
}
