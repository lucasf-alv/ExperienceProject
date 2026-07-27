package com.ProjectExperience.api.service;

import com.ProjectExperience.api.config.S3Properties;
import com.ProjectExperience.api.dto.CheckInDto;
import com.ProjectExperience.api.dto.UpdateActivityDto;
import com.ProjectExperience.api.exceptions.*;
import com.ProjectExperience.api.models.*;
import com.ProjectExperience.api.repository.ActivityParticipantsRepository;
import com.ProjectExperience.api.repository.ActivityRepository;
import com.ProjectExperience.api.repository.ActivityTypeRepository;
import com.ProjectExperience.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {
    @Mock
    private UserProgressService userProgressService;
    @Mock
    private ActivityRepository activityRepository;
    @Mock
    private ActivityTypeRepository activityTypeRepository;
    @Mock
    private ActivityParticipantsRepository activityParticipantsRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private S3Client s3Client;
    @Mock
    private S3Properties s3Properties;
    @InjectMocks
    private ActivityService activityService;

    //====================================================================
    //                    Testes do listActivityTypes
    //===================================================================

    @Test
    void shouldReturnListActivityTypes() {
        // Arrange
        ActivityType type1 = new ActivityType();
        type1.setId(1L);
        type1.setName("Crossfit");

        ActivityType type2 = new ActivityType();
        type2.setId(2L);
        type2.setName("Corrida");

        List<ActivityType> expected = List.of(type1, type2);

        when(activityTypeRepository.findAll())
                .thenReturn(expected);

        // Act
        List<ActivityType> result = activityService.listActivityTypes();

        // Assert
        assertEquals(expected.size(), result.size());
        assertIterableEquals(expected, result);

        verify(activityTypeRepository).findAll();
    }

    //=====================================================================
    //                       Testes do listActivities
    //====================================================================
    @Test
    void shouldReturnPageOfActivities() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);

        Activity activity1 = new Activity();
        activity1.setId(1L);

        Activity activity2 = new Activity();
        activity2.setId(2L);

        Page<Activity> expected =
                new PageImpl<>(List.of(activity1, activity2), pageable, 2);

        when(activityRepository.findAll(pageable))
                .thenReturn(expected);

        // Act
        Page<Activity> result = activityService.listActivities(pageable);

        // Assert
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(activity1, result.getContent().get(0));
        assertEquals(activity2, result.getContent().get(1));

        verify(activityRepository).findAll(pageable);
    }
    //======================================================================
    //                    Testes do listAllActivities
    //=====================================================================
    @Test
    void shouldReturnAllActivities(){
        // Arrange
        Activity a1 = new Activity();
        a1.setId(1L);
        Activity a2 = new Activity();
        a2.setId(2L);
        List<Activity> expected = new ArrayList<>();
        expected.add(a1);
        expected.add(a2);
        when(activityRepository.findAll()).thenReturn(expected);

        // Act
        List<Activity> result = activityService.listAllActivities();

        // Assert

        assertEquals(expected.size(), result.size());
        assertEquals(a1, result.get(0));
        assertEquals(a2, result.get(1));
        verify(activityRepository).findAll();

    }
    //=====================================================================
    //                    Testes do findActivitiesCreatedByUser
    //====================================================================
    @Test
    void shouldReturnActivitiesCreatedByUser() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);

        Activity activity1 = new Activity();
        activity1.setId(1L);

        Activity activity2 = new Activity();
        activity2.setId(2L);

        Page<Activity> expected =
                new PageImpl<>(List.of(activity1, activity2), pageable, 2);

        when(activityRepository.findByCreatorId(user.getId(), pageable))
                .thenReturn(expected);

        // Act
        Page<Activity> result =
                activityService.findActivitiesCreatedByUser(user, pageable);

        // Assert
        assertEquals(2, result.getContent().size());
        assertIterableEquals(expected.getContent(), result.getContent());
        assertEquals(2, result.getTotalElements());

        verify(activityRepository)
                .findByCreatorId(user.getId(), pageable);
    }
    //=======================================================================
    //                 Testes do findAllActivitiesCreatedByUser
    //======================================================================
    @Test
    void shouldReturnAllActivitiesCreatedByUser() {
        // Arrange
        User user = new User();
        user.setId(1L);

        Activity activity1 = new Activity();
        activity1.setId(1L);

        Activity activity2 = new Activity();
        activity2.setId(2L);

        List<Activity> expected = List.of(activity1, activity2);

        when(activityRepository.findByCreatorId(user.getId()))
                .thenReturn(expected);

        // Act
        List<Activity> result =
                activityService.findAllActivitiesCreatedByUser(user);

        // Assert
        assertIterableEquals(expected, result);
        verify(activityRepository).findByCreatorId(user.getId());
    }
    //====================================================================
    //                   Testes do findAllUsersByActivityId
    //===================================================================
    @Test
    void shouldFindAllUsersByActivityId() {

        // Arrange
        Activity activity = new Activity();
        activity.setId(1L);

        User u1 = new User();
        u1.setId(1L);

        User u2 = new User();
        u2.setId(2L);

        ActivityParticipants p1 = new ActivityParticipants();
        p1.setUser(u1);

        ActivityParticipants p2 = new ActivityParticipants();
        p2.setUser(u2);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityParticipantsRepository.findByActivityId(1L))
                .thenReturn(List.of(p1, p2));

        // Act
        List<User> result =
                activityService.findAllUsersByActivityId(1L);

        // Assert
        assertEquals(2, result.size());
        assertEquals(u1, result.get(0));
        assertEquals(u2, result.get(1));

        verify(activityRepository).findById(1L);
        verify(activityParticipantsRepository).findByActivityId(1L);
    }
    //========================================================================
    //                   Testes do createActivity
    //========================================================================
    @Test
    void shouldCreateActivity() throws IOException {

        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);

        UpdateActivityDto dto = new UpdateActivityDto(
                "Futebol",                  // title
                "Jogar bola",               // description
                LocalDateTime.now().plusDays(1), // scheduleDate
                "Esporte",                  // type
                false,                      // Private
                -15.79,                     // latitute
                -47.88                      // longitude
        );

        ActivityType type = new ActivityType();
        type.setId(1L);
        type.setName("Esporte");

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("foto.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[]{1,2,3});

        when(activityTypeRepository.findByName("Esporte"))
                .thenReturn(Optional.of(type));

        when(activityRepository.save(any(Activity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Activity result =
                activityService.createActivity(dto, loggedUser, file);

        // Assert

        assertEquals(dto.title(), result.getTitle());
        assertEquals(dto.description(), result.getDescription());
        assertEquals(type, result.getActivityType());
        assertEquals(loggedUser, result.getCreator());

        assertNotNull(result.getCriated_At());
        assertNotNull(result.getConfirmation_code());
        assertNotNull(result.getActivityAddress());
        assertNotNull(result.getImage());

        verify(activityTypeRepository).findByName("Esporte");
        verify(activityRepository).save(any(Activity.class));

        verify(s3Client).putObject(
                any(PutObjectRequest.class),
                any(RequestBody.class)
        );

        verify(userProgressService).addXp(loggedUser, 30);

        verify(userProgressService).grantAchievement(
                loggedUser,
                "Primeira Atividade"
        );
    }
    @Test
    void shouldThrowWhenActivityTypeNotFound() {

        User user = new User();
        MockMultipartFile file =
                new MockMultipartFile(
                        "file",
                        "foto.jpg",
                        "image/jpeg",
                        "abc".getBytes()
                );

        UpdateActivityDto dto =
                new UpdateActivityDto(
                        "Titulo",
                        "Descricao",
                        LocalDateTime.now(),
                        "Esporte",
                        false,
                        1.0,
                        2.0
                );

        when(activityTypeRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> activityService.createActivity(dto, user, file)
        );

        verify(activityRepository, never()).save(any());
    }
    //=======================================================================
    //                     Testes do subscribeActivity
    //======================================================================
    @Test
    void shouldSubscribeActivity() {

        // Arrange
        User creator = new User();
        creator.setId(2L);

        User loggedUser = new User();
        loggedUser.setId(1L);

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setCreator(creator);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityParticipantsRepository
                .existsByActivityIdAndUserId(1L, 1L))
                .thenReturn(false);

        when(activityParticipantsRepository.save(any(ActivityParticipants.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ActivityParticipants result =
                activityService.subscribeActivity(1L, loggedUser);

        // Assert
        assertEquals(activity, result.getActivity());
        assertEquals(loggedUser, result.getUser());
        assertFalse(result.getApproved());

        verify(activityRepository).findById(1L);
        verify(activityParticipantsRepository)
                .existsByActivityIdAndUserId(1L, 1L);
        verify(activityParticipantsRepository)
                .save(any(ActivityParticipants.class));
    }
    @Test
    void shouldThrowActivityRegisterError() {

        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);

        User creator = new User();
        creator.setId(2L);

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setCreator(creator);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityParticipantsRepository
                .existsByActivityIdAndUserId(1L, 1L))
                .thenReturn(true);

        // Act + Assert
        assertThrows(
                ActivityRegisterError.class,
                () -> activityService.subscribeActivity(1L, loggedUser)
        );

        verify(activityRepository).findById(1L);

        verify(activityParticipantsRepository)
                .existsByActivityIdAndUserId(1L, 1L);

        verify(activityParticipantsRepository, never())
                .save(any(ActivityParticipants.class));
    }
    @Test
    void shouldThrowCreatorParticipantError(){
        // Arrange

        User creator = new User();
        creator.setId(1L);

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setCreator(creator);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));


        // Act + Assert
        assertThrows(CreatorParticipantError.class, () -> activityService.subscribeActivity(1L,creator));

    }
    @Test
    void shouldThrowActivityCompletedError() {

        // Arrange
        User creator = new User();
        creator.setId(1L);

        User loggedUser = new User();
        loggedUser.setId(2L);

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setCreator(creator);
        activity.setCompleted_At(LocalDateTime.now());

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityParticipantsRepository
                .existsByActivityIdAndUserId(1L, 2L))
                .thenReturn(false);

        // Act + Assert
        assertThrows(
                ActivityCompletedError.class,
                () -> activityService.subscribeActivity(1L, loggedUser)
        );

        verify(activityParticipantsRepository, never())
                .save(any(ActivityParticipants.class));
    }
    //============================================================================
    //                         Testes do updateActivity
    //===========================================================================
    @Test
    void shouldUpdateActivity() throws IOException {

        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);

        ActivityType activityType = new ActivityType();
        activityType.setId(1L);
        activityType.setName("Corrida");

        ActivityAddress activityAddress = new ActivityAddress();

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setCreator(loggedUser);
        activity.setActivityAddress(activityAddress);

        UpdateActivityDto dto = new UpdateActivityDto(
                "Novo título",
                "Nova descrição",
                LocalDateTime.now().plusDays(4),
                "Corrida",
                false,
                434.343,
                3434.34
        );

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityTypeRepository.findByName("Corrida"))
                .thenReturn(Optional.of(activityType));

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("foto.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        when(s3Properties.getBucket()).thenReturn("bucket");
        when(s3Properties.getEndpoint()).thenReturn("http://localhost");

        when(s3Client.putObject(
                any(PutObjectRequest.class),
                any(RequestBody.class)
        )).thenReturn(PutObjectResponse.builder().build());

        when(activityRepository.save(any(Activity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Activity result = activityService.updateActivity(
                1L,
                dto,
                file,
                loggedUser
        );

        // Assert
        assertEquals(dto.title(), result.getTitle());
        assertEquals(dto.description(), result.getDescription());
        assertEquals(dto.scheduleDate(), result.getScheduled_Date());
        assertEquals(dto.Private(), result.getPrivate());
        assertEquals(activityType, result.getActivityType());

        assertEquals(dto.latitute(),
                result.getActivityAddress().getLatitude());

        assertEquals(dto.longitude(),
                result.getActivityAddress().getLongitude());

        assertNotNull(result.getImage());

        verify(activityRepository).findById(1L);
        verify(activityTypeRepository).findByName("Corrida");

        verify(s3Client).putObject(
                any(PutObjectRequest.class),
                any(RequestBody.class)
        );

        verify(activityRepository).save(activity);
    }
    @Test
    void shouldThrowRuntimeExceptionWhenActivityNotFound() {

        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);

        UpdateActivityDto dto = new UpdateActivityDto(
                "Título",
                "Descrição",
                LocalDateTime.now(),
                "Corrida",
                false,
                10.0,
                20.0
        );

        MultipartFile file = mock(MultipartFile.class);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                RuntimeException.class,
                () -> activityService.updateActivity(
                        1L,
                        dto,
                        file,
                        loggedUser
                )
        );

        verify(activityRepository).findById(1L);
        verify(activityRepository, never()).save(any());
    }
    @Test
    void shouldThrowRuntimeExceptionWhenActivityTypeNotFound() {

        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setCreator(loggedUser);

        UpdateActivityDto dto = new UpdateActivityDto(
                "Título",
                "Descrição",
                LocalDateTime.now(),
                "Corrida",
                false,
                10.0,
                20.0
        );

        MultipartFile file = mock(MultipartFile.class);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityTypeRepository.findByName("Corrida"))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                RuntimeException.class,
                () -> activityService.updateActivity(
                        1L,
                        dto,
                        file,
                        loggedUser
                )
        );

        verify(activityRepository).findById(1L);
        verify(activityTypeRepository).findByName("Corrida");
        verify(activityRepository, never()).save(any());
    }
    @Test
    void shouldThrowEditActivityErrorWhenLoggedUserIsNotCreator() {

        // Arrange
        User creator = new User();
        creator.setId(1L);

        User loggedUser = new User();
        loggedUser.setId(2L);

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setCreator(creator);

        ActivityType type = new ActivityType();
        type.setId(1L);
        type.setName("Corrida");

        UpdateActivityDto dto = new UpdateActivityDto(
                "Título",
                "Descrição",
                LocalDateTime.now(),
                "Corrida",
                false,
                10.0,
                20.0
        );

        MultipartFile file = mock(MultipartFile.class);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityTypeRepository.findByName("Corrida"))
                .thenReturn(Optional.of(type));

        // Act + Assert
        assertThrows(
                EditActivityError.class,
                () -> activityService.updateActivity(
                        1L,
                        dto,
                        file,
                        loggedUser
                )
        );

        verify(activityRepository).findById(1L);
        verify(activityTypeRepository).findByName("Corrida");
        verify(activityRepository, never()).save(any());
    }
    @Test
    void shouldThrowPhotoErrorWhenIOExceptionOccurs() throws IOException {

        User loggedUser = new User();
        loggedUser.setId(1L);

        Activity activity = new Activity();
        activity.setCreator(loggedUser);
        activity.setActivityAddress(new ActivityAddress());

        ActivityType type = new ActivityType();
        type.setName("Corrida");

        UpdateActivityDto dto = new UpdateActivityDto(
                "Título",
                "Descrição",
                LocalDateTime.now(),
                "Corrida",
                false,
                10.0,
                20.0
        );

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("foto.png");
        when(file.getBytes()).thenThrow(new IOException());

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityTypeRepository.findByName("Corrida"))
                .thenReturn(Optional.of(type));

        assertThrows(
                PhotoError.class,
                () -> activityService.updateActivity(
                        1L,
                        dto,
                        file,
                        loggedUser
                )
        );
    }
    //===================================================================
    //                    Testes do concludeActivity
    //==================================================================
    @Test
    void shouldConcludeActivity(){

        // Arrange
        Activity activity = new Activity();
        activity.setId(1L);

        User loggedUser = new User();
        loggedUser.setId(1L);

        activity.setCreator(loggedUser);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));


        // Act
        Activity result = activityService.concludeActivity(1L, loggedUser);


        // Assert
        assertNotNull(result.getCompleted_At());
        assertTrue(result.getPrivate());

        verify(activityRepository).findById(1L);
        verify(activityRepository).save(activity);

    }
    @Test
    void shouldNotConcludeActivityWhenUserIsNotCreator(){

        Activity activity = new Activity();
        activity.setId(1L);

        User creator = new User();
        creator.setId(1L);

        User loggedUser = new User();
        loggedUser.setId(2L);

        activity.setCreator(creator);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));


        assertThrows(
                ConcludeActivityError.class,
                () -> activityService.concludeActivity(1L, loggedUser)
        );

        verify(activityRepository, never())
                .save(any());
    }
    @Test
    void shouldThrowWhenActivityDoesNotExist(){

        when(activityRepository.findById(1L))
                .thenReturn(Optional.empty());


        assertThrows(
                RuntimeException.class,
                () -> activityService.concludeActivity(1L, new User())
        );
    }
    //=============================================================================
    //                  Testes do approveParticipants
    //============================================================================

    @Test
    void shouldApproveParticipant(){

        // Arrange
        Activity activity = new Activity();
        activity.setId(1L);

        User loggedUser = new User();
        loggedUser.setId(1L);

        activity.setCreator(loggedUser);


        ActivityParticipants activityParticipants = new ActivityParticipants();
        activityParticipants.setId(1L);
        activityParticipants.setActivity(activity);
        activityParticipants.setApproved(false);


        when(activityParticipantsRepository.findById(1L))
                .thenReturn(Optional.of(activityParticipants));


        // Act
        activityService.approveParticipant(1L, loggedUser);


        // Assert
        assertTrue(activityParticipants.getApproved());

        verify(activityParticipantsRepository)
                .save(activityParticipants);
    }
    @Test
    void shouldThrowApproveParticipantsErrorWhenUserIsNotCreator(){

        Activity activity = new Activity();

        User creator = new User();
        creator.setId(1L);

        User loggedUser = new User();
        loggedUser.setId(2L);

        activity.setCreator(creator);


        ActivityParticipants participant = new ActivityParticipants();
        participant.setActivity(activity);


        when(activityParticipantsRepository.findById(1L))
                .thenReturn(Optional.of(participant));


        assertThrows(
                ApproveParticipantsError.class,
                () -> activityService.approveParticipant(1L, loggedUser)
        );


        verify(activityParticipantsRepository, never())
                .save(any());
    }
    @Test
    void shouldThrowRuntimeExceptionWhenParticipantNotFound(){

        // Arrange
        when(activityParticipantsRepository.findById(1L))
                .thenReturn(Optional.empty());


        User loggedUser = new User();
        loggedUser.setId(1L);


        // Act + Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> activityService.approveParticipant(1L, loggedUser)
        );


        assertEquals(
                "Participante não encontrado",
                exception.getMessage()
        );


        verify(activityParticipantsRepository)
                .findById(1L);

        verify(activityParticipantsRepository, never())
                .save(any());
    }
    //======================================================================================
    //                        Testes do checkInActivity
    //=====================================================================================
    @Test
    void shouldCheckInActivity(){

        // Arrange
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setConfirmation_code("ABC123");

        User creator = new User();
        creator.setId(1L);

        User participantUser = new User();
        participantUser.setId(2L);
        participantUser.setXp(0);

        activity.setCreator(creator);


        ActivityParticipants participant = new ActivityParticipants();
        participant.setId(1L);
        participant.setActivity(activity);
        participant.setUser(participantUser);
        participant.setApproved(true);


        CheckInDto dto = new CheckInDto("ABC123");


        User loggedUser = participantUser;


        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityParticipantsRepository
                .findByActivityIdAndUserId(1L,2L))
                .thenReturn(Optional.of(participant));


        // Act
        activityService.checkInActivity(
                1L,
                dto,
                loggedUser
        );


        // Assert
        assertNotNull(participant.getConfirmed_at());

        verify(activityParticipantsRepository)
                .save(participant);

        verify(userRepository)
                .save(participantUser);

        verify(userProgressService)
                .addXp(participantUser,50);

        verify(userProgressService)
                .addXp(creator,20);

        verify(userProgressService)
                .grantAchievement(
                        participantUser,
                        "Primeiro Check-In"
                );
    }
    @Test
    void shouldThrowExceptionWhenActivityNotFound(){

        // Arrange
        when(activityRepository.findById(1L))
                .thenReturn(Optional.empty());


        User loggedUser = new User();
        loggedUser.setId(1L);


        CheckInDto dto = new CheckInDto("ABC123");


        // Act + Assert

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () ->
                        activityService.checkInActivity(
                                1L,
                                dto,
                                loggedUser
                        )
        );


        assertEquals(
                "Atividade não encontrada",
                exception.getMessage()
        );


        verify(activityParticipantsRepository, never())
                .save(any());
    }
    @Test
    void shouldThrowConfirmationConcludeActivityError(){

        // Arrange
        Activity activity = new Activity();
        activity.setId(1L);
        activity.setCompleted_At(LocalDateTime.now());


        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));


        User loggedUser = new User();
        loggedUser.setId(1L);


        CheckInDto dto = new CheckInDto("ABC123");


        // Act + Assert

        assertThrows(
                ConfirmationConcludeActivityError.class,
                () ->
                        activityService.checkInActivity(
                                1L,
                                dto,
                                loggedUser
                        )
        );


        verify(activityParticipantsRepository, never())
                .save(any());
    }
    @Test
    void shouldThrowExceptionWhenUserIsNotParticipant(){

        // Arrange

        Activity activity = new Activity();
        activity.setId(1L);


        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));


        when(activityParticipantsRepository
                .findByActivityIdAndUserId(1L,2L))
                .thenReturn(Optional.empty());


        User loggedUser = new User();
        loggedUser.setId(2L);


        CheckInDto dto = new CheckInDto("ABC123");


        // Act + Assert

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () ->
                        activityService.checkInActivity(
                                1L,
                                dto,
                                loggedUser
                        )
        );


        assertEquals(
                "Usuário não está inscrito na atividadcriouPrimeiraAtividadee",
                exception.getMessage()
        );
    }
    @Test
    void shouldThrowCheckInErrorWhenParticipantNotApproved(){

        // Arrange

        Activity activity = new Activity();
        activity.setId(1L);
        activity.setConfirmation_code("ABC123");


        User user = new User();
        user.setId(2L);


        ActivityParticipants participant =
                new ActivityParticipants();

        participant.setUser(user);
        participant.setApproved(false);
        participant.setActivity(activity);



        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));


        when(activityParticipantsRepository
                .findByActivityIdAndUserId(1L,2L))
                .thenReturn(Optional.of(participant));


        CheckInDto dto =
                new CheckInDto("ABC123");


        // Act + Assert

        assertThrows(
                CheckInError.class,
                () ->
                        activityService.checkInActivity(
                                1L,
                                dto,
                                user
                        )
        );


        verify(activityParticipantsRepository, never())
                .save(any());
    }
    @Test
    void shouldThrowIncorrectConfirmationCodeError() {

        Activity activity = new Activity();
        activity.setConfirmation_code("ABC123");

        User user = new User();
        user.setId(1L);

        ActivityParticipants participant = new ActivityParticipants();
        participant.setActivity(activity);
        participant.setUser(user);
        participant.setApproved(true);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityParticipantsRepository.findByActivityIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(participant));

        CheckInDto dto = new CheckInDto("ERRADO");

        assertThrows(
                IncorrectConfirmationCodeError.class,
                () -> activityService.checkInActivity(1L, dto, user)
        );

        verify(activityParticipantsRepository, never()).save(any());
    }
    @Test
    void shouldThrowConfirmationActivityError() {

        Activity activity = new Activity();
        activity.setConfirmation_code("ABC123");

        User user = new User();
        user.setId(1L);

        ActivityParticipants participant = new ActivityParticipants();
        participant.setActivity(activity);
        participant.setUser(user);
        participant.setApproved(true);
        participant.setConfirmed_at(LocalDateTime.now());

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityParticipantsRepository.findByActivityIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(participant));

        CheckInDto dto = new CheckInDto("ABC123");

        assertThrows(
                ConfirmationActivityError.class,
                () -> activityService.checkInActivity(1L, dto, user)
        );

        verify(activityParticipantsRepository, never()).save(any());
    }
    //============================================================================
    //                   Testes do unsubscribeActivity
    //===========================================================================
    @Test
    void shouldUnsubscribeActivity(){

        // Arrange
        Activity activity = new Activity();
        activity.setId(1L);

        User loggedUser = new User();
        loggedUser.setId(1L);


        ActivityParticipants activityParticipants =
                new ActivityParticipants();

        activityParticipants.setId(1L);
        activityParticipants.setUser(loggedUser);
        activityParticipants.setActivity(activity);
        activityParticipants.setConfirmed_at(null);


        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityParticipantsRepository
                .findByActivityIdAndUserId(1L,1L))
                .thenReturn(Optional.of(activityParticipants));


        // Act
        activityService.unsubscribeActivity(1L, loggedUser);


        // Assert

        verify(activityParticipantsRepository)
                .delete(activityParticipants);

    }
    @Test
    void shouldThrowExceptionWhenUserIsNotSubscribed(){

        Activity activity = new Activity();
        activity.setId(1L);

        User loggedUser = new User();
        loggedUser.setId(1L);


        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));


        when(activityParticipantsRepository
                .findByActivityIdAndUserId(1L,1L))
                .thenReturn(Optional.empty());


        assertThrows(
                RuntimeException.class,
                () -> activityService.unsubscribeActivity(1L, loggedUser)
        );


        verify(activityParticipantsRepository, never())
                .delete(any());
    }
    @Test
    void shouldThrowCancelSubscribeErrorWhenAlreadyConfirmed(){

        Activity activity = new Activity();
        activity.setId(1L);


        User loggedUser = new User();
        loggedUser.setId(1L);


        ActivityParticipants participant =
                new ActivityParticipants();

        participant.setUser(loggedUser);
        participant.setActivity(activity);
        participant.setConfirmed_at(LocalDateTime.now());


        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));


        when(activityParticipantsRepository
                .findByActivityIdAndUserId(1L,1L))
                .thenReturn(Optional.of(participant));


        assertThrows(
                CancelSubscribeError.class,
                () -> activityService.unsubscribeActivity(1L, loggedUser)
        );


        verify(activityParticipantsRepository, never())
                .delete(any());
    }
    //==================================================================================
    //                            Testes do removeActivity
    //=================================================================================
    @Test
    void shouldRemoveActivity(){
        // Arrange
        Activity activity = new Activity();
        activity.setId(1L);
        User loggedUser = new User();
        loggedUser.setId(1L);
        activity.setCreator(loggedUser);
        activity.setDeleted_At(null);
        when(activityRepository.findById(1L)).thenReturn(Optional.of(activity));

        // Act
        activityService.removeActivity(1L,loggedUser);

        // Assert
        assertNotEquals(null, activity.getDeleted_At());
        verify(activityRepository).findById(1L);
        verify(activityRepository).save(activity);
    }
    @Test
    void shouldThrowDeleteActivityErrorWhenUserIsNotCreator() {

        // Arrange
        Activity activity = new Activity();

        User creator = new User();
        creator.setId(1L);

        User loggedUser = new User();
        loggedUser.setId(2L);

        activity.setCreator(creator);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        // Act + Assert
        assertThrows(
                DeleteActivityError.class,
                () -> activityService.removeActivity(1L, loggedUser)
        );

        verify(activityRepository, never()).save(any());
    }
    //==================================================================
    //                Testes de upload de arquivo
    //===============================================================
    @Test
    void shouldThrowPhotoErrorWhenFileIsNull() {

        User loggedUser = new User();
        loggedUser.setId(1L);

        Activity activity = new Activity();
        activity.setCreator(loggedUser);
        activity.setActivityAddress(new ActivityAddress());

        ActivityType type = new ActivityType();
        type.setName("Corrida");

        UpdateActivityDto dto = new UpdateActivityDto(
                "Título",
                "Descrição",
                LocalDateTime.now(),
                "Corrida",
                false,
                10.0,
                20.0
        );

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityTypeRepository.findByName("Corrida"))
                .thenReturn(Optional.of(type));

        assertThrows(
                PhotoError.class,
                () -> activityService.updateActivity(
                        1L,
                        dto,
                        null,
                        loggedUser
                )
        );

        verify(activityRepository, never()).save(any());
    }
    @Test
    void shouldThrowPhotoErrorWhenFileIsEmpty() {

        User loggedUser = new User();
        loggedUser.setId(1L);

        Activity activity = new Activity();
        activity.setCreator(loggedUser);
        activity.setActivityAddress(new ActivityAddress());

        ActivityType type = new ActivityType();
        type.setName("Corrida");

        UpdateActivityDto dto = new UpdateActivityDto(
                "Título",
                "Descrição",
                LocalDateTime.now(),
                "Corrida",
                false,
                10.0,
                20.0
        );

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(true);

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityTypeRepository.findByName("Corrida"))
                .thenReturn(Optional.of(type));

        assertThrows(
                PhotoError.class,
                () -> activityService.updateActivity(
                        1L,
                        dto,
                        file,
                        loggedUser
                )
        );
    }
    @Test
    void shouldThrowPhotoErrorWhenExtensionIsInvalid() {

        User loggedUser = new User();
        loggedUser.setId(1L);

        Activity activity = new Activity();
        activity.setCreator(loggedUser);
        activity.setActivityAddress(new ActivityAddress());

        ActivityType type = new ActivityType();
        type.setName("Corrida");

        UpdateActivityDto dto = new UpdateActivityDto(
                "Título",
                "Descrição",
                LocalDateTime.now(),
                "Corrida",
                false,
                10.0,
                20.0
        );

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("foto.pdf");

        when(activityRepository.findById(1L))
                .thenReturn(Optional.of(activity));

        when(activityTypeRepository.findByName("Corrida"))
                .thenReturn(Optional.of(type));

        assertThrows(
                PhotoError.class,
                () -> activityService.updateActivity(
                        1L,
                        dto,
                        file,
                        loggedUser
                )
        );
    }






    }









