package ma.ensaf.bookappkotlin2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import ma.ensaf.bookappkotlin2.databinding.ActivityDashboardAdminBinding
import ma.ensaf.bookappkotlin2.databinding.ActivityDashboardUserBinding
import ma.ensaf.bookappkotlin2.databinding.ActivityMainBinding

class DashboardUserActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityDashboardUserBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle  click  logout
        binding.logoutBtn.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

    }

    private fun checkUser(){

        val firebaseUser = firebaseAuth.currentUser!!
        if (firebaseUser==null) {
            //not logged in , user can stay in user dasgboard without login too
            binding.subTitleTv.text="Not Logged In"
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