package fhj.swengb.project

/**
  * Created by IMA14/PizzaBu on 03.01.2016.
  */

/**
  * for a pizza game, there are three different machines --> oven, beverage, fries
  */
sealed trait Machines

case object Oven extends Machines

case object Beverage extends Machines

case object Fries extends Machines

/**
  * Different Products, whioh can be produced serveral machines
  */
sealed trait Product

case object Pizza extends Product

case object Cola extends Product

case object CurlyFries extends Product


object Production {

  def main(args: Array[String]) {
  Production(products)
  }

  /*
  get the correct machine which should produce from the player
   */
  def apply(): Production = Production(products)


  /*
  A Map to save all the produced Products
   */
  val products: Map[Machines, Product] = Map(Oven -> null, Beverage -> null, Fries -> null)
}

case class Production(products:Map[Machines,Product]) {

  val mas = getMachine(readLine("Please choose a product -> "))


  def getMachine(machine: String) = machine match {
    case pizza if pizza == "pizza" => if (ready(Oven) == true) pickedUp(Oven) else start(Oven)
    case cola if cola == "cola" => if (ready(Beverage) == true) pickedUp(Beverage) else start(Beverage)
    case fries if fries == "fries" => if (ready(Fries) == true) pickedUp(Fries) else start(Fries)
  }

  /*
  If the player moves to the machine, the product will be produced which is ordered by the guest
  Every different order (pizza,cola or fries) has a different producing time
  The produced product is stored in the Map "products"
   */
  def start(machine: Machines) = machine match {
    case Oven => println("Pizza wird gemacht"); println(products + (machine -> Pizza));Production(products + (machine -> Pizza))
    case Beverage => println("Produziere Getränke"); println(products + (machine -> Cola));Production(products + (machine -> Cola))
    case Fries => println("CurlyFries werden gemacht"); println(products + (machine -> CurlyFries));Production(products + (machine -> CurlyFries))
  }

  /*
   Products picked up by the player to bring the products to the quests are removed
  */
  def pickedUp(machine: Machines) = machine match {
    case Oven => println("Pizza wurde ausgeliefert");println(products + (machine -> null));Production(products + (machine -> null))
    case Beverage => println("Cola wurde ausgeliefert");println(products + (machine -> null));Production(products + (machine -> null))
    case Fries => println("Fries wurden ausgeliefert");println(products + (machine -> null));Production(products + (machine -> null))
  }

  /*
  Status of the machine => if ready == true, the product is produced and ready to pick up
  Otherwise and new product must be produced
   */
  def ready(machine: Machines): Boolean = machine match {
    case Oven if products.getOrElse(Oven, Pizza) == Pizza => true
    case Beverage if products.getOrElse(Beverage, Cola) == Cola => true
    case Fries if products.getOrElse(Fries, CurlyFries) == CurlyFries => true
    case _ => false
  }
}