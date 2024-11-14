package com.example.sqlite

import android.content.ContentValues
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sqlite.ui.theme.SqliteTheme
import com.example.sqlite.FeedReaderDbHelper
import com.example.sqlite.FeedReaderContract

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: FeedReaderDbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = FeedReaderDbHelper(this)

        setContent {
            SqliteTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DatabaseOperationsScreen(
                        onInsert = { insertData("Titre Exemple", "Sous-titre Exemple") },
                        onRead = { readData() },
                        onUpdate = { updateData("Titre Exemple", "Nouveau Titre") },
                        onDelete = { deleteData("Titre Exemple") },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun insertData(title: String, subtitle: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE, subtitle)
        }
        val newRowId = db?.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
        Log.d("DatabaseOperation", "Nouvelle ligne insérée avec ID : $newRowId")
    }

    private fun readData() {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
            FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE
        )
        val cursor = db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            "${FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE} DESC"
        )
        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(BaseColumns._ID))
                val title = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE))
                val subtitle = getString(getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE))
                Log.d("DatabaseOperation", "ID : $itemId, Title : $title, Subtitle : $subtitle")
            }
        }
        cursor.close()
    }

    private fun updateData(oldTitle: String, newTitle: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, newTitle)
        }

        // Selection criteria to match rows with the old title
        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} = ?"
        val selectionArgs = arrayOf(oldTitle)

        // Log the values for debugging purposes
        Log.d("DatabaseOperation", "Trying to update title: $oldTitle to new title: $newTitle")

        val count = db.update(
            FeedReaderContract.FeedEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )

        Log.d("DatabaseOperation", "Nombre de lignes mises à jour : $count")
    }


    private fun deleteData(title: String) {
        val db = dbHelper.writableDatabase
        val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE} LIKE ?"
        val selectionArgs = arrayOf(title)
        val deletedRows = db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null)
        Log.d("DatabaseOperation", "Nombre de lignes supprimées : $deletedRows")
    }
}

@Composable
fun DatabaseOperationsScreen(
    onInsert: () -> Unit,
    onRead: () -> Unit,
    onUpdate: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = onInsert, modifier = Modifier.padding(8.dp)) {
            Text("Insert Data")
        }
        Button(onClick = onRead, modifier = Modifier.padding(8.dp)) {
            Text("Read Data")
        }
        Button(onClick = onUpdate, modifier = Modifier.padding(8.dp)) {
            Text("Update Data")
        }
        Button(onClick = onDelete, modifier = Modifier.padding(8.dp)) {
            Text("Delete Data")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DatabaseOperationsScreenPreview() {
    SqliteTheme {
        DatabaseOperationsScreen(
            onInsert = {},
            onRead = {},
            onUpdate = {},
            onDelete = {}
        )
    }
}
