package com.reachout.ReachoutSystem.archive.entity;

public enum ArchiveContext {
    ADVERTISEMENT("advertisements"),
    USER("users"),
    ESTABLISHMENT("establishments");

    private final String folder;

    ArchiveContext(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }
}
