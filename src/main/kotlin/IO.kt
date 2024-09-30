package ufc.comp.ed

import java.io.BufferedReader
import java.io.BufferedWriter
import java.nio.file.Files
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter

object IO {
    private val classLoader = javaClass.classLoader

    private const val OUTPUT_DIR = "output"
    private const val INPUT_DIR = "input"

    private fun inputBufferedReader(name: String) = "$INPUT_DIR/$name"
        .let(classLoader::getResourceAsStream)!!
        .bufferedReader()

    private fun outputBufferedWriter(
        name: String,
        time: LocalDateTime = LocalDateTime.now(),
    ) = "$OUTPUT_DIR/$name"
        .also { dirRawPath ->
            dirRawPath
                .let(::Path)
                .let(Files::createDirectories)
        }.plus("/$time").filterNot(":"::contains).let(::Path)
        .let(Files::createFile)
        .bufferedWriter()

    operator fun invoke(
        inputStream: BufferedReader,
        outputStream: BufferedWriter,
        block: (Sequence<Command>) -> Sequence<String>,
    ) = exec(inputStream, outputStream, block)

    operator fun invoke(
        inputFileName: String,
        outputFileName: String,
        block: (Sequence<Command>) -> Sequence<String>,
    ) = inputBufferedReader(inputFileName).use { inputBuffer ->
        outputBufferedWriter(outputFileName).use { outputBuffer ->
            exec(inputBuffer, outputBuffer, block)
        }
    }

    private fun exec(
        inputStream: BufferedReader,
        outputStream: BufferedWriter,
        block: (Sequence<Command>) -> Sequence<String>,
    ) = inputStream.useLines { lines ->
        lines.map(Command.Companion::fromLine)
            .let(block)
            .forEach(outputStream::appendLine)
    }
}