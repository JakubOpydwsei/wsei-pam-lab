package pl.wsei.pam

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.Lab01Activity
import pl.wsei.pam.R
import pl.wsei.pam.lab02.Lab02Activity
import pl.wsei.pam.lab06.Lab06

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun onClickMainBtnRunLab01(v: View){
        Toast.makeText(this, "Opening Lab 01", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Lab01Activity::class.java)
        startActivity(intent)
    }
    fun onClickMainBtnRunLab02(v: View){
        Toast.makeText(this, "Opening Lab 02", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Lab02Activity::class.java)
        startActivity(intent)
    }
    fun onClickMainBtnRunLab06(v: View) {
        Toast.makeText(this, "Opening Lab 6", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, Lab06::class.java)
        startActivity(intent)
    }

}
