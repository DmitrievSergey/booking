package com.example.bookingservice.service;

import com.example.bookingservice.dto.filter.RoomFilter;
import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.exception.EntityNotFoundException;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.model.Room;
import com.example.bookingservice.repository.HotelRepository;
import com.example.bookingservice.repository.RoomRepository;
import com.example.bookingservice.repository.RoomSpecification;
import com.example.bookingservice.utils.AppMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    ReentrantLock lock = new ReentrantLock();
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Override
    public Room save(Room room) {
        //TODO: добавить проверку на отель
        lock.lock();

        Optional<Room> creatingRoom = roomRepository.findByNumberAndHotel(
                room.getNumber(),
                room.getHotel());
        if (creatingRoom.isPresent()) {
            lock.unlock();
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Комната", room.getNumber())
            );
        }

        Room newRoom = roomRepository.saveAndFlush(room);
        newRoom.getHotel().addRoom(room);
        lock.unlock();
        return newRoom;
    }

    @Override
    public Room update(String roomId, Room room) {
        lock.lock();
        Optional<Room> findingRoom = roomRepository.findByNumberAndHotel(
                room.getNumber(),
                room.getHotel()
        );

        if (findingRoom.isPresent()
                && !roomId.equals(findingRoom.get().getId())) {
            lock.unlock();
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Комната", room.getNumber())
            );
        }
        Room updatingRoom = findRoomById(roomId);
        updatingRoom.setName(room.getName());
        updatingRoom.setDescription(room.getDescription());
        updatingRoom.setNumber(room.getNumber());
        updatingRoom.setPricePerDay(room.getPricePerDay());
        updatingRoom.setPeopleCount(room.getPeopleCount());
        roomRepository.saveAndFlush(updatingRoom);
        updatingRoom.getHotel().addRoom(room);
        lock.unlock();
        return updatingRoom;
    }

    @Override
    public Room findRoomById(String roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow();
    }

    @Override
    public void deleteRoomById(String roomId) {

        Room deletingRoom = findRoomById(roomId);
        roomRepository.deleteById(deletingRoom.getId());
    }

    @Override
    public List<Room> filterBy(RoomFilter filter) {
        List<Room> roomList = roomRepository.findAll(RoomSpecification.withFilter(filter)
                , PageRequest.of(
                        filter.getPageNumber(),
                        filter.getPageSize()
                )).getContent();
        return roomList;
    }
}
