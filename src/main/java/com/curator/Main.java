package com.curator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.wrapper.spotify.SpotifyApi.getAccessToken;
import com.wrapper.spotify.*;

//TODO: /lib almost 200MB, need to audit and reduce dependencies, remove unused file/classes
//TODO: create sqlite database to keep user usage data, e.g. favorites etc
//TODO: add clear cache feature in settings menu bar, to remove downloaded mp3 files

/**
 * Entry to the app.
 */
public class Main extends Application {
    //TODO: secure spotify API keys, store in config file OR prompt login
    private static String clientId = "f9c190003d09495d9915681495281934";
    private static String clientSecret = "520b7eee145049cc8d655ad5b3df668f";

    //Resources variables to be passed to children
    //TODO: find a better way to inject dependency
    public static SpotifyApi api = new SpotifyApi.Builder().setAccessToken(getAccessToken(clientId, clientSecret)).build();

    //TODO: set logger + log files

    /**
     * Initialize GUI app
     *
     * @param stage - the main stage of the app
     * @throws Exception
     */
    @java.lang.Override
    public void start(Stage stage) throws Exception {
        Parent root = new FXMLLoader(getClass().getResource("/views/main.fxml")).load();

        stage.setScene(new Scene(root, 300, 300));
        stage.setHeight(600);
        stage.setWidth(1200);
        stage.show();
    }

    public static void main(String[] args) {
        YoutubeTools.initializeInterpreter();
        launch();
    }
}
