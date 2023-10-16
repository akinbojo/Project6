package com.example.projectnum6

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.widget.Toast
import com.example.projectnum6.databinding.ActivityMainBinding
import com.example.projectnum6.databinding.ActivityNoteFragmentBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.logging.SimpleFormatter

class NoteFragment : AppCompatActivity() {
    private lateinit var binding: ActivityNoteFragmentBinding

    private lateinit var note : com.example.projectnum6.Models.Note
    private lateinit var old_note : com.example.projectnum6.Models.Note

    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteFragmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {
            old_note = intent.getSerializableExtra("current_note") as com.example.projectnum6.Models.Note
            binding.etTitle.setText(old_note.title)
            binding.etNote.setText(old_note.note)
            isUpdate = true
        }catch(e : Exception){
            e.printStackTrace()
        }

        binding.saveButton.setOnClickListener{
            val title = binding.etTitle.text.toString()
            val note_desc = binding.etNote.text.toString()

            if(title.isNotEmpty() || note_desc.isNotEmpty()){
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a ")

                if (isUpdate){

                    note = com.example.projectnum6.Models.Note(
                        old_note.id, title, note_desc, formatter.format(Date())
                    )
                }
                else{
                    note = com.example.projectnum6.Models.Note(
                        null, title, note_desc, formatter.format(Date())
                    )
                }

                val intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK,intent)

            }else{
                Toast.makeText(this@NoteFragment, "Please enter some data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


        }
    }
}