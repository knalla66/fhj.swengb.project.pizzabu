package fhj.swengb.project

import javafx.beans.property._
import scala.collection.mutable
import scala.util.Random

/**
  * Created by PizzaBudeTeam on 15.01.2016.
  */
sealed trait Move
case object Product1 extends Move
case object Product2 extends Move
case object Product3 extends Move
case object Customer1 extends Move
case object Customer2 extends Move
case object Customer3 extends Move
case object Customer4 extends Move

sealed trait Order
case object Order1 extends Order
case object Order2 extends Order
case object Order3 extends Order
case object Order4 extends Order
case object ScoreAll extends Order

sealed trait Product
case object Pizza extends Product
case object Cola extends Product
case object Fries extends Product

sealed trait Table {

  val time:SimpleLongProperty = new SimpleLongProperty(0L)
  def getTime:Long = time.get()
  def setTime(t:Long):Unit = time.set(t)

  val angryTime:SimpleLongProperty = new SimpleLongProperty(0L)
  def getAngryTime:Long = angryTime.get()
  def setAngryTime(t:Long):Unit = angryTime.set(t)

  val btnProperty:SimpleBooleanProperty = new SimpleBooleanProperty()
  def getProperty:Boolean = btnProperty.get()
  def setProperty(b:Boolean) = btnProperty.set(b)

  /**
    * Indicates is a Table is free or not
    */
  val tableStatus:SimpleBooleanProperty = new SimpleBooleanProperty(true)
  def getTableStatus:Boolean = tableStatus.get()
  def setTableStatus(b:Boolean) = tableStatus.set(b)

  /**
    * Saves the order from the customer to an SimpleObjectProperty[Seq[Product]]
    */
  val order:SimpleObjectProperty[Seq[Product]] = new SimpleObjectProperty[Seq[Product]](Nil)
  def getOrder: Seq[Product] = order.get()
  def setOrder(o: Seq[Product]) = order.set(o)

  /**
    * Saves the delieverd Products to an SimpleObjectProperty[Seq[Product]]
    */
  val delivered:SimpleObjectProperty[Seq[Product]] = new SimpleObjectProperty[Seq[Product]](Nil)
  def getDeliverd:Seq[Product] = delivered.get()
  def setDeliverd(d: Seq[Product]) = delivered.set(d)

  /**
    * IntegerProperty to save the Score
    */
  val score:SimpleIntegerProperty = new SimpleIntegerProperty(0)
  def getScore:Int = score.get()
  def setScore(s:Int) = score.set(s)

  /**
    * Indicates the angryLevel of a customer
    */
  val angryLevel:SimpleIntegerProperty = new SimpleIntegerProperty(0)
  def getAngryLevel:Int = angryLevel.get()
  def setAngryLevel(s:Int) = angryLevel.set(s)

  /**
    * deliver function for delivering products to a table
    */
  def deliver() = {
    if(getProperty){
      /**
        * Checks if chef has a product with him
        */
      if(PizzaBude.getDeliverProperty!=null) {
        /**
          * Checks if delivered product was ordered by the customer
          */
        if((getOrder diff getDeliverd).contains(PizzaBude.getDeliverProperty)){
          setDeliverd(getDeliverd:+PizzaBude.getDeliverProperty)
          PizzaBude.getDeliverProperty match {
            case Pizza => setScore(getScore+100)
            case Fries => setScore(getScore+60)
            case Cola => setScore(getScore+30)
          }
          println(getScore)
          PizzaBude.setDeliverProperty(null)
          println(getDeliverd)
        } else {
          println("NICHT BESTELLT")
          PizzaBude.setDeliverProperty(null)
          setAngryLevel(getAngryLevel+1)
          setScore(getScore-50)
        }

      }
      if((getOrder diff getDeliverd) == Nil){
        println(getOrder)
        println(getDeliverd)
        setOrder(Nil)
        setDeliverd(Nil)
        println("Bestellung FERTIG")
        setTableStatus(true)
        setAngryLevel(0)
      }
      setProperty(false)
    }
  }

  def checkAngryLevel(n:Long,t:Long = 20000000000L) =  {
    if(getAngryTime+t < n && getAngryTime!=0){
      setAngryLevel(getAngryLevel+1)
      setAngryTime(n)
      println(getAngryLevel)
    }
  }

  /**
    * Creates an order for a customer
    * @param n time from GameLoop, to save startTime of the Order
    */
  def makeOrder(n:Long) = {
    if(getOrder==Nil){
      setOrder(createOrder(Random.shuffle(1 to maxLength).head))
      println(getOrder)
      setAngryTime(n)
      setAngryLevel(0)
    }
  }

  /**
    * List of Products which are available for order
    */
  val products:Seq[Product] = Seq(Pizza,Cola,Fries)
  /**
    * maxLength => set the maximum length of an order
    * oderLength => create Randomlength for an order
    */
  val maxLength = products.length + 1

  /**
    * Create an RandomOrder - recursive
    *
    * @param orderSeq is returned at the end
    * @param orderLength length of the order created
    * @param products list of available Products
    * @return Seq[Product]
    */
  def createOrder(orderLength:Int,orderSeq:Seq[Product] = Nil,products: Seq[Product] = products):Seq[Product] = orderLength match {
    case orderLength if orderLength > 0 => createOrder(orderLength - 1, orderSeq :+ Random.shuffle(products).head, products)
    case _ => orderSeq
  }

  val tableDelay:Seq[Long] = Seq(4000000000L,8000000000L,12000000000L)

  /**
    * Checks if a table is free for a customer. If a table is free an order will be created after 4,8 or 12 seconds
    * @param n current time from GameLoop
    * @param t random time
    */
  def checkTables(n:Long,t:Long =Random.shuffle(Table1.tableDelay).head) = {
    if(getTableStatus) setTime(n+t);setTableStatus(false)
    if(getTime < n && getOrder == Nil) {
      makeOrder(n)
    }
  }
}

case object Table1 extends Table
case object Table2 extends Table
case object Table3 extends Table
case object Table4 extends Table

sealed trait Machine{

  val t:Long
  val product:Product

  val time:SimpleLongProperty = new SimpleLongProperty()
  def getTime:Long = time.get()
  def setTime(t:Long):Unit = time.set(t)

  val state:SimpleBooleanProperty = new SimpleBooleanProperty()
  def getState:Boolean = state.get()
  def setState(b:Boolean) = state.set(b)

  /**
    * Indicates if product is ready to deliver
    */
  val ready:SimpleBooleanProperty = new SimpleBooleanProperty()
  def getReady:Boolean = ready.get()
  def setReady(b:Boolean) = ready.set(b)

  val btnProperty:SimpleBooleanProperty = new SimpleBooleanProperty()
  def getProperty:Boolean = btnProperty.get()
  def setProperty(b:Boolean) = btnProperty.set(b)

  val waiting:SimpleBooleanProperty = new SimpleBooleanProperty()
  def getWaiting:Boolean = waiting.get()
  def setWaiting(b:Boolean) = waiting.set(b)

  def checkMachine(n:Long,t:Long,product:Product) = {
    if(getProperty){
      if(getReady) {
        PizzaBude.setDeliverProperty(product)
        setProperty(false)
        setReady(false)
        setWaiting(false)
        println("DELIVER: "+PizzaBude.getDeliverProperty)

      } else {
        println("START: "+product)
        setTime(n+t)
        setState(true)
        setProperty(false)
      }
    }
    if(getTime < n && getState) {
      println()
      println(product+" FERTIG")
      setWaiting(true)
      setState(false)
      setReady(true)
    }
  }

}
case object PizzaOven extends Machine{
  override val t:Long = 10000000000L
  override val product:Product = Pizza
}
case object Drink extends Machine {
  override val t:Long = 3000000000L
  override val product:Product = Cola
}
case object Pommes extends Machine {
  override val t:Long = 5000000000L
  override val product:Product = Fries
}


case class PizzaBude() {

  val gameState: SimpleObjectProperty[PizzaBude] = new SimpleObjectProperty[PizzaBude]()
  def getGameState:PizzaBude = gameState.get()
  def setGameState(g: PizzaBude) = gameState.set(g)

  val guestProperty: SimpleIntegerProperty = new SimpleIntegerProperty(1)
  def getGuestProperty:Int = guestProperty.get()
  def setGuestProperty(x:Int) = guestProperty.set(x)

  val customerProperty: SimpleIntegerProperty = new SimpleIntegerProperty(0)
  def getCustomerProperty:Int = customerProperty.get()
  def setCustomerProperty(c:Int) = customerProperty.set(c)

  val deliverProperty: SimpleObjectProperty[Product] = new SimpleObjectProperty[Product]()
  def getDeliverProperty: Product = deliverProperty.get()
  def setDeliverProperty(p:Product) = deliverProperty.set(p)

}

object PizzaBude {

  val gameOver: SimpleBooleanProperty = new SimpleBooleanProperty()
  def getGameOver:Boolean = gameOver.get()
  def setGameOver(b:Boolean) = gameOver.set(b)

  val deliverProperty: SimpleObjectProperty[Product] = new SimpleObjectProperty[Product]()
  def getDeliverProperty: Product = deliverProperty.get()
  def setDeliverProperty(p:Product) = deliverProperty.set(p)

  val startTime:SimpleLongProperty = new SimpleLongProperty()
  def getStartTime:Long = startTime.get()
  def setStartTime(t:Long):Unit = startTime.set(t)

  def saveStartTime(n:Long) = {
    if(getStartTime == 0) setStartTime(n)
  }

  def checkGameOver() = {
    if(Table1.getAngryLevel == 4 || Table2.getAngryLevel == 4 || Table3.getAngryLevel == 4 || Table4.getAngryLevel == 4){
      setGameOver(true)
      println("GAME OVER!!!")
    }
  }

}
