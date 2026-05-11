package com.study.filescontroltool.controller;

import com.study.filescontroltool.model.FileMetadata;
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

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/files")
@RequiredArgsConstructor
@Slf4j
public class FileController {
    private final FileService fileService;

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("files", fileService.getAllFiles());
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

    @PostMapping("/replace")
    public String processFileReplace(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            fileService.replaceFile(file);
            redirectAttributes.addFlashAttribute("message", "Файл замінено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Помилка: " + e.getMessage());
        }
        return "redirect:/files";
    }

    @PostMapping("/delete/{name}")
    public String deleteFile(@PathVariable String name, RedirectAttributes redirectAttributes) {
        try {
            fileService.deleteFile(name);
            redirectAttributes.addFlashAttribute("message", "Файл видалено");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Не вдалося видалити");
        }
        return "redirect:/files";
    }

    @GetMapping("/api/filter/date/{date}")
    @ResponseBody
    public List<FileMetadata> getFilesByDate(@PathVariable String date) {
        return fileService.getFilesByDate(LocalDate.parse(date));
    }

    @GetMapping("/api/filter/customer/{customer}")
    @ResponseBody
    public List<FileMetadata> getFilesByCustomer(@PathVariable String customer) {
        return fileService.getFilesByCustomer(customer);
    }

    @GetMapping("/api/filter/type/{type}")
    @ResponseBody
    public List<FileMetadata> getFilesByType(@PathVariable String type) {
        return fileService.getFilesByType(type);
    }
}
