package com.fiuba.digitalmd.Medico

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.fiuba.digitalmd.Models.InfoActual
import com.fiuba.digitalmd.Paciente.MisDiagnosticosActivity
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignInActivity
import com.fiuba.digitalmd.SignedInActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_medico_landing.*

class MedicoLandingActivity : SignedInActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medico_landing)

        val toolbar: Toolbar = findViewById(R.id.toolbarProfile)
        setSupportActionBar(toolbar)
        setViews(toolbar)

        btnHacerDiagnosticos.setOnClickListener {
            startActivity(Intent(baseContext, SwipeActivity::class.java))
        }

        btnNuevaReceta.setOnClickListener {
            startActivity(Intent(baseContext, HacerRecetaActivity::class.java))
        }

        btnVerRecetas.setOnClickListener {
            startActivity(Intent(baseContext, VerRecetasMedicoActivity::class.java))
        }
    }
    override fun onBackPressed() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }


    private fun setViews(toolbar: Toolbar) {

        toolbar.title = InfoActual.getMedicoActual().apellido + " " + InfoActual.getMedicoActual().matricula
        //Seteo logo de la orga
        Picasso.get().load(InfoActual.getMedicoActual().url).into(imgMedicoProfile)

    }

}
