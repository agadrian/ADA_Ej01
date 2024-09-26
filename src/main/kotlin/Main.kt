package org.example

import java.io.BufferedReader
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.notExists
import kotlin.math.max


/*
* Construir una función reciba el fichero de cotizaciones y devuelva un diccionario con los datos del fichero por columnas.

* Construir una función que reciba el diccionario devuelto por la función anterior y cree un fichero en formato csv con el mínimo, el máximo y la media de dada columna.
* */

fun createDict(fichero: Path): Map<String, MutableList<String>>{

    //val fichero = rutaFich.resolve("cotizacion.csv")

    val dicc = mutableMapOf<String,MutableList<String>>()

    if (Files.notExists(fichero)){
        println("No existe el fichero en esta ruta")
    }else{
        val lineas = Files.readAllLines(fichero)
        val cabecera = lineas[0].split(";")
        val datos = lineas.drop(1) // No coger la cabecera
        //println(datos)

        // Meter cada titulo de columna en el diccionario
        for (titulo in cabecera){
            dicc[titulo] = mutableListOf()
        }

        // Meter los valores dentro de cada lista
        for (linea in datos){
            val valores = linea.split(";")
            for (i in valores.indices){
                dicc[cabecera[i]]?.add(valores[i])
            }
        }
// Comprobaciones
//        for (i in dicc.keys){
//            println("${i}: ${dicc[i]}")
//        }
    }
    return dicc
}

fun createFile(dict: Map<String,MutableList<String>>, rutaFile: Path){
    val lineas = mutableListOf<String>()

    val rutaArchivo = rutaFile.resolve("newFile.csv")

    if (Files.notExists(rutaArchivo)){
        Files.createFile(rutaArchivo)
    }

    lineas.add("Columna;Minimo;Maximo;Media")


    for ((columna,valores) in dict){
        // Lista de valores no nulos, para obtener solo las lineas con numeros y no letras, ya que las lineas de strings devuelven null
        val numeros = valores.mapNotNull { it.replace(",",".").toDoubleOrNull() }

        if (numeros.isNotEmpty()){
            // Si no esta vacia la lista de numeros, calculamos
            val maximo = numeros.maxOrNull()
            val minimo = numeros.minOrNull()
            val media = numeros.average()

            lineas.add("${columna};${minimo};${maximo};${"%.2f".format(media)}")
        }
    }

    Files.write(rutaArchivo, lineas)

}


fun main() {

    val ruta = Path.of("src/main/resources/cotizacion.csv")
    val dicc = createDict(ruta)

    val rutaFile = Path.of("src/main/resources")
    createFile(dicc, rutaFile)
}

