package tk.meven.eisenhower;

import java.util.Calendar;

public class Tache {
    //Classe définissant une tache avec les getter et setter
    private String Titre;
    private String Description;
    private long date;//date en ms
    private boolean isTime;
    private int categorie;
    private String id;

    /**
     * Création d'une tâche vierge
     */
    public Tache(){
        Titre = null;
        Description = null;
        date = 0;
        isTime = false;
    }

    /**
     * Enregistre le titre de la tâche
     * @param titre
     * Titre à enegistrer
     */
    public void setTitre(String titre) {
        Titre = titre;
    }

    /**
     * Enregistre la catégorie dans la tache
     * @param categorie
     * Catégorie à enregistrer, commence à 0
     */
    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }

    /**
     * Enregistre la description fournie et enregistre la présence d'une description
     * @param description
     * Description à enregistrer
     */
    public void setDescription(String description) {
        Description = description;
    }

    /**
     * Enregistre la date fournie et enregistre la présence d'une date.
     * @param date
     * Date à enregistrer, 0 pour réinitisaliser
     * @param presence
     * présence d'heure et minute
     */
    public void setDate(long date, boolean presence) {
        isTime = presence;
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Récupère la catégorie de la tache
     * @return
     * Catégorie de la tache (indice commence à 0)
     */
    public String getCategorie() {
        return String.valueOf(categorie);
    }

    /**
     * Récupère la date de la tache
     * @return
     * Date de la tache
     */
    public String getDateString() {
        if(date != 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);

            String date = calendar.get(Calendar.DAY_OF_MONTH) +"/"+
                    calendar.get(Calendar.MONTH)+"/"+
                    calendar.get(Calendar.YEAR);
            if(isTime) {
                date += " " + calendar.get(Calendar.HOUR_OF_DAY) + ":";
                if (calendar.get(Calendar.MINUTE) < 10)
                    date += "0";
                date += calendar.get(Calendar.MINUTE);
            }
            return date;
        }
        else
            return null;
    }

    /**
     * Surcharge pour récuperer la date de la tache en millisecondes
     * @return
     * Date de la tache
     */
    public long getDateMillisecond() { return date; }

    /**
     * Récupère la description de la tâche
     * @return
     *  Description de la tâche
     */
    public String getDescription() {
        return Description;
    }

    /**
     * Récupère le titre de la tâche
     * @return
     * Titre de la tâche
     */
    public String getTitre() {
        return Titre;
    }

    public int getId() {
        return Integer.parseInt(id);
    }

    public boolean isTime() {
        return isTime;
    }
}
