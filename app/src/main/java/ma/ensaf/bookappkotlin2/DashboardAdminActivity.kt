package ma.ensaf.bookappkotlin2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ma.ensaf.bookappkotlin2.databinding.ActivityDashboardAdminBinding

class DashboardAdminActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityDashboardAdminBinding

    // Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    // ArrayList to hold categories
    private lateinit var categoryArrayList: ArrayList<ModelCategory>

    // Adapter
    private lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadCategories()

        // Search
        binding.searchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Called as and when user types anything
                try {
                    adapterCategory.filter.filter(s)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Handle click logout
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        // Handle click, start add category page
        binding.addCategoryBtn.setOnClickListener {
            startActivity(Intent(this, CategoryAddActivity::class.java))
        }

        // handle click start add pdf page

        binding.addPdfFab.setOnClickListener{

            startActivity(Intent(this,PdfAddActivity::class.java))
        }


    }

    private fun loadCategories() {
        // Initialize ArrayList
        categoryArrayList = ArrayList()

        // Get all categories from Firebase database
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear list before adding data into it
                categoryArrayList.clear()
                for (ds in snapshot.children) {
                    // Get data as model
                    val model = ds.getValue(ModelCategory::class.java)
                    // Add to ArrayList
                    model?.let { categoryArrayList.add(it) }
                }

                // Setup adapter
                adapterCategory = AdapterCategory(this@DashboardAdminActivity, categoryArrayList)

                // Set adapter to RecyclerView
                binding.categoriesRv.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun checkUser() {
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            // Not logged in, go to main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            // Logged in, get and show user info
            val email = firebaseUser.email
            // Set to TextView of toolbar
            binding.subTitleTv.text = email
        }
    }
}