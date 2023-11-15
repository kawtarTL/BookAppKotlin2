package ma.ensaf.bookappkotlin2

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ma.ensaf.bookappkotlin2.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    //viewbinding
    private lateinit var binding: ActivityLoginBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress  dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog , will show while creating account /Register user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click not hane account ,goto register screen
        binding.noAccountTv.setOnClickListener() {
            startActivity(Intent(this, RegisterActivity::class.java))

        }
        //handle click,begin login
        binding.loginBtn.setOnClickListener {
            /* steps
            input data
            validate data
            login- firebase auth
            check  usr type - firebase auth
              - if user-move to user dashboard
              -if admin - move to admin dashboard
             */
            validatedata()


        }
    }

    private var email = ""
    private var password = ""

    private fun validatedata() {
        //1- input data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()


        //2validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            Toast.makeText(this, "Invalid email form ...", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show()
        } else {
            loginUser()
        }
    }

    private fun loginUser() {
        //3 login - firebase auth

        // show progress
        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // login success
                Log.d("FirebaseDebug", "Connexion réussie.")
                checkUser()

            }
            .addOnFailureListener { e ->
                // failed login
                progressDialog.dismiss()
                Log.e("FirebaseDebug", "Échec de la connexion en raison de ${e.message}")
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()

            }


    }

    private fun checkUser() {
        progressDialog.setMessage("Checking User ...")

        val firebaseUser = firebaseAuth.currentUser!!

        Log.d("FirebaseDebug", "UID used for query: ${firebaseUser.uid}")

        val ref = FirebaseDatabase.getInstance().getReference("user")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()

                    // Log the entire snapshot for debugging
                    Log.d("FirebaseDebug", "Snapshot: $snapshot")

                    if (snapshot.exists()) {
                        // Continue with the rest of the code
                        val userType = snapshot.child("userType").value
                        Log.d("FirebaseDebug", "UserType from snapshot: $userType")

                        if (userType == "user") {
                            // User dashboard
                            startActivity(Intent(this@LoginActivity, DashboardUserActivity::class.java))
                            finish()
                        } else if (userType == "admin") {
                            // Admin dashboard
                            startActivity(Intent(this@LoginActivity, DashboardAdminActivity::class.java))
                            finish()
                        }
                    } else {
                        Log.e("FirebaseDebug", "User does not exist in the database.")
                        // Handle the case where the user does not exist
                        Toast.makeText(
                            this@LoginActivity,
                            "User does not exist in the database.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FirebaseDebug", "Database error: ${error.message}")
                }
            })
    }



}

