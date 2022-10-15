package org.mattpayne.pgppainless1;

import org.pgpainless.sop.SOPImpl;
import org.bouncycastle.util.io.Streams;
import org.springframework.util.FileSystemUtils;
import sop.ByteArrayAndResult;
import sop.DecryptionResult;

import java.io.*;

public class DecryptMain {
    public static void main(String args[]) {
        try {
            new DecryptMain().run();
            System.out.println("Normal Termination.");
        } catch (Exception bland) {
            bland.printStackTrace();
        }
    }

    private void run() throws IOException {
        SOPImpl sop = new SOPImpl();
        byte[] bobKey = readKey("bob.key");
        String encryptedString = readWholeTextFile("encrypted.asc");
        byte[] encrypted = encryptedString.getBytes();

        ByteArrayAndResult<DecryptionResult> bytesAndResult = sop.decrypt()
                .withKey(bobKey)
                // .verifyWithCert(aliceCert)
                .ciphertext(encrypted)
                .toByteArrayAndResult();
        ByteArrayOutputStream decrypted = new ByteArrayOutputStream();
        Streams.pipeAll(bytesAndResult.getInputStream(), decrypted);
        String decryptedString = decrypted.toString();
        System.out.println(decryptedString);
    }

    private String readWholeTextFile(String fileName) throws IOException {
        String contents="";
        String line;
        LineNumberReader in = new LineNumberReader(new FileReader(fileName));
        while (null != (line = in.readLine())) {
            contents+=line+"\n";
        }
        in.close();
        return contents;
    }

    private byte[] readKey(String fileName) throws IOException {
        FileInputStream in = new FileInputStream(fileName);
        byte[] content = in.readAllBytes();
        in.close();
        return content;
    }
}
