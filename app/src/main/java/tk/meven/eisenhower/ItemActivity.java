package tk.meven.eisenhower;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity{
    //Affiche les elements de la BDD dans une liste

    private ArrayList<Tache> liste;//Liste contenant les tâches
    private Tache save;//tache a restorer si annulation de la suppression
    private View mView;

    /*Si touche retour du téléphone*/
    @Override
    public void onBackPressed(){
        setResult(RESULT_OK, new Intent());
        finish();
    }

    /*Si touche retour de l'action Bar*/
    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_OK, new Intent());
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        /*Nécessaire à la touche retour de l'action Bar*/
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        mView = findViewById(R.id.item_activity_root);

        /*Récupère la catégorie des tâches à afficher*/
        Intent intent = getIntent();
        int categorie = intent.getIntExtra("CATEGORIE_TACHE", 0);

        /*Récupère les tâches depuis la BDD*/
        TacheDAO tacheDAO = new TacheDAO(this);
        liste = tacheDAO.recupTaches(categorie);

        /*View contenant la liste des tâches*/
        RecyclerView recyclerView = findViewById(R.id.item_activity_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        ItemArrayAdapter adapter = new ItemArrayAdapter(R.layout.affichageitems,liste);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                save = liste.get(position);
                int Id = liste.get(position).getId();
                tacheDAO.suppTache(Id);
                liste.remove(position);
                adapter.notifyDataSetChanged();
                Snackbar snackbar = Snackbar.make(mView,"Tache supprimée",Snackbar.LENGTH_LONG).setAction("Annuler", v -> {
                    tacheDAO.ajouterTache(save);
                    liste.add(save);
                    adapter.notifyDataSetChanged();
                    Snackbar snackbar1 = Snackbar.make(mView,"Annulé", Snackbar.LENGTH_LONG);
                    snackbar1.show();
                });
                snackbar.show();

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}