package net.egordmitriev.popshows.adapters;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.JsonObject;

import net.egordmitriev.popshows.MainApplication;
import net.egordmitriev.popshows.R;
import net.egordmitriev.popshows.api.PreferencesHelper;
import net.egordmitriev.popshows.api.WatchAllServiceHelper;
import net.egordmitriev.popshows.pojo.watchall.ClientCredentials;
import net.egordmitriev.popshows.ui.activities.AuthenticatorActivity;
import net.egordmitriev.popshows.utils.APIUtils;
import net.egordmitriev.popshows.utils.DataCallback;
import net.egordmitriev.popshows.utils.exceptions.UnauthorizedException;

import java.io.IOException;
import java.util.Date;

import retrofit2.Response;

/**
 * Created by EgorDm on 4/4/2016.
 */
public class WatchAllAuthenticator extends AbstractAccountAuthenticator {
    public static final String WATCHALL_AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String WATCHALL_AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an Watchit account";
    public static final String WATCHALL_ACCOUNT_TYPE = "nl.egordmitriev.popshows";

    private final Context mContext;

    public WatchAllAuthenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AuthenticatorActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        if (!authTokenType.equals(WATCHALL_AUTHTOKEN_TYPE_FULL_ACCESS)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ERROR_MESSAGE, "Invalid authentication token type.");
            return result;
        }

        final AccountManager am = AccountManager.get(mContext);
        String authToken = am.peekAuthToken(account, authTokenType);

        Date expires = ClientCredentials.load();
        if (TextUtils.isEmpty(authToken) || new Date().after(expires)) {
            final String password = am.getPassword(account);
            if (password != null) {
                try {
                    ClientCredentials credentials = APIUtils.sGlobalParser.fromJson(WatchAllServiceHelper.sService.getToken(account.name, password)
                            .execute().body().get("data").getAsJsonObject(), ClientCredentials.class);
                    authToken = credentials.token;
                    expires = credentials.exp;
                    credentials.save();
                } catch (Exception error) {
                    error.printStackTrace();
                }
            }
        }

        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        //Prompt login or register
        final Intent intent = new Intent(mContext, AuthenticatorActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AuthenticatorActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(AuthenticatorActivity.ARG_AUTH_TYPE, authTokenType);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    private static AccountManager sAccountManager;

    public static AccountManager getAccountManager() {
        if(sAccountManager == null) {
            sAccountManager = AccountManager.get(MainApplication.getAppContext());
        }
        return sAccountManager;
    }

    public static Account getAccount() {
        final Account availableAccounts[] = getAccountManager().getAccountsByType(WATCHALL_ACCOUNT_TYPE);
        if (availableAccounts.length == 0) {
            return null;
        }
        return availableAccounts[0];
    }

    public static ClientCredentials refreshCredentials() throws UnauthorizedException {
        Account account = getAccount();
        if(account == null) {
            throw new UnauthorizedException();
        }
        String authToken = getAccountManager().peekAuthToken(account, WATCHALL_AUTHTOKEN_TYPE_FULL_ACCESS);
        Date expires = ClientCredentials.load();
        if(expires != null && expires.after(new Date()) && authToken != null) {
            return new ClientCredentials(expires, -1, authToken);
        }
        final String password = getAccountManager().getPassword(account);
        if(password == null) {
            throw new UnauthorizedException();
        }
        try {
            Response<JsonObject> response = WatchAllServiceHelper.sService.getToken(account.name, password).execute();
            if (response.isSuccessful()) {
                ClientCredentials ret = APIUtils.sGlobalParser.fromJson(response.body().get("data").getAsJsonObject(), ClientCredentials.class);
                getAccountManager().invalidateAuthToken(WATCHALL_ACCOUNT_TYPE, authToken);
                getAccountManager().setAuthToken(account, WATCHALL_AUTHTOKEN_TYPE_FULL_ACCESS, ret.token);
                ret.save();
                return ret;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new UnauthorizedException();
    }

    public static void setupAccount(Activity activity, final DataCallback<Boolean> callback) {
        getAccountManager().addAccount(WATCHALL_ACCOUNT_TYPE, WATCHALL_AUTHTOKEN_TYPE_FULL_ACCESS, null, null, activity, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                try {
                    Bundle bnd = future.getResult();
                    callback.success(true);
                } catch (Exception e) {
                    callback.success(false);
                    e.printStackTrace();
                }
            }
        }, null);
    }

    public static void logout() {
        Account account = getAccount();
        if(account == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            getAccountManager().removeAccountExplicitly(getAccount());
        } else {
            getAccountManager().removeAccount(getAccount(), null, null);
        }
        WatchAllServiceHelper.sCredentials = null;
        PreferencesHelper.getInstance().build()
                .removePref(R.string.pref_account_user_id)
                .removePref(R.string.pref_account_user_name)
                .removePref(R.string.pref_account_user_email)
                .removePref(R.string.pref_account_user_avatar)
                .removePref(R.string.pref_account_user_color).commit();
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (WATCHALL_AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType))
            return WATCHALL_AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        else
            return authTokenType + " (Label)";
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
