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
      val s = Map(Oven->null,Beverage->null,Fries->null)
      assertEquals(s,Production.products)
    }

  /*
  the order of a pizza
   */
  @Test def orderingOfAPizza: Unit = {
    val t = Map(Oven -> Pizza,Beverage->null,Fries->null)
    val op = Production().start(Oven)
    assertEquals(t,op.products)
  }

  /*
  all possible products are ordered
   */
  @Test def orderingAllProducts: Unit = {
    val oa = Production().start(Oven).start(Beverage).start(Fries)//.getMachine("cola")
    //Production().getMachine("fries")
    assertEquals(allProducts,oa.products)
  }

  /*
  Pick up a Pizza from a order
   */
    @Test def pickUpPizza: Unit = {
      val t = Map(Oven -> null,Beverage->null,Fries->null)
      val pp = Production().start(Oven).pickedUp(Oven)
      assertEquals(t,pp.products)
    }

  /*
  The status of the machine Oven must be true, if a Pizza was produced
   */
  @Test def statusOfOvenTrue: Unit = {
    val t = true
    val pp = Production().start(Oven)
    assertEquals(t,pp.ready(Oven))
  }

  @Test def statusOfOvenFalse: Unit = {
    val t = false
    val pp = Production().pickedUp(Oven)
    assertEquals(t,pp.ready(Oven))
  }
}



