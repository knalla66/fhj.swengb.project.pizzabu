package fhj.swengb.project

import java.awt.Image
import java.net.URL
import java.util.ResourceBundle
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.fxml.{FXML, Initializable, FXMLLoader}
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.{Scene, Parent}
import javafx.stage.Stage

import scala.collection.immutable.IndexedSeq
import scala.compat.Platform
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

  val loader = new FXMLLoader(getClass.getResource("GUI-Startscreen.fxml"))

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


class PizzaBuAppStartController {

  @FXML var start: Button = _
  @FXML var highscore: Button = _
  @FXML var help: Button = _
  @FXML var exit: Button = _

  def onStart(): Unit = {
    val loaderGame = new FXMLLoader(getClass.getResource("GUI-Game.fxml"))
    val gameStage = new Stage()

    gameStage.setTitle("PizzaBu - HighScore!")
    loaderGame.load[Parent]()
    gameStage.setScene(new Scene(loaderGame.getRoot[Parent]))

    gameStage.show()
  }

  def goToHighScore(): Unit = {
    val loaderScore = new FXMLLoader(getClass.getResource("GUI-Highscore.fxml"))
    val highScoreStage = new Stage()

    highScoreStage.setTitle("PizzaBu - HighScore!")
    loaderScore.load[Parent]()
    highScoreStage.setScene(new Scene(loaderScore.getRoot[Parent]))

    highScoreStage.show()

    // Zurück zu Startbilschirm Button einbauen! (muss im Highscore Controller eingebaut sein)

    // Beenden des anderen Fensters wenn auf Button geklickt wird

    //highScoreStage.close()

  }
  def onHelp(): Unit = {
    val loaderHelp = new FXMLLoader(getClass.getResource("GUI-Help.fxml"))
    val helpStage = new Stage()

    helpStage.setTitle("PizzaBu - Help!")
    loaderHelp.load[Parent]()
    helpStage.setScene(new Scene(loaderHelp.getRoot[Parent]))

    helpStage.show()
  }

  def onExit(): Unit = {
    println("hello Exit")
  }

}


class PizzaBuAppHighscoreController {

  @FXML var zurueck: Button = _

  // Wenn der Button Zurück gedrückt wird, soll das aktuelle Fenster geschlossen werden und
  // der Startbildschirm wieder angezeigt werden.
  def onZurueck(): Unit = {
    println("Ich will zurüc zum Startbildschirm")

  }
}

class PizzaBuAppHelpController {

  @FXML var zurueck: Button = _

  // Wenn der Button Zurück gedrückt wird, soll das aktuelle Fenster geschlossen werden und
  // der Startbildschirm wieder angezeigt werden.
  def onZurueck(): Unit = {
    println("Ich will zurück zum Startbildschirm")

  }
}


class PizzaBuAppFXController extends Initializable {

  @FXML var canvasAnchorPane: AnchorPane = _
  @FXML var pommes: Button = _
  @FXML var ofen: Button = _
  @FXML var cola: Button = _

  @FXML var reihe1: Button = _
  @FXML var reihe2: Button = _
  @FXML var reihe3: Button = _
  @FXML var reihe4: Button = _

  @FXML var stop: Button = _


  var animationTimer: CircleAnimation = _

  def randColor(): Color = Color.color(Random.nextDouble, Random.nextDouble(), Random.nextDouble)


  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    val width = canvasAnchorPane.getMinWidth
    val height = canvasAnchorPane.getMinHeight

    val circles: IndexedSeq[Circle] =
      for (i <- 1 to 1) yield {
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


