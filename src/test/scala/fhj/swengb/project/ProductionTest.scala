package fhj.swengb.project

import fhj.swengb.project._
import org.junit.Assert._
import org.junit.Test

/**
  * Created by Daniel on 04.01.2016.
  */

class ProductionTest {
  /*
  all products are produced
 */
  lazy val allProducts = Map(Oven -> Pizza, Beverage->Cola,Fries->CurlyFries)

  @Test def setOnEmpty(): Unit = {
      val t = Map(Oven->null,Beverage->null,Fries->null)
      assertEquals(t,Production.products)
    }

  /*
  the order of a pizza
   */
  @Test def orderingOfAPizza: Unit = {
    val t = Map(Oven -> Pizza,Beverage->null,Fries->null)
    val x = {
      Production().start(Oven)
      Production.products
    }
    assertEquals(t,x)
  }

  /*
  Order a Cola, then order CurlyFries and then pick up the Cola
   */
  @Test def pickUpACola: Unit = {
    val t = Map(Oven -> null,Beverage->null,Fries->CurlyFries)
    val x = {
      Production().start(Beverage)
      Production().start(Fries)
      Production().pickedUp(Beverage)
      Production.products
    }
    assertEquals(t,x)
  }

}
