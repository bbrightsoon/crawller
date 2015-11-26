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
	 	static Komoran komoran = new Komoran("C:/Users/���/Desktop/java workspace/Capstone/lib/models-full");
		static MongoClient mongoClient = new MongoClient("localhost", 27017); // ������ ����
		static DB db = mongoClient.getDB("test"); // db ������ testDB DB�� ����
		static DBCollection wordCollection = db.getCollection("wordCollection"); // coll ������ testCol �÷��ǿ� ����
		static DBCollection infintiveCollection = db.getCollection("infinitiveCollection"); 
		static String tempSL;
		static String tempSN;
		static String temp;
		static String tempNNGNNP;
		static boolean flagSL=false;
		static boolean flagSN=false;
		static boolean flagNNGNNP=false;
/*
		public static void checkSL(String inputText, Pair<String, String> pair){ //������������ ��Ʈ���ƿ��ٿ�� �ͼ��� �߻�
				
				if (pair.toString().contains("SL") ){ // SL�ϰ�쿡
					
					if(inputText.charAt(inputText.indexOf(pair.getFirst() ) +pair.getFirst().length())!=' '){// �� ���� �ܾ ��ĭ�� �ƴ϶��
						tempSL = pair.getFirst(); // �ӽ� ��Ʈ���� �ܾ �����Ѵ�
						flagSL=true; //�÷��׸� Ʈ���						
					}
					else{
						flagSL=false; // ��ĭ�̶�� �÷��� false��
					}
				}
			}
		public static void combineSLWithSN(String inputText, Pair<String, String> pair){
			if(!pair.getSecond().equals("SN") && !pair.getSecond().equals("SL")) { //ù��° ���� �ι�° ���¼Ұ� SN���ƴϰ� SL���ƴҰ�쿡��
			
					flagSL=false; // �÷��׸� false�� �ٲ��ָ�
				}
				if(flagSL==true && pair.getSecond().equals("SN")){ //���� ���¼Ұ� SL�̾��� ������ SN�̶��
					temp=tempSL+pair.getFirst(); //�� �ܾ �����־� ���� �����.
				System.out.println("SL�� SN�� ���ļ� "+temp);
				}
		}
		
		public static void checkSN(String inputText, Pair<String, String> pair){ 
			
			if (pair.toString().contains("SN") ){ // SL�ϰ�쿡
				
				if(inputText.charAt(inputText.indexOf(pair.getFirst() ) +pair.getFirst().length())!=' '){// �� ���� �ܾ ��ĭ�� �ƴ϶��
					tempSN = pair.getFirst(); // �ӽ� ��Ʈ���� �ܾ �����Ѵ�
					flagSN=true; //�÷��׸� Ʈ���							
				}
				else flagSN=false; // ��ĭ�̶�� �÷��� false��
			}
		}
		public static void combineSNWithSL(String inputText, Pair<String, String> pair){
			if(!pair.getSecond().equals("SN") && !pair.getSecond().equals("SL")) {
					
					flagSN=false;
					
				}
				if(flagSN==true && pair.getSecond().equals("SL")){
					temp=tempSN+pair.getFirst();
				System.out.println("SN�� SL�� ���ļ� "+temp);
				}
		}
		*/

	
		
		public static void combineNN(List listA, List listB,List listC, Pair<String, String> pair,String inputText, HashMap<Integer, String> map){ //���� ��縦 �ռ�
		
			int i=1;	 //������ �����ϱ� ���� ����
			
			if(listB.get(listB.size()-i).equals("NNG")||listB.get(listB.size()-i).equals("NNP")){
			while(i<=5){ //�ִ� 4���� �ܾ �ռ�
				
				if(listB.get(listB.size()-i).equals("NNG")||listB.get(listB.size()-i).equals("NNP")){ //���� ���¼Ұ� ����̰�
					temp=(String) listA.get(listA.size()-1);
					if(listB.size()>i){
						
				if(listB.get(listB.size()-i-1).equals("NNG")||listB.get(listB.size()-i-1).equals("NNP")){ //���� ���¼Ұ� �����

					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-i-1).toString())+ listA.get(listA.size()-i-1).toString().length())  != ' ' &&
							inputText.charAt(inputText.indexOf(listA.get(listA.size()-i-1).toString())+ listA.get(listA.size()-i-1).toString().length())  != '\n') {//�׸��� �������¼ҿ� ���� ���¼� ���̿� ��ĭ �Ǵ� �ٹٲ� �� ���ٸ�
						
						i++;
	//					System.out.println("���� ������ "+ pair+ "i�� ++���� i�� "+i);
						
						} else
							break; // ��ĭ������ break
					} else
						break; //���� ���¼Ұ� ��簡 �ƴϸ�
				} else
					break; // ������ �������ϸ�
			}
				}
				if(i>=2) { //�ռ������� �����ȴٸ�
				
			//	System.out.println("i�� "+i);
			//		if (i==3)temp = listA.get(listA.size()-2).toString()+ listA.get(listA.size()-1).toString(); //�δܾ� �ռ�
				//	else if(i==5)temp =listA.get(listA.size()-4).toString()+ listA.get(listA.size()-2).toString()+ listA.get(listA.size()-1).toString(); //���ܾ� �ռ�
					//else if (i==6)temp = listA.get(listA.size()-6).toString()+listA.get(listA.size()-4).toString()+listA.get(listA.size()-2).toString()+listA.get(listA.size()-1).toString(); //�״ܾ� �ռ�
				for(int j=1;j<i;j++){
					temp=listA.get(listA.size()-j-1).toString()+temp;
				}
				System.out.println("NN+ NN�� ������ ���� " + temp); //��簡 �������ٸ�
					listC.add(temp);			
					listC.remove(listC.size()-3);
					listC.remove(listC.size()-2); //����� �ߺ������� ���� ��
					
					map.put(map.size()-2, new String(temp));
					map.remove(map.size()-1);
					
				}
			}
					
				 //���� �� ��� ���̼����� �϶� ���̼��ﵵ ���ո�� �����ϰ� ���̼������� �����ϰ� �ִ� �����߻�
		}
		
		
		public static void combineNX(List listA, List listB,List listC,Pair<String, String> pair, String inputText,HashMap<Integer, String> map){

			if(listB.get(listB.size()-1).toString().equals("XSN")){ //�������¼Ұ� X*�̰� -> ���̻��̰�
				if(listB.size()>1){
				if(listB.get(listB.size()-2).toString().equals("NNG")||listB.get(listB.size()-2).toString().equals("NNP")){ //���� ���¼Ұ� ����϶�
					
					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
							&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //��ĭ�� ���ٸ�
						
						temp = listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //���� ���� temp�� ����
						System.out.println("���+���̻� ���ļ� "+ temp);
						listC.add(temp);
						listC.remove(listC.size()-2); //����� �ߺ������� �������� ����
						map.put(map.size()-1, new String(temp));
						
					}
				}
			}
		}
	}
		public static void combineSNWithNN(List listA, List listB,List listC,Pair<String, String> pair, String inputText,HashMap<Integer, String> map){
		
			if(listB.get(listB.size()-1).toString().equals("NNG")||listB.get(listB.size()-1).toString().equals("NNP")){ //�������¼Ұ� ����̰�
				if(listB.size()>1){
				if(listB.get(listB.size()-2).toString().equals("SN")){ //���� ���¼Ұ� �����϶� (SL�� SE�� �ٸ��� �߰�������)
					
					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
							&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //��ĭ�� ���ٸ�
						
						temp = listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //���� ���� temp�� ����
						System.out.println("����+��� ���ļ� "+ temp);
						listC.add(temp);
						map.put(map.size()-1, new String(temp));
					}
				}
			}
		}
	}
		
		public static void combineNNWithS(List listA, List listB,List listC,Pair<String, String> pair, String inputText,HashMap<Integer, String> map){ //NN�� SN�Ǵ� SL�� �ִ� 3������ ��ġ��
			if(listB.size()>=2){
			if(listB.get(listB.size()-1).toString().equals("SN")||listB.get(listB.size()-1).toString().equals("SL")){ //�������¼Ұ� SN�Ǵ� SL�̰�
				if(listB.get(listB.size()-2).toString().equals("NNG")||listB.get(listB.size()-2).toString().equals("NNP")){ //�������¼Ұ� NN�϶�
					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
							&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //��ĭ�� ���ٸ�
						
					temp = listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //���� ���� temp�� ����
					System.out.println("NNG(NNP)+SN(SL) ���ļ� "+ temp);
					listC.add(temp);
					map.put(map.size()-1, new String(temp));
				}
			}
			
				else if(listB.get(listB.size()-2).toString().equals("SN")||listB.get(listB.size()-2).toString().equals("SL")){ //���� ���¼Ұ� SN�Ǵ� SL�ϰ��
					if(listB.size()>=3){
					if(listB.get(listB.size()-3).toString().equals("NNG")||listB.get(listB.size()-3).toString().equals("NNP")){//���� ���¼Ұ� NN�϶�
						if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
								&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //��ĭ�� ���ٸ�
							
						temp =listA.get(listA.size()-3).toString()+ listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //���� ���� temp�� ����
						System.out.println("���+SN(SL) +SL(SN)���ļ� "+ temp);
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

			if(listB.get(listB.size()-1).toString().equals("XSV")){ //������� ��� ���ٷ� ó�� (�������, ������ �� ����� �߸����� ���ͼ�)
				if(listB.size()>1){
				if(listB.get(listB.size()-2).toString().equals("NNG")){ //���� ���¼Ұ� ����϶�
					
					if (inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != ' '
							&& inputText.charAt(inputText.indexOf(listA.get(listA.size()-2).toString())+ listA.get(listA.size()-2).toString().length())  != '\n') { //��ĭ�� ���ٸ�
						
						temp = listA.get(listA.size()-2).toString() + listA.get(listA.size()-1).toString(); //���� ���� temp�� ����
						System.out.println("���+���̻�(XSV) ���ļ� (VV��) "+ temp);
						listC.add(temp+"��");
						map.put(map.size(), new String(temp+"��(notification!@#hab)"));
						IsNegative in = new IsNegative();
						in.isNegative=false;
						in.text=temp+"��";
						in.type="VV";
						in.order=incnt;
						inList.add(in);
						
						IsNegative all = new IsNegative();
						all.isNegative=false;
						all.text=temp+"��";
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
				if(allList.get(i).type.equals("VV")||allList.get(i).type.equals("VA")){ //���¼Ұ� ����||������̰�
					if(allList.get(i-1).text.equals("��")||allList.get(i-1).text.equals("��")){//���� �ܾ �� �Ǵ� ���϶�
						System.out.println("�� ���� " + allList.get(i).text +" �� Negative ��Ҹ� ���� ����");
						allList.get(i).isNegative=true;
						
						for (int j=0;j<inList.size();j++){
							if(allList.get(i).order==inList.get(j).order){ //inlist�� alllist�� order�� ���� ���� ã�Ƽ� inlist�� �� �ε��� ���� ���� inlist�� negative�� Ʈ���
								k=j;
								break;
							}
						}
						inList.get(k).isNegative=true;
					}
					
					if (allList.get(i+1).text.equals("��")||allList.get(i+1).text.equals("��")||allList.get(i+2).text.equals("��")||allList.get(i+2).text.equals("��")){ //���� �δܾ �� �Ǵ� ���� �������� ��
						System.out.println("�� ���� " + allList.get(i).text +" �� Negative ��Ҹ� ���� ����");
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
		
		//VA����� VV����
		int i=0; //�ؽ� ���� ����
		int INCount=0;
		
				System.out.println("\n-----------------------���ο� �� ����---------------------");
					System.out.println(inputText);
				List listA = new ArrayList();
				List listB = new ArrayList();
				List finalWords = new ArrayList(); //��� �����ų ���� ����Ʈ
				List infinitive =new ArrayList(); //������� ����Ʈ
				List posNeg =new ArrayList(); //�������� ����Ʈ
				List<IsNegative> inList = new ArrayList(); //����ý ����Ʈ 
				List<IsNegative> allList = new ArrayList(); //���� ��� ����Ʈ 
				HashMap<Integer , String> index = new HashMap<Integer , String>(); //�ε����� ���� �ؽ� (���,������ ��� ��������)
				HashMap<String , Integer> hashFinalWordsindex = new HashMap<String , Integer>(); //�ε����� ���� �ؽ� �������
				HashMap<String , Integer> hashInfintive = new HashMap<String , Integer>(); //�ε����� ���� �ؽ� ����������
				HashMap<String , Integer> hashVCA = new HashMap<String , Integer>(); //�ε����� ���� �ؽ� VCP �Ǵ� VCN����
			
			
				
				List<List<Pair<String, String>>> result = komoran.analyze(inputText); //�̰��� �м��ϰ���� ����ֱ�
				
				for (List<Pair<String, String>> eojeolResult : result) {
			
					for (Pair<String, String> wordMorph : eojeolResult) {
						{
			//		if(wordMorph.getSecond().contains("NNG")||wordMorph.getSecond().contains("NNP")) 
							System.out.println(wordMorph);
					
							if(wordMorph.getSecond().contains("NNG")||wordMorph.getSecond().contains("NNP")) {
								finalWords.add(wordMorph.getFirst()); //���� �߰�
								index.put(index.size(), new String(wordMorph.getFirst()));
								i++;
							}
							else if(wordMorph.getSecond().contains("VV")||wordMorph.getSecond().contains("VA")){ 
								infinitive.add(wordMorph.getFirst()+"��"); //�����Ǵ� ���縦 ����
								index.put(index.size(), new String(wordMorph.getFirst()+"��(notification!@#hab)"));
								i++;
							}
							else if(wordMorph.getSecond().contains("VCP")||wordMorph.getSecond().contains("VCN")){ 
								posNeg.add(wordMorph.getFirst()); //�� ������ ����
								index.put(index.size(), new String(wordMorph.getFirst()+"(notification!@#vca)"));
								i++;
							}
							
							listA.add(wordMorph.getFirst());
		 					listB.add(wordMorph.getSecond());
						
		 					IsNegative in = new IsNegative(); // ����ý���� Ŭ���� ����
		 					IsNegative all = new IsNegative(); // ��� ������ ��� Ŭ����
		 					
		 					
		 					all.text=wordMorph.getFirst();
		 					all.order=INCount;
		 					all.isNegative=false;	 				
		 					all.type=wordMorph.getSecond();
		 					allList.add(all);
		 					
		 					if(wordMorph.getSecond().toString().equals("VV")||wordMorph.getSecond().toString().equals("VA")){
		 					in.text=wordMorph.getFirst()+"��";
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
 							if(wordMorph.getSecond()=="NNG" || wordMorph.getSecond()=="NNP"){ //���� �м������ �����
 							
 								System.out.print(wordMorph.getFirst()+", ");						//��簪�� �ֿܼ����	
 								BasicDBObject doc = new BasicDBObject().append("Word",wordMorph.getFirst() ); //��簪�� DB�� ����
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
				if(e.getValue().contains("(notification!@#hab)")){ //���� �Ǵ� �������
					hashFinalWordsindex.put(e.getValue().substring(0,e.getValue().length()-20 ), new Integer(e.getKey()));
				}
				else if(e.getValue().contains("(notification!@#vca)")){ //VCP �Ǵ� VCP���
					hashVCA.put(e.getValue().substring(0,e.getValue().length()-20),new Integer(e.getKey()));
				}
				else {//�����
					hashInfintive.put(e.getValue(),new Integer(e.getKey()));
				}
				System.out.println("��ȣ : " + e.getKey() + ", ���� : " + e.getValue()); //-> �̰� �̿��ؼ� ��� �����Ѵ�.
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
			
		//	"n" : [{"word" : "��", "tf":0.5}, {"word" : "��"}, {"word" : "è��"}, {"word" : "���"}], "v" : [{"word" : "����"}]})
			
			checkNegative(listA, listB, allList,inList);
					
			String json = "{ 'name' : '�̿��' , " ;
			json+="'website' : '����.com' ,";
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
			String json = "{ 'name' : '�̿��' , " +  "'website' : '����.com' , " +  "'n' : [{ 'word' : 'Some address' , " + "'TF' : 'asd' }],"
					 +  "'v' :[{'word' : '"+aaa + "', 'isNeg' : '1' } ]"
			+ "}";
	                         */    
			
			//+  "'v' :[{'word' :'"+aaa + "'}, "+"{'isNeg' : '1' } ],"
			//+  "'v' :[{'word' : '����'}, "+"{'isNeg' : '1' } ]"
	System.out.println(json);
	DBObject dbObject = (DBObject)JSON.parse(json);
	 wordCollection.insert(dbObject);

			
		//	for(int j=0;j<infinitive.size();j++){
			//	String tmp= (String) infinitive.get(j);
				//BasicDBObject doc2 = new BasicDBObject().append("Infinitive", tmp);

		//		infintiveCollection.insert(doc2);}
		 
			
 
		} //analyzer�Լ� ��
 }//Ŭ���� ��

	
	
