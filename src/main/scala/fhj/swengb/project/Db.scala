package fhj.swengb.project

import java.sql.{ResultSet, Statement, DriverManager, Connection}


import scala.util.Try

/**
 * Created by Verena on 19.01.2016.
 */
object Db {


    /*
    *
    * Datenbank-Verbindung erstellen
    *
    * */

    lazy val maybeConnectionWindows: Try[Connection] = {

      Try(DriverManager.getConnection("jdbc:sqlite://C:\\PizzaBu\\score.db"))
    }

  lazy val maybeConnectionMac: Try[Connection] = {

    Try(DriverManager.getConnection("jdbc:sqlite:Macintosh HD\\PizzaBu\\score.db"))
  }

    /**
     * A marker interface for datastructures which should be persisted to a jdbc database.
     *
     * @tparam T the type to be persisted / loaded
     */
    trait DbEntity[T] {

      /**
       * Recreates the table this entity is stored in
       *
       * @param stmt
       * @return
       */
      def reTable(stmt: Statement): Int


      /*
      *
      * erstellt eine neue Datenbank, wenn noch keine Existiert
      */

      def createTable (stmt: Statement): Int

      /**
       * Saves given type to the database.
       *
       * @param c
       * @param t
       * @return
       */
      def toDb(c: Connection)(t: T): Int

      /**
       * Given the resultset, it fetches its rows and converts them into instances of T
       *
       * @param rs
       * @return
       */
      def fromDb(rs: ResultSet): List[T]

      /**
       * Queries the database
       *
       * @param con
       * @param query
       * @return
       */
      def query(con: Connection)(query: String): ResultSet = {
        con.createStatement().executeQuery(query)
      }

      def queryMaster(con: Connection)(query: String): ResultSet = {
        con.createStatement().executeQuery(query)
      }

      /**
       * Sql code necessary to execute a drop table on the backing sql table
       *
       * @return
       */
      def dropTableSql: String

      /**
       * sql code for creating the entity backing table
       */
      def createTableSql: String

      /**
       * sql code for inserting an entity.
       */
      def insertSql: String


    }


}
