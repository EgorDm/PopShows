package net.egordmitriev.popshows.pojo.user;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class SearchRecord implements Comparable<SearchRecord> {

    @SerializedName("query")
    public String query;
    @SerializedName("date")
    public Date date;
    public boolean isHistory;

    public SearchRecord(Date date, boolean isHistory, String query) {
        this.date = date;
        this.isHistory = isHistory;
        this.query = query;
    }

    public boolean contains(String another) {
        return another == null || (query.toLowerCase().contains(another.toLowerCase()) && !query.equalsIgnoreCase(another));
    }

    @Override
    public int compareTo(@NonNull SearchRecord another) {
        if (isHistory && !another.isHistory)
            return -1;
        else if (!isHistory && another.isHistory)
            return 1;
        else if (isHistory)
            return date.compareTo(another.date);
        else
            return 0;
    }
}
