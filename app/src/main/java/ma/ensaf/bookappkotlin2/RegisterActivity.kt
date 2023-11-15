package ma.ensaf.bookappkotlin2

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ma.ensaf.bookappkotlin2.databinding.ActivityMainBinding
import ma.ensaf.bookappkotlin2.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityRegisterBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress dialog , will show while creating account / Register  user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)


        //handle back button click
        binding.backBtn.setOnClickListener {
        onBackPressed()
        }

        //handle click, begin register
        binding.registerBtn.setOnClickListener {
            //steps
            //input data
            //validate data
            //create  account -firebase auth
            //save user info-firebase Realtime database
            validateData()

        }


    }

    private var name = ""
    private var email = ""
    private var password = ""

    private fun validateData() {
        // input data
        name = binding.nameEt.text.toString()
        email = binding.emailEt.text.toString()
        password = binding.passwordEt.text.toString()
        val cPassword = binding.cPasswordEt.text.toString()


        //2 Validate Data

        if (name.isEmpty()) {

            //empty nase...
            Toast.makeText(this, "Enter your name...", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //invalid emalt pattern

            Toast.makeText(this, "Invalid Email Pattern...", Toast.LENGTH_SHORT).show()

        }
        else if (password.isEmpty()) {
            //empty password
            Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show()

        }

        else if (cPassword.isEmpty()) {
            //empty password
            Toast.makeText(this, "Confirm password...", Toast.LENGTH_SHORT).show()
        }
        else if (cPassword != password) {

            Toast.makeText(this, "Password doesn't match...", Toast.LENGTH_SHORT).show()
        }
        else
        {
            createUserAccount()
        }
    }
    private fun createUserAccount() {
        // show progress
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        // create user in fire auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // account created
                Log.d("FirebaseDebug", "Compte utilisateur créé avec succès.")
                updateUserInfo()
            }
            .addOnFailureListener { e ->
                // failed creating account
                progressDialog.dismiss()
                Log.e("FirebaseDebug", "Échec de la création du compte en raison de ${e.message}")
                Toast.makeText(this, "Failed creating account due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateUserInfo(){

        //4 save user info - firebase realtime database

        progressDialog.setMessage("saving user info ...")

        //timestamp
        val timestamp=System.currentTimeMillis()

        //get current user uid , since user is registered so we can get it now
        val uid = firebaseAuth.uid

        //setup data to ad in db
        val hashMap:HashMap<String,Any?> = HashMap()
        hashMap["uid"]=uid
        hashMap["email"]=email
        hashMap["name"]=name
        hashMap["profileImage"] =""//add empty will do in profile edit
        hashMap["userType"]="user" //possible values are/admin, will change value  to admin manually
        hashMap["timestamp"]= timestamp

        //set date ta db
        val ref =FirebaseDatabase.getInstance().getReference("user")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                //user info saved , open  user dashboard

                progressDialog.dismiss()
                Toast.makeText(this,"Account created...",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity,DashboardUserActivity::class.java))
                finish()
            }
            .addOnFailureListener{e->
                //failed adding data  to db
                progressDialog.dismiss()
                Toast.makeText(this,"failed saving user info due to ${e.message}",Toast.LENGTH_SHORT).show()

            }




    }


}

private operator fun Unit.invoke(s: String) {

}