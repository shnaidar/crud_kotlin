package com.example.sqlite
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val DATABASE_VERSION = 1
private const val DATABASE_NAME = "FeedReader.db"

private val SQL_CREATE_ENTRIES =
    "CREATE TABLE ${FeedReaderContract.FeedEntry.TABLE_NAME} (" +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} TEXT," +
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE} TEXT)"

private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedReaderContract.FeedEntry.TABLE_NAME}"

class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
}