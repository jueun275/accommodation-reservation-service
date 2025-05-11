package com.example.reservation.room;

import com.example.reservation.room.dto.RoomRequestDto;
import com.example.reservation.room.dto.RoomResponseDto;
import com.example.reservation.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @PostMapping
  public ResponseEntity<Long> registerRoom(@RequestBody RoomRequestDto dto) {
    Long roomId = roomService.registerRoom(dto);
    URI location = URI.create("/api/rooms/" + roomId);

    return ResponseEntity
        .created(location)
        .body(roomId);
  }

  @PostMapping("/saveAll")
  public ResponseEntity<List<Long>> registerRooms(@RequestBody List<RoomRequestDto> dtos) {
    List<Long> roomIds = roomService.registerRooms(dtos);
    return ResponseEntity.ok(roomIds);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Void> updateRoom(@PathVariable Long id, @RequestBody RoomRequestDto dto) {
    roomService.updateRoom(id, dto);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<RoomResponseDto> getRoom(@PathVariable Long id) {
    RoomResponseDto room = roomService.getRoom(id);
    return ResponseEntity.ok(room);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
    roomService.deleteRoom(id);
    return ResponseEntity.noContent().build();
  }
}
