package com.jesu.datastore

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.jesu.datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var manager: UserManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        manager = UserManager(applicationContext)
        binding.btnSave.setOnClickListener {
            storeUser()
        }
        observeData()
    }

    private fun observeData() {
        lifecycleScope.launch {
            manager.userAgeFlow.collect { age ->
                if (age > 0)
                    binding.tvAge.text = getString(R.string.tv_age, age)
            }
        }
        lifecycleScope.launch {
            manager.userNameFlow.collect { name ->
                if (name.isNotEmpty())
                    binding.tvName.text = getString(R.string.tv_username, name)
            }
        }

        lifecycleScope.launch {
            manager.userNoFlow.collect { regno ->
                if (regno > 0L)
                    binding.tvUserno.text = getString(R.string.tv_regno, regno)

            }
        }

    }


    private fun storeUser() {

        val name = binding.eitUserName.text.toString()
        val age = binding.eitAge.text.toString()
        // for understanding purpose i predefined the reg no
        val regNo = binding.eitRegNo.text.toString()

        if (validateData(name, age, regNo)) {
            lifecycleScope.launch {
                manager.storeUserData(age.toInt(), name, regNo.toLong())
                Toast.makeText(applicationContext, "User saved", Toast.LENGTH_SHORT).show()
            }
            binding.eitAge.text?.clear()
            binding.eitUserName.text?.clear()
            binding.eitRegNo.text?.clear()
        } else
            Toast.makeText(applicationContext, "Fields should not be empty", Toast.LENGTH_SHORT)
                .show()


    }

    private fun validateData(name: String, age: String, regNo: String): Boolean {
        var valResult = true

        if (name.isEmpty()) {

            binding.tilUserName.isErrorEnabled = true
            binding.tilUserName.error = "Field is empty"
            valResult = false
        } else {
            binding.tilUserName.isErrorEnabled = false
            binding.tilUserName.error = null
        }
        if (age.isEmpty() || age.toInt() <= 0) {
            binding.tilAge.isErrorEnabled = true
            binding.tilAge.error = "Field is empty"
            valResult = false

        } else {
            binding.tilAge.isErrorEnabled = false
            binding.tilAge.error = null
        }
        if (regNo.isEmpty() || regNo.toLong() <= 0L) {
            binding.tilRegNo.isErrorEnabled = true
            binding.tilRegNo.error = "Field is empty"
            valResult = false

        } else {
            binding.tilRegNo.isErrorEnabled = false
            binding.tilRegNo.error = null
        }

        return valResult
    }


}