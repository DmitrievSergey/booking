package com.example.bookingservice.service;

import com.example.bookingservice.dto.filter.RoomFilter;
import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.exception.EntityNotFoundException;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Override
    public synchronized Room save(Room room) {
        //TODO: добавить проверку на отель
        //TODO: вместо synchronized reentrantlock

        Optional<Room> creatingRoom = roomRepository.findByNumberAndHotel(
                room.getNumber(),
                room.getHotel());
        if(creatingRoom.isPresent()) throw new EntityAlreadyExistsException(
                MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Комната", room.getNumber())
        );

        Room newRoom = roomRepository.saveAndFlush(room);
        newRoom.getHotel().addRoom(room);
        return newRoom;
    }

    @Override
    public synchronized Room update(String roomId, Room room) {
        Optional<Room> findingRoom = roomRepository.findByNumberAndHotel(
                room.getNumber(),
                room.getHotel()
        );

        if(findingRoom.isPresent()
                && !roomId.equals(findingRoom.get().getId())) throw new EntityAlreadyExistsException(
                MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Отель", room.getNumber())
        );
        Room updatingRoom = findRoomById(roomId);
        updatingRoom.setName(room.getName());
        updatingRoom.setDescription(room.getDescription());
        updatingRoom.setNumber(room.getNumber());
        updatingRoom.setPricePerDay(room.getPricePerDay());
        updatingRoom.setPeopleCount(room.getPeopleCount());
        roomRepository.saveAndFlush(updatingRoom);
        updatingRoom.getHotel().addRoom(room);
        return updatingRoom;
    }
    @Override
    public Room findRoomById(String roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> {
                            throw new EntityNotFoundException(
                                    MessageFormat.format(AppMessages.ENTITY_NOT_EXISTS, "Комната", roomId)
                            );
                        }
                );
    }

    @Override
    public void deleteRoomById(String roomId) {
        roomRepository.deleteById(roomId);
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
