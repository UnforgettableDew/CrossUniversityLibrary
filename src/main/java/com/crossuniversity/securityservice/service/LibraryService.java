package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.dto.LibraryDTO;
import com.crossuniversity.securityservice.entity.Document;
import com.crossuniversity.securityservice.entity.Library;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.exception.LibraryNotFoundException;
import com.crossuniversity.securityservice.exception.UniversityNotFoundException;
import com.crossuniversity.securityservice.exception.UserNotFoundException;
import com.crossuniversity.securityservice.repository.LibraryRepository;
import com.crossuniversity.securityservice.repository.UniversityRepository;
import com.crossuniversity.securityservice.repository.UniversityUserRepository;
import com.crossuniversity.securityservice.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LibraryService {
    private final UniversityUserRepository universityUserRepository;
    private final UniversityRepository universityRepository;
    private final LibraryRepository libraryRepository;
    private final SecurityUtils securityUtils;

    @Autowired
    public LibraryService(UniversityUserRepository universityUserRepository,
                          UniversityRepository universityRepository,
                          LibraryRepository libraryRepository,
                          SecurityUtils securityUtils) {
        this.universityUserRepository = universityUserRepository;
        this.universityRepository = universityRepository;
        this.libraryRepository = libraryRepository;
        this.securityUtils = securityUtils;
    }

    private Library getLibraryById(Long libraryId) {
        return libraryRepository.findById(libraryId)
                .orElseThrow(() -> new LibraryNotFoundException("Library with id = " + libraryId + " does not exist"));
    }

    private boolean checkLibraryOwnerAccess(Long libraryId) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        boolean flag = false;
        for (Library library : universityUser.getOwnLibraries()) {
            if (library.getId().equals(libraryId))
                return true;
        }
        return flag;
    }

    private boolean checkLibrarySubscriberAccess(Long libraryId) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        boolean flag = false;
        for (Library library : universityUser.getSubscribedLibraries()) {
            if (library.getId().equals(libraryId))
                return true;
        }
        return flag;
    }

    public List<Library> getOwnLibraries() {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        return universityUser.getOwnLibraries();
    }

    public List<Library> getSubscribedLibraries() {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        return universityUser.getSubscribedLibraries();
    }

    public List<Library> getUniversityLibraries(Long universityId) {
        if (!universityRepository.existsById(universityId))
            throw new UniversityNotFoundException("University with id = " + universityId + " does not exist");
        return libraryRepository.findLibrariesByUniversityIdAndLibraryAccess(universityId, true);
    }

    public List<UniversityUser> getOwnersList(Long libraryId) {
        Library library = getLibraryById(libraryId);
        return library.getOwners();
    }

    public List<UniversityUser> getSubscribersList(Long libraryId) {
        Library library = getLibraryById(libraryId);
        return library.getSubscribers();
    }

    public List<Document> getDocumentsList(Long libraryId) throws AccessException {
        Library library = getLibraryById(libraryId);
        if (!library.isLibraryAccess()) {
            if (checkLibraryOwnerAccess(libraryId) || checkLibrarySubscriberAccess(libraryId)){
                return library.getDocuments();
            }
        } else return library.getDocuments();
        throw new AccessException("Access forbidden");
    }

    public Library createLibrary(LibraryDTO libraryDTO) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        Library library = Library.parseDtoToEntity(libraryDTO);
        library.setUniversity(universityUser.getUniversity());
        libraryRepository.save(library);

        universityUser.addOwnLibrary(library);
        universityUserRepository.save(universityUser);
        return library;
    }

    public List<Library> subscribeOnLibrary(Long libraryId) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        Library library = getLibraryById(libraryId);

        universityUser.addSubscribedLibrary(library);
        universityUserRepository.save(universityUser);
        return universityUser.getSubscribedLibraries();
    }

    public void subscribeUser(Long libraryId, String email) throws AccessException {
        if (checkLibraryOwnerAccess(libraryId)) {
            Library library = getLibraryById(libraryId);
            UniversityUser universityUser = universityUserRepository.findUniversityUserByEmail(email)
                    .orElseThrow(() -> new UniversityNotFoundException("University with email = " + email + " does not exist"));

            universityUser.addSubscribedLibrary(library);
            universityUserRepository.save(universityUser);
        } else throw new AccessException("Access restricted");
    }

    public void unsubscribeUser(Long libraryId, String email) throws AccessException {
        if (checkLibraryOwnerAccess(libraryId)) {
            Library library = getLibraryById(libraryId);
            UniversityUser universityUser = universityUserRepository.findUniversityUserByEmail(email)
                    .orElseThrow(() -> new UniversityNotFoundException("University with email = " + email + " does not exist"));

            universityUser.removeSubscribedLibrary(library);
            universityUserRepository.save(universityUser);
        } else throw new AccessException("Access restricted");
    }

    public List<Library> unsubscribeLibrary(Long libraryId) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        Library library = getLibraryById(libraryId);

        universityUser.removeSubscribedLibrary(library);
        universityUserRepository.save(universityUser);
        return universityUser.getSubscribedLibraries();
    }

    public Library updateLibrary(LibraryDTO libraryDTO) throws AccessException {
        Long libraryId = libraryDTO.getId();
        if (checkLibraryOwnerAccess(libraryId)) {
            Library library = getLibraryById(libraryId);

            library.setTitle(libraryDTO.getTitle());
            library.setTopic(libraryDTO.getTopic());
            library.setLibraryAccess(libraryDTO.isLibraryAccess());

            return libraryRepository.save(library);
        } else throw new AccessException("Access restricted");
    }

    public void deleteLibrary(Long libraryId) throws AccessException {
        if (checkLibraryOwnerAccess(libraryId)) {
            int rows = libraryRepository.deleteLibrary(libraryId);
            log.info("Delete from library table");
        } else throw new AccessException("Access restricted");
    }

    public List<Library> findLibrariesBy(Long universityId, String title, String topic, String owner) {
        if (title == null && topic == null && owner == null)
            return getUniversityLibraries(universityId);

        if (topic == null && owner == null)
            return libraryRepository.findLibrariesByTitle(title);

        if (title == null && owner == null)
            return libraryRepository.findLibrariesByTopic(topic);

        if (title == null && topic == null)
            return libraryRepository.findLibrariesByOwner(owner);

        if (title == null)
            return libraryRepository.findLibrariesByTopicAndOwner(topic, owner);

        if (topic == null)
            return libraryRepository.findLibrariesByTitleAndOwner(title, owner);

        if (owner == null)
            return libraryRepository.findLibrariesByTitleAndTopic(title, topic);

        return libraryRepository.findLibrariesByTitleAndTopicAndOwner(title, topic, owner);
    }
}
