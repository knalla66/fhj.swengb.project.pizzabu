package fhj.swengb.project

import java.net.URL
import java.util.ResourceBundle
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.fxml._
import javafx.scene.layout.{BorderPane, AnchorPane}
import javafx.scene.{Scene, Parent}
import javafx.scene.control.{TextField, Button}
import javafx.stage.Stage

import fhj.swengb.project.Highscore.Score
import fhj.swengb.project.PizzaBude._

import scala.collection.mutable
import scala.util.control.NonFatal

/**
  * Created by loete on 17.01.2016
  */
object PizzaBudeApp{
  def main(args: Array[String]) {
    Application.launch(classOf[PizzaBudeApp], args: _*)
  }
}

class PizzaBudeApp extends Application {

  val loader = new FXMLLoader(getClass.getResource("PizzaBude.fxml"))

  override def start(stage: Stage): Unit = try {
    stage.setTitle("PizzaBude")
    loader.load[Parent]()
    stage.setScene(new Scene(loader.getRoot[Parent]))
    stage.show()
    } catch {
    case NonFatal(e) => e.printStackTrace()
  }
}

case class GameLoop(game: PizzaBude) extends AnimationTimer{

  override def handle(now:Long):Unit = {

    PizzaBude.saveStartTime(now)

    PizzaOven.checkMachine(now, PizzaOven.t, PizzaOven.product)
    Drink.checkMachine(now, Drink.t, Drink.product)
    Pommes.checkMachine(now, Pommes.t, Pommes.product)

    Table2.checkTables(now)
    Table2.deliver()
    Table2.checkAngryLevel(now)

    if(PizzaBude.getStartTime()+60000000000L < now) {
      Table3.checkTables(now)
      Table3.deliver()

    }
    if(PizzaBude.getStartTime()+120000000000L < now) {
      Table1.checkTables(now)
      Table1.deliver()
    }
    if(PizzaBude.getStartTime()+180000000000L < now) {
      Table4.checkTables(now)
      Table4.deliver()
    }

    PizzaBude.checkGameOver()

    if(PizzaBude.getGameOver()) {
      stop()

      val gameOver = new FXMLLoader(getClass.getResource("GUI-GameOver.fxml"))
      val gameOverStage = new Stage()

      gameOverStage.setTitle("PizzaBu - HighScore!")
      gameOver.load[Parent]()
      gameOverStage.setScene(new Scene(gameOver.getRoot[ Parent ]))

      gameOverStage.show()
     }
  }
}
class PizzaBudeController extends Initializable {


  @FXML var bordertop: BorderPane = _
  @FXML var btnPizza: Button = _
  @FXML var btnDrink: Button = _
  @FXML var btnStart: Button = _
  @FXML var btnStop: Button = _
  @FXML var btnTable1: Button = _
  @FXML var btnTable2: Button = _
  @FXML var btnTable3: Button = _
  @FXML var btnTable4: Button = _

  @FXML var canvasAnchorPane: AnchorPane = _

  var game:GameLoop = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    val machines = Seq(PizzaOven,Drink)
    val guests: mutable.Map[Guest, Seq[Product]] = mutable.Map()
    val g = PizzaBude.apply(guests,machines)
    g.setGameState(g)
    game = GameLoop(g)
  }

  @FXML def pizza():Unit = if(!PizzaOven.getState()) PizzaOven.setProperty(true)
  @FXML def drink():Unit = if(!Drink.getState()) Drink.setProperty(true)
  @FXML def pommes():Unit = if(!Pommes.getState()) Pommes.setProperty(true)
  @FXML def start():Unit = game.start()
  @FXML def stop():Unit = game.stop()

  @FXML def table1():Unit = Table1.setProperty(true)
  @FXML def table2():Unit = Table2.setProperty(true)
  @FXML def table3():Unit = Table3.setProperty(true)
  @FXML def table4():Unit = Table4.setProperty(true)
  @FXML def close():Unit = bordertop.getScene.getWindow.hide()

}

//HighScoreTest

class GameOverController extends Initializable {

  @FXML var nameField: TextField = _
  @FXML var scoreField: TextField = _
  @FXML var btn_save: Button = _
  @FXML var btn_toHighscore: Button = _

  val highscore = Table1.getScore() + Table2.getScore() + Table3.getScore() + Table4.getScore()

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    scoreField.setText(highscore.toString())
  }

  def save() = {
    val name = nameField.getCharacters.toString
    Score.toDb(Db.maybeConnection.get)(Score(name, highscore))

    println("Name: " + name + " Highscore:" + highscore)

    val loaderScore = new FXMLLoader(getClass.getResource("GUI.fxml"))
    val highScoreStage = new Stage()

    highScoreStage.setTitle("PizzaBu - HighScore!")
    loaderScore.load[Parent]()
    highScoreStage.setScene(new Scene(loaderScore.getRoot[ Parent ]))

    highScoreStage.show()

  }

  def toHighscore() = {

    val loaderScore = new FXMLLoader(getClass.getResource("GUI.fxml"))
    val highScoreStage = new Stage()

    highScoreStage.setTitle("PizzaBu - HighScore!")
    loaderScore.load[Parent]()
    highScoreStage.setScene(new Scene(loaderScore.getRoot[ Parent ]))

    highScoreStage.show()

  }

}