package com.fiuba.digitalmd

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fiuba.digitalmd.Farmacia.FarmaciaLandingActivity
import com.fiuba.digitalmd.Medico.MedicoLandingActivity
import com.fiuba.digitalmd.Models.*
import com.fiuba.digitalmd.ObraSocial.ObraSocialLandingActivity
import com.fiuba.digitalmd.Paciente.LandingActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_signin_user.*


class SignInActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_user)
        mAuth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            startActivity(Intent(this, ElegirUsuarioActivity::class.java))

        }
        btnForgot.setOnClickListener {
            //startActivity(Intent(this, ForgotActivity::class.java))
        }

        btnSignIn.setOnClickListener{
            confirmSignIn()
        }
    }

    private fun confirmSignIn() {
        ValidacionUtils.validarNoVacio(etEmail, "Por favor ingrese su email")
        ValidacionUtils.validarNoVacio(etPassword, "Por favor ingrese su password")

        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString()).matches()) {
            etEmail.error = "Por favor ingrese un email valido"
            etEmail.requestFocus()
            return
        }
        val dialog = SignInDialog()
        hideKeyboard(this)
        dialog.show(supportFragmentManager, "Dialog1")
        signIn(dialog)
    }

    private fun signIn(dialog: SignInDialog) {
        mAuth.signInWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Bienvenido a DIGITAL MD", Toast.LENGTH_SHORT).show()
                    val uid = FirebaseAuth.getInstance().uid
                    val ref = FirebaseDatabase.getInstance().getReference("/signup/$uid")
                    ref.addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onCancelled(p0: DatabaseError) {
                            dialog.dismiss()
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            val data = p0.value.toString()
                            if (data.contains("paciente", ignoreCase = true)) {
                                InfoActual.setUsuarioActual(p0.getValue(User::class.java)!!)
                                startActivity(Intent(baseContext, LandingActivity::class.java))
                            }
                            if (data.contains("medico", ignoreCase = true)) {
                                InfoActual.setMedicoActual(p0.getValue(Medico::class.java)!!)
                                startActivity(Intent(baseContext, MedicoLandingActivity::class.java))
                            }
                            if (data.contains("farmacia", ignoreCase = true)) {
                                InfoActual.setFarmaciaActual(p0.getValue(Farmacia::class.java)!!)
                                startActivity(Intent(baseContext, FarmaciaLandingActivity::class.java))
                            }
                            if (data.contains("obra social", ignoreCase = true)) {
                                InfoActual.setObraSocialActual(p0.getValue(ObraSocial::class.java)!!)
                                startActivity(Intent(baseContext, ObraSocialLandingActivity::class.java))
                            }
                            dialog.dismiss()
                        }
                    })
                } else {
                    dialog.dismiss()
                    Toast.makeText(baseContext, "Usuario o contraseña invalidos", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
