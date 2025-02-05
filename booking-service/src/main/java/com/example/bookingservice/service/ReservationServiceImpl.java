package com.example.bookingservice.service;

import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.model.ReservationInterval;
import com.example.bookingservice.repository.ReservationRepository;
import com.example.bookingservice.utils.AppMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{
    ReentrantLock lock = new ReentrantLock();
    private final ReservationRepository reservationRepository;
    private final UserService userService;

    @Override
    public ReservationInterval save(ReservationInterval reservationInterval) {
        lock.lock();
        log.info(" check result {}", checkInterval(reservationInterval.getRoom().getId(), reservationInterval.getStartDate()
                , reservationInterval.getEndDate()));
        if (!checkInterval(
                reservationInterval.getRoom().getId()
                ,reservationInterval.getStartDate()
                , reservationInterval.getEndDate())) {
            lock.unlock();
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Интервал",
                            reservationInterval.getStartDate() + " - " + reservationInterval.getEndDate())
            );

        }
        ReservationInterval reservation = reservationRepository.save(reservationInterval);
        lock.unlock();
        return reservation;
    }

    @Override
    public List<ReservationInterval> findAllReservations() {
        return reservationRepository.findAll();
    }


    private Boolean checkInterval(String roomId,LocalDate startDate, LocalDate endDate) {
        return reservationRepository.checkIntervalForRoom(roomId, startDate, endDate).isEmpty();
    }
    //TODO: добавить проверку на то, что такая комната в этом отеле уже зарезервирована на эти даты
}
