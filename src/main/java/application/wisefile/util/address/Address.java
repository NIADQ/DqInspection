package application.wisefile.util.address;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Address {

    private String siDo = "";
    private String siGoonGoo = "";
    private String eupMyunDong = "";
    private String ri = "";
    private String jibunMain = "";
    private String jibunSub = "";

    private String doro = "";
    private String buildMain = "";
    private String buildSub = "";
    private String dong = "";
    private String ho = "";
    private String etc = "";


    public boolean isRoad() {
        return !doro.equals("") || doro.length() > 2;
    }

    public boolean isEupMyun() {
        if (eupMyunDong.matches("[가-힣]*[읍면]"))
            return true;
        else
            return false;
    }

}
