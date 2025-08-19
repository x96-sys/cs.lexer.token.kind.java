package org.x96.sys.foundation.cs;

import org.x96.sys.foundation.cs.lexer.token.BuildInfo;
import org.x96.sys.foundation.cs.lexer.token.Kind;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CLI {
    public static void main(String[] args) {
        if (args.length == 0) {
            processStdin();
            return;
        }

        String command = args[0];

        switch (command) {
            case "-v":
            case "--version":
                System.out.println(BuildInfo.VERSION);
                break;
            case "-b":
            case "--build-info":
                printBuildInfo();
                break;
            case "-h":
            case "--help":
                printUsage();
                break;
            case "--stdin":
                processStdin();
                break;
            default:
                for (String arg : args) {
                    processText(arg);
                }
                break;
        }
    }

    private static void printUsage() {
        System.out.println("Usage: make cli ARGS=\"<text>\"");
        System.out.println(
                "Usage: java -cp build/main:build/cli org.x96.sys.foundation.CLI <text>");
        System.out.println("java -jar org.x96.sys.foundation.tokenizer.kind.cli.jar \"<text>\"");
        System.out.println(
                "cat README.md | java -jar org.x96.sys.foundation.tokenizer.kind.cli.jar --stdin");
        System.out.println("Options:");
        System.out.println("  -v, --version     Show version");
        System.out.println("  -b, --build-info  Show build information");
        System.out.println("  -h, --help        Show this help");
        System.out.println("  --stdin           Read text from standard input");
        System.out.println("  text              Process text as tokens (default behavior)");
        System.out.println("  (no args)         Read text from standard input");
    }

    private static void processStdin() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processText(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from stdin: " + e.getMessage());
        }
    }

    private static void processText(String text) {
        for (byte b : text.getBytes()) {
            System.out.println(Kind.is(b));
        }
    }

    private static void printBuildInfo() {
        System.out.println("Build Information:");
        System.out.println("  Version: " + BuildInfo.VERSION);
        System.out.println("  Build Date: " + BuildInfo.BUILD_TIMESTAMP);
        System.out.println("  Build User: " + BuildInfo.BUILD_USER);
        System.out.println("  Major: " + BuildInfo.VERSION_MAJOR);
        System.out.println("  Minor: " + BuildInfo.VERSION_MINOR);
        System.out.println("  Patch: " + BuildInfo.VERSION_PATCH);
        System.out.println("  Full: " + BuildInfo.getFullVersion());
    }
}
