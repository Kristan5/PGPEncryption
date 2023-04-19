    private static void encrypt(InputStream input, OutputStream output, PGPPublicKey publicKey) throws Exception {
        PGPEncryptedDataGenerator encryptedDataGenerator = new PGPEncryptedDataGenerator(new BcPGPDataEncryptorBuilder(PGPEncryptedData.AES_256));
        encryptedDataGenerator.addMethod(new BcPublicKeyKeyEncryptionMethodGenerator(publicKey));
        
        OutputStream encryptedOutputStream = encryptedDataGenerator.open(output, new byte[BUFFER_SIZE]);
        
        PGPCompressedDataGenerator compressedDataGenerator = new PGPCompressedDataGenerator(PGPCompressedData.ZIP);
        OutputStream compressedOutputStream = compressedDataGenerator.open(encryptedOutputStream);
        
        PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
        OutputStream literalOutputStream = literalDataGenerator.open(compressedOutputStream, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, input.available(), new byte[BUFFER_SIZE]);
        
        byte[] buffer = new byte[BUFFER_SIZE];
        int length;
        while ((length = input.read(buffer)) != -1) {
            literalOutputStream.write(buffer, 0, length);
        }
        
        literalDataGenerator.close();
        compressedDataGenerator.close();
        encryptedDataGenerator.close();
        input.close();
        output.close();
    }
