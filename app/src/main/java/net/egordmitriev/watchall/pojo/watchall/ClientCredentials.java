package net.egordmitriev.watchall.pojo.watchall;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.PreferencesHelper;

import java.util.Date;

/**
 * Created by EgorDm on 4/4/2016.
 */
public class ClientCredentials implements Parcelable {

    @SerializedName("id")
    public int id;
    @SerializedName("token")
    public String token;
    @SerializedName("exp")
    public Date exp;

    public ClientCredentials() {
    }

    public ClientCredentials(Date exp, int id, String token) {
        this.exp = exp;
        this.id = id;
        this.token = token;
    }

    public void save() {
        PreferencesHelper.getInstance().build().setLong(R.string.pref_account_user_expires, exp.getTime());
    }

    public static Date load() {
        if(PreferencesHelper.getInstance().isPrefContains(R.string.pref_account_user_expires)) {
            return new Date(PreferencesHelper.getInstance().getLong(R.string.pref_account_user_expires));
        }
        return null;
    }

    @Override
    public String toString() {
        return "ClientCredentials{" +
                "exp=" + exp +
                ", id=" + id +
                ", token='" + token + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.token);
        dest.writeLong(exp != null ? exp.getTime() : -1);
    }

    protected ClientCredentials(Parcel in) {
        this.id = in.readInt();
        this.token = in.readString();
        long tmpExp = in.readLong();
        this.exp = tmpExp == -1 ? null : new Date(tmpExp);
    }

    public static final Creator<ClientCredentials> CREATOR = new Creator<ClientCredentials>() {
        @Override
        public ClientCredentials createFromParcel(Parcel source) {
            return new ClientCredentials(source);
        }

        @Override
        public ClientCredentials[] newArray(int size) {
            return new ClientCredentials[size];
        }
    };
}
