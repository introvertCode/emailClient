package com.barosanu.view;

import com.barosanu.EmailManager;
import com.barosanu.controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


import java.io.IOException;
import java.util.ArrayList;

public class ViewFactory {

    private EmailManager emailManager;
    private ArrayList<Stage> activeStages;
    private boolean mainViewInitialized = false;

    public ViewFactory(EmailManager emailManager){
        this.emailManager = emailManager;
        activeStages = new ArrayList<Stage>();
    }

    public boolean isMainViewInitialized(){
        return mainViewInitialized;
    }

    //View options handling
    private ColorTheme colorTheme = ColorTheme.DEFAULT;
    private FontSize fontSize = FontSize.MEDIUM;

    public ColorTheme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(ColorTheme colorTheme) {
        this.colorTheme = colorTheme;
    }

    public FontSize getFontSize() {
        return fontSize;
    }

    public void setFontSize(FontSize fontSize) {
        this.fontSize = fontSize;
    }

    public void showLoginWindow() {
        BaseController controller = new LoginWindowController(emailManager, this, "/fxml/LoginWindow.fxml");
        initializeStage(controller);
//        System.out.println("show Login Window");
    }

    public void showMainWindow(){
        BaseController controller = new MainWindowController(emailManager, this, "/fxml/MainWindow.fxml");
        initializeStage(controller);
//        System.out.println("show Main Window");

        mainViewInitialized = true;
    }

    public void showOptionsWindow(){
        BaseController controller = new OptionsWindowController(emailManager, this, "/fxml/OptionsWindow.fxml");
        initializeStage(controller);
//        System.out.println("show Option Window");

    }

    public void showComposeMessageWindow(){
        BaseController controller = new ComposeMessageControler(emailManager, this, "/fxml/ComposeMessageWindow.fxml");
        initializeStage(controller);
//        System.out.println("composed window called");

    }
    public void showEmailDetailsWindow(){
        BaseController controller = new EmailDetailsController(emailManager, this, "/fxml/EmailDetailsWindow.fxml");
        initializeStage(controller);
//        System.out.println("composed window called");

    }

    private void initializeStage(BaseController baseController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(baseController.getFxmlName()));
//        System.out.println(getClass().getResource(baseController.getFxmlName()));
        fxmlLoader.setController(baseController);

        Parent parent;
        try {
            parent = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        activeStages.add(stage);
        updateStyles();
    }

    public void closeStage(Stage stageToClose){
        stageToClose.close();//zamyka okno
        activeStages.remove(stageToClose);
    }

    public void updateStyles() {
        for(Stage stage:activeStages) {
            Scene scene = stage.getScene();
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource(ColorTheme.getCssPath(colorTheme)).toExternalForm());
            scene.getStylesheets().add(getClass().getResource(FontSize.getCssPath(fontSize)).toExternalForm());

        }
    }
}
