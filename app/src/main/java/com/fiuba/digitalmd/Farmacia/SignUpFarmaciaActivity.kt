package com.fiuba.digitalmd.Farmacia

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.fiuba.digitalmd.Models.Farmacia
import com.fiuba.digitalmd.R
import com.fiuba.digitalmd.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_sign_up_farmacia.*
import kotlinx.android.synthetic.main.activity_sign_up_medico.CircleImageViewMedico
import kotlinx.android.synthetic.main.activity_sign_up_medico.btnSubirFotoMedico
import kotlinx.android.synthetic.main.activity_sign_up_medico.emailbox
import kotlinx.android.synthetic.main.activity_sign_up_medico.nombrebox
import kotlinx.android.synthetic.main.activity_sign_up_medico.passwordbox

class SignUpFarmaciaActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    var photoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_farmacia)
        mAuth = FirebaseAuth.getInstance()
        btnSignUpFarmacia.setOnClickListener {
            if(validarCampos())
                subirImagenAFirebase()
        }

        btnSubirFotoMedico.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }


    private fun subirDoctorAFirebase(url: String) {

        val name = nombrebox.text.toString()
        val direccion = direccionbox.text.toString()
        val cuit =cuitbox.text.toString()
        val email=emailbox.text.toString()
        val password =  passwordbox.text.toString()



        val farmacia = Farmacia(name, direccion, cuit, email, "farmacia", url)
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    subirloAFirebase(farmacia)
                } else {
                    Toast.makeText(baseContext, "Sign up failed", Toast.LENGTH_SHORT).show()
                }
            }


    }

    private fun subirImagenAFirebase(){

        val ref = FirebaseStorage.getInstance().getReference("/images/Medicos")
        val progressDialog = ProgressDialog(this)

        progressDialog.setTitle("Creando nuevo perfil de farmacia ")
        progressDialog.show()
        ref.putFile(photoUri!!)
            .addOnSuccessListener { taskSnapshot ->
                ref.downloadUrl.addOnCompleteListener { taskSnapshot ->
                    var url = taskSnapshot.result
                    Log.d("SignUp Medico", "Image added to firebase: ${url.toString()}")
                    subirDoctorAFirebase(url.toString())
                }
            }
            .addOnProgressListener { taskSnapShot ->
                val progress = 100 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount
                progressDialog.setMessage("% ${progress}")
            }


    }

    private fun subirloAFirebase(farmacia: Farmacia) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/signup/$uid")
        ref.setValue(farmacia)
            .addOnSuccessListener {
                Log.d("SignUpActivity", "User added to database")
                startActivity(Intent(this, SignInActivity::class.java))
            }

    }


    private fun validarCampos(): Boolean {
        if (nombrebox.text.toString().isEmpty()) {
            nombrebox.error = "Por favor ingresa el nombre de la farmacia"
            nombrebox.requestFocus()
            return false
        }

        if (direccionbox.text.toString().isEmpty()) {
            direccionbox.error = "Por favor ingresa tu direccion"
            direccionbox.requestFocus()
            return false
        }

        if (cuitbox.text.toString().isEmpty()) {
            cuitbox.error = "Por favor ingresa tu CUIT"
            cuitbox.requestFocus()
            return false
        }

        if (emailbox.text.toString().isEmpty()) {
            emailbox.error = "Por favor ingresa tu email"
            emailbox.requestFocus()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailbox.text.toString()).matches()) {
            emailbox.error = "Por favor ingresa un email valido"
            emailbox.requestFocus()
            return false
        }

        if (passwordbox.text.toString().isEmpty()) {
            passwordbox.error = "Por favor ingresa una contraseña"
            passwordbox.requestFocus()
            return false
        }
        if (photoUri == null) {
            btnSubirFotoMedico.error = "Por favor elija o suba una foto"
            btnSubirFotoMedico.requestFocus()
            return false
        }


        return true
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            photoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, photoUri)
            CircleImageViewMedico.setImageBitmap(bitmap)
            btnSubirFotoMedico.background = null
            btnSubirFotoMedico.text = null


        }
    }
}
