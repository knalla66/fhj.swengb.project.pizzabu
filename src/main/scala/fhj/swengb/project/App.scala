package fhj.swengb.project

import scala.concurrent.Future

/**
 * @author ${user.name}
 */
object App {
  def main(args: Array[String]) {

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

