package com.curator.tools;

import com.curator.models.AlbumSimple;
import com.curator.models.Artist;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;

import java.io.IOException;
import java.util.ArrayList;

import static com.wrapper.spotify.SpotifyApi.getAccessToken;

/**
 * Class containing static utility methods for interfacing with Spotify wrapper's objects
 */
public class SpotifyTools {
    //TODO: secure spotify API keys, store in config file OR prompt login
    private static String clientId = "f9c190003d09495d9915681495281934";
    private static String clientSecret = "520b7eee145049cc8d655ad5b3df668f";
    public static SpotifyApi api = new SpotifyApi.Builder().setAccessToken(getAccessToken(clientId, clientSecret)).build();

    /**
     * Prevent instance creation
     */
    private SpotifyTools() {
    }


    /**
     * Return api instance
     *
     * @return wrapper's SpotifyApi instance
     */
    public static SpotifyApi getApi() {
        return api;
    }

    /**
     * Given array of wrapper's object TrackSimplified, convert to ArrayList of com.curator.TrackSimple objects
     *
     * @param sTracks array of wrapper's object TrackSimplified
     * @return arrayList of com.curator.TrackSimple objects
     */
    public static ArrayList<com.curator.models.TrackSimple> toTrackSimple(TrackSimplified[] sTracks) {
        ArrayList<com.curator.models.TrackSimple> trackSimple = new ArrayList<>();
        for (TrackSimplified sTrack : sTracks) {
            trackSimple.add(toTrackSimple(sTrack));
        }
        return trackSimple;
    }

    /**
     * Given wrapper's TrackSimplified object, convert to com.curator.TrackSimple objects
     *
     * @param sTrack wrapper's TrackSimplified object
     * @return com.curator.TrackSimple objects
     */
    public static com.curator.models.TrackSimple toTrackSimple(TrackSimplified sTrack) {
        try {
            return new com.curator.models.TrackSimple(api.getTrack(sTrack.getId()).build().execute());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Given wrapper's object ArtistSimplified, convert to com.curator.Artist object
     *
     * @param sArtist wrapper's object ArtistSimplified
     * @return com.curator.Artist object of
     */
    public static com.curator.models.Artist toArtist(ArtistSimplified sArtist) {
        try {
            return new Artist(api.getArtist(sArtist.getId()).build().execute());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Given array of wrapper's object ArtistSimplified, convert to ArrayList of com.curator.Artist objects
     *
     * @param sArtists array of wrapper's object ArtistSimplified
     * @return ArrayList of com.curator.Artist objects
     */
    public static ArrayList<com.curator.models.Artist> toArtist(ArtistSimplified[] sArtists) {
        ArrayList<com.curator.models.Artist> artists = new ArrayList<>();
        for (com.wrapper.spotify.model_objects.specification.ArtistSimplified sArtist : sArtists) {
            artists.add(toArtist(sArtist));
        }
        return artists;
    }

    /**
     * Given array of wrapper's object AlbumSimplified, convert to ArrayList of com.curator.Album objects
     *
     * @param sAlbums array of wrapper's object AlbumSimplified
     * @return ArrayList of com.curator.Album objects
     */
    public static ArrayList<com.curator.models.AlbumSimple> toAlbumSimple(AlbumSimplified[] sAlbums) {
        ArrayList<com.curator.models.AlbumSimple> albums = new ArrayList<>();
        for (com.wrapper.spotify.model_objects.specification.AlbumSimplified sAlbum : sAlbums) {
            albums.add(toAlbumSimple(sAlbum));
        }
        return albums;
    }

    /**
     * Given wrapper's object AlbumSimplified, convert to com.curator.Album object
     *
     * @param sAlbum wrapper's object AlbumSimplified
     * @return com.curator.Album object
     */
    public static com.curator.models.AlbumSimple toAlbumSimple(AlbumSimplified sAlbum) {
        try {
            return new AlbumSimple(api.getAlbum(sAlbum.getId()).build().execute());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Given wrapper's object Track, get AudioFeatures object
     *
     * @param track wrapper's object Track
     * @return AudioFeatures object corresponding to the wrapper's Track object
     */
    public static AudioFeatures getAudioFeatures(com.wrapper.spotify.model_objects.specification.Track track) {
        return getAudioFeatures(track.getId());
    }

    /**
     * Given com.curator Track object, get its AudioFeatures object
     *
     * @param track com.curator Track object
     * @return AudioFeatures object of the Track object
     */
    public static AudioFeatures getAudioFeatures(com.curator.models.Track track) {
        return getAudioFeatures(track.getTrackID());
    }

    /**
     * Given com.curator Track object, get its AudioFeatures object
     *
     * @param trackSimple com.curator TrackSimple object
     * @return AudioFeatures object of the Track object
     */
    public static AudioFeatures getAudioFeatures(com.curator.models.TrackSimple trackSimple) {
        return getAudioFeatures(trackSimple.getTrackID());
    }


    /**
     * Given Spotify track id, return its AudioFeatures object
     *
     * @param sTrackId Spotify track id
     * @return AudioFeatures object of track with sTrackId
     */
    public static AudioFeatures getAudioFeatures(String sTrackId) {
        try {
            return api.getAudioFeaturesForTrack(sTrackId).build().execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }
        return null;
    }
}