package fhj.swengb.project

import scala.collection.mutable.Map

/**
  * Created by Daniel on 03.01.2016.
  */

/**
  * for a pizza game, there are three different machines --> oven, beverage, fries
  */
sealed trait Machines

case object Oven extends Machines

case object Beverage extends Machines

case object Fries extends Machines


sealed trait Product

case object Pizza extends Product

case object Cola extends Product

case object CurlyFries extends Product


object Production {
  def main(args: Array[String]) {
    println(Production.products)
    Production().start(Oven)
    println(Production.products)
    Production().start(Beverage)
    println(Production.products)
    /*Production().pickedUp(Oven)
    println(Production.products)*/
  }
  /*
  get the correct machine which should produce from the player
   */
  def apply():Production = Production(Oven)

  /*def test():Production = {//Production()
  val machine : Machines = Fries
    Production(machine)
  }*/

  /*
  A Map to save all the produced Products
   */
  val products: Map[Machines,Product] = Map(Oven -> null, Beverage -> null, Fries -> null)
}

case class Production(machine:Machines){


  /*
  If the player moves to the machine, the product will be produced which is ordered by the guest
  Every different order (pizza,cola or fries) has a different producing time
  The produced product is stored in the Map "products"
   */
  def start(machine: Machines): Map[Machines, Product] = machine match {
    case Oven => Production.products += (machine->Pizza)
    case Beverage => Production.products += (machine->Cola)
    case Fries => Production.products += (machine->CurlyFries)
  }

  /*
   Products picked up by the player to bring the products to the quests are removed
  */
  def pickedUp(machine:Machines) : Map[Machines,Product] = machine match {
    case Oven => Production.products += (machine->null)
    case Beverage => Production.products += (machine->null)
    case Fries => Production.products += (machine->null)
  }

  /*
  Status of the machine => if ready == true, the product is produced and ready to pick up
  Otherwise and new product must be produced
   */
  def ready(machine:Machines):Boolean = machine match {
    case Oven if Production.products.filter(_._2==Pizza) => true
    case Beverage if Production.products.filter(_._2==Cola) => true
    case Fries if Production.products.filter(_._2==CurlyFries) => true
    case _ => false
  }
  /*

/*
thread.sleep
oder
http://doc.akka.io/docs/akka/2.3.6/scala/scheduler.html
scheduler -> akka
 */
*/
}
/*
case class Oven(machine:Machines = Oven) {

  val start = {
    Thread.sleep(5000)
    Production.products += (Oven->Pizza)
  }
}

case class Beverage(machine:Machines = Beverage) {

  val start = {
    Thread.sleep(2000)
    Production.products += (Beverage->Pizza)
  }
}
*/
