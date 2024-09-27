package com.example.blackbirdlofi.controller;

import com.example.blackbirdlofi.JPAentity.InstrumentItem;
import com.example.blackbirdlofi.service.InstrumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sound.midi.Instrument;
import java.util.List;

@Controller
public class InstrumentController {

    @Autowired
    private InstrumentService instrumentService;

    // 업로드 폼 표시
    @GetMapping("/uploadForm")
    public String showUploadForm(Model model) {
        System.err.println("===============InstrumentController 작동================");
        // 악기 리스트 가져오기
        List<Instrument> instruments = instrumentService.getAllInstruments();
        model.addAttribute("instruments", instruments);
        System.err.println("Instrument list: " + instruments);
        return "uploadForm";  // JSP 또는 HTML 파일 이름
    }

    // Ajax 요청으로 항목 리스트 제공
    @GetMapping("/getInstrumentItems")
    @ResponseBody
    public List<InstrumentItem> getInstrumentItems(@RequestParam("instrumentId") int instrumentId) {
        return instrumentService.getInstrumentItemsByInstrumentId(instrumentId);  // itemsId로 수정
    }
}
