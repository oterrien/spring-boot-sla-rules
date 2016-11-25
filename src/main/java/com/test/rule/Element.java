package com.test.rule;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Element<T> {

    private final T bean;

    private final List<String> rejectionCodes = new ArrayList<>();

    private Status status = Status.ACCEPTED;

    enum Status {
        ACCEPTED, REJECTED;
    }
}
