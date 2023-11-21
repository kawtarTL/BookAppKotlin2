package ma.ensaf.bookappkotlin2
import android.widget.Filter

class FilterCategory : Filter {

    // arraylist in which we want to search
    private var filterList: ArrayList<ModelCategory>

    // adapter in which filter need to be implemented
    private var adapterCategory: AdapterCategory

    constructor(filterList: ArrayList<ModelCategory>, adapterCategory: AdapterCategory) : super() {
        this.filterList = filterList
        this.adapterCategory = adapterCategory
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        val results = FilterResults()

        // value should not be null and not empty
        if (constraint != null && constraint.isNotEmpty()) {
            // searched value is not null and not empty

            // change to upper case, or to avoid case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredModels: ArrayList<ModelCategory> = ArrayList()
            for (i in 0 until filterList.size) {
                // validate
                // if(filterList[i].category.uppercase().contains(constraint)){
                if (filterList[i].category.uppercase().contains(constraint.toString())) {
                    // add to filtered list
                    filteredModels.add(filterList[i])
                }
            }

            results.count = filteredModels.size
            results.values = filteredModels
        } else {
            // search value is either null or empty
            results.count = filterList.size
            results.values = filterList
        }

        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        // Update the categoryArrayList in the adapter with the filtered results
        adapterCategory.categoryArrayList.clear()
        adapterCategory.categoryArrayList.addAll(results?.values as ArrayList<ModelCategory>)
        adapterCategory.notifyDataSetChanged()
    }

}
