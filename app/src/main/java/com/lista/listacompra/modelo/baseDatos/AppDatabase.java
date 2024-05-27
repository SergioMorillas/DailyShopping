package com.lista.listacompra.modelo.baseDatos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * Base de datos de la aplicación que gestiona las entidades {@link ListaCompraBD},
 * {@link ProductoBD} y {@link ListaProductoJoin}.
 */
@Database(entities = {ListaCompraBD.class, ProductoBD.class}, version = 2)
@TypeConverters({ ConvertidorProducto.class })
public abstract class AppDatabase extends RoomDatabase {
    /**
     * Obtiene el DAO para la entidad {@link ListaCompraBD}.
     *
     * @return Un {@link ListaCompraDao} para interactuar con la base de datos.
     */
    public abstract ListaCompraDao listaCompraDao();

    /**
     * Obtiene el DAO para la entidad {@link ProductoBD}.
     *
     * @return Un {@link ProductoDao} para interactuar con la base de datos.
     */
    public abstract ProductoDao productoDao();

    /**
     * Instancia única de la base de datos.
     */
    private static volatile AppDatabase INSTANCE;

    /**
     * Obtiene la instancia de la base de datos.
     * Si no existe, la crea utilizando el contexto proporcionado.
     *
     * @param context El contexto de la aplicación.
     * @return La instancia única de {@link AppDatabase}.
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                                    context,
                                    AppDatabase.class,
                                    "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
