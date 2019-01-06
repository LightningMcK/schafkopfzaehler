
package project.schafkopfzaehler_2.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class POJOClass {

    @SerializedName("safely executed")
    @Expose
    private Boolean safelyExecuted;
    @SerializedName("Karte")
    @Expose
    private String karte;

    public Boolean getSafelyExecuted() {
        return safelyExecuted;
    }

    public void setSafelyExecuted(Boolean safelyExecuted) {
        this.safelyExecuted = safelyExecuted;
    }

    public String getKarte() {
        return karte;
    }

    public void setKarte(String karte) {
        this.karte = karte;
    }

}
