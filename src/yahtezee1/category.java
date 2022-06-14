package yahtezee1;

import java.util.*;

public class category {
	int piket; boolean[] is_taken=new boolean[13];
		
	
	category(int[] zara,int row){
		Arrays.fill(is_taken, false);
		
		if(row==1)
		{
			for(int i=0;i<5;i++)
			{
				if(zara[i]==1)
					piket++;
			}
			
			is_taken[0]=true;
		}
		else if(row==2)
		{
			for(int i=0;i<5;i++)
			{
				if(zara[i]==2)
					piket+=2;
			}
			is_taken[1]=true;
		}
		else if(row==3)
		{
			for(int i=0;i<5;i++)
			{
				if(zara[i]==3)
					piket+=3;
			}
			is_taken[2]=true;
		}
		else if(row==4)
		{
			for(int i=0;i<5;i++)
			{
				if(zara[i]==4)
					piket+=4;
			}
			is_taken[3]=true;
		}
		else if(row==5)
		{
			for(int i=0;i<5;i++)
			{
				if(zara[i]==5)
					piket+=5;
			}
			is_taken[4]=true;
		}
		else if(row==6)
		{
			for(int i=0;i<5;i++)
			{
				if(zara[i]==6)
					piket+=6;
			}
			is_taken[5]=true;
		}
		else if(row==7)
		{
			Arrays.sort(zara);
			if(zara[0]==zara[1]&&zara[0]==zara[2])
				piket=zara[0]*3+zara[3]+zara[4];
			else if(zara[1]==zara[2]&&zara[1]==zara[3])
				piket=zara[1]*3+zara[0]+zara[4];
			else if(zara[4]==zara[3]&&zara[4]==zara[2])
				piket=zara[4]*3+zara[0]+zara[1];
			else 
				piket=0;
			is_taken[6]=true;
		}
		else if(row==8)
		{
			Arrays.sort(zara);
			if(zara[0]==zara[1]&&zara[0]==zara[2]&&zara[0]==zara[3])
				piket= zara[0]*4+zara[4];
			else if(zara[4]==zara[3]&&zara[4]==zara[2]&&zara[4]==zara[1])
				piket= zara[4]*4+zara[0];
			else 
				piket= 0;
			is_taken[7]=true;
		}
		else if(row==9)
		{
			Arrays.sort(zara);
			if(zara[0]==zara[1]&&zara[2]==zara[3]&&zara[2]==zara[4])
				piket=25;
			else if(zara[3]==zara[4]&&zara[0]==zara[1]&&zara[0]==zara[2])
				piket=25;
			else
				piket=0;
			is_taken[8]=true;
		}
		else if(row==10)
		{
			Arrays.sort(zara);
			if(zara[0]==zara[1]-1&&zara[0]==zara[2]-2&&zara[0]==zara[3]-3)
				piket=30;
			else if(zara[1]==zara[2]-1&&zara[1]==zara[3]-2&&zara[1]==zara[4]-3)
				piket=30;
			else
				piket=0;
			is_taken[9]=true;
		}
		else if(row==11)
		{
			Arrays.sort(zara);
			if(zara[0]==zara[1]-1&&zara[0]==zara[2]-2&&zara[0]==zara[3]-3&&zara[0]==zara[4]-4)
				piket=40;
			else
				piket=0;
			is_taken[10]=true;
		}
		else if(row==12)
		{
			Arrays.sort(zara);
			if(zara[0]==zara[1]&&zara[0]==zara[2]&&zara[0]==zara[3]&&zara[0]==zara[4])
				piket=50;
			else
				piket=0;
			is_taken[11]=true;
		}
		else if(row==13)
		{
			for(int i=0;i<5;i++)
			{
				piket+=zara[i];
			}
			is_taken[12]=true;
		}
		
	}
	
	public int get_piket()
	{
		return piket;
	}
	
	public boolean[] get_bool()
	{
		return is_taken;
	}
	
	public void setbool(int j) {
		this.is_taken[j]=false;
	}
	
}
