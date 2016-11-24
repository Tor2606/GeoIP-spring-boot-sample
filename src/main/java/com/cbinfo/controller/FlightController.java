package com.cbinfo.controller;

import com.cbinfo.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FlightController {
    @Autowired
    private FlightService flightService;

}
