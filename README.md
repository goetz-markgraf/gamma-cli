# Gamma CLI

A CLI (command line interface) for the `gamma` programming language. This project uses `gamma-library`.

This project is an example of how to integrate `gamma-library` in a project.[](https://)

# how to use gamma CLI

## invoke with a gamma file

You can call the CLI with a location of a `.gma`-File as a parameter. The file is then parsed, executed and the
result of the last expression printed out as the result of the script.

## Use the CLI as a REPL

If you start the CLI without any parameter, you will be greeted with a REPL. You can then
type gamma code and have it executed with the command `.` (a single dot on the input line).

Type `.help` to see all commands.

## use the CLI as an interpreter for a script in another file

If you have a file with valid _gamma_-code you can use the CLI to execute that. Simply supply the name of the file
as the first (and only) parameter.

The CLI will parse and execute the script. The result of the last expression together with the runtime in
milliseconds will be printed out.

# native compile the _gamma_ interpreter

You can install [GraalVM](https://www.graalvm.org/) together with its module `native-image` to produce
a machine native executable binary.

Once you have installed, just call `build.sh` and the current CLI – together with the latest build of
`gamma-library` – will be natively compiled. This executable can be moved to any position on the __classpath__
to run the REPL or the interpreter for gamma-script files natively.
