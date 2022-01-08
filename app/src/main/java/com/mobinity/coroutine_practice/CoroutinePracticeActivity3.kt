package com.mobinity.coroutine_practice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePractice2Binding
import com.mobinity.coroutine_practice.databinding.ActivityCoroutinePractice3Binding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

/**
 * Simple coroutine practice 3
 * 1. jobs
 */
class CoroutinePracticeActivity3 : AppCompatActivity() {

    private lateinit var binding: ActivityCoroutinePractice3Binding

    private val PROGRESS_MAX = 100
    private val PROGRESS_START = 0
    private val JOB_TIME = 4000 // ms
    private lateinit var job: CompletableJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoroutinePractice3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.jobButton.setOnClickListener {
            if (!::job.isInitialized) {
                initJob()
            }
            binding.jobProgressBar.startJobOrCancel(job)
        }
    }

    private fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            println("$job is already active. Cancelling...")
            resetJob()
        } else {
            binding.jobButton.text = "Cancel job #1"
            CoroutineScope(IO + job).launch {
                println("coroutine $this is activated with job $job")

                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }

                updateJobCompleteTextView("Job is complete")
            }
        }
    }

    private fun updateJobCompleteTextView(text: String) {
        GlobalScope.launch(Main) {
            binding.jobCompleteText.text = text
        }
    }

    private fun resetJob() {
        if(job.isActive || job.isCompleted) {
            job.cancel(CancellationException("Resetting job"))
        }
        initJob()
    }

    private fun initJob() {
        binding.jobButton.text = "Start Job #1"
        updateJobCompleteTextView("")
        job = Job()
        job.invokeOnCompletion {
            it?.message.let {
                var msg = it
                if (msg.isNullOrBlank()) {
                    msg = "Unknown cancellation error."
                }
                println("$job was cancelled. Reason: $msg")
                showToast(msg)
            }
        }
        binding.jobProgressBar.max = PROGRESS_MAX
        binding.jobProgressBar.progress = PROGRESS_START
    }

    private fun showToast(text: String) {
        GlobalScope.launch(Main) {
            Toast.makeText(this@CoroutinePracticeActivity3, text, Toast.LENGTH_SHORT).show()
        }
    }

}