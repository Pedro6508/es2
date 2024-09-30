package ufc.comp.ed

import java.io.BufferedReader
import java.io.BufferedWriter
import java.nio.file.Files
import java.time.LocalDateTime
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter

object IO {
    operator fun invoke(
        inputFileName: String,
        outputDirectoryName: String,
        tag: String,
        block: (Command) -> String?,
    ) = exec(
        inputBufferedReader(inputFileName),
        outputBufferedWriter("$outputDirectoryName-$tag"),
    ) { lines -> lines.mapNotNull(block) }

    operator fun invoke(
        inputFileName: String,
        outputDirectoryName: String,
        tag: String,
        block: (Int, Command) -> String?,
    ) = exec(
        inputBufferedReader(inputFileName),
        outputBufferedWriter("$outputDirectoryName-$tag"),
    ) { lines -> lines.mapIndexedNotNull(block) }

    private fun exec(
        inputStream: BufferedReader,
        outputStream: BufferedWriter,
        block: (Sequence<Command>) -> Sequence<String>,
    ) = inputStream.useLines { lines ->
        lines.map(Command.Companion::fromLine)
            .let(block)
            .forEach(outputStream::appendLine); outputStream.close()
    }

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
        }.plus("/$time").filterNot(":"::contains).plus(".txt").let(::Path)
        .let(Files::createFile).bufferedWriter()
}