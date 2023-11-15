package ma.ensaf.bookappkotlin2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed(Runnable{
            checkUser()
        }, 1000)
    }

    private fun checkUser(){

        val firebaseUser = firebaseAuth.currentUser!!
        if (firebaseUser==null) {
            //user not logged gota ain screen
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        else{




            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {


                        //get user type e.g.user or admin
                        val userType = snapshot.child("userType").value
                        if (userType == "user") {

                            //its simple user dashboard
                            startActivity(Intent(this@SplashActivity, DashboardUserActivity::class.java))
                            finish()

                        } else if (userType == "admin") {

                            //its simple admin dashboard
                            startActivity(
                                Intent(
                                    this@SplashActivity,
                                    DashboardAdminActivity::class.java
                                )
                            )
                            finish()

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }


    }

}