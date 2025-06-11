package foxiwhitee.hellmod.utils.localization;

public enum LocalizationFiles {
    UA_LANG("uk_UA", "UnlocalizedNamesUa"),
    EN_LANG("en_US", "UnlocalizedNamesEn");

    public String filename;
    public String outfilename;
    LocalizationFiles(String filename, String outfilename) {
        this.filename = filename;
        this.outfilename = outfilename;
    }
}
