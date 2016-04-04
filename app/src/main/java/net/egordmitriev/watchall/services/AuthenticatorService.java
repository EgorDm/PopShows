package net.egordmitriev.watchall.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import net.egordmitriev.watchall.adapters.WatchAllAuthenticator;

/**
 * Created by EgorDm on 4/4/2016.
 */
public class AuthenticatorService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        WatchAllAuthenticator authenticator = new WatchAllAuthenticator(this);
        return authenticator.getIBinder();
    }
}
