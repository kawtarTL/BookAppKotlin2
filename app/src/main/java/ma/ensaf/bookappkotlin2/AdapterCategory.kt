package ma.ensaf.bookappkotlin2

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import ma.ensaf.bookappkotlin2.databinding.CategoryRowBinding

class AdapterCategory : RecyclerView.Adapter<AdapterCategory.HolderCategory> , Filterable{
    private val context: Context
    public var categoryArrayList: ArrayList<ModelCategory>
    private var filterList : ArrayList<ModelCategory>

    private var filter : FilterCategory? = null

    private lateinit var binding: CategoryRowBinding

    //constructor
    constructor(context: Context, categoryArrayList: ArrayList<ModelCategory>) {
        this.context = context
        this.categoryArrayList = categoryArrayList
        this.filterList = categoryArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderCategory {
        //inflate/bind row_category.xml
        binding = CategoryRowBinding.inflate(LayoutInflater.from(context), parent,false)
        return HolderCategory (binding.root)
    }

    override fun onBindViewHolder(holder: HolderCategory, position: Int) {

        //get data
        val model = categoryArrayList [position]
        val id = model.id
        val category = model.category
        val uid = model.uid
        val timestamp = model.timestamp

        //set data
        holder.categoryTv.text = category

        //handle click, delete category
        holder.deleteBtn.setOnClickListener{
            //confirm before delete
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete")
                .setMessage("Are sure you want to delete this category?")
                .setPositiveButton("confirm"){a, d->
                    Toast.makeText(context,"Deleting...", Toast.LENGTH_SHORT).show()
                    deleteCategory(model, holder)
                }
                .setNegativeButton("Cancel"){a, d->
                    a.dismiss()
                }
                .show()

        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, PdfListAdminActivity::class.java)
            intent.putExtra("categoryId", id)
            intent.putExtra("category", category)
            context.startActivity(intent)
        }
    }

    private fun deleteCategory(model: ModelCategory, holder: HolderCategory) {
        //get id of category to delete
        val id = model.id
        //Firebase DB > Categories > categoryId
        val ref= FirebaseDatabase.getInstance().getReference ("Categories")
        ref.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Deleted...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                Toast.makeText(context,"Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return categoryArrayList.size // number of items in the list
    }





    //ViewHolder class to hold/init UI views for row_category.xml
    inner class HolderCategory (itemView: View): RecyclerView.ViewHolder(itemView) {
        //init ui views
        var categoryTv: TextView = binding.categoryTv
        var deleteBtn: ImageButton = binding.deleteBtn
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = FilterCategory(filterList,this)
        }
        return filter as FilterCategory
    }


}