package ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.i;

/**
 * Created by Marc-Antoine on 2016-12-27.
 */

public interface SQLiteCompany extends SQLite {

    String T_Company = "Companys";        // nom de la table
                  // nom de chacun des champs (F pour field)
    String F_nom = "F1_1";

    String CREATE_TABLE_Company = "CREATE TABLE " + T_Company + " ( " +
            F_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            F_nom + " TEXT " +
            " )";

}
