package com.example.sqlite
import android.provider.BaseColumns

object FeedReaderContract {
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "entry"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_SUBTITLE = "subtitle"
        const val COLUMN_NAME_ID = "_id"  // Ajoutez cette ligne
    }
}
