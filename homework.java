package homework3;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

class postfixExpression {
	   private Stack theStack;
	   private String input;
	   private String output = "";
	   public postfixExpression(String in) {
	      input = in;
	      int stackSize = input.length();
	      theStack = new Stack(stackSize);
	  }
	   
	   public String doTrans() {
	      for (int j = 0; j < input.length(); j++) {
	         char ch = input.charAt(j);
	         switch (ch) {
	            case '~': 
	            case '=': 
	            case '&':
	            case '|':
	            gotOperator(ch, 1); 
	            if(ch == '=')j++;
	            break; 
	            case '(': 
	            theStack.push(ch);
	            break;
	            case ')': 
	            gotParent(ch); 
	            break;
	            default: 
	            	if(Character.isUpperCase(ch) == true){ 
	            		int predicateEnd = input.indexOf(')', j+1);
	            		String predicate = input.substring(j, predicateEnd+1);
	            		output = output + predicate;
	            		j = predicateEnd;
	            	}
	            break;
	         }
	      }
	      while (!theStack.isEmpty()) {
	         output = output + theStack.pop();
	      }
	      return output; 
	   }
	   public void gotOperator(char opThis, int prec1) {
	      while (!theStack.isEmpty()) {
	         char opTop = theStack.pop();
	         if (opTop == '(') {
	            theStack.push(opTop);
	            break;
	         }
	         else {
	        	 if (opThis == '=') {
	        		 output = output + "=>";
	              }else{
	            	  output = output + opTop;
	              }
	         }
	      }
	      theStack.push(opThis);
	   }
	   public void gotParent(char ch){ 
	      while (!theStack.isEmpty()) {
	         char chx = theStack.pop();
	         if (chx == '(') 
	        	 break; 
	         else if(chx == '=')
	        	 output = output + "=>"; 
	         else
	             output = output + chx; 
	      }
	   }
	   
	   class Stack {
	      private int maxSize;
	      private char[] stackArray;
	      private int top;
	      public Stack(int max) {
	         maxSize = max;
	         stackArray = new char[maxSize];
	         top = -1;
	      }
	      public void push(char j) {
	         stackArray[++top] = j;
	      }
	      public char pop() {
	         return stackArray[top--];
	      }
	      public char peek() {
	         return stackArray[top];
	      }
	      public boolean isEmpty() {
	         return (top == -1);
	     }
	   }
}

class UnifiedObject {
	
	private boolean isUnified;
	private ArrayList<String> unifiedStrings;
	
	public boolean isUnified() {
		return isUnified;
	}
	
	public void setUnified(boolean isUnified) {
		this.isUnified = isUnified;
	}
	
	public ArrayList<String> getUnifiedStrings() {
		return unifiedStrings;
	}
	
	public void setUnifiedStrings(ArrayList<String> unifiedStrings) {
		this.unifiedStrings = unifiedStrings;
	}
	
}


class Node {
	private String value;
	private Node right;
	private Node left;
	
	public Node() {
		this.value = "";
		this.right = null;
		this.left = null;
	}
	
	public Node(char c) {
		this.value = "c";
		this.right = null;
		this.left = null;
	}
	
	public Node(String value) {
		this.value = value;
		this.right = null;
		this.left = null;
	}
	
	public Node(String string, Node left2, Node right2) {
		this.value = string;
		this.right = right2;
		this.left = left2;
	}

	public Node(Node root) {
		this.value = root.getValue();
		this.left = root.getLeft();
		this.right = root.getRight();
	}

	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Node getRight() {
		return right;
	}
	
	public void setRight(Node right) {
		this.right = right;
	}
	
	public Node getLeft() {
		return left;
	}
	
	public void setLeft(Node left) {
		this.left = left;
	}
}

class ResolutionTry {
	
	static Boolean resolutionAlgo(Boolean res1, Boolean res2, ArrayList<ArrayList<String>> KB, String substringQuery, String query, ArrayList<String> trace) {
		ArrayList<String> OrQuery;
		substringQuery = substringQuery.replaceAll("\\s","");
		query = query.replaceAll("\\s","");
		for(int k =0;k<KB.size();k++){
			OrQuery = KB.get(k);
			for(int j = 0;j<OrQuery.size();j++){
				String temp;
				temp = isnegated(substringQuery) ? substringQuery.substring(1,substringQuery.length()) : substringQuery;
				if(OrQuery.get(j).contains(temp)){
					if((isnegated(substringQuery) == true && OrQuery.get(j).charAt(0) == '~') ||
							(isnegated(substringQuery) == false && OrQuery.get(j).charAt(0) != '~')){
						continue;
					}
					UnifiedObject unified = homework.unifyString(true, KB.get(k),query);
					boolean isUnified = unified.isUnified();
					ArrayList<String> unifiedStrings = unified.getUnifiedStrings();
					
					if(isUnified == true && unifiedStrings.size() == 0){
						return true;
					}else if(isUnified == true){
							if(trace.contains(unifiedStrings.toString())){
								continue;
							}else{
								KB.add(unifiedStrings);
								trace.add(unifiedStrings.toString());
								if(resolutionAlgo(true, true, KB,unifiedStrings.get(0).substring(0, unifiedStrings.get(0).indexOf('(') + 1)
										,unifiedStrings.get(0),trace ) && ( unifiedStrings.size() < 2 ||
										resolutionAlgo(true, true, KB,unifiedStrings.get(1).substring(0, unifiedStrings.get(1).indexOf('(') + 1)
												,unifiedStrings.get(1),trace )) && ( unifiedStrings.size() < 3 ||
														resolutionAlgo(true, true, KB,unifiedStrings.get(2).substring(0, unifiedStrings.get(2).indexOf('(') + 1)
																,unifiedStrings.get(2),trace )))
									return true;
								else
									continue;
							}
					}else{
						continue;
					}
				}
			}
		}
		return false;
	}

	static boolean isnegated(String str){
		if(str.charAt(0) == '~'){
			return true;
		}
		return false;
	}

}


public class homework {
	
	static String AndOperator = "&";
	static String OrOperator = "|";
	static String NotOperator = "~";
	
	@SuppressWarnings("unchecked")
	public static void main(String args[]) throws Exception {
		ArrayList<String> predicatesQueries = new ArrayList<String>();
		ArrayList<String> queries = new ArrayList<String>();
		//Scanner inputFileData = new Scanner(new File("/Users/Aakanksha/Desktop/USC/AI/Homework/HW3/input13.txt"));
		Scanner inputFileData = new Scanner(new File("input13.txt"));
		if(null != inputFileData) {
			int queryCount = Integer.parseInt(inputFileData.nextLine());
			if(queryCount!=0) {
				while(queryCount>0) {
					queries.add(inputFileData.nextLine());
					queryCount --;
				}
			}
			int knowledgeBaseCount = Integer.parseInt(inputFileData.nextLine());
			if(knowledgeBaseCount!=0) {
				while(knowledgeBaseCount>0) {
					predicatesQueries.add(inputFileData.nextLine());
					knowledgeBaseCount --;
				}
			}
		}
		
		inputFileData.close();

		ArrayList<String> KBinCNF = new ArrayList<String>();
		for(int i =0;i<predicatesQueries.size();i++){
			String postfixExp = "";
			postfixExpression intermediate = new postfixExpression(predicatesQueries.get(i));
			postfixExp = intermediate.doTrans(); 
			Stack<Node> predicates = new Stack<Node>();
			convertToExpressionTree(postfixExp.trim(),0,predicates);
			Node root = predicates.pop();
			convertToCNFForm(root);
			formTree(root);
			root = applyDistributionLaw(root);
			ArrayList<String> cnfStrings = new ArrayList<String>();
			splitInArray(root,cnfStrings);
			ArrayList<String> splitString = new ArrayList<String>();
			Boolean flag = false;
			for(String str : cnfStrings){
				if(str.equals(AndOperator)){
					flag = true;
					StringBuilder temp = new StringBuilder();
					Iterator<String> sub = splitString.iterator();
					while(sub.hasNext()){
						String val = sub.next();
						if(!val.equals(",")){
							temp.append(val);
						}
					}
					KBinCNF.add(temp.toString());
					splitString = new ArrayList<String>();
				}else{
					splitString.add(str);
				}
			}
			if(flag == true){
				StringBuilder temp = new StringBuilder();
				Iterator<String> sub = splitString.iterator();
				while(sub.hasNext()){
					String val = sub.next();
					if(!val.equals(",")){
						temp.append(val);
					}
				}
				KBinCNF.add(temp.toString());
			}
			else
				KBinCNF.add(splitAndOr(root, cnfStrings).get(0));
		}

		int KBSize = 0;
		ArrayList<String> splitKB = new ArrayList<String>();
		ArrayList<ArrayList<String>> KnowledgeBase = new ArrayList<ArrayList<String>>();
		ArrayList<String> querySentences = new ArrayList<String>();
		ArrayList<ArrayList<String>> KnowledgeBasePerQuery;
		
		while(KBSize<KBinCNF.size()){
			String[] clauses = KBinCNF.get(KBSize).split("\\|");
			splitKB = new ArrayList<String>();
			for(int j= 0;j<clauses.length;j++)
			{
				splitKB.add(clauses[j]);
			}
			if(clauses.length >1){
				KnowledgeBase.add(splitKB);
			}
			else{
				KnowledgeBase.add(0,splitKB);
			}
			KBSize++;
		}
		
		File outputFile = new File("output.txt");
		FileWriter fout = new FileWriter(outputFile);
		
		for(int queryCount = 0;queryCount < queries.size();queryCount++){
			KnowledgeBasePerQuery = new ArrayList<ArrayList<String>>();
			KnowledgeBasePerQuery = (ArrayList<ArrayList<String>>) KnowledgeBase.clone();
			querySentences = new ArrayList<String>();
			String substring = null, intactQuery = null;
			String query = queries.get(queryCount);
			query.trim();
			String[] splitSentences = query.split("\\|");
			for(int j= 0;j<splitSentences.length;j++)
			{
				intactQuery = negateQuery(splitSentences[j]);
				querySentences.add(intactQuery);
			}
			substring = intactQuery.substring(0, intactQuery.indexOf('(') + 1);
			KnowledgeBasePerQuery.add(querySentences);
			
			Boolean result = ResolutionTry.resolutionAlgo(true, true, KnowledgeBasePerQuery, substring, intactQuery, new ArrayList<>());
			
			if(result){
				System.out.println("True");
				fout.write("TRUE");
			}				
			else{
				System.out.println("False");
				fout.write("FALSE");
			}
            fout.write("\n");
		}
		
		fout.close();
	}

	private static String negateQuery(String actualQuery) {
		StringBuilder query = new StringBuilder();
		if(actualQuery.charAt(0) == '~'){
			query.append(actualQuery.substring(1,actualQuery.length()));
		}else{
			query.append("~");
			query.append(actualQuery);
			
		}
		return query.toString();
	}

	private static ArrayList<String> splitAndOr(Node root, ArrayList<String> cnfStrings) {
		ArrayList<String> expressions = new ArrayList<String>();
		Iterator<String> iterator  = cnfStrings.iterator();
		StringBuffer stringBuffer  =new StringBuffer();
		while(iterator.hasNext()) {
			String value = iterator.next();
			if(!value.equals(AndOperator)) {
				stringBuffer.append(value);
			} else {
				expressions.add(stringBuffer.toString());
				stringBuffer = new StringBuffer();
				
			}
		}
		expressions.add(stringBuffer.toString());
		return expressions;
	}

	private static void splitInArray(Node root,ArrayList<String> cnfExpressions) {
		if(root==null) return;
		splitInArray(root.getLeft(), cnfExpressions);
		cnfExpressions.add(root.getValue());
		splitInArray(root.getRight(), cnfExpressions);
	}

	private static Node applyDistributionLaw(Node root) {
		root = applyDistribution(root);
		if(null!=root.getLeft())  root.setLeft(applyDistribution(root.getLeft()));
		if(null!=root.getRight()) root.setRight(applyDistribution(root.getRight()));
		return root;
	}

	private static Node applyDistribution(Node root) {
		if(root.getValue().equals("|") || root.getValue().equals(AndOperator)) {
			Node left = applyDistribution(root.getLeft());
			Node right = applyDistribution(root.getRight());
			
			root= distribute(root,left,right);
			return root;
		} else return root;
		
	}

	private static Node distribute(Node root, Node left, Node right) {
		 if (root.getValue().equals("|")) {
			    if (left.getValue().equals(AndOperator)) {
			      return new Node(AndOperator,new Node("|",left.getLeft(), right), new Node("|",left.getRight(), right));
			    }if (right.getValue().equals(AndOperator)) {
			        return new Node(AndOperator,new Node("|",right.getLeft(), left), new Node("|",right.getRight(), left));
		 	    }
		}
		return root;
	}

	private static void formTree(Node root) {
		if(root != null ) {
			if(root.getValue().equals("")) {
				if(root.getRight()!=null) {
					root.setValue(root.getRight().getValue());
					root.setLeft(root.getRight().getLeft());
					root.setRight(root.getRight().getRight());
				}
				formTree(root);
			}
			formTree(root.getLeft());
			formTree(root.getRight());
		}
		
	}

	private static void convertToCNFForm(Node root) {
		if(null !=root) {
			if(null != root.getValue()) {
				if(root.getValue().equals("=>")) {
					root.setValue("|");
					Node leftChild = root.getLeft();
					negateSubtree(leftChild);
				} if(root.getValue().equals("~")) {
					root.setValue("");
					negateSubtree(root.getRight());
				}
			}
			convertToCNFForm(root.getLeft());
			convertToCNFForm(root.getRight());
		}
	}

	private static void negateSubtree(Node child) {
		if(null != child) {
			child = negateSubTree(child);
			Node leftChild = child.getLeft();
			Node rightChild = child.getRight();
			if(leftChild!=null) {
				if(!leftChild.getValue().equals(""))negateSubtree(leftChild);
				else leftChild = leftChild.getRight();
			}
			if(rightChild!=null) {
				if(!child.getValue().equals(""))negateSubtree(rightChild);
				else child = rightChild;
			}
		}
		
	}

	private static Node negateSubTree(Node child) {
		if(child!=null && !child.getValue().equals("")) {
			char c = child.getValue().charAt(0);
			if(Character.isUpperCase(c)) {
				child.setValue("~" + child.getValue());
			} else if(c == '~') {
				if(child.getValue().length()>1 )
					child.setValue(child.getValue().substring(1));
				else{child = negateSubTree(child.getRight());}
			} else if(c == '|') {
				child.setValue(AndOperator);
			} else if(c == '&') {
				child.setValue("|");
			}
		}
		return child;
	}

	private static void convertToExpressionTree(String postfixExpression,int i,Stack<Node> predicates) {
		char[] expressionCharacters = postfixExpression.toCharArray();
		while (i<expressionCharacters.length) {
	        Character c = expressionCharacters[i];
	        if(isOperator(c)){
	        	Node node = new Node();
	        	if(c!='~') {
	        		if(c== '=') {
	        			String operatorImplies = postfixExpression.substring(i,i+2);
	        			i = i+1;
	        			node.setValue(operatorImplies);
	        		} else node.setValue(""+c);
	        	} else if(c == '~') {
	        		char next = postfixExpression.charAt(i-1);
	        		if(!Character.isUpperCase(next)) {
		        		if(Character.isUpperCase(predicates.peek().getValue().charAt(0))) {
	        				predicates.peek().setValue("~"+predicates.peek().getValue());
	        				i=i+1;
		        			continue;
	        			} else  {
		        			node.setValue(""+c);
		        			node.setLeft(new Node());
	        			}
	        			
	        		} else  {
	        			node.setValue(""+c);
	        			node.setLeft(new Node());
	        		}
	        	}
	        	if(!predicates.isEmpty()) {
		        	Node child1 = predicates.pop();
		        	if(null == node.getRight()) node.setRight(child1);
		        	else predicates.push(child1);
	        	}
	        	if(!predicates.isEmpty()) {
		        	Node child2 = predicates.pop();
		        	if(null == node.getLeft()) node.setLeft(child2);
		        	else predicates.push(child2);
	        	}
	        	predicates.push(node);
	        	i=i+1;
	        } else if(Character.isUpperCase(c)) {
	        		int position = postfixExpression.indexOf(")", i+1);
	        		String predicate= postfixExpression.substring(i, position+1);
	        		predicates.push(new Node(predicate));
	        		i = position+1;
	        }
	        
	    }
	    
	}
	
	private static boolean isOperator(Character c) {
		if(c == '~' || c == '|' || c =='>' || c=='&' || c=='=')return true;
		return false;
	}
	
	public static UnifiedObject unifyString(Boolean res, ArrayList<String> kb, String query) {
		Boolean value = false;
		String value2 = "false";
		UnifiedObject unifiedResult = new UnifiedObject();
		ArrayList<String> queryValues = getParam(query);
		String predicateStart = getPredicateStart(query);
		HashMap<String,ArrayList<String>> output = replaceWithConstant(queryValues,predicateStart,kb,query,value2);
		ArrayList<String> constantsKB = output.get("constants");
		query = output.get("query").get(0);
		value2 = output.get("value").get(0);
		if(constantsKB.isEmpty()) {
			value  = unify(kb,query);
		}else{
			value = unify(constantsKB,query);
			if(value == false && value2.equals("true")) 
				value=true;
		} 
		if(value == true) {
			unifiedResult.setUnified(true);
			unifiedResult.setUnifiedStrings(constantsKB);
		} else {
			unifiedResult.setUnified(false);
			unifiedResult.setUnifiedStrings(constantsKB);
		}
		return unifiedResult;
	}


	private static Boolean unify(ArrayList<String> kb, String query) {
		Iterator<String> kbIterator = kb.iterator();
		Boolean value = false;
		while(kbIterator.hasNext()) {
			String kbString = kbIterator.next();
			if(query.contains("~")) {
				String equateString = query.substring(1);
				if(equateString.equals(kbString))  {
					kbIterator.remove();
					value = true;
				}
			} else {
				String equateString = "~" +query;
				if(equateString.equals(kbString)) {
					kbIterator.remove();
					value = true;
				}
			}
			
		}
		return value;
	}

	private static HashMap<String,ArrayList<String>> replaceWithConstant(ArrayList<String> constants, String predicateStart, ArrayList<String> kb, String query, String value2) {
		Iterator<String> kbIterator = kb.iterator();
		HashMap<String,ArrayList<String>> output = new HashMap<String,ArrayList<String>>();
		ArrayList<String> queryString = new ArrayList<String>();
		ArrayList<String> valueString = new ArrayList<String>();
		ArrayList<String> constantStrings = new ArrayList<String>();
		HashMap<String, String> replacementValues = new HashMap<String,String>();
		HashSet<String> variables = new HashSet<String>();
		while(kbIterator.hasNext()) {
			String kbString = kbIterator.next();
			if(kbString.contains(predicateStart) && isnegated(kbString) == !isnegated(query)){
				String [] KBSplit = kbString.substring(kbString.indexOf("(") +1,kbString.indexOf(")")).split(",");
				ArrayList<String> kbValues = new ArrayList<String>(Arrays.asList(KBSplit));
				if(constants.size() == kbValues.size()) {
					for(int i=0;i<constants.size();i++) {
						String val = constants.get(i);
						if(Character.isUpperCase(val.charAt(0))) {
							if(!Character.isUpperCase(kbValues.get(i).charAt(0))) {
								replacementValues.put(kbValues.get(i),val);
							}else{
								if(!val.equals(kbValues.get(i))){
									replacementValues.clear();
									break;
								}
							} 
						} else {
							if(Character.isUpperCase(kbValues.get(i).charAt(0))) {
								replacementValues.put(val,kbValues.get(i));
								variables.add(val);
							}else{
								replacementValues.put(kbValues.get(i), val);
							}
						}
					}
				} 
			}
		}
		if(!replacementValues.isEmpty()){
			value2 = "true";
		}
		kbIterator = kb.iterator();
		while(kbIterator.hasNext()) {
			String kbString = kbIterator.next();
				Set<String> keys = replacementValues.keySet();
				Iterator<String> keysIterator = keys.iterator();
				while(keysIterator.hasNext()) {
					String key = keysIterator.next();
					kbString = kbString.replace("," + key + ",", "," + replacementValues.get(key) + ",");
					kbString = kbString.replace("(" + key , "(" + replacementValues.get(key));
					kbString = kbString.replace("," + key + ")", "," + replacementValues.get(key) + ")");
				}
				constantStrings.add(kbString);
		}
		
		if(variables.size()>0) {
			Set<String> keys = replacementValues.keySet();
			Iterator<String> keysIterator = keys.iterator();
			while(keysIterator.hasNext()) {
				String key = keysIterator.next();
				query = query.replace("," + key + ",", "," + replacementValues.get(key) + ",");
				query = query.replace("(" + key , "(" + replacementValues.get(key));
				query = query.replace("," + key + ")", "," + replacementValues.get(key) + ")");
				
			}
		}
		queryString.add(query);
		valueString.add(value2);
		output.put("constants", constantStrings);
		output.put("query", queryString);
		output.put("value", valueString);
		return output;
	}
	
	private static ArrayList<String> getParam(String query) {
		String [] querySplit = query.substring(query.indexOf("(") +1,query.indexOf(")")).split(",");
		return new ArrayList<String>(Arrays.asList(querySplit));
	}
 
	static boolean isnegated(String str){
		if(str.charAt(0) == '~'){
			return true;
		}
		return false;
	}
	
	private static String getPredicateStart(String query) {
		char[] charArray = query.toCharArray();
		for(int i=0;i<charArray.length;i++) {
			if(Character.isUpperCase(query.charAt(i))) {
				int position = query.indexOf("(");
				return query.substring(i, position);
			} 
			
		}
		return "";
	}

}




