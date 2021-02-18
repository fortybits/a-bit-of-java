package edu.bit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class RemoveDuplicatesEmails {

    public static void main(String[] args) throws IOException {
        System.out.println("Preparing corrupt data in corruptEmailIds.txt");
        String inputFileName = prepareCorruptDataWithDuplicates("corruptEmailIds.txt");

        long startTime = System.currentTimeMillis();
        Set<String> distinctEmails = findDistinctEmails(inputFileName);
        System.out.println("Time taken in milliseconds:" + (System.currentTimeMillis() - startTime));

        String outputFile = writeCollectionOfEmailsToFile("distinctEmails.txt", distinctEmails);
        System.out.println("Your clean data is in " + outputFile);
    }

    private static Set<String> findDistinctEmails(String completeSystemFilePath) throws IOException {
        Path filePath = Paths.get(completeSystemFilePath);
        Set<String> resultingEmails = new LinkedHashSet<>();
        try (Stream<String> emailIds = Files.lines(filePath, StandardCharsets.UTF_8)) {
            emailIds.forEach(resultingEmails::add);
        }
        return resultingEmails;
    }

    private static String prepareCorruptDataWithDuplicates(String fileName) throws IOException {
        final String emailFormat = "dummy_email%d@example.com";
        Random random = new Random();
        List<String> actualEmails = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            String id = String.format(emailFormat, random.nextInt(50_000)); // 50% duplicates
            actualEmails.add(id);
        }
        return writeCollectionOfEmailsToFile(fileName, actualEmails);
    }

    private static String writeCollectionOfEmailsToFile(String fileName, Collection<String> emailIds) throws IOException {
        // create the file
        File myObj = new File(fileName);
        if (myObj.createNewFile()) {
            System.out.println("File created: " + myObj.getName());
        } else {
            System.out.println("File already exists.");
        }
        // convert everything into a string
        String finalText = String.join("\n", emailIds);

        // write to a file
        FileWriter writer = new FileWriter(fileName);
        writer.write(finalText);
        writer.close();
        return fileName;
    }

    // this method took 24s to process a 75GB file with a 10% clutter of duplicates and reduce it to 67GB output
    public void deDuplicateEfficiently() throws IOException {
        final String inputFileName = "uIdHash.txt";
        final String outputFileName = "distinctUIdHash.txt";
        File outputFile = new File(outputFileName);
        boolean newFile = outputFile.createNewFile();
        System.out.printf("%s new file created: %b\n", outputFileName, newFile);

        long startTime = System.currentTimeMillis();

        Set<String> distinctEmails = new HashSet<>();
        try (FileInputStream inputStream = new FileInputStream(inputFileName);
             Scanner sc = new Scanner(inputStream, StandardCharsets.UTF_8.name());
             FileWriter writer = new FileWriter(outputFileName)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (distinctEmails.add(line)) {
                    writer.write(line);
                    writer.append("\n");
                }
            }
        }
        System.out.println("Time taken to process the data in milliseconds:" + (System.currentTimeMillis() - startTime));
    }

}