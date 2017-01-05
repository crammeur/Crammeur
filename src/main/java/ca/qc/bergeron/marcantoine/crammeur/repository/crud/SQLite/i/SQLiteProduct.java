package ca.qc.bergeron.marcantoine.crammeur.repository.crud.SQLite.i;

/**
 * Created by Marc-Antoine on 2016-12-27.
 */

public interface SQLiteProduct extends SQLite {

    String T_Produit = "Products";        // nom de la table
    String F_nom = "F1_1";
    String F_description = "F1_2";
    String F_price = "F1_3";
    String F_unit = "F1_4";
    String F_company = "F1_5";

    String CREATE_TABLE_Product = "CREATE TABLE " + T_Produit + " (" +
            F_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            F_nom + " TEXT, " +
            F_description + " TEXT, " +
            F_price + " REAL, " +
            F_unit + " INTEGER, " +
            F_company + " INTEGER, " +
            "FOREIGN KEY ("+ F_company +") REFERENCES Companys(Id) ON DELETE CASCADE" +
            ")";

}
