package ma.ensaf.bookappkotlin2

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import ma.ensaf.bookappkotlin2.databinding.ActivityCategoryAddBinding


@Suppress("DEPRECATION")
class CategoryAddActivity : AppCompatActivity() {

    //view binding
    private lateinit var binding : ActivityCategoryAddBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //init //firebase auth
        firebaseAuth = FirebaseAuth.getInstance()



        //configure progress dining
        progressDialog= ProgressDialog( this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle  click , go back
        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        // handle click, begin upload category
        binding.submitBtn.setOnClickListener{
            validateDate()
        }
    }
    private var  category=""
    private  fun validateDate() {
        //validate data
        //  get data
        category = binding.categoryEt.text.toString().trim()

        //validate data

        if (category.isEmpty()) {

            Toast.makeText(this, "Enter Category...", Toast.LENGTH_SHORT).show()
        }
        else {
            addCaterogyFirebase()

        }


    }
    private fun addCaterogyFirebase(){
        //show  progress
        progressDialog.show()
        // get timestamp
        val timestamp=System.currentTimeMillis()

        // setup data to add infire base db



        val hashMap= HashMap<String, Any>() //second param is Any; because the value could be of any type hashMap["id"] = "$timestamp" //put in string quotes because timestamp is in double, we need in string you hashMap["category"]= category
        hashMap["id"] = "$timestamp"
        hashMap["category"] = category
        hashMap["timestamp"] = timestamp
        hashMap["uid"] = "${firebaseAuth.uid}"
//add to firebase db: Gatabase Root Categories categoryId category info
        val ref= FirebaseDatabase.getInstance().getReference( "Categories")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener{
                progressDialog.dismiss()
                Toast.makeText(this,"Added successfully...", Toast.LENGTH_SHORT).show()
            }


            .addOnFailureListener {e->
                //failed to add
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to add due to $(e.message)", Toast.LENGTH_SHORT).show()
            }


    }

}