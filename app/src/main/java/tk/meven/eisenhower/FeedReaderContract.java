package tk.meven.eisenhower;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    //Contrat d'utilisation de la BDD : nom de la table, des colonnes, etc.
    private FeedReaderContract(){}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "taches";
        public static final String COLUMN_NAME_TITRE = "titre";
        public static final String COLUMN_NAME_CATEGORIE = "categorie";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IsTime = "isTime";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_CATEGORIE + " INTEGER NOT NULL," +
                    FeedEntry.COLUMN_NAME_TITRE + " TEXT NOT NULL," +
                    FeedEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    FeedEntry.COLUMN_NAME_IsTime + " INTEGER DEFAULT 0," +
                    FeedEntry.COLUMN_NAME_DATE + " LONG)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
}
