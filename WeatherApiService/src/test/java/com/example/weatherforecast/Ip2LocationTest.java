package com.example.weatherforecast;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;

public class Ip2LocationTest {
    private final String DB_PATH = "ip2location/IP2LOCATION-LITE-DB3.BIN";
    private final IP2Location location = new IP2Location();

    @Test
    public void testInvalidIpAddress() throws IOException {
        String ipAddress = "abc";
        location.Open(DB_PATH);
        IPResult result = location.IPQuery(ipAddress);

        assertThat(result.getStatus()).isEqualTo("INVALID_IP_ADDRESS");

        System.out.println(result);
    }

    @Test
    public void testValidIpAddress() throws IOException {
        String ipAddress = "116.110.7.198";
        location.Open(DB_PATH);
        IPResult result = location.IPQuery(ipAddress);

        assertThat(result.getStatus()).isEqualTo("OK");
        assertThat(result.getCity()).isEqualTo("Da Nang");

        System.out.println(result);
    }
}
