package com.lista.listacompra.accesoDatos.baseDatos;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {ListaCompraBD.class, ProductoBD.class, ListaProductoJoin.class}, version = 2)
@TypeConverters({ ConvertidorProducto.class })
public abstract class AppDatabase extends RoomDatabase {
    public abstract ListaCompraDao listaCompraDao();

    public abstract ProductoDao productoDao();

    private static volatile AppDatabase INSTANCE;

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
