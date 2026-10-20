/* Autor: Miguel Mosquera. Fecha: 19/10/2026. Pruebas de captura y clasificación. */
package ec.edu.indoamerica.inventario

import org.junit.Assert.*
import org.junit.Test

class RegistroBienTest {
    // Caso normal: valida y construye el resumen de 25 unidades.
    @Test fun registroNormal() {
        assertNull(RegistroBien.validar("Arroz", "Alimentos", "25", "Maria Perez"))
        assertEquals("Bien: Arroz\nCategoría: Alimentos\nCantidad: 25 unidades\nDonante: Maria Perez\nClasificación: Stock medio",
            RegistroBien.construirResumen(Bien("Arroz", "Alimentos", 25, "Maria Perez")))
    }
    // Caso límite: cero unidades representa stock bajo.
    @Test fun stockCero() {
        assertNull(RegistroBien.validar("Arroz", "Alimentos", "0", "Maria Perez"))
        assertEquals("Stock bajo", RegistroBien.clasificarStock(0))
    }
    // Verifica los puntos exactos donde cambia el nivel de stock.
    @Test fun fronterasDeStock() {
        for ((cantidad, esperado) in listOf(9 to "Stock bajo", 10 to "Stock medio", 49 to "Stock medio", 50 to "Stock alto", Int.MAX_VALUE to "Stock alto")) {
            assertEquals(esperado, RegistroBien.clasificarStock(cantidad))
        }
    }
    // Caso erróneo: rechaza letras, decimales, vacío y desbordamiento.
    @Test fun cantidadesNoEnteras() {
        for (texto in listOf("abc", "2.5", "", "2147483648")) {
            assertEquals("Ingresa una cantidad entera entre 0 y 2147483647.", RegistroBien.validar("Arroz", "Alimentos", texto, "Maria Perez"))
        }
    }
    // Impide registrar una cantidad negativa.
    @Test fun cantidadNegativa() {
        assertEquals("La cantidad no puede ser negativa.", RegistroBien.validar("Arroz", "Alimentos", "-1", "Maria Perez"))
    }
    // Rechaza campos obligatorios vacíos o con espacios.
    @Test fun camposObligatorios() {
        assertNotNull(RegistroBien.validar(" ", "Alimentos", "1", "Maria Perez"))
        assertNotNull(RegistroBien.validar("Arroz", "", "1", "Maria Perez"))
        assertNotNull(RegistroBien.validar("Arroz", "Alimentos", "1", ""))
    }
    // Acepta los límites de texto y rechaza un carácter adicional.
    @Test fun longitudDeTextos() {
        assertNull(RegistroBien.validar("a".repeat(100), "b".repeat(60), "1", "c".repeat(120)))
        assertNotNull(RegistroBien.validar("a".repeat(101), "Alimentos", "1", "Maria Perez"))
    }
    // Protege también el uso directo de la función de clasificación.
    @Test(expected = IllegalArgumentException::class) fun clasificacionNegativa() {
        RegistroBien.clasificarStock(-1)
    }
}
