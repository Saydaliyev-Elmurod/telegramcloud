package com.example.tgcloud.util;

import com.example.tgcloud.dto.DocumentDTO;
import com.example.tgcloud.enums.FileType;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class ButtonUtil {
    public InlineKeyboardMarkup replyButton(List<DocumentDTO> list, boolean haveNext, boolean havePrev,boolean back) {
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row0 = new ArrayList<>();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        if (back){
            row0.add(button("Back ⬆️","back"));
        }
        row0.add(button("Sort \uD83D\uDCC6", "date"));
        row0.add(button("Sort \uD83C\uDD70️", "name"));
        row0.add(button("Sort Size", "size"));
        rowList.add(row0);

        for (int i = 0; i < list.size(); i++) {
            Integer id = list.get(i).getId();
            FileType type = list.get(i).getType();
            DocumentDTO dto = list.get(i);
            switch (type) {
                case VIDEO -> rowList.add(row(button("\uD83C\uDF9E " + dto.getName(), "d/" + id)));
                case AUDIO -> rowList.add(row(button("\uD83C\uDFA7 " + dto.getName(), "d/" + id)));
                case DOCUMENT -> rowList.add(row(button("\uD83D\uDCDA " + dto.getName(), "d/" + id)));
                case PHOTO -> rowList.add(row(button("\uD83C\uDF05 " + dto.getName(), "d/" + id)));
                case FOLDER -> rowList.add(row(button("\uD83D\uDDC2 " + dto.getName(), "f/" + id)));
            }
        }

        if (havePrev) {
            row2.add(button("◀️", "prev"));
        }
        row2.add(button("➕", "createFolder"));
        row2.add(queryButton("\uD83D\uDD0E"));
        if (haveNext) {
            row2.add(button("▶️", "next"));
        }
        rowList.add(row2);
        return keyboard(rowList);
    }


    private InlineKeyboardButton button(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }
    private InlineKeyboardButton queryButton(String text) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setSwitchInlineQueryCurrentChat("");
        return button;
    }


    private List<InlineKeyboardButton> row(InlineKeyboardButton... inlineKeyboardButtons) {
        List<InlineKeyboardButton> row = new LinkedList<>();
        row.addAll(Arrays.asList(inlineKeyboardButtons));
        return row;
    }


    private List<List<InlineKeyboardButton>> rowList(List<InlineKeyboardButton>... rows) {
        List<List<InlineKeyboardButton>> collection = new LinkedList<>();
        collection.addAll(Arrays.asList(rows));
        return collection;
    }


    private InlineKeyboardMarkup keyboard(List<List<InlineKeyboardButton>> collection) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        keyboardMarkup.setKeyboard(collection);
        return keyboardMarkup;
    }

    public InlineKeyboardMarkup replyDocument(Integer id) {
        return keyboard(rowList(row(button("Delete", "delete/" + id),
                button("Rename", "rename/" + id))));
    }


}
