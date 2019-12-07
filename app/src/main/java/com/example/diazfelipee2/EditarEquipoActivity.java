package com.example.diazfelipee2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diazfelipee2.dal.EquipoDAL;
import com.example.diazfelipee2.dto.Equipo;

public class EditarEquipoActivity extends AppCompatActivity {

    private EditText editMarca;
    private EditText editDescripcion;
    private Button btnActualizar;
    private EquipoDAL equipoDAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_equipo);

        this.editMarca = (EditText) findViewById(R.id.editMarca);
        this.editDescripcion = (EditText) findViewById(R.id.editDescripcion);;
        this.btnActualizar = (Button) findViewById(R.id.btnActualizar2);

        this.equipoDAL = new EquipoDAL(getApplicationContext(), (Equipo) getIntent().getSerializableExtra("equipo") );


        this.editMarca.setText(equipoDAL.getEquipo().getMarca());
        this.editDescripcion.setText(equipoDAL.getEquipo().getDescripcion());

        // Acción para el botón actualizar
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Equipo s = equipoDAL.getEquipo();
                s.setMarca(String.valueOf(editMarca.getText()));
                s.setDescripcion(String.valueOf(editDescripcion.getText()));


                if(equipoDAL.actualizar(s)) {
                    Toast.makeText(getApplicationContext(), "Actualizado!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "NO Actualizado!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
