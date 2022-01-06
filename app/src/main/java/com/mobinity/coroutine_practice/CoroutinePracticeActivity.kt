package com.mobinity.coroutine_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePracticeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Simple coroutine practice
 * 1. coroutineScope
 * 2. withContext
 */
class CoroutinePracticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinePracticeBinding

    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinePracticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {

            // IO(network and local database), Main(Interact with UI), Default(heavy calculation)
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }

    }

    private fun setNewText(input: String){
        val newText = binding.text.toString() + "\n$input"
        binding.text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        //switch context of coroutine
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun fakeApiRequest() {

        val result1 = getResult1FromApi() // wait until completion
        println("debug: $result1")
        setTextOnMainThread(result1)

        val result2 = getResult2FromApi(result1) // wait until completion
        setTextOnMainThread(result2)
    }

    private suspend fun getResult1FromApi(): String {
        logThread("gerResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(result: String): String {
        logThread("gerResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }
}