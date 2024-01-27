package com.example.retrofit

import android.net.http.HttpException
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit.databinding.ActivityMainBinding
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var todoAdapter: TodoAdapter

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRecyclerView()

        lifecycleScope.launchWhenCreated {
            binding.progresBar.isVisible = true
            val response = try{
                RetrofitInstance.api.getTodos()
            }catch (e:IOException){
                Log.e(TAG,"IOException, you might not have internet connection")
                binding.progresBar.isVisible=false
                return@launchWhenCreated
            }catch (e:HttpException){
                Log.e(TAG,"HttpException, unexpected response")
                binding.progresBar.isVisible=false
                return@launchWhenCreated
            }

            if (response.isSuccessful && response.body() != null){
                todoAdapter.todos= response.body()!!
            }else{
                Log.e(TAG,"Response not successful")
            }
            binding.progresBar.isVisible=false
        }
    }

    private fun setupRecyclerView()= binding.rvTodos.apply {
        todoAdapter = TodoAdapter()
        adapter = todoAdapter
        layoutManager = LinearLayoutManager(this@MainActivity)
    }
}