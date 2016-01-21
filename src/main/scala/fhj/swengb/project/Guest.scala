package fhj.swengb.project

import scala.util.Random

/**
  * Created by IMA14/PizzaBu on 14.01.2016.


object Guest1 extends Guest
object Guest2 extends Guest
object Guest3 extends Guest
object Guest4 extends Guest

object Guests {
  def main(args: Array[String]) {
    val g1 = Guests
  }

 val order = Guest1.order
  println(order)
  Guests(order)

}


case class Gast(id : Int)
case class Order(id : Int)

/*case class PizzaHut(guests : Seq[Guest]) {

  def createNextState() : PizzaHut
}*/

object TestProgramm {



  def main(args: Array[String]) {
    val gäste = for (i <- 1 to 10) yield Gast(i)
    gäste.foreach(println)

  }
}

case class Guests(order:Seq[Product], recList:Seq[Product] = Nil) {

  val compare:Unit =  if (order.diff(recList) == Nil ) {
    //guest please leave the pizza-bu
    println("alles bekommen")
      }
  else {
    // wait for the next received product
    println("warte auf weitere Produkte")
    getProduct(readLine("Gib ein Produkt ein: "))
  }

  def getProduct(del:String) = del match {
    case pizza if pizza == "pizza" => savereceived(Pizza,recList,order)
    case cola if cola == "cola" => savereceived(Cola,recList,order)
    case fries if del == "fries" => println("Fries");savereceived(CurlyFries,recList,order)
  }

    def savereceived(product:Product,recList:Seq[Product]=Nil,order:Seq[Product]) =  {
      println(recList.+:(product)); Guests(order, recList.+:(product))//wait for received product (getProduct)
    }

}**/