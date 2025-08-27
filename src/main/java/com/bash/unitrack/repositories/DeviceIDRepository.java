package com.bash.unitrack.repositories;

import com.bash.unitrack.data.models.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public interface DeviceIDRepository extends JpaRepository<Device, Long> {

    public Optional<Device> findDeviceByDeviceID(String deviceID);

}
