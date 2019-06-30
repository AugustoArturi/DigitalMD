package com.fiuba.digitalmd.Models


class Medico  (val nombre: String, val apellido: String, val matricula: String, val email: String,
              val obraSocial: String, val userType:String, val url: String) {
    constructor() : this("", "", "", "", "", "","")

}