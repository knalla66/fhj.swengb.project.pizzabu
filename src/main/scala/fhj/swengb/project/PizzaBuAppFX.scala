package fhj.swengb.project

import java.io.File
import java.net.URL
import java.nio.file.{Files, Path, Paths}
import java.util.ResourceBundle
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control._
import javafx.scene.layout.{AnchorPane, BorderPane}
import javafx.scene.paint.Color
import javafx.scene.shape.Circle
import javafx.scene.text.{Font, Text, TextFlow}
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import fhj.swengb.project.Highscore._

import scala.collection.immutable.IndexedSeq
import scala.util.Random
import scala.util.control.NonFatal

/**
 * Created by KnallerMJ on 14.01.16.
 */

// companion object
object PizzaBuApp {
  def main(args: Array[ String ]) {
    Application.launch(classOf[ PizzaBuAppFX ], args: _*)
  }

  def checkos: Int = {
    var os = 0

    if (System.getProperty("os.name").startsWith("Windows")) os = 1
    if (System.getProperty("os.name").startsWith("Mac")) os = 2




    return os

  }
}

case class PizzaBuAppFX() extends Application {

  val loader = new FXMLLoader(getClass.getResource("GUI-Startscreen.fxml"))

  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("PizzaBu - Die Pizza kommt in nu!")
      loader.load[Parent]()
      stage.setScene(new Scene(loader.getRoot[ Parent ]))

      stage.show()
    } catch {
      case NonFatal(e) => e.printStackTrace()
    }

}

case class CircleAnimation(circles: Seq[ Circle ]) extends AnimationTimer {

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

case class PizzaBuAppStartController() extends Initializable {

  @FXML var borderTop: BorderPane = _
  @FXML var start: Button = _
  @FXML var highscore: Button = _
  @FXML var help: Button = _
  @FXML var exit: Button = _


  def onStart(): Unit = {
    val loaderGame = new FXMLLoader(getClass.getResource("PizzaBude.fxml"))
    val gameStage = new Stage()

    gameStage.setTitle("PizzaBu - Die Pizza kommt in nu!")
    loaderGame.load[Parent]()
    gameStage.setScene(new Scene(loaderGame.getRoot[ Parent ]))
    gameStage.getScene.getStylesheets.add("/fhj/swengb/project/PizzaBude.css")

    gameStage.show()
    borderTop.getScene.getWindow.hide()

  }

  def goToHighScore(): Unit = {
    val loaderScore = new FXMLLoader(getClass.getResource("GUI-Highscore.fxml"))
    val highScoreStage = new Stage()
    highScoreStage.setTitle("PizzaBu - HighScore!")
    loaderScore.load[Parent]()
    highScoreStage.setScene(new Scene(loaderScore.getRoot[ Parent ]))

    highScoreStage.show()
    borderTop.getScene.getWindow.hide()

  }

  def onHelp(): Unit = {
    val loaderHelp = new FXMLLoader(getClass.getResource("GUI-Help.fxml"))
    val helpStage = new Stage()

    helpStage.setTitle("PizzaBu - Help!")
    loaderHelp.load[Parent]()
    helpStage.setScene(new Scene(loaderHelp.getRoot[ Parent ]))

    helpStage.show()
    borderTop.getScene.getWindow.hide()
  }

  def onExit(): Unit = {
    borderTop.getScene.getWindow.hide()
  }


  override def initialize(location: URL, resources: ResourceBundle): Unit = {


    if (PizzaBuApp.checkos == 1) {


      println("Oh, it's a Windows PC!")
      var path: Path = Paths.get("C:\\PizzaBu")

      if (Files.exists(path) == false) {

        val dir: File = new File("C:\\PizzaBu")
        // attempt to create the directory
        dir.mkdir()
      }
    }

    if (PizzaBuApp.checkos == 2) {
      println("Oh, it's a Mac!")
      var path: Path = Paths.get("Macintosh HD\\PizzaBu")

      if (Files.exists(path) == false) {

        val dir: File = new File("Macintosh HD:\\PizzaBu")
        // attempt to create the directory
        dir.mkdir()
      }


    }
  }
}


case class PizzaBuAppHighscoreController() extends Initializable {

  import JfxUtils._

  type ScoreTC[ T ] = TableColumn[ MutableScore, T ]

  @FXML var rootHighScore: BorderPane = _
  @FXML var tableView: TableView[ MutableScore ] = _
  @FXML var listView: ListView[ Int ] = _

  @FXML var columnRang: ScoreTC[ Int ] = _
  @FXML var columnName: ScoreTC[ String ] = _
  @FXML var columnScore: ScoreTC[ Int ] = _

  @FXML var zurueck: Button = _

  // Wenn der Button Zurück gedrückt wird, soll das aktuelle Fenster geschlossen werden und
  // der Startbildschirm wieder angezeigt werden.
  def onZurueck(): Unit = {
    rootHighScore.getScene.getWindow.hide()

    val loader = new FXMLLoader(getClass.getResource("GUI-Startscreen.fxml"))
    val startStage = new Stage()
    startStage.setTitle("PizzaBu - Die Pizza kommt in nu!")
    loader.load[Parent]()
    startStage.setScene(new Scene(loader.getRoot[ Parent ]))

    startStage.show()
  }

  /**
   * provide a table column and a generator function for the value to put into
   * the column.
   *
   * @tparam T the type which is contained in the property
   * @return
   */

  def initTableViewColumn[ T ]: (ScoreTC[ T ], (MutableScore) => Any) => Unit =
    initTableViewColumnCellValueFactory[ MutableScore, T ]

  //läd die Daten von der Datenbank

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    //teilt den einzelnen Spalten einen Wert zu
    initTableViewColumn[String](columnName, _.nameProperty)
    initTableViewColumn[Int](columnScore, _.scoreProperty)

    for (i <- 1 to 10) listView.getItems().add(i)


    if (PizzaBuApp.checkos == 1) {
      if (new java.io.File("C:\\PizzaBu\\score.db").exists == true) {

        println("ich komme in die if schleife")
        for {
          con <- Db.maybeConnectionWindows
          s <- Score.fromDb(Score.queryAll(con))
        } {
          tableView.getItems().add(MutableScore(s))
        }

        println("Es musste keine Datenbank erstellt werden!")


      }
      else {

        for {
          con <- Db.maybeConnectionWindows
          _ = Score.reTable(con.createStatement())
          _ = Score.highscorelist.map(Score.toDb(con)(_))
          s <- Score.fromDb(Score.queryAll(con))
        } {
          tableView.getItems().add(MutableScore(s))
        }

        println("Es wurde eine neue Datenbank erstellt!")

      }

    }


    if (PizzaBuApp.checkos == 2) {
      if (new java.io.File("Macintosh HD\\PizzaBu\\score.db").exists == true) {

        println("ich komme in die if schleife")
        for {
          con <- Db.maybeConnectionMac
          s <- Score.fromDb(Score.queryAll(con))
        } {
          tableView.getItems().add(MutableScore(s))
        }

        println("Es musste keine Datenbank erstellt werden!")


      }
      else {

        for {
          con <- Db.maybeConnectionMac
          _ = Score.reTable(con.createStatement())
          _ = Score.highscorelist.map(Score.toDb(con)(_))
          s <- Score.fromDb(Score.queryAll(con))
        } {
          tableView.getItems().add(MutableScore(s))
        }

        println("Es wurde eine neue Datenbank erstellt!")

      }

    }
  }
}

case class PizzaBuAppHighscoreGameOverController() extends Initializable {

  import JfxUtils._

  type ScoreTC[ T ] = TableColumn[ MutableScore, T ]

  @FXML var rootHighScore: BorderPane = _
  @FXML var tableView: TableView[ MutableScore ] = _
  @FXML var listView: ListView[ Int ] = _

  @FXML var columnRang: ScoreTC[ Int ] = _
  @FXML var columnName: ScoreTC[ String ] = _
  @FXML var columnScore: ScoreTC[ Int ] = _

  @FXML var newgame: Button = _
  @FXML var exit: Button = _

  // Wenn der Button Zurück gedrückt wird, soll das aktuelle Fenster geschlossen werden und
  // der Startbildschirm wieder angezeigt werden.
  def onNewGame(): Unit = {
    rootHighScore.getScene.getWindow.hide()

    val loaderGame = new FXMLLoader(getClass.getResource("PizzaBude.fxml"))
    val gameStage = new Stage()

    gameStage.setTitle("PizzaBu - Die Pizza kommt in nu!")
    loaderGame.load[Parent]()
    gameStage.setScene(new Scene(loaderGame.getRoot[ Parent ]))

    gameStage.show()
  }

  def onExit(): Unit = {
    sys.exit(1)
  }

  /**
   * provide a table column and a generator function for the value to put into
   * the column.
   *
   * @tparam T the type which is contained in the property
   * @return
   */

  def initTableViewColumn[ T ]: (ScoreTC[ T ], (MutableScore) => Any) => Unit =
    initTableViewColumnCellValueFactory[ MutableScore, T ]

  //läd die Daten von der Datenbank

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    //teilt den einzelnen Spalten einen Wert zu
    initTableViewColumn[String](columnName, _.nameProperty)
    initTableViewColumn[Int](columnScore, _.scoreProperty)

    for (i <- 1 to 10) listView.getItems().add(i)


    if (PizzaBuApp.checkos == 1) {
      if (new java.io.File("C:\\PizzaBu\\score.db").exists == true) {

        println("ich komme in die if schleife")
        for {
          con <- Db.maybeConnectionWindows
          s <- Score.fromDb(Score.queryAll(con))
        } {
          tableView.getItems().add(MutableScore(s))
        }

        println("Es musste keine Datenbank erstellt werden!")


      }
      else {

        for {
          con <- Db.maybeConnectionWindows
          _ = Score.reTable(con.createStatement())
          _ = Score.highscorelist.map(Score.toDb(con)(_))
          s <- Score.fromDb(Score.queryAll(con))
        } {
          tableView.getItems().add(MutableScore(s))
        }

        println("Es wurde eine neue Datenbank erstellt!")

      }

    }


    if (PizzaBuApp.checkos == 2) {
      if (new java.io.File("Macintosh HD\\PizzaBu\\score.db").exists == true) {

        println("ich komme in die if schleife")
        for {
          con <- Db.maybeConnectionMac
          s <- Score.fromDb(Score.queryAll(con))
        } {
          tableView.getItems().add(MutableScore(s))
        }

        println("Es musste keine Datenbank erstellt werden!")


      }
      else {

        for {
          con <- Db.maybeConnectionMac
          _ = Score.reTable(con.createStatement())
          _ = Score.highscorelist.map(Score.toDb(con)(_))
          s <- Score.fromDb(Score.queryAll(con))
        } {
          tableView.getItems().add(MutableScore(s))
        }

        println("Es wurde eine neue Datenbank erstellt!")

      }

    }
  }
}

case class PizzaBuAppHelpController() extends Initializable {

  @FXML var borderTop: BorderPane = _
  @FXML var zurueck: Button = _
  @FXML var textFlow: TextFlow = _

  @FXML var scrollPane: ScrollPane = _

  // Wenn der Button Zurück gedrückt wird, soll das aktuelle Fenster geschlossen werden und
  // der Startbildschirm wieder angezeigt werden.
  def onZurueck(): Unit = {
    borderTop.getScene.getWindow.hide()


    val loader = new FXMLLoader(getClass.getResource("GUI-Startscreen.fxml"))
    val startStage = new Stage()
    startStage.setTitle("PizzaBu - Die Pizza kommt in nu!")
    loader.load[Parent]()
    startStage.setScene(new Scene(loader.getRoot[ Parent ]))

    startStage.show()


  }

  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    val fontUe = new Font("Segoe UI Light", 24)
    val fontText = new Font("Times New Roman", 20)

    val text1 = new Text("\nSpiel starten")
    text1.setFill(Color.DARKRED)
    text1.setFont(fontUe)


    val text2 = new Text("\n\nUm das Spiel zu starten, drücke im Menü auf den Button Start. Du wirst dann automatisch zum Spiel weitergeleitet")
    text2.setFill(Color.BLACK)
    text2.setFont(fontText)



    val text3 = new Text("\n\nIm Spiel")
    text3.setFill(Color.DARKRED)
    text3.setFont(fontUe)



    /**
     * 10 Sekunden Pizza
        3 Sekunden Cola
        5 Sekunden Pommes

     */
    val text4 = new Text("\n\nAm Rechten Rand siehst du die Kunden, die an ihren Tischen sitzen und ihre Bestellungen aufgeben \n" +
      "Die Tische füllen sich entweder in 4,8 oder 12 Sekunden mit Kunden\n" +
      "Deine Aufgabe ist es nun, die Bestellungen der Kunden zu erfüllen. \n" +
      "Dazu klickst du auf die gewünschten Produkte (Cola, Pizza oder Pommes) um die Geräte zu starten. \n" +
      "Jedes Produkt braucht eine gewisse Zeit, bis es fertig ist. \n" +
      "\t\t • ein Cola dauert 3 Sekunden \n" +
      "\t\t • Pommes dauern 5 Sekunden und \n" +
      "\t\t • eine Pizza dauert 10 Sekunden\n" +
      "Sobald ein Produkt fertig ist, kannst du es dem Kunden bringen. Dazu klickst du zuerst auf das fertige Produkt und dann auf den Kunden, der das Produkt erhält")
    text4.setFill(Color.BLACK)
    text4.setFont(fontText)


    val text5 = new Text("\n\nAngry-Levels")
    text5.setFill(Color.DARKRED)
    text5.setFont(fontUe)

    val text6 = new Text("\n\nJe länger ein Kunde auf seine Bestellung warten muss, oder je öfter er etwas falsches Geliefert bekommt" +
      "desto zorniger wird der Kunde.\n" +
      "Alle 20 Minuten ändert sich der Angry-Level eines Kunden, wenn die Bestellung nicht erfüllt wird\n" +
      "\t\t • Grün: Zuerst ist der Kunde noch fröhlich und lächelt.\n" +
      "\t\t • Gelb: Der Kunde wird langsam ungeduldig\n" +
      "\t\t • Orange: Der Kunde wird immer ungeduldiger\n" +
      "\t\t • Rot: Der Kunde ist kurz davor das Restaurant zu verlassen" +
      "\t\t danach hast du noch 20 Sekunden Zeit, den Kunden zu besänftigen")

    text6.setFill(Color.BLACK)
    text6.setFont(fontText)

    val text7 = new Text("\n\nPunktevergabe")
    text7.setFill(Color.DARKRED)
    text7.setFont(fontUe)


    val text8 = new Text("\n\nDu bekommst für folgende Tätigkeiten Punkte:\n" +
      "\t\t • Pizza: 100 Punkte\n " +
      "\t\t • Pommes: 60 Punkte\n " +
      "\t\t • Cola: 30 Punkte\n " +
      "\t\t • falsche Lieferung: -50 Punkte "
    )
    text8.setFill(Color.BLACK)
    text8.setFont(fontText)

    val text9 = new Text("\n\nSpiel Ende")
    text9.setFill(Color.DARKRED)
    text9.setFont(fontUe)


    val text10 = new Text("\n\nDas Spiel ist zu Ende, wenn einer der Kunden den Angry-Level Rot erreicht " +
      "und die Bestellung nicht rechtzeitig geliefert wird.\n ")
    text10.setFill(Color.BLACK)
    text10.setFont(fontText)

    textFlow.getChildren().addAll(text1, text2, text3, text4, text5, text6, text7, text8, text9, text10)

    scrollPane.setContent(textFlow)

  }
}


/**
 * GAME OVER CONTROLLER
 */


case class PizzaBuAppFXController() extends Initializable {

  @FXML var borderTop: BorderPane = _
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

    val circles: IndexedSeq[ Circle ] =
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


