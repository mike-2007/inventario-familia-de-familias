/* Autor: Miguel Mosquera. Fecha: 19/10/2026.
 * Taller 1, parte 1: inventario de la Fundación Familia de Familias, Quito. */
package ec.edu.indoamerica.inventario

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    // Inicia la pantalla declarativa de captura de bienes.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TemaInventario { PantallaRegistro() } }
    }
}

// Define colores verdes y fondo claro para el formulario.
@Composable
fun TemaInventario(contenido: @Composable () -> Unit) {
    MaterialTheme(colorScheme = lightColorScheme(
        primary = Color(0xFF155E53), background = Color(0xFFF6F8F5),
        surface = Color.White, onSurface = Color(0xFF20352F),
        primaryContainer = Color(0xFFDDEFE7), onPrimaryContainer = Color(0xFF123E34)
    ), content = contenido)
}

// Conserva las entradas al recrear la pantalla; esta parte no utiliza una base de datos.
@Composable
fun PantallaRegistro() {
    var nombre: String by rememberSaveable { mutableStateOf("") }
    var categoria: String by rememberSaveable { mutableStateOf("") }
    var cantidadTexto: String by rememberSaveable { mutableStateOf("") }
    var donante: String by rememberSaveable { mutableStateOf("") }
    var resumen: String by rememberSaveable { mutableStateOf("") }
    var error: String by rememberSaveable { mutableStateOf("") }
    val foco = LocalFocusManager.current

    Column(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
        .safeDrawingPadding().imePadding().verticalScroll(rememberScrollState())
        .padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("FAMILIA DE FAMILIAS", color = MaterialTheme.colorScheme.primary,
            fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Text("Registro de bienes", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text("Fundación Familia de Familias · Quito", fontSize = 14.sp)

        // Solicita los cuatro campos; cada edición descarta el resumen anterior.
        CampoRegistro("Nombre del bien", nombre, { nombre = it; error = ""; resumen = "" })
        CampoRegistro("Categoría", categoria, { categoria = it; error = ""; resumen = "" })
        CampoRegistro("Cantidad", cantidadTexto, { cantidadTexto = it; error = ""; resumen = "" }, true)
        CampoRegistro("Donante", donante, { donante = it; error = ""; resumen = "" })

        // Valida antes de convertir la cantidad a Int y generar el resumen.
        Button(onClick = {
            foco.clearFocus()
            val mensaje: String? = RegistroBien.validar(nombre, categoria, cantidadTexto, donante)
            if (mensaje != null) {
                error = mensaje
                resumen = ""
            } else {
                val cantidad: Int = cantidadTexto.trim().toInt()
                val bien = Bien(nombre.trim(), categoria.trim(), cantidad, donante.trim())
                resumen = RegistroBien.construirResumen(bien)
                error = ""
            }
        }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp)) {
            Text("Registrar", fontSize = 17.sp)
        }

        // Muestra un mensaje de validación o el resumen del bien registrado.
        if (error.isNotEmpty()) Text(error, color = MaterialTheme.colorScheme.error)
        if (resumen.isNotEmpty()) {
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Resumen del bien registrado", fontWeight = FontWeight.Bold)
                    Text(resumen, lineHeight = 23.sp)
                }
            }
        }
        Text("Stock: bajo 0–9 · medio 10–49 · alto desde 50", fontSize = 12.sp)
        Text("Práctica de captura de datos. El resumen se conserva al girar la pantalla; " +
            "esta primera parte no guarda un inventario permanente.", fontSize = 12.sp)
        Text("Autor: Miguel Mosquera", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
    }
}

// Reutiliza el estilo y ofrece teclado numérico para la cantidad.
@Composable
fun CampoRegistro(etiqueta: String, valor: String, alCambiar: (String) -> Unit, numerico: Boolean = false) {
    OutlinedTextField(value = valor, onValueChange = alCambiar, label = { Text(etiqueta) },
        singleLine = true, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = if (numerico) KeyboardType.Number else KeyboardType.Text,
            imeAction = ImeAction.Next))
}

// Permite revisar el formulario con la vista previa de Compose en Android Studio.
@Preview(showBackground = true, widthDp = 390, heightDp = 850)
@Composable
fun VistaPreviaRegistro() { TemaInventario { PantallaRegistro() } }
