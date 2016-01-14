package fhj.swengb.project

import scala.util.Random

/**
  * Created by IMA14/PizzaBu on 14.01.2016.
  */
trait Guest {

  val products:List[Product] = List(Pizza,Cola,CurlyFries)

  def createOrder(howMany:Int = Random.shuffle(List(1,2,3)).head, products: List[Product] = products,orderList: List[Product]=Nil): Seq[Product] = howMany match {
    case howMany if howMany > 0 => createOrder(howMany-1,products,orderList.::(Random.shuffle(products).head))
    case _ => orderList
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

object Guests {
  def main(args: Array[String]) {
    val g1 = Guests
  }

 val order = Guest1.order
  println(order)
  Guests(order)

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

}