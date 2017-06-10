package net.egordmitriev.popshows.api.database.tables;

import net.egordmitriev.popshows.api.database.tables.base.MediaTable;
import net.egordmitriev.popshows.pojo.tmdb.SerieModel;

import java.util.Date;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class SeriesTable extends MediaTable {
    public static final String sTableName = "series_lists";
    private static boolean sCreated = false;

    public static boolean createTable() {
        if (sCreated) return false;
        createTable(sTableName);
        sCreated = true;
        return true;
    }

    public static boolean upsert(SerieModel.Base[] item, int identifier) {
        return upsert(sTableName, item, identifier);
    }

    public static SerieModel.Base[] get(int identifier) {
        return get(sTableName, identifier, SerieModel.Base[].class);
    }

    public static SerieModel.Base[] tryGetLocal(int identifier) {
        return tryGetLocal(sTableName, identifier, SerieModel.Base[].class);
    }

    public static Date getModified(int identifier) {
        return getModified(sTableName, identifier);
    }

    public static void drop() {
        drop(sTableName);
        sCreated = false;
    }
}
