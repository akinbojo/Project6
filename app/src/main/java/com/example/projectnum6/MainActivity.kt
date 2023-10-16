package com.example.projectnum6

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.projectnum6.Adapter.NotesAdapter
import com.example.projectnum6.Database.NoteDatabase
import com.example.projectnum6.Models.Note
import com.example.projectnum6.Models.NoteViewModel
import com.example.projectnum6.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity(), NotesAdapter.NotesClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var  database: NoteDatabase
    lateinit var viewModel: NoteViewModel
    lateinit var adapter : NotesAdapter
    lateinit var selectedNote : Note

    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == Activity.RESULT_OK) {
            val note = result.data?.getSerializableExtra("note") as? Note
            if(note != null) {
                viewModel.updateNote(note)
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)
        viewModel.allnotes.observe(this){list ->

            list?.let{
                adapter.updateList(list)
            }

        }

        database = NoteDatabase.getdatabase(this)
    }

    private fun initUI(){
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        adapter = NotesAdapter(this, this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == Activity.RESULT_OK){
                val note = result.data?.getSerializableExtra("note") as? Note
                if(note != null){
                    viewModel.insertNote(note)
                }
            }
        }


        binding.addButton.setOnClickListener{
            val intent = Intent(this,NoteFragment::class.java)
            getContent.launch(intent)
        }

    }

    override fun onItemClicked(note: Note) {
        val  intent = Intent(this@MainActivity,NoteFragment::class.java)
        intent.putExtra("current_note", note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
    }

}

