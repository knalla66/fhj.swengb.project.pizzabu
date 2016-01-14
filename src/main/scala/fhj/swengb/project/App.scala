package fhj.swengb.project

import com.sun.scenario.effect.impl.prism.PrDrawable

import scala.util.Random

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global
import concurrent.Future


/**
 * @author ${user.name}
 */
object App {
  def main(args: Array[String]) {
    val f = concurrent.Future { println("hi") }

  }
}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

object ScalaFuture {

  def main(args: Array[String]) {

    val f: Future[String] = Future {
      Thread.sleep(2000)
      "future value"
    }

    val f2 = f.map {
      case s => {
        println("OK!")
        println("OK!")
      }
    }

    Await.ready(f2, 60 seconds)
    println("exit")
  }
}

object RandomOrder{
def main (args: Array[String] ) {
  val bestellung: List[Product] = List(Pizza,Cola,CurlyFries)
  val items = List(1,2,3)
  val howMany = Random.shuffle(items).head

  def order(howMany: Int, bestellung: List[Product],orderList: List[Product]=Nil): List[Product] = howMany match {
      case howMany if howMany > 0 => order(howMany-1,bestellung,orderList.::(Random.shuffle(bestellung).head))
      case _ => orderList
    }

  order(howMany,bestellung)

}
}

