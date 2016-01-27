package fhj.swengb.project

import java.net.URL
import java.util.ResourceBundle
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.beans.property._
import javafx.fxml._
import javafx.scene.image.{ImageView, Image}
import javafx.scene.layout.{BorderPane, AnchorPane}
import javafx.scene.media.{AudioClip, Media, MediaPlayer}
import javafx.scene.{Scene, Parent}
import javafx.scene.control.{TextField, Button, Label}
import javafx.fxml._
import javafx.scene.control.{Button, Label, TextField}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.{AnchorPane, BorderPane}
import javafx.scene.media.{AudioClip, Media, MediaPlayer}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import fhj.swengb.project.Highscore.Score

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

case class GameLoop(game: PizzaBude,buttons: Map[Move, Button],labels: Map[Order,Label],images: Map[Images, ImageView],m:MediaPlayer,s: Map[Sounds,AudioClip]) extends AnimationTimer{

  override def handle(now:Long):Unit = {
    m.setCycleCount(999)
    m.play()
    PizzaBude.saveStartTime(now)

    PizzaOven.checkMachine(now, PizzaOven.t, PizzaOven.product)
    Drink.checkMachine(now, Drink.t, Drink.product)
    Pommes.checkMachine(now, Pommes.t, Pommes.product)

    if(PizzaOven.getWaiting) buttons(Product2).setGraphic(images(BtnPizza_3))
    if(PizzaOven.getState) buttons(Product2).setGraphic(images(BtnPizza_2))
    if(!PizzaOven.getState && !PizzaOven.getWaiting) buttons(Product2).setGraphic(images(BtnPizza_1))

    if(Drink.getWaiting) buttons(Product1).setGraphic(images(BtnDrink_3))
    if(Drink.getState) buttons(Product1).setGraphic(images(BtnDrink_2))
    if(!Drink.getState && !Drink.getWaiting) buttons(Product1).setGraphic(images(BtnDrink_1))

    if(Pommes.getWaiting) buttons(Product3).setGraphic(images(BtnFries_3))
    if(Pommes.getState) buttons(Product3).setGraphic(images(BtnFries_2))
    if(!Pommes.getState && !Pommes.getWaiting) buttons(Product3).setGraphic(images(BtnFries_1))

    labels(ScoreAll).setText("Score: "+(Table1.getScore+Table2.getScore+Table3.getScore+Table4.getScore).toString)

    if(Table1.getOrder==Nil) buttons(Customer1).setGraphic(images(BtnTable_0))
    if(Table2.getOrder==Nil) buttons(Customer2).setGraphic(images(BtnTable2_0))
    if(Table3.getOrder==Nil) buttons(Customer3).setGraphic(images(BtnTable3_0))
    if(Table4.getOrder==Nil) buttons(Customer4).setGraphic(images(BtnTable4_0))


    if(Table1.getOrder!=Nil) Table1.getAngryLevel match {
      case 0 => buttons(Customer1).setGraphic(images(BtnTable_1))
      case 1 => buttons(Customer1).setGraphic(images(BtnTable_2))
      case 2 => buttons(Customer1).setGraphic(images(BtnTable_3))
      case 3 => buttons(Customer1).setGraphic(images(BtnTable_4))
      case _ => buttons(Customer1).setGraphic(images(BtnTable_0))
    }

    if(Table2.getOrder!=Nil) Table2.getAngryLevel match {
      case 0 => buttons(Customer2).setGraphic(images(BtnTable2_1))
      case 1 => buttons(Customer2).setGraphic(images(BtnTable2_2))
      case 2 => buttons(Customer2).setGraphic(images(BtnTable2_3))
      case 3 => buttons(Customer2).setGraphic(images(BtnTable2_4))
      case _ => buttons(Customer2).setGraphic(images(BtnTable2_0))
    }

    if(Table3.getOrder!=Nil) Table3.getAngryLevel match {
      case 0 => buttons(Customer3).setGraphic(images(BtnTable3_1))
      case 1 => buttons(Customer3).setGraphic(images(BtnTable3_2))
      case 2 => buttons(Customer3).setGraphic(images(BtnTable3_3))
      case 3 => buttons(Customer3).setGraphic(images(BtnTable3_4))
      case _ => buttons(Customer3).setGraphic(images(BtnTable3_0))
    }

    if(Table4.getOrder!=Nil) Table4.getAngryLevel match {
      case 0 => buttons(Customer4).setGraphic(images(BtnTable4_1))
      case 1 => buttons(Customer4).setGraphic(images(BtnTable4_2))
      case 2 => buttons(Customer4).setGraphic(images(BtnTable4_3))
      case 3 => buttons(Customer4).setGraphic(images(BtnTable4_4))
      case _ => buttons(Customer4).setGraphic(images(BtnTable4_0))
    }

    if(Table1.getOrder!=Nil) labels(Order1).setText((Table1.getOrder diff Table1.getDeliverd).mkString("\n"))
    if(Table2.getOrder!=Nil) labels(Order2).setText((Table2.getOrder diff Table2.getDeliverd).mkString("\n"))
    if(Table3.getOrder!=Nil) labels(Order3).setText((Table3.getOrder diff Table3.getDeliverd).mkString("\n"))
    if(Table4.getOrder!=Nil) labels(Order4).setText((Table4.getOrder diff Table4.getDeliverd).mkString("\n"))

    if(Table1.getTableStatus) labels(Order1).setText("")
    if(Table2.getTableStatus) labels(Order2).setText("")
    if(Table3.getTableStatus) labels(Order3).setText("")
    if(Table4.getTableStatus) labels(Order4).setText("")

    Table2.checkTables(now)
    Table2.deliver()
    Table2.checkAngryLevel(now)

    if(PizzaBude.getStartTime+60000000000L < now) {
      Table3.checkTables(now)
      Table3.deliver()
      Table3.checkAngryLevel(now)
    }
    if(PizzaBude.getStartTime+120000000000L < now) {
      Table1.checkTables(now)
      Table1.deliver()
      Table1.checkAngryLevel(now)
    }
    if(PizzaBude.getStartTime+180000000000L < now) {
      Table4.checkTables(now)
      Table4.deliver()
      Table4.checkAngryLevel(now)
    }
    PizzaBude.checkGameOver()
    if(PizzaBude.getGameOver) {
      s(GameO).play()
      m.stop()
      stop()
      PizzaBudeController().goToHighscore()
    }
  }
}


/**
 * GAMEOVER
 */

case class GameOverController() extends Initializable {

  @FXML var borderPaneTop: BorderPane = _
  @FXML var nameField: TextField = _
  @FXML var scoreField: TextField = _
  @FXML var btn_save: Button = _
  @FXML var btn_toHighscore: Button = _

  val highscore = Table1.getScore + Table2.getScore + Table3.getScore + Table4.getScore

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    scoreField.setText(highscore.toString())
  }

  def save() = {
    val name = nameField.getCharacters.toString
    if (PizzaBuApp.checkos == 1)  Score.toDb(Db.maybeConnectionWindows.get)(Score(name, highscore))
    if (PizzaBuApp.checkos == 2)  Score.toDb(Db.maybeConnectionMac.get)(Score(name, highscore))
    println("Name: " + name + " Highscore:" + highscore)
    val loaderScore = new FXMLLoader(getClass.getResource("GUI-Highscore_gameover.fxml"))
    val highScoreStage = new Stage()
    highScoreStage.setTitle("PizzaBu - HighScore!")
    loaderScore.load[Parent]()
    highScoreStage.setScene(new Scene(loaderScore.getRoot[Parent]))
    highScoreStage.show()
    borderPaneTop.getScene.getWindow.hide()
  }
}


case class PizzaBudeController() extends Initializable {

  @FXML var borderPaneTop: BorderPane = _
  @FXML var btnPizza: Button = _
  @FXML var btnDrink: Button = _
  @FXML var btnPommes: Button = _
  @FXML var btnStart: Button = _
  @FXML var btnStop: Button = _
  @FXML var btnTable1: Button = _
  @FXML var btnTable2: Button = _
  @FXML var btnTable3: Button = _
  @FXML var btnTable4: Button = _
  @FXML var lblTable1: Label = _
  @FXML var lblTable2: Label = _
  @FXML var lblTable3: Label = _
  @FXML var lblTable4: Label = _
  @FXML var lblScore: Label = _

  @FXML var canvasAnchorPane: AnchorPane = _
  @FXML var mediaPlayer: MediaPlayer = _
  @FXML var sound: Media = _

  def goToHighscore():Unit = {
//    borderPaneTop.getScene().getWindow().setOpacity(0.0)

    val loaderGameOver = new FXMLLoader(getClass.getResource("GUI-GameOver.fxml"))
    val gameOverStage = new Stage()

    gameOverStage.setTitle("GAMEOVER!")
    loaderGameOver.load[Parent]()
    gameOverStage.setScene(new Scene(loaderGameOver.getRoot[ Parent ]))

    gameOverStage.show()

  }


  var game:GameLoop = _

  lazy val buttons: Map[Move, Button] = Map(
    Product1 -> btnDrink,
    Product2 -> btnPizza,
    Product3 -> btnPommes,
    Customer1 -> btnTable1,
    Customer2 -> btnTable2,
    Customer3 -> btnTable3,
    Customer4 -> btnTable4
  )

  lazy val labels: Map[Order,Label] = Map(
    Order1 -> lblTable1,
    Order2 -> lblTable2,
    Order3 -> lblTable3,
    Order4 -> lblTable4,
    ScoreAll -> lblScore
  )

  val cash = new AudioClip(getClass.getResource("cash.wav").toString)
  val gameO = new AudioClip(getClass.getResource("gameover.wav").toString)
  val bad = new AudioClip(getClass.getResource("bad.mp3").toString)
  val good = new AudioClip(getClass.getResource("bad.mp3").toString)

  lazy val sounds: Map[Sounds,AudioClip] = Map(
    Cash -> cash,
    GameO -> gameO,
    Bad -> bad,
    Good -> good
  )


  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    println("NEUES GAME!!!!!!!!!!!!!!!!!!!!!!")
    resetGame

    val res:URL = getClass.getResource("loop1.wav")
    sound = new Media(res.toString)
    mediaPlayer = new MediaPlayer(sound)
    var g = PizzaBude()
    val pane = canvasAnchorPane
    game = GameLoop(g,buttons,labels,images,mediaPlayer,sounds)
    game.start()

  }

  @FXML def pizza():Unit = if(!PizzaOven.getState) PizzaOven.setProperty(true)
  @FXML def drink():Unit = if(!Drink.getState) Drink.setProperty(true)
  @FXML def pommes():Unit = if(!Pommes.getState) Pommes.setProperty(true)
  @FXML def start():Unit = game.start()
  @FXML def stop():Unit = game.stop()

  @FXML def table1():Unit = Table1.setProperty(true)
  @FXML def table2():Unit = Table2.setProperty(true)
  @FXML def table3():Unit = Table3.setProperty(true)
  @FXML def table4():Unit = Table4.setProperty(true)
  @FXML def close():Unit = borderPaneTop.getScene.getWindow.hide()

  lazy val images: Map[Images, ImageView] = Map(
    BtnDrink_1 -> btnDrink_1,
    BtnDrink_2 -> btnDrink_2,
    BtnDrink_3 -> btnDrink_3,
    BtnPizza_1 -> btnPizza_1,
    BtnPizza_2 -> btnPizza_2,
    BtnPizza_3 -> btnPizza_3,
    BtnFries_1 -> btnFries_1,
    BtnFries_2 -> btnFries_2,
    BtnFries_3 -> btnFries_3,
    BtnTable_0 -> btnTable_0,
    BtnTable_1 -> btnTable_1,
    BtnTable_2 -> btnTable_2,
    BtnTable_3 -> btnTable_3,
    BtnTable_4 -> btnTable_4,
    BtnTable2_0 -> btnTable2_0,
    BtnTable2_1 -> btnTable2_1,
    BtnTable2_2 -> btnTable2_2,
    BtnTable2_3 -> btnTable2_3,
    BtnTable2_4 -> btnTable2_4,
    BtnTable3_0 -> btnTable3_0,
    BtnTable3_1 -> btnTable3_1,
    BtnTable3_2 -> btnTable3_2,
    BtnTable3_3 -> btnTable3_3,
    BtnTable3_4 -> btnTable3_4,
    BtnTable4_0 -> btnTable4_0,
    BtnTable4_1 -> btnTable4_1,
    BtnTable4_2 -> btnTable4_2,
    BtnTable4_3 -> btnTable4_3,
    BtnTable4_4 -> btnTable4_4
  )

  /**
    * Images 4 buttons
    */
  lazy val btnDrink_1: ImageView = new ImageView(new Image(getClass.getResourceAsStream("btnDrink_DrinkWait.png")))
  lazy val btnDrink_2: ImageView = new ImageView(new Image(getClass.getResourceAsStream("btnDrink_DrinkWorking.png")))
  lazy val btnDrink_3: ImageView = new ImageView(new Image(getClass.getResourceAsStream("btnDrink_DrinkRdy.png")))
  lazy val btnPizza_1: ImageView = new ImageView(new Image(getClass.getResourceAsStream("btnPizza_OvenWait.png")))
  lazy val btnPizza_2: ImageView = new ImageView(new Image(getClass.getResourceAsStream("btnPizza_OvenWorking.png")))
  lazy val btnPizza_3: ImageView = new ImageView(new Image(getClass.getResourceAsStream("btnPizza_OvenRdy.png")))
  lazy val btnFries_1: ImageView = new ImageView(new Image(getClass.getResourceAsStream("btnFries_FriesWait.png")))
  lazy val btnFries_2: ImageView = new ImageView(new Image(getClass.getResourceAsStream("btnFries_FriesWorking.png")))
  lazy val btnFries_3: ImageView = new ImageView(new Image(getClass.getResourceAsStream("btnFries_FriesRdy.png")))
  lazy val btnTable_0: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_0.png")))
  lazy val btnTable_1: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_1.png")))
  lazy val btnTable_2: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_2.png")))
  lazy val btnTable_3: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_3.png")))
  lazy val btnTable_4: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_4.png")))
  lazy val btnTable2_0: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_0.png")))
  lazy val btnTable2_1: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_1.png")))
  lazy val btnTable2_2: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_2.png")))
  lazy val btnTable2_3: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_3.png")))
  lazy val btnTable2_4: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_4.png")))
  lazy val btnTable3_0: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_0.png")))
  lazy val btnTable3_1: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_1.png")))
  lazy val btnTable3_2: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_2.png")))
  lazy val btnTable3_3: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_3.png")))
  lazy val btnTable3_4: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_4.png")))
  lazy val btnTable4_0: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_0.png")))
  lazy val btnTable4_1: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_1.png")))
  lazy val btnTable4_2: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_2.png")))
  lazy val btnTable4_3: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_3.png")))
  lazy val btnTable4_4: ImageView = new ImageView(new Image(getClass.getResourceAsStream("Table_4.png")))

  def resetGame = {
    PizzaBude.setGameOver(false)
    Table1.setAngryLevel(0)
    Table2.setAngryLevel(0)
    Table3.setAngryLevel(0)
    Table4.setAngryLevel(0)
    Table1.setScore(0)
    Table2.setScore(0)
    Table3.setScore(0)
    Table4.setScore(0)
    Table1.setOrder(Nil)
    Table2.setOrder(Nil)
    Table3.setOrder(Nil)
    Table4.setOrder(Nil)
    Table1.setDeliverd(Nil)
    Table2.setDeliverd(Nil)
    Table3.setDeliverd(Nil)
    Table4.setDeliverd(Nil)
    Table1.setTableStatus(true)
    Table2.setTableStatus(true)
    Table3.setTableStatus(true)
    Table4.setTableStatus(true)
    Table1.setTime(0L)
    Table2.setTime(0L)
    Table3.setTime(0L)
    Table4.setTime(0L)
    Table1.setAngryTime(0L)
    Table2.setAngryTime(0L)
    Table3.setAngryTime(0L)
    Table4.setAngryTime(0L)
    PizzaOven.setReady(false)
    PizzaOven.setProperty(false)
    PizzaOven.setWaiting(false)
    PizzaOven.setState(false)
    Drink.setReady(false)
    Drink.setProperty(false)
    Drink.setWaiting(false)
    Drink.setState(false)
    Pommes.setReady(false)
    Pommes.setProperty(false)
    Pommes.setWaiting(false)
    Pommes.setState(false)
  }
}

sealed trait Sounds
case object Cash extends Sounds
case object GameO extends Sounds
case object Bad extends Sounds
case object Good extends Sounds

sealed trait Images
case object BtnDrink_1 extends Images
case object BtnDrink_2 extends Images
case object BtnDrink_3 extends Images
case object BtnPizza_1 extends Images
case object BtnPizza_2 extends Images
case object BtnPizza_3 extends Images
case object BtnFries_1 extends Images
case object BtnFries_2 extends Images
case object BtnFries_3 extends Images
case object BtnTable_0 extends Images
case object BtnTable_1 extends Images
case object BtnTable_2 extends Images
case object BtnTable_3 extends Images
case object BtnTable_4 extends Images
case object BtnTable2_0 extends Images
case object BtnTable2_1 extends Images
case object BtnTable2_2 extends Images
case object BtnTable2_3 extends Images
case object BtnTable2_4 extends Images
case object BtnTable3_0 extends Images
case object BtnTable3_1 extends Images
case object BtnTable3_2 extends Images
case object BtnTable3_3 extends Images
case object BtnTable3_4 extends Images
case object BtnTable4_0 extends Images
case object BtnTable4_1 extends Images
case object BtnTable4_2 extends Images
case object BtnTable4_3 extends Images
case object BtnTable4_4 extends Images
