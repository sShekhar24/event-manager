package com.shaan.event.manager.service.service;

import com.shaan.event.manager.service.dto.RegistrationDTO;
import com.shaan.event.manager.service.entity.AttendanceStatus;
import com.shaan.event.manager.service.entity.Event;
import com.shaan.event.manager.service.entity.Registration;
import com.shaan.event.manager.service.entity.User;
import com.shaan.event.manager.service.exception.ResourceNotFoundException;
import com.shaan.event.manager.service.exception.UnauthorizedException;
import com.shaan.event.manager.service.repository.RegistrationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final UserService userService;
    private final EventService eventService;

    public RegistrationService(RegistrationRepository registrationRepository, UserService userService, EventService eventService) {
        this.registrationRepository = registrationRepository;
        this.userService = userService;
        this.eventService = eventService;
    }

    // Register student for event
    public Registration registerForEvent(Long eventId, String studentEmail) {
        User student = userService.getUserByEmail(studentEmail);
        Event event = eventService.getEventById(eventId);

        // Check if already registered
        if (registrationRepository.existsByStudentIdAndEventId(student.getId(), event.getId())) {
            throw new com.shaan.event.manager.service.exception.IllegalArgumentException(
                    "Student is already registered for this event"
            );
        }

        // Check if registration deadline has passed
        if (LocalDateTime.now().isAfter(event.getRegistrationDeadline())) {
            throw new com.shaan.event.manager.service.exception.IllegalArgumentException(
                    "Registration deadline has passed"
            );
        }

        // Check event capacity
        long registrationCount = registrationRepository.countByEventId(event.getId());
        if (registrationCount >= event.getCapacity()) {
            throw new com.shaan.event.manager.service.exception.IllegalArgumentException(
                    "Event is full. No more registrations accepted"
            );
        }

        Registration registration = Registration.builder()
                .student(student)
                .event(event)
                .build();

        return registrationRepository.save(registration);
    }

    // Get registration by ID
    public Registration getRegistrationById(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .orElseThrow(() -> new ResourceNotFoundException("Registration not found with id: " + registrationId));
    }

    // Get student's registrations
    public Page<Registration> getStudentRegistrations(Long studentId, Pageable pageable) {
        return registrationRepository.findByStudentId(studentId, pageable);
    }

    // Get event's registrations
    public Page<Registration> getEventRegistrations(Long eventId, Pageable pageable) {
        return registrationRepository.findByEventId(eventId, pageable);
    }

    // Cancel registration
    public void cancelRegistration(Long registrationId, String studentEmail) {
        Registration registration = getRegistrationById(registrationId);

        // Check if user is the student
        if (!registration.getStudent().getEmail().equals(studentEmail)) {
            throw new UnauthorizedException("You can only cancel your own registrations");
        }

        // Check if event has already started
        if (LocalDateTime.now().isAfter(registration.getEvent().getStartDateTime())) {
            throw new com.shaan.event.manager.service.exception.IllegalArgumentException(
                    "Cannot cancel registration after event has started"
            );
        }

        registration.setAttendanceStatus(AttendanceStatus.CANCELLED);
        registrationRepository.save(registration);
    }

    // Mark attendance (Admin only)
    public Registration markAttendance(Long registrationId, AttendanceStatus status) {
        Registration registration = getRegistrationById(registrationId);
        registration.setAttendanceStatus(status);
        return registrationRepository.save(registration);
    }

    // Get count of registrations for an event
    public Long getRegistrationCount(Long eventId) {
        return registrationRepository.countByEventId(eventId);
    }

    // Check if user is registered for event
    public boolean isRegisteredForEvent(Long studentId, Long eventId) {
        return registrationRepository.existsByStudentIdAndEventId(studentId, eventId);
    }

    // Convert Registration to DTO
    public RegistrationDTO convertToDTO(Registration registration) {
        return RegistrationDTO.builder()
                .id(registration.getId())
                .student(userService.convertToDTO(registration.getStudent()))
                .event(eventService.convertToDTO(registration.getEvent()))
                .registrationDate(registration.getRegistrationDate())
                .attendanceStatus(registration.getAttendanceStatus().toString())
                .cancellationReason(registration.getCancellationReason())
                .removalReason(registration.getRemovalReason())
                .createdAt(registration.getCreatedAt())
                .updatedAt(registration.getUpdatedAt())
                .build();
    }

    // Convert list of Registrations to DTOs
    public List<RegistrationDTO> convertToDTOs(List<Registration> registrations) {
        return registrations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}
