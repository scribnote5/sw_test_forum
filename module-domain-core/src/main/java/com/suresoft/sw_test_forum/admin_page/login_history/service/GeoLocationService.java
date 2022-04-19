package com.suresoft.sw_test_forum.admin_page.login_history.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CityResponse;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
public class GeoLocationService {
    private final DatabaseReader databaseReader;

    public GeoLocationService(DatabaseReader databaseReader) {
        this.databaseReader = databaseReader;
    }

    public String getLocationByIp(String ip) {
        InetAddress ipAddress = null;
        CityResponse cityResponse;
        String location;

        try {
            ipAddress = InetAddress.getByName(ip);
            cityResponse = databaseReader.city(ipAddress);

            String continent = (cityResponse.getContinent() != null) ? cityResponse.getContinent().getName() : " No Data";
            String country = (cityResponse.getCountry() != null) ? cityResponse.getCountry().getName() : " No Data";
            String city = (cityResponse.getCity().getName() != null) ? cityResponse.getCity().getName() : " No Data";

            location = country + ": " + city;
        }
        // localhost에서 테스트하는 경우 예외처리
        catch (AddressNotFoundException e) {
            location = "localhost";
        } catch (Exception e) {
            location = "에러 발생: " + ipAddress;
        }

        return location;
    }
}