package com.api.utils;

import com.api.pojo.UserCredentials;

public enum Role {

    FRONT_DESK("iamfd", "password"),
    SUPERVISOR("iamsup", "password"),
    ENGINEER("iameng", "password"),
    QA("iamqa", "password");

    private final String username;
    private final String password;

    Role(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserCredentials getUserCredentials() {
        return new UserCredentials(username, password);
    }

    public String getDisplayName() {
        switch (this) {
            case FRONT_DESK:
                return "Front Desk";
            case SUPERVISOR:
                return "Supervisor";
            case ENGINEER:
                return "Engineer";
            case QA:
                return "Quality Assurance";
            default:
                return this.name();
        }
    }

    public static Role fromString(String roleString) {
        if (roleString == null || roleString.trim().isEmpty()) {
            throw new IllegalArgumentException("Role string cannot be null or empty");
        }

        String normalizedRole = roleString.trim().toUpperCase();

        try {
            return Role.valueOf(normalizedRole);
        } catch (IllegalArgumentException e) {
            switch (normalizedRole) {
                case "FD":
                case "FRONTDESK":
                case "FRONT_DESK":
                    return FRONT_DESK;
                case "SUP":
                case "SUPER":
                case "SUPERVISOR":
                    return SUPERVISOR;
                case "ENG":
                case "ENGINEER":
                    return ENGINEER;
                case "QA":
                case "QUALITY":
                case "TESTER":
                    return QA;
                default:
                    throw new IllegalArgumentException(
                            "Invalid role: " + roleString +
                                    ". Valid roles are: FD, SUP, ENG, QA");
            }
        }
    }

    @Override
    public String toString() {
        return getDisplayName();
    }
}
