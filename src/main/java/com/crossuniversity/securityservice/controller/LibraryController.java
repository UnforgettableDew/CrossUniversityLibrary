package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.DocumentDTO;
import com.crossuniversity.securityservice.dto.LibraryDTO;
import com.crossuniversity.securityservice.dto.UserBriefProfile;
import com.crossuniversity.securityservice.model.DownloadedFile;
import com.crossuniversity.securityservice.service.LibraryService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.crossuniversity.securityservice.constant.ValidationViolation.BLANK_EMAIL;
import static com.crossuniversity.securityservice.constant.ValidationViolation.EMAIL_NOT_RECOGNIZED;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

@RestController
@RequestMapping("/library")
@CrossOrigin(origins = "*",
        allowedHeaders = "*",
        exposedHeaders = "*",
        methods = {GET, POST, PUT, DELETE},
        maxAge = 3600)
public class LibraryController {
    private final LibraryService libraryService;


    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/own")
    public ResponseEntity<List<LibraryDTO>> getOwnLibraries() {
        return new ResponseEntity<>(libraryService.getOwnLibraries(), HttpStatus.OK);
    }

    @GetMapping("/subscribed")
    public ResponseEntity<List<LibraryDTO>> getSubscribedLibraries() {
        return new ResponseEntity<>(libraryService.getSubscribedLibraries(), HttpStatus.OK);
    }

    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<LibraryDTO>> getUniversityLibraries(@PathVariable Long universityId) {
        return new ResponseEntity<>(libraryService.getUniversityLibraries(universityId), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/owners")
    public ResponseEntity<List<UserBriefProfile>> getOwnersList(
            @PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.getOwnersList(libraryId), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/subscribers")
    public ResponseEntity<List<UserBriefProfile>> getSubscribersList(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.getSubscribersList(libraryId), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/documents")
    private ResponseEntity<List<DocumentDTO>> getDocumentsByLibraryId(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.getDocumentsByLibraryId(libraryId), HttpStatus.OK);
    }

    @GetMapping("/university/{universityId}/findBy")
    public ResponseEntity<List<LibraryDTO>> findLibrariesBy(@PathVariable Long universityId,
                                                            @RequestParam(required = false) String title,
                                                            @RequestParam(required = false) String topic,
                                                            @RequestParam(required = false) String ownerEmail) {
        return new ResponseEntity<>(libraryService
                .findLibrariesBy(universityId, title, topic, ownerEmail), HttpStatus.OK);
    }

    @GetMapping(value = "/document/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) throws IOException {
        DownloadedFile downloadedFile = libraryService.downloadDocument(documentId);
        Resource resource = downloadedFile.getResource();

        return ResponseEntity.ok()
                .contentType(APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + downloadedFile.getFileName() + "\"")
                .body(resource);
    }


    @PostMapping("/create-with-access")
    public ResponseEntity<LibraryDTO> createLibraryWithAccess(@RequestParam String title,
                                                              @RequestParam String topic,
                                                              @RequestParam Boolean libraryAccess) {
        return new ResponseEntity<>(libraryService.createLibrary(title, topic, libraryAccess), HttpStatus.CREATED);
    }

    @PostMapping("/create")
    public ResponseEntity<LibraryDTO> createLibrary(@RequestParam String title,
                                                    @RequestParam String topic) {
        return new ResponseEntity<>(libraryService.createLibrary(title, topic, false), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{libraryId}/document/upload", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDTO> uploadDocument(@RequestParam("file") MultipartFile file,
                                                      @RequestParam String title,
                                                      @RequestParam String topic,
                                                      @RequestParam String description,
                                                      @PathVariable Long libraryId) throws IOException {

        return new ResponseEntity<>(libraryService
                .uploadDocument(file, title, topic, description, libraryId), HttpStatus.CREATED);
    }


    @PutMapping("/{libraryId}/subscribe")
    public ResponseEntity<List<LibraryDTO>> subscribeToLibrary(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.subscribeToLibrary(libraryId), HttpStatus.OK);
    }

    @PutMapping("{libraryId}/subscribe/{email}")
    public ResponseEntity<?> subscribeUser(@PathVariable Long libraryId,
                                           @NotBlank(message = BLANK_EMAIL)
                                           @Email(message = EMAIL_NOT_RECOGNIZED)
                                           @PathVariable String email) {
        libraryService.subscribeUser(libraryId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{libraryId}/unsubscribe/{email}")
    public ResponseEntity<?> unsubscribeUser(@PathVariable Long libraryId,
                                             @NotBlank(message = BLANK_EMAIL)
                                             @Email(message = EMAIL_NOT_RECOGNIZED)
                                             @PathVariable String email) {
        libraryService.unsubscribeUser(libraryId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{libraryId}/unsubscribe")
    public ResponseEntity<List<LibraryDTO>> unsubscribeLibrary(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.unsubscribeLibrary(libraryId), HttpStatus.OK);
    }

    @PutMapping("/{libraryId}/document/{documentId}/add")
    public ResponseEntity<?> addExistedDocumentToLibrary(@PathVariable Long libraryId,
                                                         @PathVariable Long documentId) {
        libraryService.addExistedDocumentToLibrary(libraryId, documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{libraryId}/document/{documentId}/remove")
    public ResponseEntity<?> removeDocumentFromLibrary(@PathVariable Long libraryId,
                                                       @PathVariable Long documentId) {
        libraryService.removeExistedDocumentFromLibrary(libraryId, documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<LibraryDTO> updateLibrary(@RequestBody LibraryDTO libraryDTO) {
        return new ResponseEntity<>(libraryService.updateLibrary(libraryDTO), HttpStatus.OK);
    }


    @PutMapping("/document/update")
    public ResponseEntity<DocumentDTO> updateDocument(@RequestBody DocumentDTO documentDTO) {
        return new ResponseEntity<>(libraryService.updateDocument(documentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{libraryId}/delete")
    public ResponseEntity<?> deleteLibrary(@PathVariable Long libraryId) {
        libraryService.deleteLibrary(libraryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/document/{documentId}/delete")
    public ResponseEntity<?> deleteDocument(@PathVariable Long documentId) {
        libraryService.deleteDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
