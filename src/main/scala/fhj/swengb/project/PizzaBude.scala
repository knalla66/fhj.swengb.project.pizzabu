package fhj.swengb.project

import javafx.beans.property.{SimpleBooleanProperty, SimpleLongProperty, SimpleObjectProperty, SimpleIntegerProperty}

import scala.util.Random
import scala.collection.mutable.Map

/**
  * Created by PizzaBude Team on 15.01.2016.
  */
sealed trait Product
case object Pizza extends Product
case object Cola extends Product
case object Fries extends Product

sealed trait Machine{

  val time:SimpleLongProperty = new SimpleLongProperty()
  def getTime():Long = time.get()
  def setTime(t:Long):Unit = time.set(t)

  val state:SimpleBooleanProperty = new SimpleBooleanProperty()
  def getState():Boolean = state.get()
  def setState(b:Boolean) = state.set(b)

  val btnProperty:SimpleBooleanProperty = new SimpleBooleanProperty()
  def getProperty():Boolean = btnProperty.get()
  def setProperty(b:Boolean) = btnProperty.set(b)

}
case object PizzaOven extends Machine
case object Drink extends Machine
case object Pommes extends Machine

case class Player(score: Int)

case class Guest(id: Int)
case class Order(id: Int) {
  /**
    * List of Products which are available for order
    */
  val products:Seq[Product] = Seq(Pizza,Cola,Fries)
  /**
    * maxLength => set the maximum length of an order
    * oderLength => create Randomlength for an order
    */
  val maxLength = products.length + 1
  val orderLength = Random.shuffle(1 to maxLength).head

  /**
    * Create an RandomOrder - recursive
    *
    * @param orderSeq is returned at the end
    * @param orderLength length of the order created
    * @param products list of available Products
    * @return Seq[Product]
    */
  def createOrder(orderSeq:Seq[Product] = Nil,orderLength:Int = orderLength,products: Seq[Product] = products):Seq[Product] = orderLength match {
    case orderLength if orderLength > 0 => createOrder(orderSeq:+Random.shuffle(products).head,orderLength-1,products)
    case _ => orderSeq
  }

}

case class PizzaBude(guests: Map[Guest, Seq[Product]], machines: Seq[Machine], player: Player) {

  val gameState: SimpleObjectProperty[PizzaBude] = new SimpleObjectProperty[PizzaBude]()
  def getGameState():PizzaBude = gameState.get()
  def setGameState(g: PizzaBude) = gameState.set(g)

  val guestProperty: SimpleIntegerProperty = new SimpleIntegerProperty(1)
  def getGuestProperty():Int = guestProperty.get()
  def setGuestProperty(x:Int) = guestProperty.set(x)

  def addCustomer(id:Int,guests: Map[Guest,Seq[Product]]) = {
    guests + (Guest(id) -> Order(id).createOrder())
  }

  def createNextGameState(guests:scala.collection.mutable.Map[Guest,Seq[Product]], player:Player): PizzaBude = {
    PizzaBude(guests,machines,player)
  }
}

object PizzaBude {

  def addCustomer(id:Int,guests: Map[Guest,Seq[Product]]) = {
    guests + (Guest(id) -> Order(id).createOrder())
  }

}

object Test {
  def main(args: Array[String]) {


  }
}
