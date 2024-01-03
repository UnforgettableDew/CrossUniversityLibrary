package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.DocumentDTO;
import com.crossuniversity.securityservice.dto.LibraryDTO;
import com.crossuniversity.securityservice.dto.UserBriefProfile;
import com.crossuniversity.securityservice.entity.Library;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.exception.ExceptionResponse;
import com.crossuniversity.securityservice.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @Operation(
            summary = "Retrieves a list of libraries owned by the current user",
            description = "Returns a list of library objects representing the libraries that the authenticated user owns.",
            responses = {
                    @ApiResponse(
                            responseCode  = "200",
                            description = "A list of libraries owned by the user"
                    ),
                    @ApiResponse(
                            responseCode  = "403",
                            description = "Access forbidden"
                    )
            }
    )
    @GetMapping("/my")
    public ResponseEntity<List<LibraryDTO>> getOwnLibraries() {
        return new ResponseEntity<>(libraryService.getOwnLibraries(), HttpStatus.OK);
    }

    @GetMapping("/subscribed")
    public ResponseEntity<List<LibraryDTO>> getSubscribedLibraries() {
        return new ResponseEntity<>(libraryService.getSubscribedLibraries(), HttpStatus.OK);
    }

    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<LibraryDTO>> getUniversityLibraries(@PathVariable Long universityId) {
        return new ResponseEntity<>(libraryService.getUniversityLibraries(universityId)
                .stream()
                .map(LibraryDTO::parseEntityToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/owners")
    public ResponseEntity<List<UserBriefProfile>> getOwnersList(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.getOwnersList(libraryId), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/subscribers")
    public ResponseEntity<List<UserBriefProfile>> getSubscribersList(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.getSubscribersList(libraryId), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/documents")
    private ResponseEntity<List<DocumentDTO>> getDocumentsByLibraryId(@PathVariable Long libraryId) throws AccessException {
        return new ResponseEntity<>(libraryService.getDocumentsByLibraryId(libraryId), HttpStatus.OK);
    }

    @GetMapping("/university/{universityId}/findBy")
    public ResponseEntity<List<LibraryDTO>> findByTopic(@PathVariable Long universityId,
                                                        @RequestParam(required = false) String title,
                                                        @RequestParam(required = false) String topic,
                                                        @RequestParam(required = false) String ownerEmail) {
        return new ResponseEntity<>(libraryService.findLibrariesBy(universityId, title, topic, ownerEmail)
                .stream()
                .map(LibraryDTO::parseEntityToDto)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/document/{documentId}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) throws MalformedURLException {
        Resource resource = libraryService.downloadDocument(documentId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/create-teacher")
    public ResponseEntity<LibraryDTO> createTeacherLibrary(@RequestBody LibraryDTO libraryDTO) {
        return new ResponseEntity<>(libraryService.createLibrary(libraryDTO), HttpStatus.CREATED);
    }

    @PostMapping("/create-student")
    public ResponseEntity<LibraryDTO> createStudentLibrary(@RequestBody LibraryDTO libraryDTO) {
        libraryDTO.setLibraryAccess(false);
        return new ResponseEntity<>(libraryService.createLibrary(libraryDTO), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{libraryId}/document/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentDTO> uploadDocument(@RequestParam("file") MultipartFile file,
                                                      @RequestParam String title,
                                                      @RequestParam String topic,
                                                      @RequestParam String description,
                                                      @PathVariable Long libraryId) throws IOException, AccessException {

        return new ResponseEntity<>(libraryService.uploadDocument(file, title, topic, description, libraryId), HttpStatus.CREATED);
    }

    @PutMapping("/{libraryId}/subscribe")
    public ResponseEntity<List<LibraryDTO>> subscribeOnLibrary(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.subscribeOnLibrary(libraryId), HttpStatus.OK);
    }

    @PutMapping("{libraryId}/subscribe/{email}")
    public ResponseEntity<?> subscribeUser(@PathVariable Long libraryId,
                                           @PathVariable String email) throws AccessException {
        libraryService.subscribeUser(libraryId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("{libraryId}/unsubscribe/{email}")
    public ResponseEntity<?> unsubscribeUser(@PathVariable Long libraryId,
                                             @PathVariable String email) throws AccessException {
        libraryService.unsubscribeUser(libraryId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{libraryId}/unsubscribe")
    public ResponseEntity<List<LibraryDTO>> unsubscribeLibrary(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.unsubscribeLibrary(libraryId), HttpStatus.OK);
    }

    @PutMapping("/{libraryId}/document/{documentId}/add")
    public ResponseEntity<?> addExistedDocumentToLibrary(@PathVariable Long libraryId,
                                                         @PathVariable Long documentId) throws AccessException {
        libraryService.addExistedDocumentToLibrary(libraryId, documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update")
    public ResponseEntity<LibraryDTO> updateLibrary(@RequestBody LibraryDTO libraryDTO) throws AccessException {
        return new ResponseEntity<>(libraryService.updateLibrary(libraryDTO), HttpStatus.OK);
    }

    @PutMapping("/{libraryId}/document/{documentId}/remove")
    public ResponseEntity<?> removeDocumentFromLibrary(@PathVariable Long libraryId,
                                                       @PathVariable Long documentId) throws AccessException {
        libraryService.removeExistedDocumentFromLibrary(libraryId, documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/document/update")
    public ResponseEntity<DocumentDTO> updateDocument(@RequestBody DocumentDTO documentDTO) throws AccessException {
        return new ResponseEntity<>(libraryService.updateDocument(documentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{libraryId}/delete")
    public ResponseEntity<?> deleteLibrary(@PathVariable Long libraryId) throws AccessException {
        libraryService.deleteLibrary(libraryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/document/{documentId}/delete")
    public ResponseEntity<?> deleteDocument(@PathVariable Long documentId) throws AccessException {
        libraryService.deleteDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
