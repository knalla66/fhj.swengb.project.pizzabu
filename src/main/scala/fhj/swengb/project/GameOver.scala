package fhj.swengb.project

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Scene, Parent}
import javafx.stage.Stage

import scala.util.control.NonFatal

/**
 * Created by Verena on 25.01.2016.
 */
object GameOver {


  def main(args: Array[ String ]) {
    Application.launch(classOf[ GameOver ], args: _*)
  }
}

class GameOver extends Application {

  val loader = new FXMLLoader(getClass.getResource("GUI-GameOver.fxml"))

  override def start(stage: Stage): Unit =
    try {
      stage.setTitle("PizzaBu - Game Over!")
      loader.load[Parent]()
      stage.setScene(new Scene(loader.getRoot[ Parent ]))

      stage.show()
    } catch {
      case NonFatal(e) => e.printStackTrace()

    }

}
