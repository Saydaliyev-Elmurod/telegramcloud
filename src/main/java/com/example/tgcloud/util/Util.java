package com.example.tgcloud.util;

import com.example.tgcloud.dto.UserDTO;
import com.example.tgcloud.enums.SortType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.text.DecimalFormat;

public class Util {
    @Value("${pageable.size}")
    private static Integer size = 8;

    public static Pageable pageableDocument(UserDTO dto) {
        SortType type = dto.getSortType();
        Sort sort = null;
        switch (type) {
            case DATE_ASC -> sort = Sort.by(Sort.Direction.ASC, "createdDate");
            case DATE_DESC -> sort = Sort.by(Sort.Direction.DESC, "createdDate");
            case NAME_ASC -> sort = Sort.by(Sort.Direction.ASC, "name");
            case NAME_DESC -> sort = Sort.by(Sort.Direction.DESC, "name");
            case SIZE_ASC -> sort = Sort.by(Sort.Direction.ASC, "size");
            case SIZE_DESC -> sort = Sort.by(Sort.Direction.DESC, "size");
        }
        return PageRequest.of(dto.getCurrentDecimalDoc() - 1, size, sort);
    }

    public static String getFolder(String text) {
        text = text.substring(0, text.length() - 1);
        return text.substring(text.lastIndexOf("/") + 1);
    }

    public static String duration(Integer duration) {
        if (duration < 3600) {
            return duration / 60 + ":" + duration % 60;
        } else {
            return duration / 3600 + ":" + (duration / 60 - (duration / 3600) * 60) + ":" + duration % 60;
        }
    }

    public static String fileSize(Long fileSizeBayt) {
        DecimalFormat decfor = new DecimalFormat("0.00");

        Double d = Double.valueOf(fileSizeBayt);
        if (fileSizeBayt < 1024) {
            return fileSizeBayt + " bayt";
        } else if (fileSizeBayt < 1024 * 1024) {
            return decfor.format(d / 1024) + " KB";
        } else if (fileSizeBayt < 1024 * 1024 * 1024) {
            return decfor.format(d / 1024 / 1024) + " MB";
        } else if (fileSizeBayt < 1024 * 1024 * 1024 * 1024) {
            return decfor.format(d / 1024 / 1024 / 1024) + " GB";
        } else {
            return String.valueOf(fileSizeBayt);
        }
    }

    public static String getAdv(String caption) {
        StringBuilder builder = new StringBuilder();
        String[] lines = caption.split("\n");
        for (String line : lines) {
            if (!(line.contains("@") || line.contains("t.me"))) {
                builder.append(line);
            }
        }
        return builder.toString();
    }
}
