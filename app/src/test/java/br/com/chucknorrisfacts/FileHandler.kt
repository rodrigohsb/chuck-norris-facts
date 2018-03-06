package br.com.chucknorrisfacts

import java.io.File
import java.util.*

/**
 * @rodrigohsb
 */
class FileHandler {

  fun readResource(fileName: String): String {
        val classLoader = javaClass.classLoader
        val resource = classLoader.getResource(fileName)
        val file = File(resource.path)

        val result = StringBuilder()

        try {
            val scanner = Scanner(file)
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                result.append(line).append("\n")
            }

            scanner.close()
            return result.toString()

        } catch (e: Throwable) {
            e.printStackTrace()
            throw RuntimeException()
        }
    }
}