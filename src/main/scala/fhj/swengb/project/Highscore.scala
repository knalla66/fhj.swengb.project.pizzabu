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
case class Score(name: String, highscore: Int) extends Db.DbEntity[ Score ] {

  def reTable(stmt: Statement): Int = 0

  def toDb(c: Connection)(t: Score): Int = 0

  def fromDb(rs: ResultSet): List[ Score ] = List()


  def dropTableSql: String = "drop table if exists score"

  def createTableSql: String = "create table if not exists score (name string, highscore integer)"

  def insertSql: String = "insert into score (name, highscore) VALUES (?, ?)"

  def tableexistsSql: String = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='score'"

  def printlist(l: List[ Score ]): Unit = {}

  def printscore(s: Score): Unit = {}

  def createTable (stmt: Statement): Int = 0

  def existTable (stmt: Statement): Int = 0

}

/**
 * domain object, but usable with javafx
 */
class MutableScore {

  val nameProperty: SimpleStringProperty = new SimpleStringProperty()
  val scoreProperty: SimpleIntegerProperty = new SimpleIntegerProperty()


  def setName(name: String) = nameProperty.set(name)

  def setScore(score: Int) = scoreProperty.set(score)
}


// companion object für eine bessere Initialisierung

object MutableScore {

  def apply(a: Score): MutableScore = {
    val ma = new MutableScore
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
  val one = Score("Verena", 15666858)
  val two = Score( "Dani", 58968)
  val three = Score("Tati", 321548)
  val four = Score("Edith",25865)
  val five = Score("Marie", 2568768)
  val six = Score("Sophie", 2727272)
  val seven = Score("Michi", 52577)
  val eight = Score("Benji", 25245)
  val nine = Score("Benni", 7000)
  val ten = Score("Alex", 78688)




  val highscore: Set[ Score ] = Set(one,two,three,four,five,six,seven,eight,nine,ten)

  lazy val highscorelist = highscore.toList

  val dropTableSql = "drop table if exists score"
  val createTableSql = "create table if not exists score (name string, highscore integer)"
  val insertSql = "insert into score (name, highscore) VALUES (?, ?)"

  def createTable (stmt: Statement) : Int ={
    stmt.executeUpdate(Score.createTableSql)
  }

  def reTable(stmt: Statement): Int = {
    stmt.executeUpdate(Score.dropTableSql)
    stmt.executeUpdate(Score.createTableSql)
  }

  def toDb(c: Connection)(i: Score): Int = {
    val pstmt: PreparedStatement = c.prepareStatement(insertSql)
    pstmt.setString(1, i.name)
    pstmt.setInt(2, i.highscore)
    pstmt.executeUpdate()
  }

  def fromDb(rs: ResultSet): List[ Score ] = {
    val lb: ListBuffer[ Score ] = new ListBuffer[ Score ]()
    while (rs.next()) lb.append(Score(rs.getString("name"), rs.getInt("highscore")))
    lb.toList
  }



  def queryAll(c: Connection): ResultSet = query(c)("select * from score order by highscore DESC")

  def queryMaster (c:Connection): ResultSet = query(c)("SELECT count(*) FROM sqlite_master WHERE type = 'table' AND tbl_name = 'score'")

  def printlist(l: List[ Score ]): Unit = println(l)

  def printscore(s: Score): Unit = println {
    " Name= " + s.name + ", Highscore = " + s.highscore
  }

}


}
