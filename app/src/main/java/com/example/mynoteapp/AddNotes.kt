package com.example.mynoteapp

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {


    var id=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        supportActionBar?.title = "Add note"
//        btnBack.setOnClickListener { finish() }


        try {
            var bundle:Bundle = intent.extras!!
            id = bundle.getInt("ID",0)
            if (id!=0){
                edTitle.setText(bundle.getString("Name").toString())
                edDesc.setText(bundle.getString("DESC").toString())
            }
        } catch (ex:Exception){}


    }



    fun btnSave(view: View) {

        if(edTitle.text.isNotEmpty() && edDesc.text.isNotEmpty()){

            var dbmanager = DbManager(this)
            var values = ContentValues()
            values.put("Title",edTitle.text.toString())
            values.put("Description",edDesc.text.toString())




            if (id==0){
                val ID = dbmanager.insertNotes(values)
                if (ID>0){
                    Toast.makeText(this,"Saved successfully" , Toast.LENGTH_SHORT).show()
                    finish()
                }else {
                    Toast.makeText(this,"Failed" , Toast.LENGTH_SHORT).show()
                }


            } else {


                var selectionArgs = arrayOf(id.toString())
                val ID = dbmanager.Update(values,"ID=?",selectionArgs)

                if (ID>0){
                    Toast.makeText(this,"Saved successfully" , Toast.LENGTH_SHORT).show()
                    finish()
                }else {
                    Toast.makeText(this,"Failed" , Toast.LENGTH_SHORT).show()
                }


            }
        } else {
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show()
        }




    }
}
