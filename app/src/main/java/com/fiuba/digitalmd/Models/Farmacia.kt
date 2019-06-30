package com.fiuba.digitalmd.Models

class Farmacia  (val nombre: String, val direccion: String, val cuit: String, val email: String,
                val userType:String, val url: String) {
    constructor() : this("", "", "", "", "", "")

}