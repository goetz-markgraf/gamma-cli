package de.gma.gamma

import de.gma.gamma.datatypes.Value
import de.gma.gamma.datatypes.expressions.Expression
import de.gma.gamma.datatypes.functions.FunctionValue
import de.gma.gamma.datatypes.scope.ModuleScope
import de.gma.gamma.datatypes.scope.Scope
import de.gma.gamma.parser.EvaluationException
import de.gma.gamma.parser.Parser
import java.io.File
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size == 0) {
        interactive()
        exitProcess(0)
    }

    if (args.size == 1) {
        val filename = args[0]
        val file = File(filename)

        if (file.exists() && !file.isDirectory) {
            executeScript(file)
            exitProcess(0)
        }

        println("file $filename does not exist or is not a gamma file")
        exitProcess(-1)
    }

    println("gamma [filename]")
    exitProcess(-1)
}

private fun interactive() {
    var cmd: String

    val buffer = StringBuffer()
    var lastBuffer = ""

    var scope = ModuleScope()

    printHelp()

    while (true) {

        print("gamma> ")
        val inp = readLine() ?: break

        if (inp == "?" || inp == ".help" || inp == "help") {
            printHelp()
        } else if (inp == "." && buffer.isNotEmpty()) {
            lastBuffer = buffer.toString()
            execute(lastBuffer, scope)
            buffer.delete(0, buffer.length)
        } else if (inp.startsWith(".")) {
            cmd = inp.drop(1)
            when (cmd) {
                "exit", "quit", "q", "bye", "b" -> break

                "clear", "c" -> buffer.delete(0, buffer.length)
                "new" -> scope = ModuleScope()

                "load", "l" -> {
                    buffer.delete(0, buffer.length)
                    buffer.append(lastBuffer)
                    println(lastBuffer)
                }
            }
        } else {
            buffer.append(inp).append('\n')
        }

    }

    println("Bye")
}

private fun printHelp() {
    println("Gamma Interpreter REPL")
    println("Commands")
    println("   .      -> execute code and clear buffer")
    println("   .clear -> clear the code buffer")
    println("   .new   -> restart the REPL (delete all bindings)")
    println("   .load  -> load code buffer from last executed code")
    println("   .exit  -> exit the REPL")
    println("   .quit  -> exit the REPL")
    println("   .bye   -> exit the REPL")
    println("   .help  -> display this help")
}

private fun executeScript(file: File) {
    println("Executing: ${file.name}")
    val scope = ModuleScope()
    val code = file.readText()

    val start = System.currentTimeMillis()
    val result = execute(code, scope, false)
    val duration = System.currentTimeMillis() - start

    if (result != null)
        println("($duration msec)-> ${result.evaluate(scope).prettyPrint()}")
}


private fun execute(code: String, scope: Scope, shallPrint: Boolean = true): Value? {
    if (shallPrint)
        println(code)

    try {
        val parser = Parser(code)
        var expr = parser.nextExpression()
        var result: Value? = null
        while (expr != null) {
            result = expr.evaluate(scope)
            if (result !is Expression && result !is FunctionValue && shallPrint)
                println("-> ${result.prettyPrint()}")

            expr = parser.nextExpression()
        }

        if (shallPrint) println()
        return result

    } catch (l: EvaluationException) {
        println()
        println("**** Exception while evaluation code:\n${l::class.java} '${l.message}' in ${l.source}, line: ${l.line}, col: ${l.col}")
        l.printStackTrace()
    } catch (e: Exception) {
        println()
        println("**** Exception while evaluation code:\n${e::class.java} '${e.message}'")
    }

    return null
}
