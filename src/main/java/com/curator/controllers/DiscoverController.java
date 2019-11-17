package com.curator.controllers;

import com.curator.tools.SpotifyTools;
import com.curator.tools.YoutubeTools;
import com.curator.models.*;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

//TODO: REFACTOR + CLEANUP

/**
 * Controller to discover.fxml
 */
public class DiscoverController implements Initializable {
    MainController mainController;
    PlayerController playerController;

    @FXML
    ScrollPane discoverScrollPane;

    @FXML
    VBox discoverVBox;

    @FXML
    Label discoverTitle;

    @FXML
    TextField searchBar;

    @FXML
    Label tracksLabel;

    @FXML
    ScrollPane tracksScrollPane;

    @FXML
    Label artistsLabel;

    @FXML
    ScrollPane artistsScrollPane;

    @FXML
    Label albumsLabel;

    @FXML
    ScrollPane albumsScrollPane;

    /**
     * Show/hide child items on discover page
     * @param isVisible
     */
    private void toggleChildrenVisibility(boolean isVisible) {
        tracksLabel.setVisible(isVisible);
        artistsLabel.setVisible(isVisible);
        albumsLabel.setVisible(isVisible);
        tracksScrollPane.setVisible(isVisible);
        artistsScrollPane.setVisible(isVisible);
        albumsScrollPane.setVisible(isVisible);
    }

    /**
     * Updates search result given query
     * @param query search query
     */
    private void update(String query) {
        if (query.equals("")) {
            toggleChildrenVisibility(false);
        } else {
            toggleChildrenVisibility(true);

            //set matching tracks
            ArrayList<Track> tracks = SpotifyTools.searchTracks(query, 8);

            if (tracks.size() == 0) {
                System.out.println("no tracks found!");
                tracksLabel.setText("No tracks found");
                tracksScrollPane.setVisible(false);
            } else {
                tracksLabel.setText("Tracks");
                tracksScrollPane.setVisible(true);
                tracksScrollPane.prefWidthProperty().bind(discoverScrollPane.widthProperty());
                tracksScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                tracksScrollPane.setContent(createTracksRecommendationBox(tracks));
            }

            //set matching artists
            ArrayList<Artist> artists = SpotifyTools.searchArtists(query, 8);
            if (artists.size() == 0) {
                System.out.println("no artists found!");
                artistsLabel.setText("No artists found");
                artistsScrollPane.setVisible(false);
            } else {
                artistsLabel.setText("Artists");
                artistsScrollPane.setVisible(true);
                artistsScrollPane.prefWidthProperty().bind(discoverScrollPane.widthProperty());
                artistsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                artistsScrollPane.setContent(createArtistsRecommendationBox(artists));
            }

            //set matching albums
            ArrayList<AlbumSimple> albums = SpotifyTools.searchAlbums(query, 8);
            if (albums.size() == 0) {
                System.out.println("no albums found!");
                albumsLabel.setText("No albums found");
                albumsScrollPane.setVisible(false);
            } else {
                albumsLabel.setText("Albums");
                albumsScrollPane.setVisible(true);
                albumsScrollPane.prefWidthProperty().bind(discoverScrollPane.widthProperty());
                albumsScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
                albumsScrollPane.setContent(createAlbumsRecommendationBox(albums));
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set to empty first
        update("");

        // when user press Enter, update results
        searchBar.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                    update(searchBar.getText());
                }
            }
        });
    }


    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Creates a horizontal box of track panes. Default of 8 tracks.
     *
     * @param artists
     * @return HBox of track panes in tracks
     */
    public ScrollPane createArtistsRecommendationBox(ArrayList<Artist> artists) {
        /*
          Nodes Hierarchy:

          ScrollPane (isPannable true (can drag horizontally by mouse), hbarPolicy NEVER (hide scrollbar))
             HBox
                Pane (one for each artists, default 8 panes)
                  ImageView (for artist image)
                  Label (artist's name)
                  ImageView (for in-pane play button)
                  ImageView (for in-pane heart button)
                  ImageView (for in-pane addToPlaylist button)
        */
        ScrollPane pane = null;

        try {
            pane = new FXMLLoader(getClass().getResource("/views/artists_hbox.fxml")).load();
            pane.prefWidthProperty().bind(discoverScrollPane.widthProperty());

            HBox box = (HBox) pane.getContent();

            //loop on each music pane in HBox
            for (int i = 0; i < artists.size(); i++) {
                Artist artist = artists.get(i);
                Pane subPane = (Pane) box.getChildren().get(i);
                ImageView trackImage = (ImageView) subPane.getChildren().get(0);
                Label artistName = (Label) subPane.getChildren().get(1);
                ImageView inPanePlayButton = (ImageView) subPane.getChildren().get(2);
                ImageView inPaneHeartButton = (ImageView) subPane.getChildren().get(3);
                ImageView inPaneAddToPlaylistButton = (ImageView) subPane.getChildren().get(4);

                if (artist.getImages().size() != 0) {
                    trackImage.setImage(artist.getImages().get(0));
                }
                artistName.setText(artist.getName());

                //when mouse enter the pane
                subPane.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        trackImage.setOpacity(0.2);
                        inPanePlayButton.setOpacity(1);
                        inPaneHeartButton.setOpacity(1);
                        inPaneAddToPlaylistButton.setOpacity(1);

                        inPanePlayButton.setDisable(false);
                        inPaneHeartButton.setDisable(false);
                        inPaneAddToPlaylistButton.setDisable(false);
                    }
                });

                //when mouse exit the pane
                subPane.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        trackImage.setOpacity(1);
                        inPanePlayButton.setOpacity(0);
                        inPaneHeartButton.setOpacity(0);
                        inPaneAddToPlaylistButton.setOpacity(0);

                        inPanePlayButton.setDisable(true);
                        inPaneHeartButton.setDisable(true);
                        inPaneAddToPlaylistButton.setDisable(true);
                    }
                });

                //when play button inside pane is clicked
                //MUST CREATE PLAYLIST
//                inPanePlayButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        Media media = com.curator.tools.YoutubeTools.getMusicFileFromQuery(
//                                com.curator.tools.YoutubeTools.createYoutubeQuery(track.getName(), track.getArtistsString())
//                        );
//                        track.setMediaFile(media);
//                        playerController.setCurrentTrack(track);
//                        event.consume();
//                    }
//                });


                //when addToPlaylist button inside pane is clicked
                inPaneAddToPlaylistButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //TODO: IMPLEMENT
                        System.out.println("playlist clicked");
                    }
                });

                //when addToPlaylist button inside pane is clicked
                inPaneHeartButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //TODO: IMPLEMENT
                        System.out.println("heart clicked");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pane;
    }

    public ScrollPane createAlbumsRecommendationBox(ArrayList<AlbumSimple> albumSimpleArr) {
        /*
          Nodes Hierarchy:

          ScrollPane (isPannable true (can drag horizontally by mouse), hbarPolicy NEVER (hide scrollbar))
             HBox
                Pane (one for each track, default 8 panes)
                  ImageView (for track image)
                  Label (album name)
                  Label (album artist)
                  ImageView (for in-pane play button)
                  ImageView (for in-pane heart button)
                  ImageView (for in-pane addToPlaylist button)
        */
        ScrollPane pane = null;

        try {
            pane = new FXMLLoader(getClass().getResource("/views/albums_hbox.fxml")).load();
            pane.prefWidthProperty().bind(discoverScrollPane.widthProperty());

            HBox box = (HBox) pane.getContent();

            //loop on each music pane in HBox
             for (int i = 0; i < albumSimpleArr.size(); i++) {
                AlbumSimple album = albumSimpleArr.get(i);

                Pane subPane = (Pane) box.getChildren().get(i);
                ImageView trackImage = (ImageView) subPane.getChildren().get(0);
                Label trackName = (Label) subPane.getChildren().get(1);
                Label trackArtist = (Label) subPane.getChildren().get(2);
                ImageView inPanePlayButton = (ImageView) subPane.getChildren().get(3);
                ImageView inPaneHeartButton = (ImageView) subPane.getChildren().get(4);
                ImageView inPaneAddToPlaylistButton = (ImageView) subPane.getChildren().get(5);

                trackImage.setImage(album.getImages().get(0));
                trackName.setText(album.getName());
                trackArtist.setText(album.getArtistsString());


                //when mouse enter the pane
                subPane.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        trackImage.setOpacity(0.2);
                        inPanePlayButton.setOpacity(1);
                        inPaneHeartButton.setOpacity(1);
                        inPaneAddToPlaylistButton.setOpacity(1);

                        inPanePlayButton.setDisable(false);
                        inPaneHeartButton.setDisable(false);
                        inPaneAddToPlaylistButton.setDisable(false);
                    }
                });

                //when mouse exit the pane
                subPane.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        trackImage.setOpacity(1);
                        inPanePlayButton.setOpacity(0);
                        inPaneHeartButton.setOpacity(0);
                        inPaneAddToPlaylistButton.setOpacity(0);

                        inPanePlayButton.setDisable(true);
                        inPaneHeartButton.setDisable(true);
                        inPaneAddToPlaylistButton.setDisable(true);
                    }
                });

                //when play button inside pane is clicked

//                inPanePlayButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
//                    @Override
//                    public void handle(MouseEvent event) {
//                        Media media = com.curator.tools.YoutubeTools.getMusicFileFromQuery(
//                                com.curator.tools.YoutubeTools.createYoutubeQuery(track.getName(), track.getArtistsString())
//                        );
//                        track.setMediaFile(media);
//                        playerController.setCurrentTrack(track);
//                        event.consume();
//                    }
//                });

                //when addToPlaylist button inside pane is clicked
                inPaneAddToPlaylistButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //TODO: IMPLEMENT
                        System.out.println("playlist clicked");
                    }
                });

                //when addToPlaylist button inside pane is clicked
                inPaneHeartButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //TODO: IMPLEMENT
                        System.out.println("heart clicked");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pane;
    }


    /**
     * Creates a horizontal box of track panes. Default of 8 tracks.
     *
     * @param tracks
     * @return HBox of track panes in tracks
     */
    public ScrollPane createTracksRecommendationBox(ArrayList<Track> tracks) {
        /*
          Nodes Hierarchy:

          ScrollPane (isPannable true (can drag horizontally by mouse), hbarPolicy NEVER (hide scrollbar))
             HBox
                Pane (one for each track, default 8 panes)
                  ImageView (for track image)
                  Label (track name)
                  Label (track artist)
                  ImageView (for in-pane play button)
                  ImageView (for in-pane heart button)
                  ImageView (for in-pane addToPlaylist button)
        */
        ScrollPane pane = null;

        try {
            pane = new FXMLLoader(getClass().getResource("/views/tracks_hbox.fxml")).load();
            pane.prefWidthProperty().bind(discoverScrollPane.widthProperty());

            HBox box = (HBox) pane.getContent();

            //loop on each music pane in HBox
            for (int i = 0; i < tracks.size(); i++) {
                Track track = tracks.get(i);
                Pane subPane = (Pane) box.getChildren().get(i);
                ImageView trackImage = (ImageView) subPane.getChildren().get(0);
                Label trackName = (Label) subPane.getChildren().get(1);
                Label trackArtist = (Label) subPane.getChildren().get(2);
                ImageView inPanePlayButton = (ImageView) subPane.getChildren().get(3);
                ImageView inPaneHeartButton = (ImageView) subPane.getChildren().get(4);
                ImageView inPaneAddToPlaylistButton = (ImageView) subPane.getChildren().get(5);

                trackImage.setImage(track.getImage());
                trackName.setText(track.getName());
                trackArtist.setText(track.getArtistsString());


                //when mouse enter the pane
                subPane.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        trackImage.setOpacity(0.2);
                        inPanePlayButton.setOpacity(1);
                        inPaneHeartButton.setOpacity(1);
                        inPaneAddToPlaylistButton.setOpacity(1);

                        inPanePlayButton.setDisable(false);
                        inPaneHeartButton.setDisable(false);
                        inPaneAddToPlaylistButton.setDisable(false);
                    }
                });

                //when mouse exit the pane
                subPane.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        trackImage.setOpacity(1);
                        inPanePlayButton.setOpacity(0);
                        inPaneHeartButton.setOpacity(0);
                        inPaneAddToPlaylistButton.setOpacity(0);

                        inPanePlayButton.setDisable(true);
                        inPaneHeartButton.setDisable(true);
                        inPaneAddToPlaylistButton.setDisable(true);
                    }
                });

                //when play button inside pane is clicked
                inPanePlayButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        Media media = YoutubeTools.getMusicFileFromQuery(
                                YoutubeTools.createYoutubeQuery(track.getName(), track.getArtistsString())
                        );
                        track.setMedia(media);
                        playerController.setCurrentTrack(track);
                        event.consume();
                    }
                });

                //when addToPlaylist button inside pane is clicked
                inPaneAddToPlaylistButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //TODO: IMPLEMENT
                        System.out.println("playlist clicked");
                    }
                });

                //when addToPlaylist button inside pane is clicked
                inPaneHeartButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        //TODO: IMPLEMENT
                        System.out.println("heart clicked");
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pane;
    }
}
