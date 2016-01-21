package fhj.swengb.project

import java.awt.{Button, Image}
import java.net.URL
import java.util.ResourceBundle
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.fxml.{FXML, Initializable, FXMLLoader}
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.{Scene, Parent}
import javafx.stage.Stage

import scala.collection.immutable.IndexedSeq
import scala.util.Random
import scala.util.control.NonFatal

/**
 * Created by KnallerMJ on 14.01.16.
 */

// companion object
object PizzaBuApp {
  def main(args: Array[String]) {
    Application.launch(classOf[PizzaBuAppFX], args: _*)
  }
}

class PizzaBuAppFX extends Application {

  val loader = new FXMLLoader(getClass.getResource("GUI-Game.fxml"))

  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("PizzaBu - Die Pizza kommt in nu!")
      loader.load[Parent]()
      stage.setScene(new Scene(loader.getRoot[Parent]))

      stage.show()
    } catch {
      case NonFatal(e) => e.printStackTrace()
    }
}

case class CircleAnimation(circles: Seq[Circle]) extends AnimationTimer {

  // every tick this method is called - you are free to do whatever you want
  // in this method. maybe animate something, maybe something else ...
  override def handle(now: Long): Unit = {
    circles.foreach {
      case c =>
        c.setCenterX(c.getCenterX + Random.nextDouble * 4 - 2)
        c.setCenterY(c.getCenterY + Random.nextDouble * 4 - 2)
    }

  }
}

class PizzaBuAppFXController extends Initializable {

  @FXML var canvasAnchorPane: AnchorPane = _
  @FXML var btnPizza: Button =_

  var animationTimer: CircleAnimation = _

  def randColor(): Color = Color.color(Random.nextDouble, Random.nextDouble(), Random.nextDouble)


  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    val width = canvasAnchorPane.getMinWidth
    val height = canvasAnchorPane.getMinHeight

    val circles: IndexedSeq[Circle] =
      for (i <- 1 to 5) yield {
        mkCircle(width.toInt, height.toInt, Random.nextInt(20) + 1)
      }
    // wieso getChildren.addAll?
    canvasAnchorPane.getChildren.addAll(circles: _*)
    animationTimer = CircleAnimation(circles)

  }


  def mkCircle(width: Int, height: Int, maxRadius: Int): Circle = {
    val c = new Circle(Random.nextInt(width), Random.nextInt(height), Random.nextInt(maxRadius))
    c.setFill(randColor())
    c
  }

  // name der def() muss dem onAction Namen entsprechen
  def start(): Unit = {
    animationTimer.start()
  }

  def exit(): Unit = {
    animationTimer.stop()
  }


}


