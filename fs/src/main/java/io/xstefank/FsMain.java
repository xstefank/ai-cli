package io.xstefank;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;

import java.util.Scanner;

@QuarkusMain
public class FsMain implements QuarkusApplication {

    @Inject
    FsBot fsBot;

    @Override
    public int run(String... args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print(">> ");

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.printf("\n" + fsBot.chat(line) + "\n\n");
            System.out.print(">> ");
        }

        return 0;
    }
}
