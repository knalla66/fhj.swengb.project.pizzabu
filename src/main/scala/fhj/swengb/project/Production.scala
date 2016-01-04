package fhj.swengb.project

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

object Production {
  def main(args: Array[String]) {
    Production(Oven)
  }
  /*
  creates an empty Production
   */
  def apply():Production = {//Production()
  val machine : Machines = Oven
    Production(machine)
  }

  /*
  receive the machine which should produce a product from the player
   */
  //def getMachine = ??? //val machine : Machines = Oven
  //Production(machine)
}

case class Production(machine: Machines){

  /*
  If the player moves to the machine, the product will be produced which is ordered by the guest
  Every different order (pizza,cola or fries) has a different producing time
   */
  def start(machine: Machines) : Unit = machine match {
    case Oven => println("Pizza wird gemacht") // if (ready == false) start producing else producing not possible; ready = true
    case Beverage => println("Cola wird zubereitet")
    case Fries => println("Pommes werden frittiert")
  }



  /*
  If the product is produced, the player can pick up the product and bring it to the guest
   */
  //val ready : Boolean = ??? // if (produced) true else false

  /*
  The machine is waiting for the next order, after the player takes the finished product
   */
  //val waiting : Machines = ???

}
