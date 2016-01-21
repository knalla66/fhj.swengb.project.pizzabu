package fhj.swengb.project

import java.sql.{Connection, PreparedStatement, ResultSet, Statement}
import javafx.beans.property.{SimpleIntegerProperty, SimpleStringProperty}
import javafx.beans.value.ObservableValue
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.control.TableColumn
import javafx.util.Callback

import scala.collection.JavaConversions
import scala.collection.mutable.ListBuffer

/**
 * Created by Verena on 19.01.2016.
 */
object Highscore {


/**
 * domain object
 */
case class Score(rang: Int, name: String, highscore: Int) extends Db.DbEntity[ Score ] {

  def reTable(stmt: Statement): Int = 0

  def toDb(c: Connection)(t: Score): Int = 0

  def fromDb(rs: ResultSet): List[ Score ] = List()

  def dropTableSql: String = "drop table if exists score"

  def createTableSql: String = "create table score (rang integer, name string, highscore integer)"

  def insertSql: String = "insert into score (rang, name, highscore) VALUES (?, ?, ?)"

  def printlist(l: List[ Score ]): Unit = {}

  def printscore(s: Score): Unit = {}

}

/**
 * domain object, but usable with javafx
 */
class MutableScore {

  val rangProperty: SimpleIntegerProperty = new SimpleIntegerProperty()
  val nameProperty: SimpleStringProperty = new SimpleStringProperty()
  val scoreProperty: SimpleIntegerProperty = new SimpleIntegerProperty()

  def setRang(id: Int) = rangProperty.set(id)

  def setName(name: String) = nameProperty.set(name)

  def setScore(score: Int) = scoreProperty.set(score)
}


// companion object für eine bessere Initialisierung

object MutableScore {

  def apply(a: Score): MutableScore = {
    val ma = new MutableScore
    ma.setRang(a.rang)
    ma.setName(a.name)
    ma.setScore(a.highscore)
    ma
  }

}

//Funktionen um Scala mit JavaFx zu verbinden

object JfxUtils {

  type TCDF[ S, T ] = TableColumn.CellDataFeatures[ S, T ]

  import JavaConversions._

  def mkObservableList[ T ](collection: Iterable[ T ]): ObservableList[ T ] = {
    FXCollections.observableList(new java.util.ArrayList[ T ](collection))
  }

  private def mkCellValueFactory[ S, T ](fn: TCDF[ S, T ] => ObservableValue[ T ]): Callback[ TCDF[ S, T ], ObservableValue[ T ] ] = {
    new Callback[ TCDF[ S, T ], ObservableValue[ T ] ] {
      def call(cdf: TCDF[ S, T ]): ObservableValue[ T ] = fn(cdf)
    }
  }

  def initTableViewColumnCellValueFactory[ S, T ](tc: TableColumn[ S, T ], f: S => Any): Unit = {
    tc.setCellValueFactory(mkCellValueFactory(cdf => f(cdf.getValue).asInstanceOf[ ObservableValue[ T ] ]))
  }

}

//Datenbank

object Score extends Db.DbEntity[ Score ] {
  val first = Score(1, "Verena", 15666858)
  val second = Score(2, "Dani", 58968)
  val third = Score(3, "Tati", 15852)

  val highscore: Set[ Score ] = Set(first, second, third)

  lazy val highscorelist = highscore.toList

  val dropTableSql = "drop table if exists score"
  val createTableSql = "create table score (rang integer, name string, highscore integer)"
  val insertSql = "insert into score (rang, name, highscore) VALUES (?, ?, ?)"


  def reTable(stmt: Statement): Int = {
    stmt.executeUpdate(Score.dropTableSql)
    stmt.executeUpdate(Score.createTableSql)
  }

  def toDb(c: Connection)(i: Score): Int = {
    val pstmt: PreparedStatement = c.prepareStatement(insertSql)
    pstmt.setInt(1, i.rang)
    pstmt.setString(2, i.name)
    pstmt.setInt(3, i.highscore)
    pstmt.executeUpdate()
  }

  def fromDb(rs: ResultSet): List[ Score ] = {
    val lb: ListBuffer[ Score ] = new ListBuffer[ Score ]()
    while (rs.next()) lb.append(Score(rs.getInt("rang"), rs.getString("name"), rs.getInt("highscore")))
    lb.toList
  }

  def queryAll(c: Connection): ResultSet = query(c)("select * from score")

  def printlist(l: List[ Score ]): Unit = println(l)

  def printscore(s: Score): Unit = println {
    "Rang= " + s.rang + " Name= " + s.name + " Highscore = " + s.highscore
  }

}
}
