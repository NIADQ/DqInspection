package application.wisefile.vo;

import lombok.Data;

@Data
public class DiagnosisDataVO {

    private String col1;
    private String col2;
    private String col3;
    private String col4;
    private String col5;
    private String col6;
    private String col7;
    private String col8;
    private String col9;
    private String col10;
    private String col11;
    private String col12;
    private String col13;
    private String col14;
    private String col15;
    private String col16;
    private String col17;
    private String col18;
    private String col19;
    private String col20;
    private String col21;
    private String col22;
    private String col23;
    private String col24;
    private String col25;
    private String col26;
    private String col27;
    private String col28;
    private String col29;
    private String col30;
    private String col31;
    private String col32;
    private String col33;
    private String col34;
    private String col35;
    private String col36;
    private String col37;
    private String col38;
    private String col39;
    private String col40;
    private String col41;
    private String col42;
    private String col43;
    private String col44;
    private String col45;
    private String col46;
    private String col47;
    private String col48;
    private String col49;
    private String col50;
    private String col51;
    private String col52;
    private String col53;
    private String col54;
    private String col55;
    private String col56;
    private String col57;
    private String col58;
    private String col59;
    private String col60;
    private String col61;
    private String col62;
    private String col63;
    private String col64;
    private String col65;
    private String col66;
    private String col67;
    private String col68;
    private String col69;
    private String col70;
    private String col71;
    private String col72;
    private String col73;
    private String col74;
    private String col75;
    private String col76;
    private String col77;
    private String col78;
    private String col79;
    private String col80;
    private String col81;
    private String col82;
    private String col83;
    private String col84;
    private String col85;
    private String col86;
    private String col87;
    private String col88;
    private String col89;
    private String col90;
    private String col91;
    private String col92;
    private String col93;
    private String col94;
    private String col95;
    private String col96;
    private String col97;
    private String col98;
    private String col99;
    private String col100;

    private Long id;
    private Long historyId;
    private String fileId;
    private String dupDelYn = "N";

    @Override
    public String toString() {
        return
         col1 +col2 +col3 +col4 +col5 +col6 +col7 +col8 +col9 +col10
        +col11 +col12 +col13 +col14 +col15 +col16 +col17 +col18 +col19 +col20
        +col21 +col22 +col23 +col24 +col25 +col26 +col27 +col28 +col29 +col30
        +col31 +col32 +col33 +col34 +col35 +col36 +col37 +col38 +col39 +col40
        +col41 +col42 +col43 +col44 +col45 +col46 +col47 +col48 +col49 +col50
        +col51 +col52 +col53 +col54 +col55 +col56 +col57 +col58 +col59 +col60
        +col61 +col62 +col63 +col64 +col65 +col66 +col67 +col68 +col69 +col70
        +col71 +col72 +col73 +col74 +col75 +col76 +col77 +col78 +col79 +col80
        +col81 +col82 +col83 +col84 +col85 +col86 +col87 +col88 +col89 +col90
        +col91 +col92 +col93 +col94 +col95 +col96 +col97 +col98 +col99 +col100;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DiagnosisDataVO && this.toString().contains(o.toString())) {
            return true;
        }
        else {
            return false;
        }
    }
}
