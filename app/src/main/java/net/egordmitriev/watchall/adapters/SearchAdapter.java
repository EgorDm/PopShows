package net.egordmitriev.watchall.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.TMDBServiceHelper;
import net.egordmitriev.watchall.api.base.APIError;
import net.egordmitriev.watchall.api.database.tables.SearchSuggestTable;
import net.egordmitriev.watchall.pojo.user.SearchRecord;
import net.egordmitriev.watchall.utils.DataCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by EgorDm on 5/6/2016.
 */
public class SearchAdapter implements ListAdapter, DataCallback<String[]> {
    private static final int SUGGESTIONS_COUNT = 5;
    private static final int ONLINESEARCH_TRESHHOLD = 5;
    private static Date lastSearched;
    private static List<SearchRecord> historySearches;
    private static List<SearchRecord> keywordSearches;

    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    private List<SearchRecord> mSearchRecords;
    private LayoutInflater mInflater;
    private boolean isSearching;

    public SearchAdapter(Context context) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mSearchRecords = new ArrayList<>();
        historySearches = new ArrayList<>();
        keywordSearches = new ArrayList<>();
        addFromCursor(SearchSuggestTable.getAllForQuery());
    }

    public void submitSearch(String query) {
        if (query == null || query.isEmpty()) return;
        SearchSuggestTable.upsert(new SearchRecord(new Date(), true, query));
    }

    public SearchRecord getAtPos(int position) {
        return mSearchRecords.get(position);
    }

    public void search(String query) {
        if (query != null && !query.isEmpty()) {
            if (query.length() >= ONLINESEARCH_TRESHHOLD && !isSearching && (lastSearched == null ||
                    (new Date().getTime() - lastSearched.getTime()) / 1000 > 3)) {
                isSearching = true;
                lastSearched = new Date();
                TMDBServiceHelper.searchKeywords(query, this);
            }
        }
        updateResults(query);
    }

    private void updateResults(String query) {
        mSearchRecords.clear();
        int i = 0;
        if (!historySearches.isEmpty())
            for (SearchRecord record : historySearches) {
                if (i <= SUGGESTIONS_COUNT && record.contains(query)) {
                    mSearchRecords.add(record);
                    i++;
                }
            }
        if (i > SUGGESTIONS_COUNT) {
            return;
        }
        if (!keywordSearches.isEmpty() && query != null && !query.isEmpty()) {
            for (SearchRecord record : keywordSearches) {
                if (i <= SUGGESTIONS_COUNT && record.contains(query)) {
                    mSearchRecords.add(record);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void addFromCursor(Cursor cursor) {
        if (cursor == null) return;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            historySearches.add(new SearchRecord(new Date(cursor.getLong(1)), true, cursor.getString(0)));
            cursor.moveToNext();
        }
    }

    private void reorderResults() {
        Collections.sort(mSearchRecords, new Comparator<SearchRecord>() {
            @Override
            public int compare(SearchRecord s1, SearchRecord s2) {
                return s1.compareTo(s2);
            }
        });
        notifyDataSetChanged();
        if (mSearchRecords.size() > SUGGESTIONS_COUNT)
            mSearchRecords.subList(SUGGESTIONS_COUNT, mSearchRecords.size() - 1).clear();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.view_search_list_item, parent, false);
        } else {
            view = convertView;
        }
        SearchRecord current = mSearchRecords.get(position);
        ((TextView) view.findViewById(R.id.search_result)).setText(current.query, TextView.BufferType.SPANNABLE);
        ((ImageView) view.findViewById(R.id.search_icon)).setImageResource(current.isHistory ? R.drawable.ic_history : R.drawable.ic_action_search);

        return view;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.registerObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mDataSetObservable.unregisterObserver(observer);
    }

    /**
     * Notifies the attached observers that the underlying data has been changed
     * and any View reflecting the data set should refresh itself.
     */
    public void notifyDataSetChanged() {
        mDataSetObservable.notifyChanged();
    }

    /**
     * Notifies the attached observers that the underlying data is no longer valid
     * or available. Once invoked this adapter is no longer valid and should
     * not report further data set changes.
     */
    public void notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated();
    }

    @Override
    public int getCount() {
        return mSearchRecords.size() < SUGGESTIONS_COUNT ? mSearchRecords.size() : SUGGESTIONS_COUNT;
    }

    @Override
    public SearchRecord getItem(int position) {
        return mSearchRecords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return mSearchRecords.size() == 0;
    }

    @Override
    public void success(String[] data) {
        isSearching = false;
        keywordSearches.clear();
        for (String kw : data) {
            keywordSearches.add(new SearchRecord(null, false, kw));
        }
        reorderResults();
    }

    @Override
    public void failure(APIError error) {
        isSearching = false;
    }
}