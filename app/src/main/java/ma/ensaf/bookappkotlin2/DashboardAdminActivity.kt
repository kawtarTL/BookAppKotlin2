package ma.ensaf.bookappkotlin2

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ma.ensaf.bookappkotlin2.databinding.ActivityDashboardAdminBinding
import ma.ensaf.bookappkotlin2.databinding.ActivityLoginBinding
import ma.ensaf.bookappkotlin2.databinding.ActivityMainBinding

class DashboardAdminActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityDashboardAdminBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


    }

    @SuppressLint("SuspiciousIndentation")
    private fun checkUser(){

        val firebaseUser = firebaseAuth.currentUser!!
        if (firebaseUser==null) {
            //not logged in goto main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else
        {
            //logged in , get and show user info
            val email = firebaseUser.email
            //set to textview of toolbar
            binding.subTitleTv.text = email


        }
    }
}