package ru.profitsw2000.diarynote.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.profitsw2000.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}