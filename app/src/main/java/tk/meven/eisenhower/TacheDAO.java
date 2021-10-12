package tk.meven.eisenhower;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class TacheDAO {
    //Permet l'enrgistrement et la récupération des taches depuis la BDD en utilisant le contrat (FeedReaderContract)
    private final FeedReaderDbHelper mDbHelper;
    private static SQLiteDatabase db;

    public TacheDAO(Context context){
        mDbHelper = new FeedReaderDbHelper(context);
        if(db == null)
            db = mDbHelper.getWritableDatabase();

    }

    /**
     * Récupère toutes les tâches d'une catégorie
     * @param categorie catégorie à récupérer
     * @return ArrayList contenant la liste des tâches
     */
    public ArrayList<Tache> recupTaches(int categorie){

        Tache tache;
        long Date;
        ArrayList<Tache> liste = new ArrayList<>();

        // Define a projection that specifies which columns from the database
    // you will actually use after this query.
        String[] projection = {
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITRE,
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION,
                FeedReaderContract.FeedEntry.COLUMN_NAME_IsTime,
                FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
        };

        // Filter results WHERE categorie" = '0' | '1' | '2' | '3'
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORIE + " = ?";
        String[] selectionArg = {String.valueOf(categorie)};

        String sortOrder = FeedReaderContract.FeedEntry.COLUMN_NAME_DATE + " ASC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArg,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while(cursor.moveToNext()) {
            tache = new Tache();
            String titre = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITRE));
            String Desc = cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION));
            Date = cursor.getLong(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE));
            boolean isTime = cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_IsTime)) == 1;
            tache.setId(cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry._ID)));
            tache.setTitre(titre);
            tache.setDescription(Desc);
            tache.setDate(Date, isTime);
            liste.add(tache);
        }
        cursor.close();

        return liste;
    }

    /**
     * Ajoute une tache dans la base de données
     * @param t tache à ajouter
     * @return faux en cas d'erreur, vrai sinon
     */
    public boolean ajouterTache(Tache t){
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORIE, t.getCategorie());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITRE, t.getTitre());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, t.getDescription());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, t.getDateMillisecond());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IsTime,t.isTime());

        long resultat = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        return resultat != -1;
    }

    /**
     * Supprime une tâche de la base de données
     * @param id id de la tache à supprimer
     */
    public void suppTache(long id){
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, "_ID=" + id, null);
    }

    public int getNbTache(int categorie){
        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM "+FeedReaderContract.FeedEntry.TABLE_NAME+
                " WHERE "+
                FeedReaderContract.FeedEntry.COLUMN_NAME_CATEGORIE+
                " =?",new String[]{String.valueOf(categorie)});
        int count = 0;
            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
        cursor.close();
        return count;
    }
}
