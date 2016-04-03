package net.egordmitriev.watchall.api.database.tables;

import net.egordmitriev.watchall.api.database.tables.base.MediaTable;
import net.egordmitriev.watchall.pojo.tmdb.SerieModel;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class SeriesTable extends MediaTable {
    private static final String sTableName = "series_lists";
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

    public SerieModel.Base[] get(int identifier) {
        return get(sTableName, identifier, SerieModel.Base[].class);
    }
}
