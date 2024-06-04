package com.figstreet.core;

import java.util.ArrayList;
import java.util.List;

public class ActionResult {
    private List<String> fOutputMessages;
    private List<String> fErrorMessages;

    public ActionResult()
    {
        this.fOutputMessages = new ArrayList<>();
        this.fErrorMessages = new ArrayList<>();
    }

    public ActionResult(String pErrorMessage)
    {
        this.fOutputMessages = new ArrayList<>();

        this.fErrorMessages = new ArrayList<>();
        this.fErrorMessages.add(pErrorMessage);
    }

    public boolean isActionAllowed()
    {
        return this.fErrorMessages.isEmpty();
    }

    public void appendError(String pErrorMessage)
    {
        this.fErrorMessages.add(pErrorMessage);
    }

    public List<String> getErrorMessages()
    {
        return this.fErrorMessages;
    }

    public String getErrorMessagesAsString() {
        return FormatUtil.comcatenate(this.fErrorMessages, ", ");
    }

    public void appendOutput(String pMessage)
    {
        this.fOutputMessages.add(pMessage);
    }

    public List<String> getOutputMessages()
    {
        return this.fOutputMessages;
    }

}
