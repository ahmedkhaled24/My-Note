package com.example.mynoteapp

import android.app.SearchManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.accessibility.AccessibilityManager
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_add_notes.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.*
import kotlinx.android.synthetic.main.ticket.view.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private var backPressedTime = 0L

    var ListNotes = ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoadQuery("%")
    }

    // علشان لما ادخل اكتب نوت جديد واحفظها اخرج الاقيها موجودة بدل ماعيد تشغيل الطبيق
    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    // اضغط مرتين للخروج
    override fun onBackPressed() {
        if(backPressedTime + 2000 > System.currentTimeMillis()){
            super.onBackPressed()
        } else {
            Toast.makeText(this,"Press back again to exit page",Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }


    // اظهار النوت ف الليست
    fun LoadQuery(title: String){
        var dbManager = DbManager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections,"Title like ?",selectionArgs,"ID")
        ListNotes.clear()
        if (cursor.moveToFirst()){
            do{
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Description = cursor.getString(cursor.getColumnIndex("Description"))
//                val datee = cursor.getInt(cursor.getColumnIndex("Date"))

                ListNotes.add(Note(ID,Title,Description))

            } while (cursor.moveToNext())
        }

        var myNoteAdapter = MyNoteAdapter(this, ListNotes)
        lvNotes.adapter = myNoteAdapter
    }


//    fun dateQuery(){
//        var calendar = Calendar.getInstance()
//        var y = calendar.get(Calendar.YEAR)
//        var m = calendar.get(Calendar.MONTH)
//        var d = calendar.get(Calendar.DAY_OF_MONTH)
//    }




    inner class MyNoteAdapter:BaseAdapter{
        var context:Context?=null
        var ListNotesAdapter = ArrayList<Note>()
        constructor(context: Context,ListNotesAdapter: ArrayList<Note>):super(){
            this.ListNotesAdapter = ListNotesAdapter
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView = layoutInflater.inflate(R.layout.ticket,null)
            var myNode = ListNotesAdapter[position]
            myView.tvTitle.text = myNode.nodeName
            myView.tvDesc.text = myNode.nodeDes
            myView.image_delete_icon.setOnClickListener(View.OnClickListener {
                val alertBuilder = AlertDialog.Builder(this@MainActivity)
                alertBuilder.setMessage("Do you want to delete this item ?")
                    .setPositiveButton("Yes" , DialogInterface.OnClickListener { dialog, which ->
                        var dbManager = DbManager(this.context!!)
                        val selectionArgs = arrayOf(myNode.nodeID.toString())
                        dbManager.Delete("ID=?",selectionArgs)
                        LoadQuery("%") })
                    .setNegativeButton("No" , DialogInterface.OnClickListener { dialog, which -> })
                    .show() })
            myView.image_update_icon.setOnClickListener(View.OnClickListener {
                goTOUpdate(myNode) })
//            myView.date_text.text = myNode.nodeDate

            return myView
        }

        override fun getItem(position: Int): Any {
            return ListNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return ListNotesAdapter.size
        }
    }
//----------------------------end inner class-------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu,menu)

        //Searching -------------------------
        val sv = menu?.findItem(R.id.search_menu)?.actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //TO Search Database and print it
                Toast.makeText(applicationContext,query,Toast.LENGTH_SHORT).show()
                LoadQuery("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                LoadQuery("%")
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item!=null){
            when(item.itemId){
                R.id.add_note_menu -> {
                    var intent = Intent(this,AddNotes::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun goTOUpdate(note:Note){
        var intent = Intent(this,AddNotes::class.java)
        intent.putExtra("ID",note.nodeID)
        intent.putExtra("Name",note.nodeName)
        intent.putExtra("DESC",note.nodeDes)
        startActivity(intent)
    }
}
