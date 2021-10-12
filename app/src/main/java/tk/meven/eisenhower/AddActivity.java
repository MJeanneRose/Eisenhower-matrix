package tk.meven.eisenhower;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    //Activité permettant d'ajouter une tache.
    //l'activité renvoie la catégorie de la tache enregistrée à MainActivity pour que celle-ci raffraichisse le bouton correspondant

    private Spinner spin;
    private TextInputLayout Titre_layout;
    private TextInputLayout Description_layout;
    private CheckBox checkBox;
    private DatePicker calendrier;
    private SwitchCompat switchDate;

    private final String CATEGORIE_TACHE = "CATEGORIE_TACHE";

    private Tache tache;
    private Calendar date;
    private TacheDAO tacheDAO;
    private Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        /*Pour afficher le bouton retour dans l'actionBar*/
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //show back button

        tache = new Tache();//Contient les informations de la tache
        tacheDAO = new TacheDAO(this);//Necessaire pour enregistrer la tache dans la bdd
        date = Calendar.getInstance();
        date.add(Calendar.MONTH,1);//L'instance obtient la date actuelle avec janvier = 0

        res = getResources();
        View mView = findViewById(R.id.add_activity_root);

        /*Spinner selection de categorie*/
        spin = findViewById(R.id.add_activity_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.choix_liste, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);

        /*Champs de texte pour le titre*/
        Titre_layout = findViewById(R.id.add_activity_Layout_Titre);
        Titre_layout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {//Affiche erreur si aucun titre
                if(s.length() < 1)
                    Titre_layout.setError("Entrez un titre");
                if(s.length() > 0)
                    Titre_layout.setError(null);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /*Champs de texte pour la description*/
        Description_layout = findViewById(R.id.add_activity_Layout_Des);

        /*Switch présence de date ou non*/
        switchDate = findViewById(R.id.add_activity_switch);
        switchDate.setText(res.getString(R.string.date,""));
        switchDate.setOnCheckedChangeListener(this);

        /*Checkbox enregistrer la date ou non*/
        checkBox = findViewById(R.id.add_activity_checkBox);
        checkBox.setOnCheckedChangeListener(this);

        /*Selection de la date (calendrier)*/
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddActivity.this, (view, hourOfDay, minute) -> {
            date.set(Calendar.HOUR_OF_DAY,hourOfDay);
            date.set(Calendar.MINUTE, minute);
            tache.setDate(date.getTimeInMillis(),true);
            switchDate.setText(res.getString(R.string.date,tache.getDateString()));
        },0,0,true);

        /*Snackbar qui demande si on veut enregistrer l'heure*/
        Snackbar snackbar = Snackbar.make(mView,"Ajouter une heure ?",Snackbar.LENGTH_LONG).setAction("oui", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.show();
            }
        });

        /*Calendrier pour selectionner la date*/
        calendrier = findViewById(R.id.add_activity_calendarView);
        calendrier.setMinDate(System.currentTimeMillis()-1000);
        calendrier.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            date.set(year,monthOfYear+1,dayOfMonth);
            tache.setDate(date.getTimeInMillis(), false);
            checkBox.setChecked(true);
            switchDate.setChecked(false);
            snackbar.show();
            switchDate.setText(res.getString(R.string.date,tache.getDateString()));
        });

        /*Bouton pour enregistrer la tache*/
        Button bouton = findViewById(R.id.add_activity_validation);
        bouton.setOnClickListener(v -> {
            if(formulaire_ok()) {
                Intent intent = new Intent();

                if(tacheDAO.ajouterTache(tache))
                    intent.putExtra(CATEGORIE_TACHE, Integer.parseInt(tache.getCategorie()));
                else//Si erreur enregistrement
                    intent.putExtra(CATEGORIE_TACHE, 42);

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    //Selection de la catégorie (spinner)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(spin.getId() == parent.getId()){
            tache.setCategorie(position);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /**
     * Vérifie les données du formulaire et les enregistre dans la tâche
     * @return
     * faux si erreur vrai sinon
     */
    private boolean formulaire_ok() {
        String titre = Titre_layout.getEditText().getText().toString();
        if (titre.isEmpty()) {
            Titre_layout.setError("Entrez un titre");
            return false;
        }
        tache.setTitre(titre);
        String description = Description_layout.getEditText().getText().toString();
        if(!description.isEmpty())
            tache.setDescription(description);
        return true;
    }

    //Si changement du switch ou de la checkbox
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.add_activity_switch){//Switch : affiche ou non le calendrier
            hideKeyboard(this);
            if(isChecked)
                calendrier.setVisibility(View.VISIBLE);
            else
                calendrier.setVisibility(View.GONE);
        }
        else if(buttonView.getId()== R.id.add_activity_checkBox){//checkbox : met la date enregistrée ou met 0
            if(isChecked){
                tache.setDate(date.getTimeInMillis(), false);
                switchDate.setText(res.getString(R.string.date,tache.getDateString()));
            }
            else{
                tache.setDate(0, false);
                switchDate.setText(res.getString(R.string.date,""));
            }
        }
    }

    //clique sur le bouton retour de l'action bar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //enlève le clavier
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


}