package com.example.diazfelipee2.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.diazfelipee2.dto.Equipo;
import com.example.diazfelipee2.helpers.DatabaseHelper;

import java.util.ArrayList;

public class EquipoDAL {
    private DatabaseHelper dbHelper; // obtener el helper
    private Equipo equipo;

    public EquipoDAL(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.equipo = new Equipo();
        // Testing
        SQLiteDatabase db = dbHelper.getWritableDatabase();
    }

    public EquipoDAL(Context context, Equipo serie) {
        this.dbHelper = new DatabaseHelper(context);
        this.equipo = serie;
    }

    public boolean insertar() {
        return this.tryInsert();
    }

    public boolean insertar(String marca, String descripcion)
    {
        this.equipo.setMarca(marca);
        this.equipo.setDescripcion(descripcion);


        return this.tryInsert();
    }

    public ArrayList<Equipo> seleccionar()
    {
        ArrayList<Equipo> lista = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor consulta = db.rawQuery("SELECT * FROM equipo", null);

        if(consulta.moveToFirst()) {
            do {
                int serie = consulta.getInt(0);
                String marca = consulta.getString(1);
                String descripcion = consulta.getString(2);

                Equipo equipo = new Equipo(serie,marca,descripcion);
                lista.add(equipo);
                /*
                // forma B
                Serie serie = new Serie();
                serie.setId(id);
                serie.setNombre(nombre);*/

            } while(consulta.moveToNext());

        }

        // EJ: Where con parámetros
        // Cursor consulta = db.rawQuery("SELECT * FROM serie WHERE categoria = ?", new String[]{ String.valueOf("Sci-fi") });

        return lista;
    }



    public boolean actualizar(Equipo equipo)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues c = new ContentValues(); // Objeto tipo clave-valor
        c.put("marca", equipo.getMarca());
        c.put("descripcion", equipo.getDescripcion());

        try {
            int filasAfectadas;
            filasAfectadas = db.update(
                    "equipo",
                    c,
                    "serie = ?",
                    new String[] { String.valueOf(equipo.getSerie()) }
            );
            // if(filasAfectadas > 0) return true; else return false;
            return (filasAfectadas > 0);
        } catch (Exception e) {

        }

        return false;
    }

    public boolean eliminar(int serie) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int filasAfectadas;

/*        db.delete("serie","id = ? and nombre = ?",
                new String[] {
                        String.valueOf(id),
                        String.valueOf("the boys")
                });*/

        try {
            filasAfectadas = db.delete("equipo","serie = ?",
                    new String[] { String.valueOf(serie) });
        } catch (Exception e) {
            return false;
        }

        return (filasAfectadas == 1);

    }


    private boolean tryInsert() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

/*
        String[] argumentos = new String[]{"Breaking Bad","Suspenso","36"};
        db.execSQL("INSERT INTO serie(nombre,categoria,capitulos) VALUES(?,?,?);", argumentos);
*/

        ContentValues c = new ContentValues(); // Objeto tipo clave-valor
        c.put("nombre", this.equipo.getMarca());
        c.put("categoria", this.equipo.getDescripcion());


        try {
            db.insert("equipo", null, c);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Equipo getEquipo()
    {
        return this.equipo;
    }
}
