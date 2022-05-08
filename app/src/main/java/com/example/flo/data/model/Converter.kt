package com.example.flo.data.model

object Converter{
    fun stringToList(str: String): List<Int>{
        val list = str.split(',')
        return List<Int>(list.size){i -> list[i].toInt()}
    }
    fun listToString(list: List<Int>): String{
        val str = list.toString()

        val string = str.substring(1, str.lastIndex)
        return  string.replace(" ", "")
    }
}