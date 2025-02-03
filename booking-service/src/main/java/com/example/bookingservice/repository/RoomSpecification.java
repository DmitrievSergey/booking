package com.example.bookingservice.repository;


import com.example.bookingservice.dto.filter.RoomFilter;
import com.example.bookingservice.model.Hotel;
import com.example.bookingservice.model.ReservationInterval;
import com.example.bookingservice.model.Room;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public interface RoomSpecification {
    static Specification<Room> withFilter(RoomFilter filter){
        return Specification.where(
                        byRoomId(filter.getId()))
                .and(byRoomName(filter.getName()))
                .and(byRoomDescription(filter.getDescription()))
                .and(byRoomRangeCost(filter.getMinCost(), filter.getMaxCost()))
                .and(byRoomPeopleCount(filter.getPeopleCount()))
                .and(byRoomHotelId(filter.getHotelId()))
                .and(byStartAndEndDate(filter.getStartDate(), filter.getEndDate()));
    }

    static Specification<Room> byStartAndEndDate(LocalDate startDate, LocalDate endDate) {
        if(startDate == null || endDate == null) return null;
        return (root, query, criteriaBuilder) -> {
            Join<Room, ReservationInterval> roomHotel = root.join("intervalList", JoinType.LEFT);
           Predicate cb1 = criteriaBuilder.lessThan(roomHotel.get("endDate"), startDate);
            Predicate cb2 = criteriaBuilder.greaterThan(roomHotel.get("startDate"), endDate);
            Predicate cb3 = criteriaBuilder.isNull(roomHotel.get("endDate"));
            Predicate cb4 = criteriaBuilder.or(cb1, cb2, cb3);
            return cb4;
        };
    }

    static Specification<Room> byRoomHotelId(String hotelId) {
        return (root, query, criteriaBuilder) -> {
            if(hotelId == null) return null;
            Join<Hotel, Room> roomHotel = root.join("hotel");
            return criteriaBuilder.equal(roomHotel.get("id"), hotelId);
        };
    }

    static Specification<Room> byRoomPeopleCount(Byte peopleCount) {
        return (root, query, criteriaBuilder) -> {
            if(peopleCount == null) return null;
            return criteriaBuilder.equal(root.get("peopleCount"), peopleCount);
        };
    }


    static Specification<Room> byRoomRangeCost(Float minCost, Float maxCost) {
        return (root, query, criteriaBuilder) -> {
            if(minCost == null && maxCost == null) return null;
            if(minCost == null) return criteriaBuilder.lessThanOrEqualTo(root.get("pricePerDay"), maxCost);
            if(maxCost == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("pricePerDay"), minCost);

            return criteriaBuilder.between(root.get("pricePerDay"), minCost, maxCost);
        };
    }

    static Specification<Room> byRoomDescription(String description) {
        return (root, query, criteriaBuilder) -> {
            if(description == null) return null;
            return criteriaBuilder.like(root.get("description"), description);
        };
    }

    static Specification<Room> byRoomName(String name) {
        return (root, query, criteriaBuilder) -> {
            if(name == null) return null;
            return criteriaBuilder.equal(root.get("id"), name);
        };
    }

    static Specification<Room> byRoomId(String id) {
        return (root, query, criteriaBuilder) -> {
            if(id == null) return null;
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

}
