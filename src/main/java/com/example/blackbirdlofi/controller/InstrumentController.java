package com.example.blackbirdlofi.controller;

import com.example.blackbirdlofi.JPAentity.InstrumentItem;
import com.example.blackbirdlofi.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;

    // Ajax 요청으로 항목 리스트 제공
    @GetMapping("/getInstrumentItems")
    @ResponseBody
    public List<InstrumentItem> getInstrumentItems(@RequestParam("instrumentId") int instrumentId) {
        System.err.println("======================getInstrumentItems 작동=======================");
        // 악기 ID로 세부 항목을 가져옴
        return instrumentService.getInstrumentItemsByInstrumentId(instrumentId);
    }
}
