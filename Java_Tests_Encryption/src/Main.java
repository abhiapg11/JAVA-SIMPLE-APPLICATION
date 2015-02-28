
public class Main {

    public static void main(String[] args) {
        System.out.println("START PROGRAM");

        String encrypterFileName = "picture.encr.JPG";
        String decrypterFileName = "picture.decr.JPG";
        String originalFileName  = "picture.JPG";

        Encrypter.encrypt(originalFileName, encrypterFileName);

        Encrypter.decrypt(encrypterFileName, decrypterFileName);

        System.out.println("END PROGRAM");
    }

}
