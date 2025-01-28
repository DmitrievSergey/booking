package com.example.bookingservice.repository;

import com.example.bookingservice.dto.filter.HotelFilter;
import com.example.bookingservice.model.Hotel;
import org.springframework.data.jpa.domain.Specification;

public interface HotelSpecification {
    static Specification<Hotel> withFilter(HotelFilter filter){
        return Specification.where(
                        byHotelId(filter.getId()))
                .and(byHotelName(filter.getName()))
                .and(byHotelTitle(filter.getTitle()))
                .and(byHotelTown(filter.getTown()))
                .and(byHotelAddress(filter.getAddress()))
                .and(byHotelDistance(filter.getDistance()))
                .and(byHotelRating(filter.getRating()))
                .and(byHotelNumberOfRating(filter.getNumberOfRating()));
    }

    static Specification<Hotel> byHotelNumberOfRating(Integer numberOfRating) {
        return (root, query, criteriaBuilder) -> {
            if(numberOfRating == null) return null;
            return criteriaBuilder.greaterThanOrEqualTo(root.get("numberOfRating"), numberOfRating);
        };
    }

    static Specification<Hotel> byHotelRating(Float rating) {
        return (root, query, criteriaBuilder) -> {
            if(rating == null) return null;

            return criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), rating);
        };
    }

    static Specification<Hotel> byHotelDistance(String distance) {
        return (root, query, criteriaBuilder) -> {
            if(distance == null) return null;
            return criteriaBuilder.equal(root.get("distance"), distance);
        };
    }

    static Specification<Hotel> byHotelAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if(address == null) return null;
            return criteriaBuilder.equal(root.get("address"), address);
        };
    }

    static Specification<Hotel> byHotelTown(String town) {
        return (root, query, criteriaBuilder) -> {
            if(town == null) return null;
            return criteriaBuilder.equal(root.get("town"), town);
        };
    }

    static Specification<Hotel> byHotelTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if(title == null) return null;
            return criteriaBuilder.equal(root.get("title"), title);
        };
    }

    static Specification<Hotel> byHotelName(String name) {
        return (root, query, criteriaBuilder) -> {
            if(name == null) return null;
            return criteriaBuilder.equal(root.get("name"), name);
        };
    }

    static Specification<Hotel> byHotelId(String id) {
        return (root, query, criteriaBuilder) -> {
            if(id == null) return null;
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }
}
