package com.unforgettable.crossuniversitylibrary.service;

import com.unforgettable.crossuniversitylibrary.dto.LibraryDTO;
import com.unforgettable.crossuniversitylibrary.entity.*;
import com.unforgettable.crossuniversitylibrary.exception.bad_request.LibraryBadRequestException;
import com.unforgettable.crossuniversitylibrary.exception.not_found.UniversityNotFoundException;
import com.unforgettable.crossuniversitylibrary.mapper.BriefProfileMapper;
import com.unforgettable.crossuniversitylibrary.mapper.DocumentMapper;
import com.unforgettable.crossuniversitylibrary.mapper.LibraryMapper;
import com.unforgettable.crossuniversitylibrary.repository.DocumentRepository;
import com.unforgettable.crossuniversitylibrary.repository.LibraryRepository;
import com.unforgettable.crossuniversitylibrary.repository.UniversityRepository;
import com.unforgettable.crossuniversitylibrary.repository.UniversityUserRepository;
import com.unforgettable.crossuniversitylibrary.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LibraryServiceTest {
    @InjectMocks
    private LibraryService libraryService;

    @Mock
    private UniversityUserRepository universityUserRepository;

    @Mock
    private UniversityRepository universityRepository;

    @Mock
    private LibraryRepository libraryRepository;

    @Mock
    private LibraryMapper libraryMapper;

    @Mock
    private DocumentMapper documentMapper;

    @Mock
    private BriefProfileMapper briefProfileMapper;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private S3Service s3Service;

    @Test
    void getOwnLibraries_defaultCase() {
        Library library = new Library(1L, "ownLibraryTestTitle", "ownLibraryTestTopic", false, null, null, null, null);
        List<Library> libraries = new ArrayList<>(List.of(library));

        List<LibraryDTO> libraryDTOs = new ArrayList<>(List.of(new LibraryDTO(1L, "ownLibraryTestTitle", "ownLibraryTestTopic", false)));
        UniversityUser universityUser = new UniversityUser(1L, "testUser", 1000.0, null,
                new University(),
                new UserCredentials(),
                new ArrayList<>(),
                libraries,
                new ArrayList<>());


        when(securityUtils.getUserFromSecurityContextHolder()).thenReturn(universityUser);

        when(libraryMapper.mapToListDTO(libraries)).thenReturn(libraryDTOs);

        List<LibraryDTO> result = libraryService.getOwnLibraries();

        ArgumentCaptor<List<Library>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(libraryMapper, times(1)).mapToListDTO(listArgumentCaptor.capture());

        assertEquals(1, result.size());
        assertEquals("ownLibraryTestTitle", result.get(0).getTitle());

        assertEquals(libraries, listArgumentCaptor.getValue());
    }

    @Test
    void getSubscribedLibraries_defaultCase() {
        Library library = new Library(1L, "subscribedLibraryTestTitle", "subscribedLibraryTestTopic", false, null, null, null, null);
        List<Library> libraries = new ArrayList<>(List.of(library));

        List<LibraryDTO> libraryDTOs = new ArrayList<>(List.of(
                new LibraryDTO(1L, "subscribedLibraryTestTitle", "subscribedLibraryTestTopic", false)));
        UniversityUser universityUser = new UniversityUser(1L, "test_user", 1000.0, null,
                new University(),
                new UserCredentials(),
                new ArrayList<>(),
                new ArrayList<>(),
                libraries);


        when(securityUtils.getUserFromSecurityContextHolder()).thenReturn(universityUser);

        when(libraryMapper.mapToListDTO(libraries)).thenReturn(libraryDTOs);

        List<LibraryDTO> result = libraryService.getSubscribedLibraries();

        ArgumentCaptor<List<Library>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(libraryMapper, times(1)).mapToListDTO(listArgumentCaptor.capture());

        assertEquals(1, result.size());
        assertEquals("subscribedLibraryTestTitle", result.get(0).getTitle());
        assertEquals(libraries, listArgumentCaptor.getValue());
    }

    @Test
    void subscribeToLibrary_userContainsSubscribedLibrary_throwLibraryException() {
        Library library = new Library(1L, "subscribedLibraryTestTitle", "subscribedLibraryTestTopic", false, null, null, null, null);
        List<Library> libraries = new ArrayList<>(List.of(library));
        Long libraryId = library.getId();

        UniversityUser universityUser = new UniversityUser(1L, "test_user", 1000.0, null,
                new University(),
                new UserCredentials(),
                new ArrayList<>(),
                new ArrayList<>(),
                libraries);

        when(securityUtils.getUserFromSecurityContextHolder()).thenReturn(universityUser);
        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));

        ArgumentCaptor<Long> capturedLibraryId = ArgumentCaptor.forClass(Long.class);

        assertThatThrownBy(() -> libraryService.subscribeToLibrary(libraryId))
                .isInstanceOf(LibraryBadRequestException.class)
                .hasMessageContaining("You are the owner of the library or are already subscribed to it");

        verify(libraryRepository).findById(capturedLibraryId.capture());

        assertEquals(1L, capturedLibraryId.getValue());
    }

    @Test
    void subscribeToLibrary_userContainsOwnLibrary_throwLibraryException() {
        Library library = new Library(1L, "ownLibraryTestTitle", "ownLibraryTestTopic", false, null, null, null, null);
        List<Library> libraries = new ArrayList<>(List.of(library));
        Long libraryId = library.getId();

        UniversityUser universityUser = new UniversityUser(1L, "test_user", 1000.0, null,
                new University(),
                new UserCredentials(),
                new ArrayList<>(),
                libraries,
                new ArrayList<>());

        when(securityUtils.getUserFromSecurityContextHolder()).thenReturn(universityUser);
        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));

        ArgumentCaptor<Long> capturedLibraryId = ArgumentCaptor.forClass(Long.class);

        assertThatThrownBy(() -> libraryService.subscribeToLibrary(libraryId))
                .isInstanceOf(LibraryBadRequestException.class)
                .hasMessageContaining("You are the owner of the library or are already subscribed to it");

        verify(libraryRepository).findById(capturedLibraryId.capture());

        assertEquals(1L, capturedLibraryId.getValue());
    }

    @Test
    void getUniversityLibraries_universityNotExists_throwException() {
        Long universityId = 1L;

        when(universityRepository.existsById(universityId))
                .thenReturn(false);

        UniversityNotFoundException exception = assertThrows(UniversityNotFoundException.class,
                () -> libraryService.getUniversityLibraries(universityId));

        assertEquals("University with id = " + universityId + " does not exist", exception.getMessage());
    }

    @Test
    void getUniversityLibraries_defaultCase() {
        Long universityId = 1L;
        List<Library> libraries = new ArrayList<>(
                List.of(new Library(1L, "LibraryTestTitle", "LibraryTestTopic", false, null, null, null, null)));
        List<LibraryDTO> libraryDTOs = new ArrayList<>(List.of(
                new LibraryDTO(1L, "LibraryTestTitle", "LibraryTestTopic", false)));

        when(universityRepository.existsById(universityId)).thenReturn(true);
        when(libraryRepository.findLibrariesByUniversityIdAndLibraryAccess(universityId, true))
                .thenReturn(libraries);
        when(libraryMapper.mapToListDTO(libraries)).thenReturn(libraryDTOs);

        ArgumentCaptor<List<Library>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<Long> universityIdCaptor = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<Boolean> libraryAccessCaptor = ArgumentCaptor.forClass(Boolean.class);

        List<LibraryDTO> result = libraryService.getUniversityLibraries(universityId);

        verify(universityRepository, times(1)).existsById(universityId);
        verify(libraryMapper, times(1)).mapToListDTO(listArgumentCaptor.capture());
        verify(libraryRepository).findLibrariesByUniversityIdAndLibraryAccess
                (universityIdCaptor.capture(), libraryAccessCaptor.capture());


        assertEquals(1, result.size());
        assertEquals("LibraryTestTitle", result.get(0).getTitle());
        assertEquals(libraries, listArgumentCaptor.getValue());
        assertEquals(universityId, universityIdCaptor.getValue());
        assertEquals(true, libraryAccessCaptor.getValue());
    }

    @Test
    void subscribeToLibrary_defaultCase() {
        Library library = new Library(1L, "subscribedLibraryTestTitle", "subscribedLibraryTestTopic", false, null, null, null, null);
        Long libraryId = library.getId();
        List<LibraryDTO> libraryDTOs = new ArrayList<>(List.of(
                new LibraryDTO(1L, "subscribedLibraryTestTitle", "subscribedLibraryTestTopic", false)));

        UniversityUser universityUser = new UniversityUser(1L, "test_user", 1000.0, null,
                new University(),
                new UserCredentials(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>());

        when(securityUtils.getUserFromSecurityContextHolder()).thenReturn(universityUser);
        when(libraryRepository.findById(libraryId)).thenReturn(Optional.of(library));
        when(universityUserRepository.save(universityUser)).thenReturn(universityUser);
        when(libraryMapper.mapToListDTO(universityUser.getSubscribedLibraries()))
                .thenReturn(libraryDTOs);

        ArgumentCaptor<Long> capturedLibraryId = ArgumentCaptor.forClass(Long.class);
        ArgumentCaptor<List<Library>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);

        List<LibraryDTO> result = libraryService.subscribeToLibrary(1L);

        verify(libraryMapper, times(1)).mapToListDTO(listArgumentCaptor.capture());
        verify(libraryRepository).findById(capturedLibraryId.capture());
        verify(universityUserRepository, times(1)).save(universityUser);

        assertEquals(1, result.size());
        assertEquals("subscribedLibraryTestTitle", result.get(0).getTitle());
        assertEquals(universityUser.getSubscribedLibraries(), listArgumentCaptor.getValue());
        assertEquals(1L, capturedLibraryId.getValue());
    }
}