package com.fiuba.digitalmd.Models


class ObraSocial  (val nombre: String, val email: String,
                 val userType:String, val url: String) {
    constructor() : this("", "", "", "")

}