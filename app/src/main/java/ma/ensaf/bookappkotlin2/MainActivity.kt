package ma.ensaf.bookappkotlin2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ma.ensaf.bookappkotlin2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gestion du clic sur le bouton de connexion
        binding.loginBtn.setOnClickListener {
            // À implémenter plus tard
            startActivity(Intent(this,LoginActivity::class.java))
        }

        // Gestion du clic sur le bouton de sauter et continuer vers l'écran principal
        binding.skipBtn.setOnClickListener {
            // À implémenter plus tard
            startActivity(Intent(this,DashboardUserActivity::class.java))
        }
    }
}