package net.egordmitriev.watchall.ui.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.adapters.WatchAllAuthenticator;
import net.egordmitriev.watchall.api.GlobalHelper;
import net.egordmitriev.watchall.api.WatchAllServiceHelper;
import net.egordmitriev.watchall.api.base.APIError;
import net.egordmitriev.watchall.pojo.watchall.ClientCredentials;
import net.egordmitriev.watchall.utils.APIUtils;
import net.egordmitriev.watchall.utils.ErrorUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by EgorDm on 4/4/2016.
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity implements Callback<JsonObject> {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public final static String KEY_EXPIRES = "IS_ADDING_ACCOUNT";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private View mLoginView;
    private View mRegisterView;

    private String mAuthTokenType;
    private boolean mRequesting;
    private TextView mErrorMessage;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_authenticator);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SignUpPagerAdapter());
        ((TabLayout) findViewById(R.id.tabs)).setupWithViewPager(viewPager);

        mLoginView = findViewById(R.id.view_signin);
        mRegisterView = findViewById(R.id.view_signup);
        mErrorMessage = (TextView) findViewById(R.id.errormsg);
        mProgressBar = (ProgressBar) findViewById(R.id.loaderview);

        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = WatchAllAuthenticator.WATCHALL_AUTHTOKEN_TYPE_FULL_ACCESS;

        if (accountName != null) {
            ((TextView) mLoginView.findViewById(R.id.fullname)).setText(accountName);
        }

        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        GlobalHelper.getNetworkState();
    }

    private String mEmail;
    private String mPassword;

    public void register() {
        if (mRequesting) return;
        if (GlobalHelper.getNetworkState() == APIUtils.NETWORK_STATE_DISCONNECTED) {
            if (GlobalHelper.getNetworkState() == GlobalHelper.updateNetworkState()) {
                mErrorMessage.setText(R.string.error_no_network);
                return;
            }
        }
        mEmail = ((TextView) mRegisterView.findViewById(R.id.email)).getText().toString();
        mPassword = ((TextView) mRegisterView.findViewById(R.id.password)).getText().toString();
        final String userFullname = ((TextView) mRegisterView.findViewById(R.id.fullname)).getText().toString();
        WatchAllServiceHelper.sService.register("Egor Dmitriev", mPassword, mEmail).enqueue(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mRequesting = true;
        mErrorMessage.setVisibility(View.GONE);
    }

    public void login() {
        if (mRequesting) return;
        if (GlobalHelper.getNetworkState() == APIUtils.NETWORK_STATE_DISCONNECTED) {
            if (GlobalHelper.getNetworkState() == GlobalHelper.updateNetworkState()) {
                mErrorMessage.setText(R.string.error_no_network);
                return;
            }
        }
        mEmail = ((TextView) mLoginView.findViewById(R.id.email)).getText().toString();
        mPassword = ((TextView) mLoginView.findViewById(R.id.password)).getText().toString();
        WatchAllServiceHelper.sService.getToken(mEmail, mPassword).enqueue(this);
        mProgressBar.setVisibility(View.VISIBLE);
        mRequesting = true;
        mErrorMessage.setVisibility(View.GONE);
    }

    private void finishAuthentication(Intent intent) {
        final Account account = new Account(
                intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME),
                intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            WatchAllAuthenticator.getAccountManager().addAccountExplicitly(account, intent.getStringExtra(PARAM_USER_PASS), null);
        } else {
            WatchAllAuthenticator.getAccountManager().setPassword(account, intent.getStringExtra(PARAM_USER_PASS));
        }
        WatchAllAuthenticator.getAccountManager().setAuthToken(account, mAuthTokenType, intent.getStringExtra(AccountManager.KEY_AUTHTOKEN));

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
        mRequesting = false;
        mProgressBar.setVisibility(View.GONE);

        APIError error = ErrorUtils.checkError(response);
        if (error != null) {
            displayError(error);
        }
        try {
            Bundle data = new Bundle();

            data.putString(AccountManager.KEY_ACCOUNT_NAME, mEmail);
            data.putString(AccountManager.KEY_ACCOUNT_TYPE, WatchAllAuthenticator.WATCHALL_ACCOUNT_TYPE);
            ClientCredentials credentials = APIUtils.sGlobalParser.fromJson(response.body().get("data").getAsJsonObject(), ClientCredentials.class);
            credentials.save();
            data.putString(AccountManager.KEY_AUTHTOKEN, credentials.token);
            data.putString(PARAM_USER_PASS, mPassword);
            final Intent res = new Intent();
            res.putExtras(data);
            finishAuthentication(res);
            mErrorMessage.setVisibility(View.GONE);
        } catch (Exception e) {
            displayError(new APIError(1337, e.getMessage()));
        }
    }

    protected void displayError(APIError error) {
        if (error.getErrorCode() == 401) {
            mErrorMessage.setText(R.string.error_incorrect_username_password);
        } else {
            mErrorMessage.setText(R.string.error_server);
        }
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailure(Call<JsonObject> call, Throwable t) {
        mRequesting = false;
        mProgressBar.setVisibility(View.GONE);
        mErrorMessage.setText(t.getMessage());
    }

    class SignUpPagerAdapter extends PagerAdapter {

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.view_signin;
                    break;
                case 1:
                    resId = R.id.view_signup;
                    break;
            }
            return findViewById(resId);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sign in";
                case 1:
                    return "Sign up";
            }
            return super.getPageTitle(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
