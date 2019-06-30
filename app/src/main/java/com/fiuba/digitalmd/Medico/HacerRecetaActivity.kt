package com.fiuba.digitalmd.Medico

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.fiuba.digitalmd.Models.InfoActual
import com.fiuba.digitalmd.Models.Receta
import com.fiuba.digitalmd.Models.idReceta
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignedInActivity
import com.fiuba.digitalmd.Models.ValidacionUtils.validarNoVacio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_hacer_receta.*


class HacerRecetaActivity : SignedInActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hacer_receta)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Realice una receta"
        setSupportActionBar(toolbar)

        mAuth = FirebaseAuth.getInstance()

        btnCrearReceta.setOnClickListener {
            if(validarCampos())
                subirRecetaAFirebase()
        }


    }

    private fun subirRecetaAFirebase() {
        val matricula = InfoActual.getMedicoActual().matricula
        val dniPaciente = dniPacientebox.text.toString()
        val obrasocial = obrasocialbox.text.toString().toUpperCase()
        val diagnostico = diagnosticobox.text.toString()
        val farmaco = farmacobox.text.toString()
        val cantidadFarmaco = cantidadbox.text.toString()
        val modoConsumo = consumobox.text.toString()
        val lugar = lugarbox.text.toString()
        val fecha = fechabox.text.toString()
        val database = FirebaseDatabase.getInstance().getReference("/idReceta")

        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val idReceta = p0.getValue(idReceta::class.java)
                database.child("numero").setValue(idReceta!!.numero+1)
                val receta = Receta(matricula,dniPaciente,obrasocial,diagnostico,farmaco,cantidadFarmaco,modoConsumo,lugar,fecha,idReceta.numero+1)
                val ref =  FirebaseDatabase.getInstance().getReference("/recetas/${idReceta.numero+1}")
                ref.setValue(receta)
                startActivity(Intent(baseContext, MedicoLandingActivity::class.java))
            }
        })
    }

    private fun validarCampos(): Boolean {
        validarNoVacio(dniPacientebox, "Por favor ingresa DNI del paciente")
        validarNoVacio(obrasocialbox, "Por favor ingresa DNI del paciente")
        validarNoVacio(diagnosticobox, "Por favor ingresa el diagnostico")
        validarNoVacio(farmacobox, "Por favor ingresa el farmaco")
        validarNoVacio(cantidadbox, "Por favor ingresa cantidad del farmaco")
        validarNoVacio(consumobox, "Por favor ingresa la forma de consumo del farmaco")
        validarNoVacio(lugarbox, "Por favor ingresa el lugar donde trabajas")
        validarNoVacio(fechabox, "Por favor ingresa la fecha")
        return true

    }

}

