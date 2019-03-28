package com.chichkanov.backend.file

import com.chichkanov.backend.security.JwtTokenProvider
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import javax.servlet.http.HttpServletRequest


@Controller
@RequestMapping("/files")
class FileController constructor(
        private val fileService: FileService,
        private val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/upload")
    fun uploadFiles(@RequestParam("file") files: List<MultipartFile>, request: HttpServletRequest): ResponseEntity<List<FileResponse>> {
        val userLogin = jwtTokenProvider.getLogin(request)!!
        return ResponseEntity.ok(fileService.saveFiles(files, userLogin))
    }

    @GetMapping("/{fileName}")
    fun downloadFile(@PathVariable("fileName") fileName: String, request: HttpServletRequest): ResponseEntity<Resource> {
        val file = fileService.getFile(fileName)
        val resource = FileSystemResource(file)

        var contentType: String? = null
        try {
            contentType = request.servletContext.getMimeType(file.absolutePath)
        } catch (ex: IOException) {
            // Do nothing
        }

        if (contentType == null) {
            contentType = "application/octet-stream"
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource)
    }

}