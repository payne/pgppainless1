package org.mattpayne.pgppainless1;


import org.pgpainless.sop.SOPImpl;
import sop.ByteArrayAndResult;
import sop.DecryptionResult;
import sop.SOP;
import sop.SessionKey;
import sop.exception.SOPGPException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class EncryptMain {
    public static void main(String[] args) {
        try {
            new EncryptMain().run();
            System.out.println("Normal Termination.");
        } catch (Exception bland) {
            bland.printStackTrace();
        }
    }

    private void run() throws IOException {
        SOPImpl sop = new SOPImpl();

        String alicePassword = "wonderland.is.c00l";
        byte[] aliceKey = sop.generateKey()
                .userId("Alice <alice@pgpainless.org>")
                .withKeyPassword(alicePassword)
                .generate()
                .getBytes();
        saveKey("alice.key", aliceKey);
        byte[] aliceCert = sop.extractCert()
                .key(aliceKey)
                .getBytes();

        byte[] bobKey = sop.generateKey()
                .userId("Bob <bob@pgpainless.org>")
                .generate()
                .getBytes();
        saveKey("bob.key", bobKey);

        byte[] bobCert = sop.extractCert()
                .key(bobKey)
                .getBytes();


        final Charset utf8 = Charset.forName("UTF8");
        byte[] message = "Hello, World!\n".getBytes(utf8);
        byte[] encrypted = sop.encrypt()
                .signWith(aliceKey)
                .withKeyPassword(alicePassword)
                .withCert(aliceCert)
                .withCert(bobCert)
                .plaintext(message)
                .getBytes();
        String encryptedString = new String(encrypted);
        System.out.println("Well?");
        PrintWriter out = new PrintWriter("encrypted.asc");
        out.println(encryptedString);
        out.close();

    }

    private void saveKey(String fileName, byte[] key) throws IOException {
        FileOutputStream out = new FileOutputStream(fileName);
        out.write(key);
        out.close();
    }
}
