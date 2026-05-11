package com.study.filescontroltool.controller;

import com.study.filescontroltool.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final FileService fileService;

    @GetMapping
    public String mainPage() {
        return "index";
    }

    @PostMapping("/upload")
    public String processFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            fileService.uploadFile(file);
            redirectAttributes.addFlashAttribute("message", "Файл завантажено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка: " + e.getMessage());
        }
        return "redirect:/files";
    }

    @GetMapping("/api/content/{name}")
    @ResponseBody
    public String getFileContent(@PathVariable String name) {
        return fileService.getFileByName(name).getJsonContent();
    }
}
