package fhj.swengb.project

import scala.collection.mutable.Map
import concurrent.ExecutionContext.Implicits.global


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
    Production(Oven).start(Oven)
    Production(Beverage).start(Beverage)
  }
  /*
  get the correct machine which should produce from the player
   */
  def apply():Production = Production()

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
  def startMaschine(machine:Machines) = machine match {
    case Oven => Oven()
    case Beverage => Beverage()
  }
  */

  /*
  If the player moves to the machine, the product will be produced which is ordered by the guest
  Every different order (pizza,cola or fries) has a different producing time
  The produced product is stored in the Map "products"
   */
  def start(machine: Machines): Map[Machines, Product] = machine match {
    case Oven => println("produktion angestoßen");produce();Production.products += (machine->Pizza)
    case Beverage => println("Produziere Getränke");Production.products += (machine->Cola)
    case Fries => Production.products += (machine->CurlyFries)
  }

  def produce() = concurrent.Future {
    Thread.sleep(5000); println("produziert")
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
    case Oven if Production.products.getOrElse(Oven,Pizza)==Pizza => true
    case Beverage if Production.products.getOrElse(Beverage,Cola)==Cola => true
    case Fries if Production.products.getOrElse(Fries,CurlyFries)==CurlyFries => true
    case _ => false
  }
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
    Production.products += (Beverage->Cola)
  }
}*/

