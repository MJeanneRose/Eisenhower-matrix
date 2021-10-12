package tk.meven.eisenhower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //Activité principale affichant les 4 boutons d'affichage et le bouton d'ajout de tâche
    //Pour lancer une des 4 activités d'affichage, on la lance en lui donnant la catégorie de tache à afficher

    private Button bouton1;//urgent-important
    private Button bouton2;//urgent-pImportant
    private Button bouton3;//Important-pUrgent
    private Button bouton4;//pImportant-pUrgent


    private Resources res;
    private int tache_a_rafraichir;
    private TacheDAO tacheDAO;

    private final int ADD_ACTIVITY_REQUEST_CODE = 1;
    private final int ITEM_ACTIVITY_REQUEST_CODE = 2;
    private final String CATEGORIE_TACHE = "CATEGORIE_TACHE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.EisenhowerTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bouton1 = findViewById(R.id.activity_main_button1);
        bouton2 = findViewById(R.id.activity_main_button2);
        bouton3 = findViewById(R.id.activity_main_button3);
        bouton4 = findViewById(R.id.activity_main_button4);
        FloatingActionButton add = findViewById(R.id.activity_main_floatingActionButton);

        bouton1.setOnClickListener(this);
        bouton2.setOnClickListener(this);
        bouton3.setOnClickListener(this);
        bouton4.setOnClickListener(this);
        add.setOnClickListener(this);

        bouton1.setTag(0);
        bouton2.setTag(1);
        bouton3.setTag(2);
        bouton4.setTag(3);
        add.setTag(4);

        tacheDAO = new TacheDAO(this);

        res = getResources();

        for(int i=0;i<4;i++)
            MAJ_bouton(i);

    }

    @Override
    public void onClick(View v) {
        int reponse = (int)v.getTag();
        Intent myIntent;
        if(reponse == 4){//Bouton Ajouter tache
            myIntent = new Intent(MainActivity.this, AddActivity.class);
            startActivityForResult(myIntent, ADD_ACTIVITY_REQUEST_CODE);
        }
        else {//les 4 boutons "catégorie"
            myIntent = new Intent(MainActivity.this, ItemActivity.class);
            tache_a_rafraichir = reponse;
            myIntent.putExtra(CATEGORIE_TACHE,reponse);
            startActivityForResult(myIntent, ITEM_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {//Si add activity
            tache_a_rafraichir = data.getIntExtra(CATEGORIE_TACHE, 0);
            if(tache_a_rafraichir != 42){
                MAJ_bouton(tache_a_rafraichir);
                Toast.makeText(this, "Tâche enregistrée", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(this, "Erreur : enregistrement", Toast.LENGTH_LONG).show();

        }
        if(requestCode == ITEM_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){//Si item activity
                    MAJ_bouton(tache_a_rafraichir);
        }
    }

    protected void MAJ_bouton(int categorie){
        switch(categorie){
            case 0:
                bouton1.setText(res.getString(R.string.urgent_important, tacheDAO.getNbTache(categorie)));
                break;
            case 1:
                bouton2.setText(res.getString(R.string.urgent_pas_important, tacheDAO.getNbTache(categorie)));
                break;
            case 2:
                bouton3.setText(res.getString(R.string.important_pas_urgent, tacheDAO.getNbTache(categorie)));
                break;
            case 3:
                bouton4.setText(res.getString(R.string.pas_important_pas_urgent, tacheDAO.getNbTache(categorie)));
                break;
        }
    }
}