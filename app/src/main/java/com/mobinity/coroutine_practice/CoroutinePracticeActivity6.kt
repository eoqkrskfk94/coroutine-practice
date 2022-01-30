package com.mobinity.coroutine_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePractice6Binding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


/**
 * Simple coroutine practice
 * 1. globalScope
 *    don't use globalScope (doesn't seem to sync up with the parent job)
 */
class CoroutinePracticeActivity6 : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinePractice6Binding

    private lateinit var parentJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinePractice6Binding.inflate(layoutInflater)
        setContentView(binding.root)

        main()

        binding.button.setOnClickListener {
            parentJob.cancel()
        }

    }

    suspend fun work(i: Int) {
        delay(3000)
        println("Work $i done. ${Thread.currentThread().name}")
    }

    private fun main() {
        val startTime = System.currentTimeMillis()
        println("starting parent job...")
        parentJob = CoroutineScope(Main).launch {

            // globalScope doesn't not get canceled when the parentJob is cancel.
            GlobalScope.launch { work(1) }
            GlobalScope.launch { work(2) }

        }
        parentJob.invokeOnCompletion { throwable ->
            if(throwable != null) {
                println("Job was canceled after ${System.currentTimeMillis() - startTime} ms.")
            }
            else {
                println("Done in ${System.currentTimeMillis() - startTime} ms.")
            }
        }
    }
}