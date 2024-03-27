package diarynote.data.room.utils

import android.content.Context
import android.os.Build
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.File
import java.security.SecureRandom

private const val PASSPHRASE_LENGTH = 32

class PassphraseGenerator(private val context: Context) {

    fun getPassphrase(): ByteArray {
        val file = File(context.filesDir, "passphrase.bin")
        val encryptedFile = EncryptedFile.Builder(
            file,
            context,
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
        ).build()

        return if (file.exists()) {
            encryptedFile.openFileInput().use {
                it.readBytes()
            }
        } else {
            generatePassphrase().also { passphrase ->
                encryptedFile.openFileOutput().use {
                    it.write(passphrase)
                }
            }
        }
    }

    private fun generatePassphrase(): ByteArray {
        val random = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            SecureRandom.getInstanceStrong()
        } else {
            SecureRandom.getInstance("NativePRNG")
        }
        val result = ByteArray(PASSPHRASE_LENGTH)

        random.nextBytes(result)

        while (result.contains(0)) {
            random.nextBytes(result)
        }

        return result
    }
}