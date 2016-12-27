package ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.i;

/**
 * Created by Marc-Antoine on 2016-12-27.
 */

public interface SQLiteProduct {

    String T_Produit = "Product";        // nom de la table
    String F_id = "F_id";                // nom de chacun des champs (F pour field)
    String F_nom = "F1_1";
    String F_description = "F1_2";
    String F_price = "F1_3";

    String CREATE_TABLE_Produit = "CREATE TABLE " + T_Produit + " (" +
            F_id + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
            F_nom + " TEXT ," +
            F_description + " TEXT ," +
            F_price + " REAL ," +
            "GEN_LASTMOD                INTEGER " + // Used for debugging purpose
            ")";

}
