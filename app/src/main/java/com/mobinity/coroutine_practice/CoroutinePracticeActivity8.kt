package com.mobinity.coroutine_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePractice8Binding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


/**
 * Simple coroutine practice
 * 1. supervisor job
 *    gives children jobs to handle their own exception without failing
 */
class CoroutinePracticeActivity8 : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinePractice8Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinePractice8Binding.inflate(layoutInflater)
        setContentView(binding.root)

        main()


    }


    val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown in one of the children: $exception")
    }


    private fun main() {
        val parentJob = CoroutineScope(IO).launch(handler) {

            supervisorScope {
                // --------- JOB A ---------
                val jobA = launch {
                    val resultA = getResult(1)
                    println("resultA: $resultA")
                }

                // --------- JOB B ---------
                val jobB = launch {
                    val resultB = getResult(2)
                    println("resultB: $resultB")
                }

                // --------- JOB C ---------
                val jobC = launch {
                    val resultC = getResult(3)
                    println("resultC: $resultC")
                }
            }
        }

        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("Parent job failed: $throwable")
            } else {
                println("Parent job SUCCESS")
            }
        }
    }


    private suspend fun getResult(number: Int): Int {
        delay(number * 500L)
        //firstScenario
        if (number == 2) {
            throw Exception("Error getting result for number $number")
        }

        return number * 2
    }

    private fun println(message: String) {
        Log.d("TAG", message)
    }
}