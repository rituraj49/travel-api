package com.jamuara.crs.flight.mapper;

import com.amadeus.resources.FlightOrder;
import com.jamuara.crs.config.CentralMapperConfig;
import com.jamuara.crs.flight.dto.TravelerRequestDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = CentralMapperConfig.class)
public interface AmadeusFlightTravelerRequestMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "firstName", target = "name.firstName")
    @Mapping(source = "lastName", target = "name.lastName")
    @Mapping(source = "phones", target = "contact.phones")
    @Mapping(source = "documents", target = "documents")
    FlightOrder.Traveler toTraveler(TravelerRequestDto traveler);

    @InheritInverseConfiguration
    TravelerRequestDto toTravelerDto(FlightOrder.Traveler traveler);

    List<FlightOrder.Traveler> toTravelerList(List<TravelerRequestDto> travelers);

    List<TravelerRequestDto> toTravelerDtoList(List<FlightOrder.Traveler> travelers);

    FlightOrder.Phone toPhone(TravelerRequestDto.Phone phone);
    TravelerRequestDto.Phone toPhoneDto(FlightOrder.Phone phone);

    FlightOrder.Document toDocument(TravelerRequestDto.IdentityDocument document);
    TravelerRequestDto.IdentityDocument toDocumentDto(FlightOrder.Document document);

    default FlightOrder.Phone.DeviceType toDeviceType(TravelerRequestDto.DeviceType dtoType) {
        return FlightOrder.Phone.DeviceType.valueOf(dtoType.toString().toUpperCase());
    }

    default TravelerRequestDto.DeviceType toDtoType(FlightOrder.Phone.DeviceType deviceType) {
        return TravelerRequestDto.DeviceType.valueOf(deviceType.toString().toUpperCase());
    }

    default FlightOrder.Document.DocumentType toDocumentType(TravelerRequestDto.DocumentType dtoType) {
        return FlightOrder.Document.DocumentType.valueOf(dtoType.toString().toUpperCase());
    }

    default TravelerRequestDto.DocumentType toDtoType(FlightOrder.Document.DocumentType documentType) {
        return TravelerRequestDto.DocumentType.valueOf(documentType.toString().toUpperCase());
    }

    @Named("mapFirstName")
    default String mapFirstName(FlightOrder.Name name) {
        return name == null ? null : name.getFirstName();
    }

    @Named("mapLastName")
    default String mapLastName(FlightOrder.Name name) {
        return name == null ? null : name.getLastName();
    }
}
