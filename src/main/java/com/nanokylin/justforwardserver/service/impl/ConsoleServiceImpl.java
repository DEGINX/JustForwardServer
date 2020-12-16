package com.nanokylin.justforwardserver.service.impl;

import com.nanokylin.justforwardserver.service.ConsoleService;

public class ConsoleServiceImpl implements ConsoleService {
    @Override
    public void execute(String command) {
        if (command.contains("help")) {
            System.out.println("假装这是一个help");
        }
    }
}
