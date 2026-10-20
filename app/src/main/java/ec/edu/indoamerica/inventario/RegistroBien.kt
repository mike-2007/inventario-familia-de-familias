/* Autor: Miguel Mosquera. Fecha: 19/10/2026.
 * Taller 1, parte 1: captura y clasificación de bienes donados. */
package ec.edu.indoamerica.inventario

// Usa String para los textos e Int para las unidades del bien validado.
data class Bien(val nombre: String, val categoria: String, val cantidad: Int, val donante: String)

object RegistroBien {
    // Comprueba campos obligatorios y evita cantidades negativas o fuera del tipo Int.
    fun validar(nombre: String, categoria: String, cantidadTexto: String, donante: String): String? {
        if (nombre.isBlank() || categoria.isBlank() || donante.isBlank()) {
            return "Completa el nombre del bien, la categoría y el donante."
        }
        if (nombre.trim().length > 100 || categoria.trim().length > 60 || donante.trim().length > 120) {
            return "Usa hasta 100 caracteres en el bien, 60 en categoría y 120 en donante."
        }
        val cantidad: Int = cantidadTexto.trim().toIntOrNull()
            ?: return "Ingresa una cantidad entera entre 0 y 2147483647."
        if (cantidad < 0) return "La cantidad no puede ser negativa."
        return null
    }

    // Rangos propuestos para el taller: se pueden ajustar a los procedimientos de la fundación.
    fun clasificarStock(cantidad: Int): String {
        require(cantidad >= 0) { "La cantidad no puede ser negativa." }
        return when (cantidad) {
            in 0..9 -> "Stock bajo"
            in 10..49 -> "Stock medio"
            else -> "Stock alto"
        }
    }

    // Construye el resumen que se muestra después de pulsar Registrar.
    fun construirResumen(bien: Bien): String {
        return "Bien: ${bien.nombre}\nCategoría: ${bien.categoria}\n" +
            "Cantidad: ${bien.cantidad} unidades\nDonante: ${bien.donante}\n" +
            "Clasificación: ${clasificarStock(bien.cantidad)}"
    }
}
