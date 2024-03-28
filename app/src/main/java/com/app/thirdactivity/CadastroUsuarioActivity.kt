import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.app.thirdactivity.R
import java.io.*

class CadastroUsuarioActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var tipoUsuarioSpinner: Spinner
    private lateinit var cadastrarButton: Button

    private val usersList = mutableListOf<Pair<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_usuario)

        usernameEditText = findViewById(R.id.editTextUsername)
        passwordEditText = findViewById(R.id.editTextPassword)
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPassword)
        tipoUsuarioSpinner = findViewById(R.id.spinnerTipoUsuario)
        cadastrarButton = findViewById(R.id.buttonCadastrar)

        cadastrarButton.isEnabled = false

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val username = usernameEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()
                val confirmPassword = confirmPasswordEditText.text.toString().trim()
                cadastrarButton.isEnabled = username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && password == confirmPassword
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
        confirmPasswordEditText.addTextChangedListener(textWatcher)

        cadastrarButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val tipoUsuario = tipoUsuarioSpinner.selectedItem.toString()

            if (isUsernameUnique(username)) {
                salvarCredenciais(username, password, tipoUsuario)
                mostrarMensagemSucesso()
                setResult(RESULT_OK)
                finish()
            } else {
                mostrarUsuarioExistenteDialog()
            }
        }
    }

    private fun isUsernameUnique(username: String): Boolean {
        return usersList.none { it.first == username }
    }

    private fun salvarCredenciais(username: String, password: String, tipoUsuario: String) {
        val credenciais = "$username:$password:$tipoUsuario\n"
        try {
            val fileOutputStream = openFileOutput("credenciais.txt", Context.MODE_APPEND)
            fileOutputStream.write(credenciais.toByteArray())
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun mostrarUsuarioExistenteDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Erro de Cadastro")
        builder.setMessage("Usuário já existe.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun mostrarMensagemSucesso() {
        Toast.makeText(this, "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
    }
}
