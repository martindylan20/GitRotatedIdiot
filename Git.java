import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Git {
    public static void main(String[] args) {
        indexFile("./Git.java");
    }

    public static void init() {
        File git = new File("git/");
        File obj = new File("git/objects/");
        File index = new File("git/index");
        File head = new File("git/HEAD");
        if (git.exists() && obj.exists() && index.exists() && head.exists()) {
            System.out.println("Git Repository Already Exists");
        } else {
            System.out.println("Git Repository Created");
            git.mkdir();
            obj.mkdir();
            try {
                index.createNewFile();
                head.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static String hashString(String input) {
        byte[] input_bytes = input.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(input_bytes);
            byte[] hashed_bytes = md.digest();
            return HexFormat.of().formatHex(hashed_bytes);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static void blob(String filePath) {
        try {
            String fileContents = getTextOfFile(filePath);
            String hashedFile = hashString(fileContents);
            FileWriter blobWriter = new FileWriter("git/objects/" + hashedFile);
            blobWriter.write(fileContents);
            indexFile(filePath);
            blobWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getTextOfFile(String filePath) {
        FileReader blobReader;
        try {
            blobReader = new FileReader(filePath);
            String fileContents = "";
            int c;
            while ((c = blobReader.read()) != -1) {
                fileContents = fileContents + (char) c;
            }
            blobReader.close();
            return fileContents;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }
    }

    public static void indexFile(String filePath) {
        try {
            BufferedReader indexReader = new BufferedReader(new FileReader("git/index"));
            String line;
            boolean hasAdded = false;
            File parent = new File("..");
            String parentName = parent.getAbsoluteFile().getParentFile().getName();
            String printName = parentName + filePath.substring(1);
            List<String> indexLines = new ArrayList<>();
            while ((line = indexReader.readLine()) != null) {
                if (line.contains(" " + printName)) {
                    String hashedFile = hashString(getTextOfFile(filePath));
                    indexLines.add(hashedFile + " " + printName);
                    hasAdded = true;
                } else {
                    indexLines.add(line);
                }
            }
            if (!hasAdded) {
                String hashedFile = hashString(getTextOfFile(filePath));
                indexLines.add(hashedFile + " " + printName);
            }
            FileWriter indexWriter = new FileWriter("git/index");
            for (String i : indexLines) {
                indexWriter.write(i + "\n");
            }
            indexWriter.close();
            indexReader.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}