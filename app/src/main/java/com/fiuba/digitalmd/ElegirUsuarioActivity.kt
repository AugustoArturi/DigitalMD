package com.fiuba.digitalmd

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import com.fiuba.digitalmd.Farmacia.SignUpFarmaciaActivity
import com.fiuba.digitalmd.Medico.SignUpMedicoActivity
import com.fiuba.digitalmd.ObraSocial.SignUpObraSocialActivity
import com.fiuba.digitalmd.Paciente.SignUpPacienteActivity

import kotlinx.android.synthetic.main.activity_dermatologia.*

class ElegirUsuarioActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dermatologia)
        setSupportActionBar(findViewById(R.id.toolbarProfile))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnMedico.setOnClickListener {
            val intent = Intent(this, SignUpMedicoActivity::class.java)
            startActivity(intent)
        }

        btnPaciente.setOnClickListener {
            val intent = Intent(this, SignUpPacienteActivity::class.java)
            startActivity(intent)
        }
        btnFarmacia.setOnClickListener {
            val intent = Intent(this, SignUpFarmaciaActivity::class.java)
            startActivity(intent)
        }

        btnObrasocial.setOnClickListener {
            val intent = Intent(this, SignUpObraSocialActivity::class.java)
            startActivity(intent)
        }



    }

}
