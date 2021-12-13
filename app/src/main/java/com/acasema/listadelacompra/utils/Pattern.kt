package com.acasema.listadelacompra.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

class Pattern {
    companion object{
        /**
         * método estático qe se el String cumple lo siguiente
         * -parte 1: permite cualquier cantidad de letras, números y "."
         * -parte 2: un "@"
         * -parte 3: permite cualquier cantidad de letras
         * -parte 2: un "."
         * -parte 3: permite de 2 a 4 de letras
         * @param email el email para comprobar
         * @return respuesta de la comprobación
         */
        fun isEmailValid(email: String): Boolean {
            val patron = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$"
            return isStringValid(email, patron)
        }

        /**
         * método estático qe se el String cumple lo siguiente
         * -tiene 8 caracteres
         * -tiene una minúscula
         * -tiene un numero
         * @param password contraseña para comprobar
         * @return respuesta de la comprobación
         */
        fun isPasswordValid(password: String): Boolean {
            val patron = "^(?=\\w*\\d)(?=\\w*[a-z])\\S{8,16}$"
            return isStringValid(password, patron)
        }

        /**
         * método estático qe se el String cumple lo siguiente
         * -tiene que ser un numero
         * -tiene que ser positivo
         * @param number numero a comprobar
         * @return respuesta de la comprobación
         */
        fun isPositiveNumberValid(number: String): Boolean {
            val patron = "^(\\d)+$" // patron para un numero mayor a 0
            return isStringValid(number, patron)
        }

        /**
         * método estático qe se el String cumple lo siguiente
         * -cualquier texto
         * -un mínimo de 3 caracteres
         * @param text titulo a comprobar
         * @return respuesta de la comprobación
         */
        fun isText3oMasValid(text: String): Boolean {
            val patron = "^.{3,}$"
            return isStringValid(text, patron)
        }

        /**
         * método estático y privado para comprobar con reglas con lo patrones
         * @param string String a comprobar
         * @param patron dicho patron
         * @return respuesta de la comprobación
         */
        private fun isStringValid(string: String, patron: String): Boolean {
            val pattern: Pattern = Pattern.compile(patron)
            val matcher: Matcher = pattern.matcher(string)
            return matcher.matches()
        }


    }
}