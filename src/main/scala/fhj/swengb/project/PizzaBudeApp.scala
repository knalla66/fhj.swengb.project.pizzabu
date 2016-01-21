package fhj.swengb.project

import java.net.URL
import java.util.ResourceBundle
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.beans.property.{SimpleBooleanProperty, SimpleObjectProperty, SimpleIntegerProperty}
import javafx.fxml._
import javafx.scene.{Scene, Parent}
import javafx.scene.control.{Button}
import javafx.stage.Stage

import scala.collection.mutable
import scala.collection.mutable.HashMap
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

    /**
      * Checks if Button for PizzaOven is clicked.
      */
    if(PizzaOven.getProperty()){
      println("SET TIME!!!")
      PizzaOven.setTime(now+10000000000L)
      PizzaOven.setState(true)
      PizzaOven.setProperty(false)
    }

    /**
      *
      */
    if(PizzaOven.getTime() < now && PizzaOven.getState()) {
      println("PIZZA FERTIG")
      PizzaOven.setState(false)
    }

    /**
      * Checks if Button for Drink is clicked
      */
    if(Drink.getProperty()){
      println("SET TIME!!!")
      Drink.setTime(now+5000000000L)
      Drink.setState(true)
      Drink.setProperty(false)
    }
    /**
      *
      */
    if(Drink.getTime() < now && Drink.getState()) {
      println("DRINK FERTIG")
      Drink.setState(false)
    }


    //println(game.guests.update(Guest(game.getGuestProperty()),Order(game.getGuestProperty()).createOrder()))
    //val g = game.guests
    //val x = g.update(Guest(game.getGuestProperty()),Order(game.getGuestProperty()).createOrder())
    //game.setGuestProperty(game.getGuestProperty()+1)
    //println(game.getGuestProperty())
    //game.setGameState(PizzaBude(game.guests + (Guest(game.getGuestProperty()) -> Order(game.getGuestProperty()).createOrder()),game.machines,game.player))
    //println(game.getGameState())

  }
}

class PizzaBudeController extends Initializable {

  @FXML var btnPizza: Button = _
  @FXML var btnDrink: Button = _
  @FXML var btnStart: Button = _
  @FXML var btnStop: Button = _

  var game:GameLoop = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    val machines = Seq(PizzaOven,Drink)
    val guests: mutable.Map[Guest, Seq[Product]] = mutable.Map()
    val player = Player(0)
    val g = PizzaBude.apply(guests,machines,player)
    g.setGameState(g)
    game = GameLoop(g)

  }

  @FXML def pizza():Unit = if(!PizzaOven.getState()) {PizzaOven.setProperty(true);println("Start PIZZAOFEN")}
  @FXML def drink():Unit = if(!Drink.getState()) {Drink.setProperty(true);println("Start DRINK")}
  @FXML def start():Unit = game.start()
  @FXML def stop():Unit = game.stop()

}