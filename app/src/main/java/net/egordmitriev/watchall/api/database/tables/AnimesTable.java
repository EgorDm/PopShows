package net.egordmitriev.watchall.api.database.tables;

import net.egordmitriev.watchall.api.database.tables.base.MediaTable;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class AnimesTable extends MediaTable {
    private static final String sTableName = "animes_lists";
    private static boolean sCreated = false;

    public static boolean createTable() {
        if (sCreated) return false;
        createTable(sTableName);
        sCreated = true;
        return true;
    }

    public static boolean upsert(AnimeModel.Base[] item, int identifier) {
        return upsert(sTableName, item, identifier);
    }

    public AnimeModel.Base[] get(int identifier) {
        return get(sTableName, identifier, AnimeModel.Base[].class);
    }
}
