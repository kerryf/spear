package spear.security

import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.security.SecureRandom

final class Pbkdf2PasswordEncoder
{
    private static final SecretKeyFactory KEY_FACTORY = SecretKeyFactory.getInstance('PBKDF2WithHmacSHA256')

    private static final int SALT_SIZE = 16
    private static final int KEY_LENGTH = 256
    private static final int ITERATIONS = 600000

    private final SecureRandom secureRandom
    private final HexFormat hexFormat

    Pbkdf2PasswordEncoder()
    {
        secureRandom = new SecureRandom()
        hexFormat = HexFormat.of()
    }

    String hash(String plain)
    {
        byte[] salt = generateSalt()
        byte[] hash = generateHash(plain, salt)

        return hexFormat.formatHex(hash)
    }

    boolean verify(String encoded, String plain)
    {
        byte[] original = hexFormat.parseHex(encoded)
        byte[] salt = extractSalt(original)
        byte[] generated = generateHash(plain, salt)

        return MessageDigest.isEqual(original, generated)
    }

    private byte[] generateSalt()
    {
        byte[] salt = new byte[SALT_SIZE]
        secureRandom.nextBytes(salt)

        return salt
    }

    private static byte[] generateHash(String plain, byte[] salt)
    {
        PBEKeySpec keySpec = new PBEKeySpec(plain.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        try
        {
            SecretKey secretKey = KEY_FACTORY.generateSecret(keySpec)
            byte[] key = secretKey.getEncoded()

            return add(salt, key)
        }
        finally
        {
            keySpec.clearPassword()
        }
    }

    private static byte[] add(byte[] one, byte[] two)
    {
        ByteBuffer buffer = ByteBuffer.allocate(one.length + two.length)
        buffer.put(one)
        buffer.put(two)

        return buffer.array()
    }

    private static byte[] extractSalt(byte[] hash)
    {
        byte[] salt = new byte[SALT_SIZE]

        ByteBuffer buffer = ByteBuffer.wrap(hash)
        buffer.get(salt)

        return salt
    }
}
