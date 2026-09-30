public class Verify {
    public static void main(String[] args) {
        Git.blob("./Git.java");
        Git.blob("./Git.java");
        Git.indexFile("./Git.java");
        Git.indexFile("./Git.java");

        Git.blob("./Verify.java");
        Git.indexFile("./Verify.java");

    }
    
}