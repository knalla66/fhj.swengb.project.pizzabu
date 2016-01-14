package fhj.swengb.project

import scala.util.Random

/**
  * Created by Stefan Leitner on 14.01.2016.
  */
trait Guest {

  val products:List[Product] = List(Pizza,Cola,CurlyFries)

  def createOrder(howMany:Int = Random.shuffle(1 to 3).head, products: List[Product] = products,orderList: List[Product]=Nil): Seq[Product] = howMany match {
    case howMany if howMany > 0 => createOrder(howMany-1,products,orderList.::(Random.shuffle(products).head))
    case _ => orderList
  }

  def order1(howMany: Int, bestellung: List[Product],orderList: List[Product]=Nil): Unit = howMany match {
    case howMany if howMany > 0 => order1(howMany-1,bestellung,orderList.::(Random.shuffle(bestellung).head))
    case _ => orderList
      println(orderList)
  }

  val order:Seq[Product] = createOrder()

  def setAngryLevel(lvl: Int = 0) = {
    0 + lvl
  }

  var angry = setAngryLevel()

}

object Guest1 extends Guest
object Guest2 extends Guest
object Guest3 extends Guest
object Guest4 extends Guest

case class Guests(delieverable: Product) extends Guest {
  Guest1.order
}

object Test {
  def main(args: Array[String]) {
    println("G1")
    println(Guest1.order)
    println("G2")
    println(Guest2.order)
    println("G3")
    println(Guest3.order)
    println("G4")
    println(Guest4.order)
    println(Guest1.order)
    println(Guest1.angry)
    Guest1.setAngryLevel(3)
    println(Guest1.angry)
    }
}
