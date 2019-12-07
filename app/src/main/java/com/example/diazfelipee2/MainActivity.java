package com.example.diazfelipee2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diazfelipee2.dal.EquipoDAL;
import com.example.diazfelipee2.dto.Equipo;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EquipoDAL equipoDAL;
    private ListView listSeries;
    private ArrayAdapter<Equipo> adapter;
    private ArrayList<Equipo> listaEquipos;
    private int codPosicion = 0;

    @Override
    protected void onResume() {
        super.onResume();
        actualizarLista();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.equipoDAL = new EquipoDAL(getApplicationContext(), new Equipo());
        this.listaEquipos = new EquipoDAL(getBaseContext()).seleccionar();

        // i.- Enlazar la interfaz gráfica al componente
        this.listSeries = (ListView) findViewById(R.id.listSeries);

        // ii.- Crear ArrayAdapter y asociarlo al cRud
        this.adapter = new ArrayAdapter<Equipo>(
                getApplicationContext(),
                android.R.layout.simple_list_item_1,
                this.listaEquipos
        ){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);

                TextView textView=(TextView) view.findViewById(android.R.id.text1);

                /*YOUR CHOICE OF COLOR*/
                textView.setTextColor(Color.BLACK);

                return view;
            }
        };

        // iii.- Asociar el ArrayAdapter al componente ListView
        this.listSeries.setAdapter(adapter);

        // Sólo para construir el mensaje de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmación");
        builder.setMessage("¿Desea borrar el equipo?");
        builder.setPositiveButton("Si, borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int serie = ((Equipo) listaEquipos.get(codPosicion)).getSerie();
                        boolean r = equipoDAL.eliminar(serie);
                        if(r){
                            Toast.makeText(getApplicationContext(), "Se ha eliminado correctamente", Toast.LENGTH_SHORT).show();
                            actualizarLista();
                        } else {
                            Toast.makeText(getApplicationContext(), "No se ha podido eliminar el equipo", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        final AlertDialog dialog = builder.create();

        // Tap presionado por un tiempo
        listSeries.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                codPosicion = posicion;
                dialog.show();
                return true;
            }
        });

        // Tap simple
        listSeries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
                codPosicion = posicion;
                abrirEditarEquipoActivity();
            }
        });
/*
        // TEST DE ELIMINAR
        if(serieDAL.eliminar(1)) {
            Toast.makeText(
                    getApplicationContext(),
                    "Se eliminó!",
                    Toast.LENGTH_LONG
            ).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "NO se eliminó!",
                    Toast.LENGTH_LONG
            ).show();
        }*/

/*
        // TEST INSERTAR
        Serie s = new Serie("The boys", "Sci-Fi", 8);
        this.serieDAL = new SerieDAL(getApplicationContext(), s);

        if(serieDAL.insertar()) {
            Toast.makeText(getApplicationContext(), "OK! Insertó", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "MAL! NO Insertó", Toast.LENGTH_LONG).show();

        }*/

    }

    private void abrirEditarEquipoActivity() {
        Intent intento = new Intent(MainActivity.this, EditarEquipoActivity.class);
        Equipo e = (Equipo) listaEquipos.get(codPosicion);

        intento.putExtra("equipo", e);
        startActivityForResult(intento, 100);
    }

    private void actualizarLista() {
        adapter.clear();
        adapter.addAll(equipoDAL.seleccionar());
        adapter.notifyDataSetChanged();
    }
}
