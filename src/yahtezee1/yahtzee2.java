package yahtezee1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.*;
import java.sql.*;

public class yahtzee2 extends JFrame implements ItemListener, ActionListener{
	public static final int Width=640, Height=Width/12*9;
	int numRows = 13;
	Checkbox c1,c2,c3,c4;
	JTextField t[]=new JTextField[4];
	JButton b1,hidhZara;
	String emrat[]=new String[4];
	int nr;//numer lojtares
	JButton zara[]=new JButton[5];
	int zara_values[]=new int[5];
	Icon first=new ImageIcon("first.png");
	Icon dice[]= {new ImageIcon("1.png"),new ImageIcon("2.png"),new ImageIcon("3.png"),
				  new ImageIcon("4.png"),new ImageIcon("5.png"),new ImageIcon("6.png")};
	String[][] data;
	String[] column;
	int nr_of_turn=1;
	Random rand = new Random();
	boolean selector=false; boolean select_a_category=false; int shuma=0;
	boolean zara_to_change[]=new boolean[6];
	category[][] kategorite;
	int player_at_turn=0;
	int total_of_category_turns=0;
	JLabel l;
	int[] stor;
	JScrollPane qq;
	boolean[] valid_emri;JTextField[] emra_to_validate, mbiemra,moshat;  JButton submit; JLabel warning; int numer_formesh; int[] vendi_i_emrit;
	private final String userName = "root";
	private final String password = "Orest132.";
	private final String serverName = "localhost";
	private final int portNumber = 3306;
	private final String dbName = "yahtzee";
	private final String tableName = "yahtzeetable";
	Connection conn = null;
	JButton shikoPik;
	
	
	
	
	yahtzee2(){
		CheckboxGroup cbg=new CheckboxGroup();
		c1=new Checkbox("1 lojtar",cbg,false);
		c2=new Checkbox("2 lojtar",cbg,false);
		c3=new Checkbox("3 lojtar",cbg,false);
		c4=new Checkbox("4 lojtar",cbg,false);
		
		c1.setBounds(Width/2-50,Height/2-120,100,40);
		c2.setBounds(Width/2-50,Height/2-80,100,40);
		c3.setBounds(Width/2-50,Height/2-40,100,40);
		c4.setBounds(Width/2-50,Height/2,100,40);
		
		add(c1); add(c2); add(c3); add(c4);
		
		c1.addItemListener(this);
		c2.addItemListener(this);
		c3.addItemListener(this);
		c4.addItemListener(this);
		
		setLayout(null);
		setSize(Width,Height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void itemStateChanged(ItemEvent e){
		 if(c1.getState())
		  { 
			 nr=1;
		  }

		 else if(c2.getState())
		 {
			 nr=2;
		 }
		 else if(c3.getState())
		 {
			 nr=3;
		 }
		 else if(c4.getState())
		 {
			 nr=4;
		 }
		remove(c1);remove(c2);remove(c3);remove(c4);
		kategorite=new category[nr][13];
		 showButton(Width, Height);
		}
	
		public Connection getConnection() throws SQLException {
			Connection conn = null;
			Properties connectionProps = new Properties();
			connectionProps.put("user", this.userName);
			connectionProps.put("password", this.password);
	
			conn = DriverManager.getConnection("jdbc:mysql://"
					+ this.serverName + ":" + this.portNumber + "/" + this.dbName,
					connectionProps);
	
			return conn;
		}

	
		public boolean executeUpdate(Connection conn, String command) throws SQLException {
		    Statement stmt = null;
		    try {
		        stmt = conn.createStatement();
		        stmt.executeUpdate(command);
				
				
		        return true;
		    } finally {
	
		    	// This will run whether we throw an exception or not
		        if (stmt != null) { stmt.close(); }
		    }
		}
	
		public void showform(int Width,int Height){
			
			try {
				conn = this.getConnection();
				System.out.println("Connected to database");
			} catch (SQLException e) {
				System.out.println("ERROR: Could not connect to the database");
				e.printStackTrace();
				return;
			}
			
			try {
				
				Statement myStmt = conn.createStatement();
				ResultSet myRs= myStmt.executeQuery("select * from "+this.tableName);
				valid_emri=new boolean[nr]; numer_formesh=0;
				Arrays.fill(valid_emri, false);
				while(myRs.next())
				{
					for(int i=0;i<nr;i++)
					{
						if(myRs.getString("Emri").equals(emrat[i]))
							valid_emri[i]=true;
					}
				}
				
				for(int i=0;i<nr;i++)	
				{
					if(valid_emri[i]==false)
					{
						numer_formesh++;
					}
				}
				
				vendi_i_emrit=new int[numer_formesh];int k=0;
				for(int i=0;i<nr;i++)
				{
					if(valid_emri[i]==false)
					{
						vendi_i_emrit[k]=i;
						k++;
					}
				}
				
				emra_to_validate=new JTextField[numer_formesh];
				mbiemra=new JTextField[numer_formesh];
				moshat=new JTextField[numer_formesh];
				for(int i=0;i<numer_formesh;i++)
				{
					emra_to_validate[i]=new JTextField(emrat[vendi_i_emrit[i]]);
					emra_to_validate[i].setBounds(50, 50+i*90, 100, 25);
					emra_to_validate[i].setEditable(false);
					add(emra_to_validate[i]);
					mbiemra[i]=new JTextField();
					mbiemra[i].setBounds(50, 80+i*90, 100, 25);
					add(mbiemra[i]);
					moshat[i]=new JTextField();
					moshat[i].setBounds(50, 110+i*90, 100, 25);
					add(moshat[i]);
					
				}
				if(numer_formesh!=0)
				{
					submit=new JButton("Submit");
					submit.setBounds(Width-120, Height/2, 100, 25);
					submit.addActionListener(this);
					add(submit);
				}
				else
				{
					repaint(0,0,Width,Height);
					showgame(Width,Height);
				}
				
				
		    } catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}
		
		public void add_to_database() {
			try {
				
				
				int pp=0;
				for(int i=0;i<nr;i++)
				{
					
					if(valid_emri[i]==false)
					{
						String query =  "INSERT INTO "+ this.tableName + 
										" (Emri,Mbiemri,Mosha, Piket)"+
										" VALUES ('"+emrat[i]+"','"+mbiemra[pp].getText()+"',"+Integer.valueOf(moshat[pp].getText())+",0)";
						pp++;
						
						this.executeUpdate(conn, query);
					}
				}
				
		    } catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}
	
		public void showButton(int Width,int Height){
			for(int i=0;i<nr;i++)
			{
				t[i]= new JTextField("Player "+(i+1));
				t[i].setBounds(Width/2-100, Height/2-25*i-50, 200, 20);
				add(t[i]);
				
			}
			b1=new JButton("Vazhdo");
			b1.setBounds(Width/2-50, Height/2, 100, 35);
			add(b1);
			b1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(int i=0;i<nr;i++)
					{
						emrat[i]=t[i].getText();
						remove(t[i]);
					}
					remove(b1);
					repaint(0,0,Width,Height);
					showform(Width,Height);
				}
			});
			
		}
		
		public void showgame(int Width,int Height) {
			for(int i=0;i<5;i++)
			{
				zara[i]= new JButton();
				zara[i].setIcon(dice[i]);
				zara[i].setBounds(50, Height/2-i*50+70, 45, 45);
				zara[i].setBorder(BorderFactory.createEmptyBorder());
				zara[i].addActionListener(this);
				add(zara[i]);
			}
			if(emrat[1]==null) {
				column =new String[]{"Kategoria",emrat[0]};
				data=new String[][]{ {"Njesha",""},    
			            		  {"Dysha","",},    
			            		  {"Tresha","",}, 
			            		  {"Katra",""},
			            		  {"Pesa",""},
			            		  {"Gjashta",""},
			            		  {"Tre me nje vlera",""},
			            		  {"Kater me nje vlere",""},
			            		  {"Tre dhe 2","",""},
			            		  {"Kater te njepasnjeshme",""},
			            		  {"Pese te njepasnjeshme","",""},
			            		  {"Yahtzee",""},
			            		  {"Cdo rast",""}
			            		}; 
			}
			else if(emrat[2]==null) {
				column=new String[]{"Kategoria",emrat[0],emrat[1]};
				data=new String[][]{ {"Njesha","",""},    
			            		  {"Dysha","",""}, 
			            		  {"Tresha","",""}, 
			            		  {"Katra","",""},
			            		  {"Pesa","",""},
			            		  {"Gjashta","",""},
			            		  {"Tre me nje vlera","",""},
			            		  {"Kater me nje vlere","",""},
			            		  {"Tre dhe 2","",""},
			            		  {"Kater te njepasnjeshme","",""},
			            		  {"Pese te njepasnjeshme","",""},
			            		  {"Yahtzee","",""},
			            		  {"Cdo rast","",""}
			            		}; 
		}
			else if(emrat[3]==null){
				column =new String[]{"Kategoria",emrat[0],emrat[1],emrat[2]};
				data=new String[][]{ {"Njesha","","",""},    
			            		  {"Dysha","","",""}, 
			            		  {"Tresha","","",""}, 
			            		  {"Katra","","",""},
			            		  {"Pesa","","",""},
			            		  {"Gjashta","","",""},
			            		  {"Tre me nje vlera","","",""},
			            		  {"Kater me nje vlere","","",""},
			            		  {"Tre dhe 2","","",""},
			            		  {"Kate te njepasnjeshme","","",""},
			            		  {"Pese te njepasnjeshme","","","",""},
			            		  {"Yahtzee","","",""},
			            		  {"Cdo rast","","",""}
			            		}; 
			}
			else
			{
				column =new String[]{"Kategoria",emrat[0],emrat[1],emrat[2],emrat[3]};
				data=new String[][]{ {"Njesha","","","",""},    
					          		  {"Dysha","","","",""},   
					          		  {"Tresha","","","",""},  
					          		  {"Katra","","","",""},
					          		  {"Pesa","","","",""},
					          		  {"Gjashta","","","",""},
					          		  {"Tre me nje vlere","","","",""},
					          		  {"Kater me nje vlere","","","",""},
					          		  {"Tre dhe 2","","","",""},
					          		  {"Kater te njepasnjeshme","","","",""},
					          		  {"Pese te njepasnjeshme","","","",""},
					          		  {"Yahtzee","","","",""},
					          		  {"Cdo rast","","","",""}
					          		}; 
			}
			
			l=new JLabel(emrat[0]+" eshte rradha jote");
			l.setBounds(50, Height-100, 500, 25);
			l.setVisible(true);
			add(l);
			
			JTable jt=new JTable(data,column);
			jt.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent me) {
					JTable table = (JTable) me.getSource();
					
					if(select_a_category==true)
					{
						if(player_at_turn==nr) {
							player_at_turn=0;
							total_of_category_turns++;
						}
						int row = table.getSelectedRow();
						int column=table.getSelectedColumn();
						int indicator=0;
						if(column==player_at_turn+1)
						{
							kategorite[player_at_turn][total_of_category_turns]=new category(zara_values,row+1);
							boolean[] bool1= kategorite[player_at_turn][total_of_category_turns].get_bool();
							
							for(int i=0;i<13;i++)
							{
								boolean[] bool= new boolean[13];
								Arrays.fill(bool, false);
								if(kategorite[player_at_turn][i]!=null&&i!=total_of_category_turns) {
									bool= kategorite[player_at_turn][i].get_bool();//i eshte per kategorite e meparshmeqe mund te jene plotesuar
								}
									for(int j=0;j<13;j++)
									{
										if(bool[j]==true&&bool1[j]==true) {
											indicator++;
									}
								}
							}
							
						
							if(indicator==0)
							{
								String a = String.valueOf(kategorite[player_at_turn][total_of_category_turns].get_piket());
								table.setValueAt(a, row, column);
								select_a_category=false;
								player_at_turn++;
								hidhZara.setEnabled(true);
								nr_of_turn=1;
								if(player_at_turn!=nr)
									l.setText(emrat[player_at_turn]+" eshte rradha jote");
								else
									l.setText(emrat[0]+" eshte rradha jote");
							}
						}
					}
					if(total_of_category_turns==12&&player_at_turn==nr)
					{
						stor= new int[nr];
						for(int i=0;i<nr;i++)
						{
							shuma=0;
							for(int j=0;j<13;j++)
							{
								shuma+=kategorite[i][j].get_piket();
							}
							stor[i]=shuma;
							System.out.println(emrat[i]+"ka"+ shuma+"pike");
						}
						for(int i=0;i<5;i++)
						{
							remove(zara[i]);
						}
						remove(l); remove(hidhZara); remove(qq); remove(jt);
						repaint(0,0,Width,Height);
						change_database();
						show_gameresults(Width,Height);
					}
				}
				});
			qq=new JScrollPane(jt);
		    TableColumnModel columnmodel= jt.getColumnModel();
		    columnmodel.getColumn(0).setWidth(200);
		    jt.setRowHeight(20);
		    qq.setBounds(Width-420, 40, 400, 305);
		    add(qq);
		    hidhZara=new JButton("Hidh Zarat");
		    hidhZara.setBounds(50, 25, 100, 25);
		    hidhZara.addActionListener(this);
		    add(hidhZara);
		}
		
		public void change_database() {
			try {
				
				Statement myStmt1 = conn.createStatement();
				ResultSet myRs1= myStmt1.executeQuery("select * from "+this.tableName);
				while(myRs1.next())
				{
					for(int i=0;i<nr;i++)
					{
						if(myRs1.getString("Emri").equals(emrat[i]))
						{
							if(myRs1.getInt("Piket")<stor[i])
							{
								String query1=  "UPDATE "+ this.tableName + 
												" SET Piket = "+stor[i]+
												" WHERE Emri = '"+emrat[i]+"';";
								this.executeUpdate(conn, query1);
							}
						}
					}
				}
				
				
		    } catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==hidhZara) {
				if(nr_of_turn==1) 
				{
					for(int i=0;i<5;i++)
					{
						zara_values[i]=rand.nextInt(6)+1;
						zara[i].setIcon(dice[zara_values[i]-1]);//shton iconen e vleres qe u be random tek secili buton zari
					}
					nr_of_turn++;
					selector=true;
				}
				else if(nr_of_turn!=4)
				{
					for(int i=0;i<5;i++)
					{
						if(zara_to_change[i]==true)
						{
							zara_values[i]=rand.nextInt(6)+1;
							zara[i].setIcon(dice[zara_values[i]-1]);
						}
					}
					nr_of_turn++;
					if(nr_of_turn==4)
					{
						hidhZara.setEnabled(false);
						select_a_category=true;
						selector=false;
						Arrays.fill(zara_to_change, false);
						for(int i=0;i<5;i++)
						{
							zara[i].setBorder(BorderFactory.createEmptyBorder());
						}
					}
				}
				
			}
			for(int i=0;i<5;i++) {
				
				if(e.getSource()==zara[i])
				{
					if(selector==true)
					{
						if(zara_to_change[i]==false) 
						{
							zara_to_change[i]=true;
							zara[i].setBorder(BorderFactory.createEtchedBorder());
						}
						else
						{
							zara_to_change[i]=false;
							zara[i].setBorder(BorderFactory.createEmptyBorder());
						}
					}
				}
			}
			
			if(e.getSource()==submit)
			{
				int k=0;
				for(int i=0;i<numer_formesh;i++)
				{
					if(mbiemra[i].getText().equals("")||moshat[i].getText().equals(""))
						k++;
				}
				if(k!=0)
				{
					warning=new JLabel("Mos ler fusha bosh");
					warning.setBounds(Width/2+150, 50, 250, 25); 
					warning.setVisible(true);
					repaint(0,0,Width,Height);
					add(warning);
				}
				else
				{
					
					if(numer_formesh!=0)
					{
						for(int i=0;i<numer_formesh;i++)
						{
							remove(emra_to_validate[i]);
							remove(mbiemra[i]);
							remove(moshat[i]);
							
						}
						remove(submit);
						if(warning!=null)
							remove(warning);
						add_to_database();
						repaint(0,0,Width,Height);
						showgame(Width,Height);
					}
					
				}
				
			}
			
			if(e.getSource()==shikoPik)
			{
				show_table_of_scores(Width,Height);
				remove(shikoPik);
			}
		}
		
		public void show_table_of_scores(int Width,int Height)
		{
			try {
				int counter=0;
				Statement myStmt2 = conn.createStatement();
				ResultSet myRs2= myStmt2.executeQuery("select * from "+this.tableName);
				while(myRs2.next())
				{
					counter++;
				}
				String[][] data1= new String[counter][4];
				ResultSet myRs3= myStmt2.executeQuery("select * from "+this.tableName);
				counter=0;
				while(myRs3.next())
				{
					for(int i=1;i<=2;i++)
					{
						data1[counter][i-1]= new String(myRs3.getString(i));
					}
					for(int i=3;i<=4;i++)
					{
						data1[counter][i-1]= new String(Integer.toString(myRs3.getInt(i)));
					}
					counter++;
				}
				
				String column1[]={"Emri","Mbiemri","Mosha","Piket"}; 
				
				JTable jt1=new JTable(data1,column1);    
				      
			    JScrollPane sp=new JScrollPane(jt1);    
			    sp.setBounds(Width-420, 40, 400, 305); 
			    add(sp);
				
		    } catch (SQLException e) {
				e.printStackTrace();
				return;
			}
		}
		
		public void show_gameresults(int Width,int Height){
			JLabel[] rezultate= new JLabel[nr];
			shikoPik=new JButton("Shiko piket");
			shikoPik.setBounds(Width-125, Height/2, 100, 25);
			shikoPik.addActionListener(this);
			add(shikoPik);
			int k=stor[0]; int lojtari_fitues=0;
			for(int i=1;i<nr;i++)
			{
				if(k<stor[i])
				{
					k=stor[i];
					lojtari_fitues=i;
				}
					
			}
			for(int i=0;i<nr;i++)
			{
				rezultate[i]=new JLabel(emrat[i]+" ka "+stor[i]+" pike");
				rezultate[i].setBounds(50,50+i*25, 500, 25);
				add(rezultate[i]);
			}
			JLabel fiton = new JLabel("Fiton "+emrat[lojtari_fitues]);
			fiton.setBounds(50, Height-100, 500, 25);
			add(fiton);
		}
		
	public static void main(String []args){
		new yahtzee2();
	}
}
