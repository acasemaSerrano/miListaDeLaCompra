package com.acasema.listadelacompra.service

import java.net.URL
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

/**
 * autor: acasema (alfonso)
 *  clase que interpreta un Barcode y devuelbe una descripcion
 */
class BarcodeInterpreterService {
    companion object {

        private const val FILTRO = "<tr><td>Description</td><td></td><td>"
        private const val FILTROFINAL = "</td></tr>"

        private fun convert(url: String): String {
            val u: URL
            var input: BufferedInputStream? = null
            return try {

                u = URL(url)

                input = BufferedInputStream(u.openStream())

                val reader = BufferedReader(InputStreamReader(input))

                lineReader(reader)
            } catch (e: IOException) {

                ""
            } finally {

                input!!.close()

            }

        }

        private fun lineReader(reader: BufferedReader): String {
            var line: String?
            var respuesta = ""

            line = reader.readLine()

            while (line != null) {
                if (line.length >= FILTRO.length && FILTRO == line.substring(0, FILTRO.length)) {
                    respuesta = line.substring(FILTRO.length)
                    respuesta = respuesta.substring(0, respuesta.length - FILTROFINAL.length)
                    return respuesta
                }
                line = reader.readLine()
            }
            return respuesta
        }

        fun itemRepresenting(barcode: String): String{

            val urlSearcher ="https://www.upcdatabase.com/item/"
            return convert(urlSearcher + barcode)

            //https://www.ean-search.org/?q=
            //https://www.barcodelookup.com/
            //Error 403: Acceso denegado o prohibido
        }
    }
}