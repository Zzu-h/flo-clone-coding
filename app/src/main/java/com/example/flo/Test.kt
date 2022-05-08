/**
 * You can edit, run, and share this code.
 * play.kotlinlang.org
 */
fun main() {
    val list = List(5){i -> i}
    var string = list.toString()
    println(list)
    string = string.substring(1,string.lastIndex)
    string = string.replace(' ', '0')
    var tlist = string.split(',')
    val intList = List<Int>(tlist.size){i -> tlist[i].toInt()}
    println(intList)
}