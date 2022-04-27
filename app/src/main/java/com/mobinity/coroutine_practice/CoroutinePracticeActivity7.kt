package com.mobinity.coroutine_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePractice7Binding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


/**
 * Simple coroutine practice
 * 1. structured concurrency
 * 2. error handling and exceptions
 */
class CoroutinePracticeActivity7 : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinePractice7Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinePractice7Binding.inflate(layoutInflater)
        setContentView(binding.root)

        firstScenario()
        secondScenario()
        thirdScenario()


    }


    val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown in one of the children: $exception")
    }



    /**
     * first scenario:
     *  -> jobA success
     *  -> jobB fail (throws exception)
     *  -> jobC success
     *
     *  result:
     *  -> jobA success
     *  -> jobB fail
     *  -> jobC fail
     *  -> parentJob fail
     *  so any jobs after the error job fails fail -> parent job fail
     */
    private fun firstScenario() {
        val parentJob = CoroutineScope(IO).launch(handler) {

            // --------- JOB A ---------
            val jobA = launch {
                val resultA = getResult(1)
                println("resultA: $resultA")
            }
            jobA.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultA: $throwable")
                }
            }

            // --------- JOB B ---------
            val jobB = launch {
                val resultB = getResult(2)
                println("resultB: $resultB")
            }
            jobB.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultB: $throwable")
                }
            }

            // --------- JOB C ---------
            val jobC = launch {
                val resultC = getResult(3)
                println("resultC: $resultC")
            }
            jobC.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultA: $throwable")
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

    /**
     * second scenario:
     *  -> jobA success
     *  -> jobB canceled
     *  -> jobC success
     *
     *  result:
     *  -> jobA success
     *  -> jobB success
     *  -> jobC success
     *  -> parentJob success
     *  eventually cancel did nothing
     */
    private fun secondScenario() {
        val parentJob = CoroutineScope(IO).launch(handler) {

            // --------- JOB A ---------
            val jobA = launch {
                val resultA = getResult(1)
                println("resultA: $resultA")
            }
            jobA.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultA: $throwable")
                }
            }

            // --------- JOB B ---------
            val jobB = launch {
                val resultB = getResult(2)
                println("resultB: $resultB")
            }
            jobB.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultB: $throwable")
                }
            }

            // --------- JOB C ---------
            val jobC = launch {
                val resultC = getResult(3)
                println("resultC: $resultC")
            }
            jobC.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultA: $throwable")
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

    /**
     * third scenario:
     *  -> jobA success
     *  -> jobB cancellation exception
     *  -> jobC success
     *
     *  result:
     *  -> jobA success
     *  -> jobB cancel
     *  -> jobC success
     *  -> parentJob success
     *
     */
    private fun thirdScenario() {
        val parentJob = CoroutineScope(IO).launch(handler) {

            // --------- JOB A ---------
            val jobA = launch {
                val resultA = getResult(1)
                println("resultA: $resultA")
            }
            jobA.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultA: $throwable")
                }
            }

            // --------- JOB B ---------
            val jobB = launch {
                val resultB = getResult(2)
                println("resultB: $resultB")
            }
            jobB.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultB: $throwable")
                }
            }

            // --------- JOB C ---------
            val jobC = launch {
                val resultC = getResult(3)
                println("resultC: $resultC")
            }
            jobC.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultA: $throwable")
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

        //thirdScenario
        if (number == 2) {
            throw CancellationException("Error getting result for number $number")
        }

        return number * 2
    }

    private fun println(message: String) {
        Log.d("TAG", message)
    }
}