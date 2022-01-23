package com.mobinity.coroutine_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePractice2Binding
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePractice3Binding
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePractice4Binding
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePracticeBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlin.system.measureTimeMillis

/**
 * Simple coroutine practice 4
 * 1. parallel background tasks with kotlin coroutine (ASYNC and AWAIT)
 */
class CoroutinePracticeActivity4 : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinePractice4Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinePractice4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            setNewText("Clicked!")

            fakeApiRequest()
            fakeApiRequestUsingAsyncAndAwait()

        }


    }

    private fun fakeApiRequest() {

        val startTime = System.currentTimeMillis()

        val parentJob = CoroutineScope(IO).launch {

            val job1 = launch {
                val time1 = measureTimeMillis {
                    println("debug: launching job1 in thread: ${Thread.currentThread().name}")
                    val result1 = getResult1FromApi()
                    setTextOnMainThread("Got $result1")
                }
                println("debug: completed job1 in $time1 ms.")
            }

            // wait for job1 to finish before executing the next line (job2)
            // job1.join()

            val job2 = launch {
                val time2 = measureTimeMillis {
                    println("debug: launching job2 in thread: ${Thread.currentThread().name}")
                    val result2 = getResult2FromApi()
                    setTextOnMainThread("Got $result2")
                }
                println("debug: completed job2 in $time2 ms.")
            }
        }
        parentJob.invokeOnCompletion {
            println("debug: total elapsed time: ${System.currentTimeMillis() - startTime}")
        }
    }

    private fun fakeApiRequestUsingAsyncAndAwait() {
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {

                val result1: Deferred<String> = async {
                    println("debug: launching job1: ${Thread.currentThread().name}")
                    getResult1FromApi()
                }

                val result2: Deferred<String> = async {
                    println("debug: launching job2: ${Thread.currentThread().name}")
                    getResult2FromApi()
                }

                setTextOnMainThread("Got ${result1.await()}")
                setTextOnMainThread("Got ${result2.await()}")

            }
            println("debug: total time elapsed: $executionTime")
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

    private suspend fun getResult2FromApi(): String {
        delay(1700)
        return "RESULT_2"
    }

}