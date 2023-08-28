import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

public class ticketKiosk {
    public static void main(String[] args) {
        // 식권 발매기 프로그램
        /*
            1) 관리자 2)사용자
            * 관리자 ---> 1) 식권충전    2)잔돈충전    3)뒤로가기
            * 사용자 ---> 1) 구입  ---> 입금 금액 입력 ---> 구매 매수 입력 ---> 잔돈 출력
                         2) 뒤로가기
              조건 ) 예시> 잔돈이 7600원. 5000원권이 없으면 1000원짜리 7장 출력(최소 화폐 단위 적용)

            #화면
            식권 가격 : 3200원
            식권 : ??매(매진시 매진)
            50000원:??매   10000원:??매   5000원:??매
            1000원 :??매   500원:??개     100원:??개
        */

        Scanner scan=new Scanner(System.in);

        int[] money={50000,10000,5000,1000,500,100};
        int[] charges={1,1,1,1,5,10};

        int tickets=5;
        final int PRICE=3200;

        // 저장, 로드 관련
        String fileName="kiosk.txt";    // 잔돈 및 티켓 수 저장 및 로드
        String border=System.getProperty("file.separator");
        String userHome=System.getProperty("user.home");
        String path=userHome+border+"Desktop"+border+"warehouse"+border+fileName;
        File file=new File(path);

        // 파일 로드
        if(file.exists()){
            try{
                FileReader fr=new FileReader(path);
                BufferedReader br=new BufferedReader(fr);

                String str="";
                while(br.ready()){
                    str+=br.readLine();
                }

                String[] div01=str.split("식권");
                tickets=Integer.parseInt(div01[0]);

                String[] div02=div01[1].split("/");

                for(int i=0; i<div02.length; i++){
                    String[] div03=div02[i].split(":");
                    money[i]=Integer.parseInt(div03[0]);
                    charges[i]=Integer.parseInt(div03[1]);
                }

                fr.close();
                br.close();
                System.out.println("데이터 로드 성공");
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("데이터 로드 실패");
            }
        }

        System.out.printf("\n식권 : %d매",tickets);
        System.out.printf("\n%d원 : %d, %d원 : %d ,%d원 : %d",money[0],charges[0],money[1],charges[1],money[2],charges[2]);
        System.out.printf("\n%d원 : %d, %d원 : %d ,%d원 : %d\n",money[3],charges[3],money[4],charges[4],money[5],charges[5]);

        while(true){
            // 파일 저장
            try{
                FileWriter fw=new FileWriter(path);

                String str="";
                str+=tickets+"식권";
                for(int i=0; i<money.length; i++){
                    str+=money[i]+":"+charges[i]+"/";
                }

                fw.write(str);
                fw.close();
                System.out.println("데이터 저장 성공");
            }catch(Exception e){
                System.err.println("데이터 저장 실패");
            }


            System.out.println("\n1)관리자 | 2)사용자");

            System.out.print("메뉴 선택>>");
            int sel=scan.nextInt();

            if(sel==1){
                while(true){
                    // 관리자 화면
                    System.out.println("\n1)식권충전 | 2)잔돈충전 | 3)뒤로가기");
                    System.out.print("메뉴 선택>>");
                    int choice=scan.nextInt();

                    if(choice==1){
                        System.out.printf("\n식권 : %d매",tickets);

                        System.out.print("\n식권 재고를 넣어주세요>>");
                        int input=scan.nextInt();

                        if(input<0){
                            continue;
                        }else{
                            tickets+=input;
                        }

                        System.out.println("식권이 충전되었습니다.");
                        System.out.printf("식권 : %d매\n",tickets);
                    }else if(choice==2){
                        boolean charging=true;

                        while(charging){
                            // 메뉴 출력
                            for(int i=0; i<money.length; i++){
                                System.out.printf("%d) %s", i+1, money[i]);
                                if(i<money.length-1){
                                    System.out.print(" | ");
                                }
                            }

                            System.out.print("\n현금 카트리지를 선택해주세요.>>");
                            int input=scan.nextInt();

                            if(input<1 || input>6){
                                System.out.println("존재하지 않는 카트리지입니다.\n");
                                continue;
                            }else{
                                System.out.print("현금 충전 : 수량을 입력하세요 >>");
                                int amount=scan.nextInt();

                                if(amount<1) {
                                    System.err.println("ERROR : NOT VALID");
                                    continue;
                                }else{
                                    charges[input-1]+=amount;
                                }
                            }

                            // 확인용 표시
                            System.out.printf("\n식권 : %d매",tickets);
                            System.out.printf("\n%d원 : %d, %d원 : %d ,%d원 : %d",money[0],charges[0],money[1],charges[1],money[2],charges[2]);
                            System.out.printf("\n%d원 : %d, %d원 : %d ,%d원 : %d\n",money[3],charges[3],money[4],charges[4],money[5],charges[5]);

                            // 지속여부 결정
                            System.out.println("1)지속 : 아무키 | 2) 종료 : x");
                            System.out.print("충전을 계속하시겠습니까? >> ");
                            String cont=scan.next();

                            if(cont.equals("x")){
                                System.out.println("잔돈 충전을 종료합니다.");
                                charging=false;
                            }
                        }
                    }else if(choice==3){
                        break;
                    }else{
                        System.out.println("없는 메뉴입니다.");
                    }
                }
            }else if(sel==2){
                while(true){
                    System.out.println("\n1)구입 | 2)뒤로가기");

                    System.out.print("메뉴 선택>>");
                    int choice=scan.nextInt();

                    if(choice==1){
                        boolean buying=true;

                        // 발매기 메인화면, 식권 구매
                        System.out.printf("\n오늘의 점심 : 왕돈까스\n가격 : %d",PRICE);
                        System.out.print("\n구매 매수를 입력해주세요 >>");
                        int quan=scan.nextInt();

                        // 식권 매수가 부족할 때 경고(입력이 양수가 아닐 때에는 에러 출력)
                        if(quan<=0){
                            System.err.println("ERROR : Not Valid");
                            continue;
                        }

                        if(quan>tickets){
                            System.out.println("죄송합니다. 매진입니다.");
                            System.out.printf("식권 : %d\n",tickets);
                            continue;
                        }

                        // 금액 투입
                        int amount=quan*PRICE;
                        int[] payment=new int[6];
                        int paying=0;

                        while(true){
                            // 금액 입력
                            int checkIdx=-1;

                            System.out.print("현금을 투입하세요>> ");
                            int input=scan.nextInt();

                            // 화폐단위의 돈은 받지 않음
                            for(int i=0; i<money.length; i++){
                                if(input==money[i]){
                                    checkIdx=i;
                                    payment[i]++;
                                    break;
                                }
                            }

                            if(checkIdx==-1){
                                System.err.println("ERROR : NOT VALID");
                                continue;
                            }else{
                                paying+=input;
                            }

                            System.out.println("지불할 금액 : "+amount);
                            System.out.println("투입된 금액 : "+paying);

                            if(amount<=paying){
                                break;
                            }
                        }

                        // 잔돈 체크
                        int idx=0;
                        int change=paying-amount;
                        boolean payingChange=true;

                        int[] chargesTemp=new int[6];
                        for(int i=0; i<chargesTemp.length; i++){
                            chargesTemp[i]=charges[i];
                        }
                        int[] chargesCount=new int[6];

                        while(true){
                            // 종료 조건 1
                            if(idx>5){
                                payingChange=false;
                                break;
                            }

                            int unit=money[idx];

                            if(unit>change){
                                idx++;
                                continue;
                            }else{
                                if(chargesTemp[idx]==0){
                                    idx++;
                                    continue;
                                }else{
                                    change=change-unit;
                                    chargesTemp[idx]--;
                                    chargesCount[idx]++;
                                }
                            }

                            // 종료 조건 2
                            if(change==0){
                                break;
                            }
                        }

                        // 잔돈 거스르기+식권 발매
                        if(payingChange){
                            System.out.println("식권이 발매되었습니다.");
                            tickets-=quan;

                            int changeAmount=0;
                            for(int i=0; i<chargesCount.length; i++){
                                changeAmount+=money[i]*chargesCount[i];
                            }
                            System.out.printf("거스름돈을 받아주세요 : %d\n",changeAmount);

                            for(int i=0; i<charges.length; i++){
                                charges[i]+=payment[i];
                                charges[i]-=chargesCount[i];
                            }
                        }else{
                            System.out.println("\n죄송합니다. 잔돈 재고가 부족합니다.");
                            System.out.println("판매가 중단됩니다.");
                        }

                        // 확인용 표시
                        System.out.printf("\n식권 : %d매",tickets);
                        System.out.printf("\n%d원 : %d, %d원 : %d ,%d원 : %d",money[0],charges[0],money[1],charges[1],money[2],charges[2]);
                        System.out.printf("\n%d원 : %d, %d원 : %d ,%d원 : %d\n",money[3],charges[3],money[4],charges[4],money[5],charges[5]);

                    }else if(choice==2){
                        break;
                    }
                }
            }
        }
    }
}
