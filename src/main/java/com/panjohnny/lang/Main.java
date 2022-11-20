package com.panjohnny.lang;

import com.panjohnny.lang.compiled.Compiler;
import com.panjohnny.lang.interpreted.Interpreter;
import com.panjohnny.lang.interpreted.Operations;
import com.panjohnny.lang.interpreted.StackProvider;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static boolean debug = false;

    public static void main(String[] args) throws IOException {
        System.out.println("[PANJOHNNY] Welcome to this utility to do stuff with my eso cat language! Enjoy!");
        System.out.println();

        if (args.length < 1) {
            System.err.println("[(flags)] <file>");
            System.err.println("For flags run with -h (flags are not mandatory)");
            return;
        }

        List<String> flags = Arrays.stream(args).filter(s -> s.startsWith("-")).toList();

        if (flags.contains("-d") || flags.contains("--debug")) {
            debug = true;
            System.out.println("Ran in debug mode");
            System.out.println("  With " + flags);
        }

        if (flags.contains("-h") || flags.contains("--help")) {
            System.out.println("Usage: [(flags)] <file>");
            System.out.println("Flags are not mandatory and mode is deducted from file extension.");
            System.out.println("  *.cats - compile");
            System.out.println("  *.meow - run");
            System.out.println("Flags:");
            System.out.println("  -h, --help - prints out this help menu");
            System.out.println("  -v, --version - prints out the version");
            System.out.println("  -d, --debug - debugs stuff");
            System.out.println("  Mode flags: - this set of flags changes mode and turns of auto file type detection");
            System.out.println("    -c, --compile - compiles the file as if it was script");
            System.out.println("    -e, --eval - evaluates the file as if it was compiled");
            System.out.println("    -i, --interactive - starts to listen for code that you write and evaluates it on spot");
            System.out.println("    -dc, --decompile - decompiles file");
        } else if (flags.contains("-v") || flags.contains("--version")) {
            System.out.println("Version: " + Main.class.getPackage().getImplementationVersion());
        } else if (flags.contains("-c") || flags.contains("--compile")) {
            compile(args);
        } else if (flags.contains("-e") || flags.contains("--eval")) {
            run(args);
        } else if (flags.contains("-dc") || flags.contains("--decompile")) {
            decompile(args);
        } else if (flags.contains("-i") || flags.contains("--interactive")) {
            Operations.register();
            System.out.println("Ran in interactive mode");

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter code or EXIT:");
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                if (s.equals("EXIT"))
                    System.exit(0);
                System.out.println(Interpreter.eval(sc.nextLine(), null));
            }
        } else {
            // AUTO determine
            String file = args[args.length - 1];

            if (file.endsWith(".meow"))
                run(args);
            else if (file.endsWith(".cats"))
                compile(args);
            else {
                System.err.println("Unknown file type, use -h to see modes");
                System.exit(1);
            }
        }
    }

    private static void compile(String[] args) throws IOException {
        File file = getFile(args[args.length - 1]);
        if (debug)
            System.out.println("Compiling " + file.getAbsolutePath());
        String s = readFile(file);

        String compiled = Compiler.compile(s);
        File f = new File("./" + file.getName() + ".meow");
        writeFile(f, compiled);
        if (debug)
            System.out.println("Compilation finished and saved to: " + f.getAbsolutePath());
    }

    private static void run(String[] args) throws FileNotFoundException {
        Operations.register();

        File file = getFile(args[args.length - 1]);
        if (debug)
            System.out.println("Executing " + file.getAbsolutePath());
        String s = readFile(file);

        StackProvider sp = Interpreter.run(s, null);
        end(sp);
    }

    private static void decompile(String[] args) throws IOException {
        File file = getFile(args[args.length - 1]);
        if (debug)
            System.out.println("Executing " + file.getAbsolutePath());
        String s = readFile(file);

        String decompiled = Compiler.decompile(s);
        File f = new File("./" + file.getName() + ".cats");
        writeFile(f, decompiled);
        if (debug)
            System.out.println("Decompiled and saved file to: " + f.getAbsolutePath());

    }

    private static String readFile(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNext()) {
            sb.append(scanner.next());
        }

        return sb.toString();
    }

    private static File getFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file = new File("./" + path);
            if (!file.exists()) {
                file = new File(".", path);
                if (!file.exists()) {
                    System.err.println("File not found: " + path);
                    System.exit(1);
                }
            }
        }

        if (file.isDirectory()) {
            System.err.println("File is a directory: " + file.getAbsolutePath());
            System.exit(1);
        }

        if (!file.canRead()) {
            System.err.println("Cannot read file: " + file.getAbsolutePath());
            System.exit(1);

        }

        return file;
    }

    private static void writeFile(File file, String s) throws IOException {
        if (!file.exists())
            if (!file.createNewFile()) {
                System.err.println("Unable to create new file to write to!");
                System.exit(1);
            }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(s);
        writer.flush();
        writer.close();
    }

    public static void end(StackProvider provider) {
        System.out.println("Program exited with " + provider);
        System.out.println("As string: " + provider.asString());

        if (debug) {
            System.out.println("Index: " + provider.index);
            System.out.println("Skip hint: " + provider.skipHint);
        }
        System.exit(0);
    }
}