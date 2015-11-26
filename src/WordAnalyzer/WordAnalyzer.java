package WordAnalyzer; 
 
 
 import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List; 
 
 



















import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.util.JSON;

 import twitter4j.*; 
import twitter4j.auth.AccessToken; 
import twitter4j.conf.*; 
import twitter4j.Query.*; 
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;
import java.lang.Math;
 public class WordAnalyzer { 
	 	static Komoran komoran = new Komoran("C:/Users/우빈/Desktop/java workspace/Capstone/lib/models-full");
		static MongoClient mongoClient = new MongoClient("localhost", 27017); // 몽고디비 연결
		static DB db = mongoClient.getDB("test"); // db 변수에 testDB DB에 연결
		static DBCollection wordCollection = db.getCollection("wordCollection"); // coll 변수에 testCol 컬렉션에 연결
		static DBCollection infintiveCollection = db.getCollection("infinitiveCollection"); 
		static String tempSL;
		static String tempSN;
		static String temp;
		static String tempNNGNNP;
		static boolean flagSL=false;
		static boolean flagSN=false;
		static boolean flagNNGNNP=false;
/*
		public static void checkSL(String inputText, Pair<String, String> pair){ //다음값없을시 스트링아웃바운드 익셉션 발생
				
				if (pair.toString().contains("SL") ){ // SL일경우에
					
					if(inputText.charAt(inputText.indexOf(pair.getFirst() ) +pair.getFirst().length())!=' '){// 그 다음 단어가 빈칸이 아니라면
						tempSL = pair.getFirst(); // 임시 스트링에 단어를 저장한다
						flagSL=true; //플래그를 트루로						
					}
					else{
						flagSL=false; // 빈칸이라면 플래그 false로
					}
				}
			}
		public static void combineSLWithSN(String inputText, Pair<String, String> pair){
			if(!pair.getSecond().equals("SN") && !pair.getSecond().equals("SL")) { //첫번째 이후 두번째 형태소가 SN도아니고 SL도아닐경우에는
			
					flagSL=false; // 플레그를 false로 바꿔주며
				}
				if(flagSL==true && pair.getSecond().equals("SN")){ //이전 형태소가 SL이었고 지금이 SN이라면
					temp=tempSL+pair.getFirst(); //두 단어를 합쳐주어 명사로 만든다.
				System.out.println("SL과 SN이 합쳐서 "+temp);
				}
		}
		
		public static void checkSN(String inputText, Pair<String, String> pair){ 
			
			if (pair.toString().contains("SN") ){ // SL일경우에
				
				if(inputText.charAt(inputText.indexOf(pair.getFirst() ) +pair.getFirst().length())!=' '){// 그 다음 단어가 빈칸이 아니라면
					tempSN = pair.getFirst(); // 임시 스트링에 단어를 저장한다
					flagSN=true; //플래그를 트루로							
				}
				else flagSN=false; // 빈칸이라면 플래그 false로
			}
		}
		public static void combineSNWithSL(String inputText, Pair<String, String> pair){
			if(!pair.getSecond().equals("SN") && !pair.getSecond().equals("SL")) {
					
					flagSN=false;
					
				}
				if(flagSN==true && pair.getSecond().equals("SL")){
					temp=tempSN+pair.getFirst();
				System.out.println("SN과 SL이 합쳐서 "+temp);
				}
		}
		*/

	
		
		public static void combineNN(List listA, List listB,List listC, Pair<String, String> pair,String inputText, HashMap<Integer, String> map){ //명사와 명사를 합성
		
			int i=1;	 //여러번 수행하기 위한 변수
			
			if(listB.get(listB.size()-i).equals("NNG")||listB.get(listB.size()-i).equals("NNP")){
			while(i<=5){ //최대 4개의 단어를 합성
				
				if(listB.get(listB.size()-i).equals("NNG")||listB.get(listB.size()-i).equals("NNP")){ //지금 형태소가 명사이고
					temp=(String) listA.get(listA.size()-1);
					if(listB.size()>i){
						
				if(listB.get(listB.size()-i-1).equals("NNG")||listB.get(listB.size()-i-1).equals("NNP")){ //이전 형태소가 명사라면

					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-i-1).toString())+ listA.get(listA.size()-i-1).toString().length())  != ' ' &&
							inputText.charAt(inputText.indexOf(listA.get(listA.size()-i-1).toString())+ listA.get(listA.size()-i-1).toString().length())  != '\n') {//그리고 이전형태소와 지금 형태소 사이에 빈칸 또는 줄바꿈 이 없다면
						
						i++;
	//					System.out.println("지금 문장은 "+ pair+ "i가 ++됐음 i는 "+i);
						
						} else
							break; // 빈칸있으면 break
					} else
						break; //이전 형태소가 명사가 아니면
				} else
					break; // 사이즈 만족안하면
			}
				}
				if(i>=2) { //합성조건이 만족된다면
				
			//	System.out.println("i는 "+i);
			//		if (i==3)temp = listA.get(listA.size()-2).toString()+ listA.get(listA.size()-1).toString(); //두단어 합성
				//	else if(i==5)temp =listA.get(listA.size()-4).toString()+ listA.get(listA.size()-2).toString()+ listA.get(listA.size()-1).toString(); //세단어 합성
					//else if (i==6)temp = listA.get(listA.size()-6).toString()+listA.get(listA.size()-4).toString()+listA.get(listA.size()-2).toString()+listA.get(listA.size()-1).toString(); //네단어 합성
				for(int j=1;j<i;j++){
					temp=listA.get(listA.size()-j-1).toString()+temp;
				}
				System.out.println("NN+ NN의 합쳐진 값은 " + temp); //명사가 합쳐졌다면
					listC.add(temp);			
					listC.remove(listC.size()-3);
					listC.remove(listC.size()-2); //명사의 중복저장을 막는 것
					
					map.put(map.size()-2, new String(temp));
					map.remove(map.size()-1);
					
				}
			}
					
				 //지금 이 경우 아이서울유 일때 아이서울도 복합명사 저장하고 아이서울유도 저장하고 있는 문제발생
		}
		
		
		public static void combineNX(List listA, List listB,List listC,Pair<String, String> pair, String inputText,HashMap<Integer, String> map){

			if(listB.get(listB.size()-1).toString().equals("XSN")){ //지금형태소가 X*이고 -> 접미사이고
				if(listB.size()>1){
				if(listB.get(listB.size()-2).toString().equals("NNG")||listB.get(listB.size()-2).toString().equals("NNP")){ //이전 형태소가 명사일때
					
					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
							&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //빈칸이 없다면
						
						temp = listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //둘의 합을 temp에 저장
						System.out.println("명사+접미사 합쳐서 "+ temp);
						listC.add(temp);
						listC.remove(listC.size()-2); //명사의 중복저장을 막기위한 제거
						map.put(map.size()-1, new String(temp));
						
					}
				}
			}
		}
	}
		public static void combineSNWithNN(List listA, List listB,List listC,Pair<String, String> pair, String inputText,HashMap<Integer, String> map){
		
			if(listB.get(listB.size()-1).toString().equals("NNG")||listB.get(listB.size()-1).toString().equals("NNP")){ //지금형태소가 명사이고
				if(listB.size()>1){
				if(listB.get(listB.size()-2).toString().equals("SN")){ //이전 형태소가 숫자일때 (SL과 SE등 다른거 추가안했음)
					
					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
							&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //빈칸이 없다면
						
						temp = listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //둘의 합을 temp에 저장
						System.out.println("숫자+명사 합쳐서 "+ temp);
						listC.add(temp);
						map.put(map.size()-1, new String(temp));
					}
				}
			}
		}
	}
		
		public static void combineNNWithS(List listA, List listB,List listC,Pair<String, String> pair, String inputText,HashMap<Integer, String> map){ //NN과 SN또는 SL이 최대 3개까지 합치기
			if(listB.size()>=2){
			if(listB.get(listB.size()-1).toString().equals("SN")||listB.get(listB.size()-1).toString().equals("SL")){ //지금형태소가 SN또는 SL이고
				if(listB.get(listB.size()-2).toString().equals("NNG")||listB.get(listB.size()-2).toString().equals("NNP")){ //이전형태소가 NN일때
					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
							&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //빈칸이 없다면
						
					temp = listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //둘의 합을 temp에 저장
					System.out.println("NNG(NNP)+SN(SL) 합쳐서 "+ temp);
					listC.add(temp);
					map.put(map.size()-1, new String(temp));
				}
			}
			
				else if(listB.get(listB.size()-2).toString().equals("SN")||listB.get(listB.size()-2).toString().equals("SL")){ //이전 형태소가 SN또는 SL일경우
					if(listB.size()>=3){
					if(listB.get(listB.size()-3).toString().equals("NNG")||listB.get(listB.size()-3).toString().equals("NNP")){//전전 형태소가 NN일때
						if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
								&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //빈칸이 없다면
							
						temp =listA.get(listA.size()-3).toString()+ listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //셋의 합을 temp에 저장
						System.out.println("명사+SN(SL) +SL(SN)합쳐서 "+ temp);
						listC.remove(listC.size()-1);
						listC.add(temp);
						map.put(map.size()-1, new String(temp));
							}
						}
					}
				}
			}
		}
	}
		
		public static boolean combineNNGWithXSV(List listA, List listB,List listC,Pair<String, String> pair, String inputText,HashMap<Integer, String> map,List<IsNegative> inList,List<IsNegative> allList,int incnt ){

			if(listB.get(listB.size()-1).toString().equals("XSV")){ //기분좋다 경우 좋다로 처리 (기분좋다, 개좋아 등 결과가 중립으로 나와서)
				if(listB.size()>1){
				if(listB.get(listB.size()-2).toString().equals("NNG")){ //이전 형태소가 명사일때
					
					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
							&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //빈칸이 없다면
						
						temp = listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //둘의 합을 temp에 저장
						System.out.println("명사+접미사(XSV) 합쳐서 (VV인) "+ temp);
						listC.add(temp+"다");
						map.put(map.size(), new String(temp+"다(notification!@#hab)"));
						IsNegative in = new IsNegative();
						in.isNegative=false;
						in.text=temp+"다";
						in.type="VV";
						in.order=incnt;
						inList.add(in);
						
						IsNegative all = new IsNegative();
						all.isNegative=false;
						all.text=temp+"다";
						all.type="VV";
						all.order=incnt;
						allList.add(all);
						return true;
					}
				}
			}
		}
			return false;
	}
		
		public static double calculateTF(List listA,int a){
			
			
			int cnt=0;
			for (int i=0;i<listA.size();i++){
				if(listA.get(i).toString().equals(listA.get(a).toString())){
					cnt++;
				}
			}
			double d = Double.parseDouble( String.format( "%.3f" , (double)cnt/listA.size()));

			return d;
		}
		
		public static void checkNegative(List listA, List listB, List<IsNegative> allList,List<IsNegative> inList){
			int k=-1;
			for (int i =0; i<allList.size();i++){
				if(allList.get(i).type.equals("VV")||allList.get(i).type.equals("VA")){ //형태소가 동사||형용사이고
					if(allList.get(i-1).text.equals("안")||allList.get(i-1).text.equals("못")){//이전 단어가 안 또는 못일때
						System.out.println("이 동사 " + allList.get(i).text +" 는 Negative 요소를 갖고 있음");
						allList.get(i).isNegative=true;
						
						for (int j=0;j<inList.size();j++){
							if(allList.get(i).order==inList.get(j).order){ //inlist와 alllist의 order가 같은 값을 찾아서 inlist의 그 인덱스 값을 갖는 inlist의 negative를 트루로
								k=j;
								break;
							}
						}
						inList.get(k).isNegative=true;
					}
					
					if (allList.get(i+1).text.equals("않")||allList.get(i+1).text.equals("못")||allList.get(i+2).text.equals("않")||allList.get(i+2).text.equals("못")){ //다음 두단어가 않 또는 못을 갖고있을 시
						System.out.println("이 동사 " + allList.get(i).text +" 는 Negative 요소를 갖고 있음");
						allList.get(i).isNegative=true;
						
						for (int j=0;j<inList.size();j++){
							if(allList.get(i).order==inList.get(j).order){
								k=j;
								break;
							}
						}
						inList.get(k).isNegative=true;
					}
				}	
			}
		}
	
		
		
	public static void analyzer(String inputText){
		
		//VA형용사 VV동사
		int i=0; //해쉬 위한 변수
		int INCount=0;
		
				System.out.println("\n-----------------------새로운 글 시작---------------------");
					System.out.println(inputText);
				List listA = new ArrayList();
				List listB = new ArrayList();
				List finalWords = new ArrayList(); //디비에 저장시킬 명사들 리스트
				List infinitive =new ArrayList(); //동사원형 리스트
				List posNeg =new ArrayList(); //긍정부정 리스트
				List<IsNegative> inList = new ArrayList(); //부정첵 리스트 
				List<IsNegative> allList = new ArrayList(); //모든것 담는 리스트 
				HashMap<Integer , String> index = new HashMap<Integer , String>(); //인덱스를 위해 해쉬 (명사,부정사 모두 섞여있음)
				HashMap<String , Integer> hashFinalWordsindex = new HashMap<String , Integer>(); //인덱스를 위해 해쉬 명사집합
				HashMap<String , Integer> hashInfintive = new HashMap<String , Integer>(); //인덱스를 위해 해쉬 부정사집합
				HashMap<String , Integer> hashVCA = new HashMap<String , Integer>(); //인덱스를 위해 해쉬 VCP 또는 VCN집합
			
			
				
				List<List<Pair<String, String>>> result = komoran.analyze(inputText); //이곳에 분석하고싶은 문장넣기
				
				for (List<Pair<String, String>> eojeolResult : result) {
			
					for (Pair<String, String> wordMorph : eojeolResult) {
						{
			//		if(wordMorph.getSecond().contains("NNG")||wordMorph.getSecond().contains("NNP")) 
							System.out.println(wordMorph);
					
							if(wordMorph.getSecond().contains("NNG")||wordMorph.getSecond().contains("NNP")) {
								finalWords.add(wordMorph.getFirst()); //명사면 추가
								index.put(index.size(), new String(wordMorph.getFirst()));
								i++;
							}
							else if(wordMorph.getSecond().contains("VV")||wordMorph.getSecond().contains("VA")){ 
								infinitive.add(wordMorph.getFirst()+"다"); //형용사또는 동사를 저장
								index.put(index.size(), new String(wordMorph.getFirst()+"다(notification!@#hab)"));
								i++;
							}
							else if(wordMorph.getSecond().contains("VCP")||wordMorph.getSecond().contains("VCN")){ 
								posNeg.add(wordMorph.getFirst()); //긍 부정을 저장
								index.put(index.size(), new String(wordMorph.getFirst()+"(notification!@#vca)"));
								i++;
							}
							
							listA.add(wordMorph.getFirst());
		 					listB.add(wordMorph.getSecond());
						
		 					IsNegative in = new IsNegative(); // 부정첵위한 클래스 선언
		 					IsNegative all = new IsNegative(); // 모든 정보를 담는 클래스
		 					
		 					
		 					all.text=wordMorph.getFirst();
		 					all.order=INCount;
		 					all.isNegative=false;	 				
		 					all.type=wordMorph.getSecond();
		 					allList.add(all);
		 					
		 					if(wordMorph.getSecond().toString().equals("VV")||wordMorph.getSecond().toString().equals("VA")){
		 					in.text=wordMorph.getFirst()+"다";
		 					in.order=INCount;		 					
		 					in.isNegative=false;	 				
		 					in.type=wordMorph.getSecond();
		 					inList.add(in);
		 					}
		 					INCount++;
		 					
							combineNN(listA,listB,finalWords,wordMorph,inputText,index);
							combineNX(listA,listB,finalWords,wordMorph, inputText,index);
							combineSNWithNN(listA,listB,finalWords,wordMorph, inputText,index);
							combineNNWithS(listA, listB, finalWords, wordMorph, inputText,index);
							if(combineNNGWithXSV(listA, listB, infinitive, wordMorph, inputText,index,inList,allList,INCount))INCount++;
							/*	
 							if(wordMorph.getSecond()=="NNG" || wordMorph.getSecond()=="NNP"){ //문장 분석대상이 명사라면
 							
 								System.out.print(wordMorph.getFirst()+", ");						//명사값을 콘솔에출력	
 								BasicDBObject doc = new BasicDBObject().append("Word",wordMorph.getFirst() ); //명사값을 DB에 저장
 								coll.insert(doc);
								}*/
						
					}
				}
			}
				
				
				
			System.out.println(finalWords);
			System.out.println(infinitive);
			System.out.println(posNeg);
			System.out.println(index);
	
			
			
			/*
			Set<Entry<Integer, String>> set = index.entrySet();
			Iterator<Entry<Integer, String>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<Integer, String> e = (Map.Entry<Integer, String>)it.next();
				if(e.getValue().contains("(notification!@#hab)")){ //동사 또는 형용사라면
					hashFinalWordsindex.put(e.getValue().substring(0,e.getValue().length()-20 ), new Integer(e.getKey()));
				}
				else if(e.getValue().contains("(notification!@#vca)")){ //VCP 또는 VCP라면
					hashVCA.put(e.getValue().substring(0,e.getValue().length()-20),new Integer(e.getKey()));
				}
				else {//명사라면
					hashInfintive.put(e.getValue(),new Integer(e.getKey()));
				}
				System.out.println("번호 : " + e.getKey() + ", 글자 : " + e.getValue()); //-> 이걸 이용해서 디비에 저장한다.
			}
		
			System.out.println(hashFinalWordsindex);
			System.out.println(hashInfintive);
			System.out.println(hashVCA);
			*/
			
			System.out.println(calculateTF(finalWords, 0)); 
			
			
			
		//	for(int j=0;j<finalWords.size();j++){
		//		BasicDBObject doc = new BasicDBObject().append("n",finalWords);
			
	//			doc.append("TF",calculateTF(finalWords, j));
		//		doc.append("index", );
	//			wordCollection.insert(doc);
			
			
			/*
			String aaa="dgh";
		String json = "{ 'name' : 'lokesh' , " +
                "'website' : 'howtodoinjava.com' , " +
                "'address' : [{ 'addressLine1' : 'Some address'} , " +
                              "{'addressLine2' : '" +aaa  +"' }, " +
                              "{'addressLine3' : 'New Delhi, India'}]" +
                            "}";

		String aa="0.5";
		String json1 = "{ 'name' : 'lokesh' , " +
                "'website' : 'howtodoinjava.com' , " +
                "'address' : [{ 'addressLine1' : 'Some address'} , " +
                              "{'addressLine2' : " + aa+" }, " +
                              "{'addressLine3' : 'New Delhi, India'}]" +
                            "}";
	      */
			
		//	"n" : [{"word" : "나", "tf":0.5}, {"word" : "롤"}, {"word" : "챔프"}, {"word" : "룰루"}], "v" : [{"word" : "좋다"}]})
			
			checkNegative(listA, listB, allList,inList);
					
			String json = "{ 'name' : '이우빈' , " ;
			json+="'website' : '없음.com' ,";
			json +="'n' : [";
			for (int k=0;k<finalWords.size();k++){
				json+="{'noun' : '" + finalWords.get(k) + "'," + "'TF' : " + Double.toString(calculateTF(finalWords, k)) + "},";
			}
			json= json.substring(0, json.length()-1);
			json+="], 'v' :[";
		
			for (int k=0;k<infinitive.size();k++){
				json+="{'verb' : '" + inList.get(k).text + "'," + "'isNeg' : '" + Boolean.toString(inList.get(k).isNegative) + "'},";
			}
			json= json.substring(0, json.length()-1);
			json+="]}";
		
			
			
			/*
			String aaa="dgh";
			String json = "{ 'name' : '이우빈' , " +  "'website' : '없음.com' , " +  "'n' : [{ 'word' : 'Some address' , " + "'TF' : 'asd' }],"
					 +  "'v' :[{'word' : '"+aaa + "', 'isNeg' : '1' } ]"
			+ "}";
	                         */    
			
			//+  "'v' :[{'word' :'"+aaa + "'}, "+"{'isNeg' : '1' } ],"
			//+  "'v' :[{'word' : '좋다'}, "+"{'isNeg' : '1' } ]"
	System.out.println(json);
	DBObject dbObject = (DBObject)JSON.parse(json);
	 wordCollection.insert(dbObject);

			
		//	for(int j=0;j<infinitive.size();j++){
			//	String tmp= (String) infinitive.get(j);
				//BasicDBObject doc2 = new BasicDBObject().append("Infinitive", tmp);

		//		infintiveCollection.insert(doc2);}
		 
			
 
		} //analyzer함수 끝
 }//클래스 끝

	
	
