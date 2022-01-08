package com.mobinity.coroutine_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePractice2Binding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

/**
 * Simple coroutine practice 2
 * 1. network timeouts
 */
class CoroutinePracticeActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinePractice2Binding

    private val JOB_TIMEOUT = 1900L
    private val RESULT_1 = "Result #1"
    private val RESULT_2 = "Result #2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinePractice2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            setNewText("Click!")

            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }

    }

    private suspend fun fakeApiRequest() {
        withContext(IO) {

            val job = withTimeoutOrNull(JOB_TIMEOUT) {
                val result1 = getResult1FromApi() // wait until completion
                setTextOnMainThread("Got $result1")

                val result2 = getResult2FromApi() // wait until completion
                setTextOnMainThread("Got $result2")
            } // wait until completion

            if (job == null) {
                val cancelMessage = "Cancelling job... Job took longer than $JOB_TIMEOUT ms"
                setTextOnMainThread(cancelMessage)
            }
        }
    }

    private fun setNewText(input: String) {
        val newText = binding.text.text.toString() + "\n$input"
        binding.text.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        //switch context of coroutine
        withContext(Dispatchers.Main) {
            setNewText(input)
        }
    }


    private suspend fun getResult1FromApi(): String {
        logThread("gerResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String {
        logThread("gerResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName: String) {
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }
}