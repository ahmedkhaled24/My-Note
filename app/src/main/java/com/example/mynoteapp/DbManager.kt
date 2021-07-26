package com.example.mynoteapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.media.projection.MediaProjection
import android.text.Selection
import android.widget.Toast
import java.nio.ByteOrder

// ده سكول داتا بيز بعمله علشان اخزن فيه اللي هكتبه واسجله وهو عبارة عن جدول به تلات اعمدة اي دي والنيم والديسكريبشن

class DbManager {
    val dbName = "MyNotes"
    val dbTable = "Notes"

    val colID = "ID"
    val colTitle = "Title"
    val colDesc = "Description"
    val colDate = "Date"
    val dbVersion = 1

    // create sqlite to store data
    val sqlCreateTeble = "CREATE TABLE IF NOT EXISTS "+ dbTable + " (" + colID + " INTEGER PRIMARY KEY, " +
            colTitle + " TEXT, " + colDate + " Text, " + colDesc + " TEXT);"

    var sqlDB: SQLiteDatabase? = null

    constructor(context: Context){
        var db = DatabaseHelperNotes(context)
        sqlDB = db.writableDatabase
    }

    inner class DatabaseHelperNotes : SQLiteOpenHelper {

        var context:Context? = null
        constructor(context: Context):super(context,dbName,null,dbVersion){
            this.context = context
        }

        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL(sqlCreateTeble)
            Toast.makeText(this.context,"Database is created",Toast.LENGTH_SHORT).show()
        }

        override fun onUpgrade(p0: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            p0!!.execSQL("Drop table IF EXISTS "+ dbTable)
        }

    }
    //---------------------- end the inner class -----------------


    fun insertNotes (values: ContentValues):Long{
        var id = sqlDB!!.insert(dbTable,"",values)
        return id
    }


    // 66. list notes from Sqlite database
    fun Query(projection:Array<String>,selection:String,selectionArgs: Array<String>,sorOrder:String):Cursor{
        val qb=SQLiteQueryBuilder()
        qb.tables = dbTable
        val cursor = qb.query(sqlDB,projection,selection,selectionArgs,null,null,sorOrder)
        return cursor
    }


    // delete query
    fun Delete(selection:String,selectionArgs: Array<String>):Int{
        val count = sqlDB!!.delete(dbTable,selection,selectionArgs)
        return count
    }


    // updata query
    fun Update(values:ContentValues,selection: String,selectionArgs: Array<String>):Int{
        val count = sqlDB!!.update(dbTable,values,selection,selectionArgs)
        return count
    }

}