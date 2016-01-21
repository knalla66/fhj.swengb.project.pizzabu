package fhj.swengb.project

import java.awt.Image
import java.net.URL
import java.util.ResourceBundle
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.beans.property.{SimpleBooleanProperty, SimpleObjectProperty}
import javafx.fxml.{FXML, Initializable, FXMLLoader}
import javafx.stage.Stage
import javafx.scene.layout.AnchorPane
import javafx.scene.control.{Label, Button, TextArea}
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.{Scene, Parent}
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
  @FXML var pommes: Button = _


  var animationTimer: CircleAnimation = _

  def randColor(): Color = Color.color(Random.nextDouble, Random.nextDouble(), Random.nextDouble)


  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    val width = canvasAnchorPane.getMinWidth
    val height = canvasAnchorPane.getMinHeight

    val circles: IndexedSeq[Circle] =
      for (i <- 1 to 1) yield {
        mkCircle(width.toInt, height.toInt, Random.nextInt(20) + 1)
      }

    val fries = setPommes(true)

    // wieso getChildren.addAll?
    canvasAnchorPane.getChildren.addAll(circles: _*)
    animationTimer = CircleAnimation(circles)

  }

  /*
  *** current Products
   */
  val currentProduct:SimpleBooleanProperty = new SimpleBooleanProperty()
  //currentGameProperty.addListener(JfxUtils.onChange(updateGame))

  def getPommes() = currentProduct.get()
  def setPommes(s: Boolean) = currentProduct.set(s)

  def getCola() = currentProduct.get()
  def setCola(s: Boolean) = currentProduct.set(s)

  def getPizza() = currentProduct.get()
  def setPizza(s: Boolean) = currentProduct.set(s)

  /*
  *** current Guest
   */
  val currentGuest:SimpleBooleanProperty = new SimpleBooleanProperty()

  def getGuest() = currentGuest.get()
  def setGuet(s: Boolean) = currentGuest.set(s)



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

  def getPommes(flag: Boolean): Boolean = flag

  @FXML def onPommes():Boolean = getPommes(true)

}


