package application.wisefile.util.address;

import java.util.HashMap;
import java.util.StringTokenizer;

public class AddressToken {

    private String siDo = "";
    private String siGoon = "";
    private String goo = "";
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

    @Override
    public String toString() {
        return "AddressTokenizer [siDo=" + siDo + ", siGoon=" + siGoon + ", goo=" + goo + ", eupMyunDong=" + eupMyunDong
                + ", ri=" + ri + ", jibunMain=" + jibunMain + ", jibunSub=" + jibunSub + ", doro=" + doro
                + ", buildMain=" + buildMain + ", buildSub=" + buildSub + ", dong=" + dong + ", ho=" + ho + ", etc="
                + etc + "]";
    }


    public Address createVO(String address) {

        StringTokenizer token = new StringTokenizer(address, " ");

        while(token.hasMoreTokens()) {
            String unit = token.nextToken();

            findSiDo(unit);

            findSiGoon(unit);

            findGoo(unit);

            findEupMyunDong(unit);

            findRi(unit);

            findJibun(unit);

            findDoro(unit);

            findBuild(unit);
            findDong(unit);
            findHo(unit);


        }

        return Address.builder()
                .siDo(siDo)
                .siGoonGoo(siGoon + " " + goo.trim())
                .eupMyunDong(eupMyunDong).ri(ri)
                .jibunMain(jibunMain)
                .jibunSub(jibunSub)
                .doro(doro)
                .buildMain(buildMain)
                .buildSub(buildSub)
                .dong(dong)
                .ho(ho)
                .etc(etc.trim())
                .build();
    }

    private void findSiDo(String unit) {
        HashMap<String, String> doMap = new HashMap<String, String>();

        doMap.put("서울", "서울특별시");		doMap.put("서울시", "서울특별시");
        doMap.put("강원", "강원도");
        doMap.put("경기", "경기도");
        doMap.put("경남", "경상남도");
        doMap.put("경북", "경상북도");
        doMap.put("광주", "광주광역시");
        doMap.put("대구", "대구광역시");
        doMap.put("대전", "대전광역시;");		doMap.put("대전시", "대전광역시;");
        doMap.put("부산", "부산광역시");		doMap.put("부산시", "부산광역시");
        doMap.put("세종", "세종특별자치시");	doMap.put("세종시", "세종특별자치시");	doMap.put("세종특별시", "세종특별자치시");
        doMap.put("울산", "울산광역시");		doMap.put("울산시", "울산광역시");
        doMap.put("인천", "인천광역시"); 		doMap.put("인천시", "인천광역시");
        doMap.put("전남", "전라남도");
        doMap.put("전북", "전라북도");
        doMap.put("제주", "제주특별자치도");	doMap.put("제주도", "제주특별자치도");
        doMap.put("충남", "충청남도");
        doMap.put("충북", "충청북도");

        if(doMap.containsValue(unit))
            siDo = unit;
        else if(doMap.containsKey(unit))
            siDo = doMap.get(unit);

        else if(unit.equals("광주시")) {
            if(siDo.equals("경기도"))
                siGoon = "광주시";
            else
                siDo = "광주광역시";
        }
    }

    private void findSiGoon(String unit) {
        /* 시 군의 정규 표현식으로 시 군 구분 */
        if (unit.matches("[가-힣]*[시군]"))
            siGoon = unit;
    }

    private void findGoo(String unit) {
        if (unit.matches("[가-힣]*[구]"))
            goo = unit;
    }

    private void findRi(String unit) {
        if (unit.matches("[가-힣]*리"))
            ri = unit;
    }

    private void findEupMyunDong(String unit) {
        if (unit.matches("[가-힣0-9]*[읍면동가]"))
            eupMyunDong = unit;
    }

    private void findDoro(String unit) {
        if (unit.matches("[가-힣0-9]*[로길]"))
            doro = unit;
    }

    private void findDong(String unit) {
        if (unit.matches("[0-9]*동"))
            dong = unit;
    }

    private void findHo(String unit) {
        if (unit.matches("[0-9]*호"))
            ho = unit;
    }

    private void findEtc(String unit) {
        if(etc.length() < 1)
            etc = unit;
        else
            etc = etc + " " + unit;
    }

    private void findJibun(String unit) {
        if(!unit.matches("[0-9]+?-?[0-9]*?[번지]*")) return;

        String tokenArr[] = unit.split("-");

        if(tokenArr.length != 2) {
            if(tokenArr[0].matches("[0-9]*")) {
                jibunMain = tokenArr[0];
                jibunSub = "0";
            }

            else {
                StringBuilder builder = new StringBuilder();

                for(int i=0; i<tokenArr[0].length(); i++) {
                    if(tokenArr[0].charAt(i) >= '0' && tokenArr[0].charAt(i) <= '9')
                        builder.append(tokenArr[0].charAt(i));
                    else break;
                }

                jibunMain = builder.toString();
                jibunSub = "0";
            }


        }

		else if(tokenArr.length == 2) {
        jibunMain = tokenArr[0];

        if(jibunSub.matches("[0-9]*"))
            jibunSub = tokenArr[1];
        else {
            StringBuilder builder = new StringBuilder();

            for(int i=0; i<tokenArr[1].length(); i++) {
                if(tokenArr[1].charAt(i) >= '0' && tokenArr[1].charAt(i) <= '9')
                    builder.append(tokenArr[1].charAt(i));
                else break;
            }

            jibunSub = builder.toString();
        }
    }

}

    private void findBuild(String unit) {
        if(!unit.matches(	"[0-9]+?-?[0-9]*?[번지]*")) return;

        String tokenArr[] = unit.split("-");

        if(tokenArr.length != 2) {
            if(tokenArr[0].matches("[0-9]*")) {
                buildMain = tokenArr[0];
                buildSub = "0";
            }

            else {
                StringBuilder builder = new StringBuilder();

                for(int i=0; i<tokenArr[0].length(); i++) {
                    if(tokenArr[0].charAt(i) >= '0' && tokenArr[0].charAt(i) <= '9')
                        builder.append(tokenArr[0].charAt(i));
                    else break;
                }

                buildMain = builder.toString();
                buildSub = "0";
            }
        }

        else if(tokenArr.length == 2) {
            buildMain = tokenArr[0];

            if(buildSub.matches("[0-9]*")) buildSub = tokenArr[1];

            else {
                StringBuilder builder = new StringBuilder();

                for(int i=0; i<tokenArr[1].length(); i++) {
                    if(tokenArr[1].charAt(i) >= '0' && tokenArr[1].charAt(i) <= '9')
                        builder.append(tokenArr[1].charAt(i));
                    else break;
                }

                buildSub = builder.toString();
            }
        }
    }

}
