package HomebodyTest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.*;

public class TestWindow extends JFrame {
	Vector<String> vq, vr;
	JProgressBar progressBar;
	JLabel startlbl, queslbl, imagelbl;
	JButton startbtn, initbtn, yesbtn, nobtn;
	ImageIcon[] image;
	int cnt, yescnt;
	String level;
    
	public TestWindow() throws FileNotFoundException, IOException {
		setTitle("집순이 테스트");	//제목 설정
		Font f = new Font("함초롬돋움", Font.BOLD, 25);	//글씨 설정
		Container c = getContentPane();
		c.setBackground(Color.white);
		setSize(300,130);
		
		//시작화면
		startlbl = new JLabel("집순이 테스트 시작~!");
		startbtn = new JButton(">>Start");
		startbtn.setBackground(Color.pink);
		//폰트 변경
		startlbl.setFont(f);
		startbtn.setFont(f);
		//컴포넌트 부착
		add(startlbl, "North");
		add(startbtn, "South");
		
		vq = new Vector<String>();	//question들을 모아둘 Vector 선언
		vr = new Vector<String>();	//result들을 모아둘 Vector 선언
		
		BufferedReader qb = new BufferedReader(new InputStreamReader(new FileInputStream("../data/Question.txt"), "UTF8"));
		BufferedReader rb = new BufferedReader(new InputStreamReader(new FileInputStream("../data/Result.txt"), "UTF8"));
		
		//Question.txt를 한줄씩 Vector vq에 추가
		String line=null;
		while((line=qb.readLine())!=null)
			vq.add(line);
		qb.close();
		
		//Result.txt를 한줄씩 Vector vr에 추가
		line=null;
		while((line=rb.readLine())!=null)
			vr.add(line);
		rb.close();
		
		//각 컴포넌트 생성 및 설정
		progressBar = new JProgressBar(0, vq.size());	//문항 개수만큼
        progressBar.setValue(0);	//0으로 초기화
        progressBar.setStringPainted(true); //퍼센트 올라가는 것 출력
        progressBar.setBackground(Color.lightGray);	//배경색 변경
        progressBar.setForeground(Color.pink);	//게이지 색 변경
        
        queslbl = new JLabel(vq.get(0));
        queslbl.setHorizontalAlignment(JLabel.CENTER);
        
        image = new ImageIcon[vq.size()+vr.size()];
        //이미지 로딩
      	for(int i=0 ; i<vq.size() ; i++) {
      		String path = "../data/Qimg"+(i+1)+".png";
      		image[i] = new ImageIcon(path);
      	}
      	for(int i=vq.size() ; i<vq.size()+vr.size() ; i++) {
      		String path = "../data/Rimg"+(i-vq.size()+1)+".png";
      		image[i] = new ImageIcon(path);
      	}
      	imagelbl = new JLabel(image[0]);	//처음 이미지
        
		initbtn = new JButton("처음으로");
		yesbtn = new JButton("그렇다");
		nobtn = new JButton("아니다");
		initbtn.setBackground(Color.lightGray);
		yesbtn.setBackground(Color.lightGray);
		nobtn.setBackground(Color.lightGray);
		
		//폰트 변경
		progressBar.setFont(f);
		queslbl.setFont(f);
		initbtn.setFont(f);
		yesbtn.setFont(f);
		nobtn.setFont(f);
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.darkGray);
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.pink);
		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.white);
		panel3.setLayout(new BorderLayout());	//배치관리자 변경
		
		//컴포넌트 부착
		panel1.add(progressBar);
		panel3.add(queslbl, "North");
		panel3.add(imagelbl, "Center");
		panel2.add(yesbtn);
		panel2.add(nobtn);
		panel2.add(initbtn);
		
		//startbtn에 대한 리스너 구현
			class startbtnAction implements ActionListener {
				@Override
				public void actionPerformed(ActionEvent e) {
					startlbl.setVisible(false);	//기존의 컴포넌트들을 사라지게 함
					startbtn.setVisible(false);
					setSize(1100,800);	//창 사이즈 변경
					add(panel1, "North");	//컴포넌트 부착
					add(panel3, "Center");
					add(panel2, "South");
				}
			}
		//yesbtn에 대한 리스너 구현
		class yesbtnAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				yescnt++;	//"그렇다"라고 답한 횟수 올리기
				cnt++;
				progressBar.setValue(cnt);	//진행률 새로고침
				if(cnt>=vq.size()) endTest();	//문항 개수를 넘어가면 테스트를 종료
				else nextQuestion();	//그렇지 않으면 다음 문항으로 넘어감
			}
		}
		//nobtn에 대한 리스너 구현
		class nobtnAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				cnt++;
				progressBar.setValue(cnt);	//진행률 새로고침
				if(cnt>=vq.size()) endTest();	//문항 개수를 넘어가면 테스트를 종료
				else nextQuestion();	//그렇지 않으면 다음 문항으로 넘어감
			}
		}
		//initbtn에 대한 리스너 구현
		class initbtnAction implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				//각종 값들 초기화
				cnt=0;
				yescnt=0;
				progressBar.setValue(progressBar.getMinimum());
				queslbl.setText(vq.get(0));
				yesbtn.setVisible(true);
				nobtn.setVisible(true);
				imagelbl.setIcon(image[0]);
			}
		}
		
		//리스너 등록
		startbtn.addActionListener(new startbtnAction());
		yesbtn.addActionListener(new yesbtnAction());
		nobtn.addActionListener(new nobtnAction());
		initbtn.addActionListener(new initbtnAction());
		
		setVisible(true);
	}
	
	//Vector에서 search값을 포함하는 값을 찾고 반환하는 메소드
	public String searchVector(Vector<String> v, String search) {
		while(true) {
			boolean found = false;
			for(int i=0; i<v.size(); i++) {
				String s = v.get(i);
				if(s.length() < search.length())	//search값이 Vector값을 초과할 경우
					continue;
				String a = s.substring(0, search.length());
				if(search.equals(a)) {
					found = true;
					return s;
				}
			}
			if(!found) {	//찾지 못할 경우
				System.out.println("결과가 없습니다.");
				return null;
			}
		}
	}
	
	//모든 문항이 끝나고 테스트가 종료될 때 실행되는 메소드
	public void endTest() {
		yesbtn.setVisible(false);
		nobtn.setVisible(false);
		
		int levelNum = 0;
		if(yescnt<=1) levelNum = 1;	//"그렇다"라고 대답한 문항의 수가 1개 이하면 1단계
		else if(yescnt>1 && yescnt<=4) levelNum = 2;	//1개 초과 4개 이하면 2단계
		else if(yescnt>4 && yescnt<=7) levelNum = 3;	//4개 초과 7개 이하면 3단계
		else if(yescnt>7) levelNum = 4;	//7개 초과면 4단계
		
		imagelbl.setIcon(image[vq.size()+levelNum-1]);	//단계에 따른 이미지 출력 
		queslbl.setText(searchVector(vr, levelNum+"단계"));	//단계에 따른 테스트 결과 출력
	}
	
	//다음 문항으로 넘어갈 때 실행되는 메소드
	public void nextQuestion() {
		queslbl.setText(vq.get(cnt));	//다음 질문 띄우기
		imagelbl.setIcon(image[cnt]);	//다음 이미지 띄우기
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		new TestWindow();
	}
}