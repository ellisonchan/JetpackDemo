package com.ellison.jetpackdemo.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ellison.jetpackdemo.databinding.ActivityCoroutinesBinding
import kotlinx.coroutines.*

class DemoActivity : AppCompatActivity() {
    val TAG = "Coroutines"
    private lateinit var binding: ActivityCoroutinesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCoroutinesBinding.inflate(layoutInflater);
        setContentView(binding.root)

        // loadDataAsync()
        // launchDataAsyncSerial()
        // launchDataAsyncBlock()
        launchDataAsyncSerialBlock()
    }

    /* - - -           --> end
     *     |          |
     *     -> async --|
     */
    private fun loadDataAsync() {
        Log.d(TAG, "loadDataAsync() invoke at${Thread.currentThread().name}")
        var deferred = GlobalScope.async {
            Log.d(TAG, "loadDataAsync() run at${Thread.currentThread().name}")
            delay(3000L)
            Log.d(TAG, "loadDataAsync() run finished")
        }
        Log.d(TAG, "loadDataAsync() invoked")

        // deferred.
    }

    /* - - > middle
     *     |
     *     -> before
     *     |
     *     -> after ----> end
     */
    private fun launchDataAsyncSerial() {
        Log.d(TAG, "launchDataAsyncSerial() invoke at ${Thread.currentThread().name}")
        // GlobalScope.launch {

        // val job = GlobalScope.launch {

        // GlobalScope.launch(Dispatchers.Default) {

        // GlobalScope.launch() {

        // GlobalScope.launch(Dispatchers.Main) {

        val job = GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "coroutine scope run 1 at ${Thread.currentThread().name}")
            // delay(3000L)
            // suspendRun()
            var input = 1

            input = suspendRunAsyncBefore(input)
            Log.d(TAG, "coroutine scope run 2 at ${Thread.currentThread().name} input:$input")

            input = suspendRunAsyncMiddle(input)
            Log.d(TAG, "coroutine scope run 3 at ${Thread.currentThread().name} input:$input")

            input = suspendRunAsyncAfter(input)
            Log.d(TAG, "coroutine scope ran at ${Thread.currentThread().name} input:$input")
        }
        Log.d(TAG, "launchDataAsyncSerial() invoked at ${Thread.currentThread().name}")
    }

    /* - - > middle ---\
     * - - > before ----> end
     * - - > after ----/
     */
    private fun launchDataAsyncBlock() {
        Log.d(TAG, "launchDataAsyncBlock() invoke at ${Thread.currentThread().name}")
        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "coroutine scope start run at ${Thread.currentThread().name}")

            val valueBefore = async { suspendRunAsyncBefore(1) }

            val valueMiddle = async { suspendRunAsyncMiddle(1) }

            val valueAfter = async { suspendRunAsyncAfter(1) }

            Log.d(TAG, "coroutine scope ran at ${Thread.currentThread().name}"
                    + " valueBefore:${valueBefore.await()}"
                    + " valueMiddle:${valueMiddle.await()}"
                    + " valueAfter:${valueAfter.await()}")
        }
        Log.d(TAG, "launchDataAsyncBlock() invoked at ${Thread.currentThread().name}")
    }

    /*          - - - > middle ---\
     *        /                    \
     * before                       ------> end
     *        \                    /
     *          - - - > after ---/
     */
    private fun launchDataAsyncSerialBlock() {
        Log.d(TAG, "launchDataAsyncSerialBlock() invoke at ${Thread.currentThread().name}")
        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "coroutine scope start run at ${Thread.currentThread().name}")
            val valueBefore = suspendRunAsyncBefore(1)

            val valueMiddle = async { suspendRunAsyncMiddle(valueBefore) }
            val valueAfter = async { suspendRunAsyncAfter(valueBefore) }

            Log.d(TAG, "coroutine scope ran at ${Thread.currentThread().name}"
                    + " valueMiddle:${valueMiddle.await()}"
                    + " valueAfter:${valueAfter.await()}")
        }
        Log.d(TAG, "launchDataAsyncSerialBlock() invoked at ${Thread.currentThread().name}")
    }

    private suspend fun suspendRun() {
        Log.d(TAG, "suspendRun() run at ${Thread.currentThread().name}")
        delay(3000L)
        Log.d(TAG, "suspendRun() ran at ${Thread.currentThread().name}")
    }

    private suspend fun suspendRunAsync() {
        withContext(Dispatchers.Default) {
            Log.d(TAG, "suspendRunAsync() run at ${Thread.currentThread().name}")
            delay(3000L)
            Log.d(TAG, "suspendRunAsync() ran at ${Thread.currentThread().name}")
        }
    }

    private suspend fun suspendRunAsyncBefore(input: Int): Int {
        withContext(Dispatchers.Default) {
            Log.d(TAG, "suspendRunAsyncBefore() run at ${Thread.currentThread().name}")
            delay(3000L)
            Log.d(TAG, "suspendRunAsyncBefore() ran at ${Thread.currentThread().name}")
        }
        return input + 1
    }

    private suspend fun suspendRunAsyncMiddle(input: Int): Int {
        withContext(Dispatchers.Default) {
            Log.d(TAG, "suspendRunAsyncMiddle() run at ${Thread.currentThread().name}")
            delay(3000L)
            Log.d(TAG, "suspendRunAsyncMiddle() ran at ${Thread.currentThread().name}")
        }
        return input + 2
    }

    private suspend fun suspendRunAsyncAfter(input: Int): Int {
        withContext(Dispatchers.Default) {
            Log.d(TAG, "suspendRunAsyncAfter() run at ${Thread.currentThread().name}")
            delay(3000L)
            Log.d(TAG, "suspendRunAsyncAfter() ran at ${Thread.currentThread().name}")
        }
        return input + 3
    }
}