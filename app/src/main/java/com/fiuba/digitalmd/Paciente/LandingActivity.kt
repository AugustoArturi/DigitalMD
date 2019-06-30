package com.fiuba.digitalmd.Paciente

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.fiuba.digitalmd.Models.InfoActual
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignInActivity
import com.fiuba.digitalmd.SignedInActivity
import kotlinx.android.synthetic.main.activity_landing.*

class LandingActivity : SignedInActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        val toolbar: Toolbar = findViewById(R.id.toolbarProfile)
        toolbar.title = InfoActual.getUsuarioActual().nombre + " " + InfoActual.getUsuarioActual().apellido
        setSupportActionBar(toolbar)

        btnDermatologia.setOnClickListener {
            val intent = Intent(this, MisDiagnosticosActivity::class.java)
            startActivity(intent)
        }

        btnRecetas.setOnClickListener {
            val intent = Intent(this, MisRecetasActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }



}
