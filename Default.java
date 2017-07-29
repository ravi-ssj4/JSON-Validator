import java.util.Arrays;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;


public class Default {
	
	static int ptr;
	public static void main(String args[]) throws Exception
	{
		//Scanner sin = new Scanner(System.in);
		
		String str_json = "";
        FileReader fr=new FileReader("/home/vivek/workspace/JsonParser/src/Complex4.json");    
        BufferedReader br=new BufferedReader(fr);    

        int i;    
        while((i=br.read())!=-1){  
        if((char)i!='\t' && (char)i!=' ' && (char)i!='\n')
          str_json = str_json + (char)i;
        }  
        
        //str_json = str_json + '$';
        
        char []input = str_json.toCharArray();
		if(input.length < 2) {
			System.out.println("The Json File is invalid.");
			System.exit(0);
		}
		
		
		S s1 = new S(input);
		boolean isValid = s1.S_express();
		
	//	System.out.println(S.ptr);
	//	System.out.println(input.length);

		if((isValid) & (S.ptr == input.length)) {
			System.out.println("The input string is valid.");
		} else {
			System.out.println("The input string is invalid.");
		}
        
        br.close();    
        fr.close();    
	}

}

class S extends Default
{
	//to check the expression S -> {X}
	//int ptr;
	char []input;
	public S(){}
	public S(char []input){
		//this.ptr = ptr;
		this.input = Arrays.copyOf(input, input.length);		
	}
	
	public boolean S_express(){
		
		try{
			int fallback = ptr;
			if(input[S.ptr++] != '{') {
				S.ptr = fallback;
				return false;
			}
			
			X x1 = new X(this.input);
			if(x1.X_Express() == false) {
				S.ptr = fallback;
				return false;
			}
			
			if(input[S.ptr++] != '}') {
				S.ptr = fallback;
				return false;
			}
		}
		catch(ArrayIndexOutOfBoundsException exception)
		{
			return false;
		}
		
		return true;
	
	}
}


class X extends S
{
	//to check the expression X -> K:Xb
	//int ptr;
	char []input;
	public X(){}
	public X(char []input){
		//this.ptr = ptr;
		this.input = Arrays.copyOf(input, input.length);
	}
	
	public boolean X_Express(){
		
		int fallback = S.ptr;
		
		K k1 = new K(this.input);
		if(k1.K_express() == false) {
			S.ptr = fallback;
			return false;
		}
		
		if(input[S.ptr++] != ':') {
			S.ptr = fallback;
			return false;
		}
		
		Xb xb1 = new Xb(this.input);
		if(xb1.Xb_express() == false) {
			S.ptr = fallback;
			return false;
		}
		
		return true;
	
	}
}

class K extends X
{
	//to check the expression K -> "String"
	
	//int ptr;
	char []input;
	public K(char []input){
		//this.ptr = ptr;
		this.input = Arrays.copyOf(input, input.length);
	}
	
	public boolean K_express(){
		
		int fallback = S.ptr;
			
		if(input[S.ptr++] != '"') {
			S.ptr = fallback;
			return false;
		}
		
		while(input[S.ptr] != '"') { 
			if(input[S.ptr] >=32 && input[S.ptr] <= 126)
				S.ptr++;
			else
				return false;
			/*if(input[S.ptr] >= 65 && input[S.ptr] <= 90)
				S.ptr++;
			else if(input[S.ptr] >= 97 && input[S.ptr] <= 122)
				S.ptr++;
			else if(input[S.ptr] >= 48 && input[S.ptr] <= 57)
				S.ptr++;
			else{
				return false;
				}
				*/
		}
		
		if(input[S.ptr++] != '"') {
			S.ptr = fallback;
			return false;
		}
		
		return true;
	}
}

class Xb extends X{
	
	//to check the expression Xb -> VY | [A]Y
	
	
		//int ptr;
		char []input;
		public Xb(){}
		public Xb(char []input){
			//this.ptr = ptr;
			this.input = Arrays.copyOf(input, input.length);
		}
		
		
	
		public boolean Xb_express(){
			
			int fallback = S.ptr;
			
			if(input[S.ptr] == '[')
			{
				++S.ptr;
				A a1 = new A(this.input);

				if(a1.A_express() == false) {
					S.ptr = fallback;
					return false;
				}
				
				if(input[S.ptr++] != ']')
				{
					S.ptr = fallback;
					return false;
				}
				
				
				Y y1 = new Y(this.input);	
				if(y1.Y_express() == false) {
					S.ptr = fallback;
					return false;
				}
				
				return true;
				
			}
			else
			{
				V v1 = new V(this.input);
				
				if(v1.V_express() == false) {
					S.ptr = fallback;
					return false;
				}	
				else
				{
					Y y1 = new Y(this.input);	
					if(y1.Y_express() == false) {
						S.ptr = fallback;
						return false;
					}		
					else
						return true;
				}
			
			}
}
}
class V extends Xb{

	//to check the expression V -> "String" | NULL | True | False | S 
			//int ptr;
			char []input;
			String val = "";
			public V(){}
			public V(char []input){
				//this.ptr = ptr;
				this.input = Arrays.copyOf(input, input.length);
			}
		
			
			public boolean V_express(){
				int fallback = S.ptr;
				
				if(input[S.ptr] == '"') {
				++S.ptr;
				while(input[S.ptr] != '"') {
					
					if(input[S.ptr] >=32 && input[S.ptr] <= 126)
						S.ptr++;
					else
						return false;
					
				/*	if(input[S.ptr] >= 65 && input[S.ptr] <= 90)
						S.ptr++;
					else if(input[S.ptr] >= 97 && input[S.ptr] <= 122)
						S.ptr++;
					else if(input[ptr] >= 48 && input[ptr] <= 57)
						S.ptr++;
					else
						return false;
					*/
							}
				
				if(input[S.ptr++] != '"') {
					S.ptr = fallback;
					return false;
						}	
				else
					return true;
				}
				
				else if(input[S.ptr] == 'N' || input[S.ptr] == 'n')
				{
					while((input[S.ptr] != ',' && input[S.ptr] != '}') && ((input[S.ptr] != '"') && (input[S.ptr] != ']'))) { 
						val = val + input[S.ptr++];
									}
					if(val.equalsIgnoreCase("null"))
					{
						return true;
					}
				}
				
				else if(input[S.ptr] == 'T' || input[S.ptr] == 't')
				{
					while((input[S.ptr] != ',' && input[S.ptr] != '}') && ((input[S.ptr] != '"') && (input[S.ptr] != ']'))) { 
						val = val + input[S.ptr++];
									}
					if(val.equalsIgnoreCase("true"));
					{
						return true;
					}
				}
				
				else if(input[S.ptr] == 'F' || input[S.ptr] == 'f')
				{
					while((input[S.ptr] != ',' && input[S.ptr] != '}') && ((input[S.ptr] != '"') && (input[S.ptr] != ']'))) {  
						val = val + input[S.ptr++];
									}
					if(val.equalsIgnoreCase("false"))
					{
						return true;
					}
				}
			
				else if((input[S.ptr] >= 48 && input[S.ptr] <= 57) || (input[S.ptr] == '.' || input[S.ptr] == '-'))
				{
					while((input[S.ptr] != ',' && input[S.ptr] != '}') && ((input[S.ptr] != '"') && (input[S.ptr] != ']'))) {
						++S.ptr;
						}
					if(input[S.ptr] == ','){
						return true;
					}
					
					if(input[S.ptr] == '}')
					{
						return true;
					}
					
					if(input[S.ptr] == ']')
					{
						return true;
					}
					
					
					else 
						return false;
				}
				
					
				S s1 = new S(input);
				if(s1.S_express() == false) {
					S.ptr = fallback;
					return false;
				}	
				
				return true;
			}
	
}

class Y extends Xb{
	
	//to check the expression Y -> ,X | $
			//int ptr;
			char []input;
			public Y(){}
			public Y(char []input){
				//this.ptr = ptr;
				this.input = Arrays.copyOf(input, input.length);
			}
		
			public boolean Y_express(){
				
				int fallback = S.ptr;
				
				if(input[S.ptr] == ',')
				{
					++S.ptr;
					X x1 = new X(input);
					if(x1.X_Express() == false)
					{
						S.ptr = fallback;
						return false;
					}
					else
						return true;
				}
				else if(input[S.ptr] == '}')
					return true;
				else 
					return false;
			
			}
}

class A extends Xb{
	//to check the expression A -> V Ab 
			//int ptr;
			char []input;
			public A(){}
			public A(char []input){
				//this.ptr = ptr;
				this.input = Arrays.copyOf(input, input.length);
			}
		
			public boolean A_express(){
			
				int fallback = S.ptr;
				V v1 = new V(input);
				
				if(v1.V_express() == false)
				{
					S.ptr = fallback;
					return false;
				}
				
				Ab ab1 = new Ab(input);
				if(ab1.Ab_express() == false)
				{
					S.ptr = fallback;
					return false;
				}
				
				return true;
			
			}
	
}

class Ab extends Xb{
	//to check the expression Ab -> ,A | $ 
			//int ptr;
			char []input;
			public Ab(){}
			public Ab(char []input){
				//this.ptr = ptr;
				this.input = Arrays.copyOf(input, input.length);
			}
		
			public boolean Ab_express(){
				
				int fallback = S.ptr;
				
				if(input[S.ptr] == ',')
				{
					S.ptr++;
					A a1 = new A(input);
					if(a1.A_express() == false)
					{
						S.ptr = fallback;
						return false;
					}
					else
						return true;
				}
				else if(input[S.ptr] == '}')
					return true;
				
			
				return true;
			}
	
}




