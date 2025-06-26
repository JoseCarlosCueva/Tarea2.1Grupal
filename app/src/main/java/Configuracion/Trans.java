package Configuracion;

public class Trans {

    //nombre de la base datos
    public static final int Version = 1;
    public static final String DBname = "PM01";
    //nombre de la tabla
    public static final String TablePersonas = "personas";

    // columnas
    public static final String id = "id";
    public static final String nombre = "nombre";
    public static final String apellido = "apellido";
    public static final String edad = "edad";
    public static final String correo = "correo";
    public static final String telefono = "telefono";
    public static final String foto = "foto";

    //DDL Create
    public static final String CreateTablePerson =
            "CREATE TABLE " + TablePersonas + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT, " +
                    "apellido TEXT, " +
                    "edad INTEGER, " +
                    "correo TEXT, " +
                    "telefono TEXT, " +
                    "foto TEXT )";

    public static final String SelectAllPerson =
            "SELECT * FROM " +
                    TablePersonas;

    public static final String DropTablePersona =
            "DROP TABLE IF EXISTS "+ TablePersonas;



}
