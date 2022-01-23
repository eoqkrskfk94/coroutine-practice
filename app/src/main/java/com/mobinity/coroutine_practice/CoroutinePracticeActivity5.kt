package com.mobinity.coroutine_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobinity.coroutine_practice.databinding.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.system.measureTimeMillis

/**
 * Simple coroutine practice 5
 * 1. sequential background tasks using ASYNC and AWAIT
 */
class CoroutinePracticeActivity5 : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinePractice5Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinePractice5Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            setNewText("Clicked!")

            fakeApiRequest()
        }


    }

    private fun fakeApiRequest() {

        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {

                val result1 = async {
                    println("debug: launching job1: ${Thread.currentThread().name}")
                    getResult1FromApi()
                }.await()

                val result2 = async {
                    println("debug: launching job2: ${Thread.currentThread().name}")
                    getResult2FromApi(result1)
                }.await()

                println("debug: got result2: $result2")
            }
            println("debug: total elapsed time $executionTime ms.")
        }
    }


    private fun setNewText(input: String) {
        val newText = binding.text.toString() + "\n$input"
        binding.text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        //switch context of coroutine
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "RESULT_1"
    }

    private suspend fun getResult2FromApi(result1: String): String {
        delay(1700)
        if(result1 == "RESULT_1") {
            return "RESULT_2"
        }
        return "Result #1 was incorrect..."

    }

}