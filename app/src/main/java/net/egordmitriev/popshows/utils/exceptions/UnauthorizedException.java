package net.egordmitriev.popshows.utils.exceptions;

/**
 * Created by EgorDm on 4/4/2016.
 */
public class UnauthorizedException extends Exception {
    public UnauthorizedException() {
        super("User is not logged in with a valid PopShows account!");
    }
}
