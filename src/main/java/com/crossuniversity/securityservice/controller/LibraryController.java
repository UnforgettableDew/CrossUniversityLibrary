package com.crossuniversity.securityservice.controller;

import com.crossuniversity.securityservice.dto.LibraryDTO;
import com.crossuniversity.securityservice.entity.Document;
import com.crossuniversity.securityservice.entity.Library;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.service.LibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;

    @Autowired
    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/my")
    public ResponseEntity<List<Library>> getOwnLibraries() {
        return new ResponseEntity<>(libraryService.getOwnLibraries(), HttpStatus.OK);
    }

    @GetMapping("/subscribed")
    public ResponseEntity<List<Library>> getSubscribedLibraries() {
        return new ResponseEntity<>(libraryService.getSubscribedLibraries(), HttpStatus.OK);
    }

    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<Library>> getUniversityLibraries(@PathVariable Long universityId) {
        return new ResponseEntity<>(libraryService.getUniversityLibraries(universityId), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/owners")
    public ResponseEntity<List<UniversityUser>> getOwnersList(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.getOwnersList(libraryId), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/subscribers")
    public ResponseEntity<List<UniversityUser>> getSubscribersList(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.getSubscribersList(libraryId), HttpStatus.OK);
    }

    @GetMapping("/{libraryId}/documents")
    public ResponseEntity<List<Document>> getDocumentsList(@PathVariable Long libraryId) throws AccessException {
        return new ResponseEntity<>(libraryService.getDocumentsList(libraryId), HttpStatus.OK);
    }

    @GetMapping("/university/{universityId}/findBy")
    public ResponseEntity<List<Library>> findByTopic(@PathVariable Long universityId,
                                                     @RequestParam(required = false) String title,
                                                     @RequestParam(required = false) String topic,
                                                     @RequestParam(required = false) String owner) {
        return new ResponseEntity<>(libraryService.findLibrariesBy(universityId, title, topic, owner), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Library> createLibrary(@RequestBody LibraryDTO libraryDTO) {
        return new ResponseEntity<>(libraryService.createLibrary(libraryDTO), HttpStatus.CREATED);
    }

    @PostMapping("/{libraryId}/subscribe")
    public ResponseEntity<List<Library>> subscribeOnLibrary(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.subscribeOnLibrary(libraryId), HttpStatus.OK);
    }

    @PostMapping("{libraryId}/subscribe/{email}")
    public ResponseEntity<?> subscribeUser(@PathVariable Long libraryId,
                                           @PathVariable String email) throws AccessException {
        libraryService.subscribeUser(libraryId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("{libraryId}/unsubscribe/{email}")
    public ResponseEntity<?> unsubscribeUser(@PathVariable Long libraryId,
                                             @PathVariable String email) throws AccessException {
        libraryService.unsubscribeUser(libraryId, email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{libraryId}/unsubscribe")
    public ResponseEntity<List<Library>> unsubscribeLibrary(@PathVariable Long libraryId) {
        return new ResponseEntity<>(libraryService.unsubscribeLibrary(libraryId), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Library> updateLibrary(@RequestBody LibraryDTO libraryDTO) throws AccessException {
        return new ResponseEntity<>(libraryService.updateLibrary(libraryDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{libraryId}/delete")
    public ResponseEntity<?> deleteLibrary(@PathVariable Long libraryId) throws AccessException {
        libraryService.deleteLibrary(libraryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
