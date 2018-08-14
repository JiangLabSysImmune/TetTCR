import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class getMatrix {
	public static void main(String[] args) throws IOException{
		String listFile = args[0];
		int thr = Integer.parseInt(args[1]);
		//String listFile = "../all.list";
		//System.out.println(listFile);
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(new File(listFile)));
			
			List<String> tempList = new ArrayList<String>();
			String temp;
			while ((temp = reader.readLine()) != null) {
				tempList.add(temp);
			}
			reader.close();
			
			//String tempLine;
			//int status = 0;
			for (int i = 0;i < tempList.size();i++) {
				//System.out.println(tempLine);
				String tempLine = tempList.get(i);
				int status = 0;		
				String[] cut = tempLine.split("\t");
				if (cut.length == 1) {
					status = 0;
				}
				else if (cut.length == 2) {
					status = Integer.parseInt(cut[1]);
				}

				if (status != 2) {
					String bc = cut[0];
					int len = bc.length();
					String bc1 = bc;
					//String ind = bc.substring(0,6);
					//String tp = bc.substring(7,8);
                                	bc = bc.substring(0,len-6);
					System.out.println(bc);
					status = 1;
					String updateLine = bc1+"\t"+status;
					tempList.set(i,updateLine);
					updateList(listFile,tempList);
					
					calDis caler = new calDis();
					caler.clusThresh = thr;
					caler.calculate(bc);
					
					status = 0;
					updateLine = bc1+"\t"+status;
                                        tempList.set(i,updateLine);
                                        updateList(listFile,tempList);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
        }


	public static void updateList(String listFile, List<String> list) {
		try{
			FileWriter writer = new FileWriter(new File(listFile));
			BufferedWriter bw = new BufferedWriter(writer);
			Iterator<String> it = list.iterator();
			while(it.hasNext()) {
				String tempLine = it.next();
				bw.write(tempLine);
				bw.newLine();
			}
			bw.flush();
			bw.close();
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

