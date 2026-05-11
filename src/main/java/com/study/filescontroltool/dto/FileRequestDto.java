package com.study.filescontroltool.dto;

import java.time.LocalDate;

public record FileRequestDto(String customer, String type, LocalDate date) {}
