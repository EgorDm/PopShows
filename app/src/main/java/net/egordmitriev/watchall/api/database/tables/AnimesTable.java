package net.egordmitriev.watchall.api.database.tables;

import net.egordmitriev.watchall.api.database.tables.base.MediaTable;
import net.egordmitriev.watchall.pojo.anilist.AnimeModel;

import java.util.Date;

/**
 * Created by EgorDm on 4/3/2016.
 */
public class AnimesTable extends MediaTable {
    public static final String sTableName = "animes_lists";
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

    public static AnimeModel.Base[] get(int identifier) {
        return get(sTableName, identifier, AnimeModel.Base[].class);
    }

    public static AnimeModel.Base[] tryGetLocal(int identifier) {
        return tryGetLocal(sTableName, identifier, AnimeModel.Base[].class);
    }

    public static Date getModified(int identifier) {
        return getModified(sTableName, identifier);
    }

    public static void drop() {
        drop(sTableName);
        sCreated = false;
    }
}
