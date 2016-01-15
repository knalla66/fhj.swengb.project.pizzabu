package fhj.swengb.project

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Scene, Parent}
import javafx.stage.Stage

import scala.util.control.NonFatal

/**
 * Created by KnallerMJ on 14.01.16.
 */


object PizzaBuApp {
  def main(args: Array[String]) {
    Application.launch(classOf[PizzaBuAppFX], args: _*)
  }

}


class PizzaBuAppFX extends javafx.application.Application {

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
