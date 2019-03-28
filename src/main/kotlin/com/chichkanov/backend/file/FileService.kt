package com.chichkanov.backend.file

import com.chichkanov.backend.user.UserRepository
import com.chichkanov.backend.user.error.UserNotFoundException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class FileService constructor(
        private val userRepository: UserRepository
) {

    private companion object {
        private const val FILE_NAME_FORMAT = "%s_%s_%s"
    }

    @Value("\${files.upload-dir}")
    private lateinit var uploadDir: String
    private val fileStorageLocation: Path by lazy { Paths.get(uploadDir).toAbsolutePath().normalize() }

    fun saveFiles(files: List<MultipartFile>, userLogin: String): List<FileResponse> {
        val user = userRepository.findByLogin(userLogin) ?: throw UserNotFoundException()
        return files.map { saveFile(it, user.id) }
    }

    fun getFile(fileName: String): File {
        val location = fileStorageLocation.resolve(fileName)
        return File(location.toUri())
    }

    private fun saveFile(file: MultipartFile, userId: Long): FileResponse {
        try {
            val fileName = FILE_NAME_FORMAT.format(
                    userId,
                    System.currentTimeMillis(),
                    StringUtils.cleanPath(file.originalFilename!!)
            )

            val targetLocation = fileStorageLocation.resolve(fileName)
            Files.write(targetLocation, file.bytes)

            val url = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files")
                    .path("/$fileName")
                    .build()
                    .toUriString()

            return FileResponse(url)
        } catch (e: Exception) {
            throw e
        }
    }

}