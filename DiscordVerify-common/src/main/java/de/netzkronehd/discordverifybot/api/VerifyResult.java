package de.netzkronehd.discordverifybot.api;

public enum VerifyResult {

    SUCCESS,
    FAILED,
    CANCELLED;

    VerifyResult() {}

    public boolean isSucceed() {
        return this.equals(SUCCESS);
    }

}
